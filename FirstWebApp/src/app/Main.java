package app;

import java.io.*;
import java.text.SimpleDateFormat;
import java.awt.Desktop;
import java.net.URI;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.nio.file.Files;

public class Main {

	final static String flume_folder = "C:/apache-flume-1.9.0-bin/";
	final static String fullFilesPath = "C:/hadoop_temp/";
	final static int datasetSample = 300;

	public static void main(String[] args) {

		// Create necessary directories
		File directory = new File(fullFilesPath);
		if (!directory.exists()) {
			directory.mkdir();
		}

		directory = new File("extra/");
		if (!directory.exists()) {
			directory.mkdir();
		}

		directory = new File("extra/Kmeans");
		if (!directory.exists()) {
			directory.mkdir();
		}

		directory = new File("extra/Kmeans/Input");
		if (!directory.exists()) {
			directory.mkdir();
		}

		directory = new File("extra/Kmeans/Output");
		if (!directory.exists()) {
			directory.mkdir();
		}
		
		try {
			Runtime.getRuntime().exec("cmd /c rmdir /s /q \"%FLUME_HOME%/temp/\"");
			wait(1);
			Runtime.getRuntime().exec("cmd /c mkdir \"%FLUME_HOME%/temp/\"");
			wait(1);
			Runtime.getRuntime().exec("cmd /c rmdir /s /q \"%KAFKA_HOME%/kafka-logs/\"");
			wait(1);
			Runtime.getRuntime().exec("cmd /c mkdir \"%KAFKA_HOME%/kafka-logs/\"");
			wait(1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// Create/Clear necessary files
		writeFile("clickedRun", "false");
		writeFile("clickedNext", "false");
		writeFile("enableRun", "true");
		writeFile("enableNext", "false");
		writeFile("logs", "");
		
		// Delete unnecessary files
		new File("extra/avro.avro").delete();
		new File("extra/dataMapReduce.csv").delete();
		new File("extra/dataMahout.csv").delete();
		new File("extra/Kmeans/Input/dataMahout.csv").delete();
		

		try {
			Desktop.getDesktop().browse(new URI("http://localhost:8080/FirstWebApp/Stage1.jsp"));
		} catch (Exception e) {
			e.printStackTrace();
		}

		while (true) {
			if (readFile("clickedRun").replace("\n", "").equals("true")) {
				break;
			}
		}

		/* Run Programs */

		writeLog("Time to start all programs");

		writeLog("Disabling Run button");
		writeFile("enableRun", "false");

		try {
			writeLog("Creating conf file for Flume");
			FileWriter myWriter = new FileWriter(flume_folder+"/conf/project.conf");
			myWriter.write(
					"myAgent.sources = source_1\nmyAgent.channels = channel_1 channel_2\nmyAgent.sinks = sink_1 sink_2\n\nmyAgent.sources.source_1.type = spooldir\nmyAgent.sources.source_1.spoolDir = "
							+ flume_folder
							+ "temp/\nmyAgent.sources.source_1.fileSuffix=.COMPLETED\nmyAgent.sources.source_1.inputCharset = ISO-8859-2\n\nmyAgent.sinks.sink_1.type = hdfs\nmyAgent.sinks.sink_1.hdfs.path = hdfs://localhost:9000/flume_1\n\nmyAgent.sinks.sink_2.type = hdfs\nmyAgent.sinks.sink_2.hdfs.path = hdfs://localhost:9000/flume_2\n\nmyAgent.channels.channel_1.type = memory\nmyAgent.channels.channel_1.capacity = 10000\nmyAgent.channels.channel_1.transactionCapacity = 10000\nmyAgent.channels.channel_1.byteCapacityBufferPercentage = 20\nmyAgent.channels.channel_1.byteCapacity = 800000\nmyAgent.channels.channel_2.type = memory\nmyAgent.channels.channel_2.capacity = 10000\nmyAgent.channels.channel_2.transactionCapacity = 10000\nmyAgent.channels.channel_2.byteCapacityBufferPercentage = 20\nmyAgent.channels.channel_2.byteCapacity = 800000\n\nmyAgent.sources.source_1.channels = channel_1 channel_2\nmyAgent.sinks.sink_1.channel = channel_1\nmyAgent.sinks.sink_2.channel = channel_2");
			myWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		writeLog("Opening HDFS");
		Main.CMDstart("%HADOOP_HOME%\\sbin\\start-all.cmd");
		wait(3);

		writeLog("Opening Kafka");
		CMDstart("%KAFKA_HOME%\\bin\\windows\\zookeeper-server-start.bat %KAFKA_HOME%\\config\\zookeeper.properties");
		wait(20);
		CMDstart("%KAFKA_HOME%\\bin\\windows\\kafka-server-start.bat %KAFKA_HOME%\\config\\server.properties");
		wait(5);

		writeLog("Opening Flume");
		Main.CMDstart(
				"%FLUME_HOME%\\bin\\flume-ng agent --conf %FLUME_HOME%\\conf -f %FLUME_HOME%\\conf\\project.conf -property \"flume.root.logger=info,console\" -n myAgent");

		wait(5);

		writeLog("Enabling next button");
		writeFile("enableNext", "True");

		writeLog("Done");
		
		while (true) { 
			
			while (true) {
				if (readFile("clickedNext").replace("\n", "").equals("true")) {
					break;
				}
			}

			writeFile("clickedRun", "false");
			writeFile("clickedNext", "false");
			writeFile("enableRun", "true");
			writeFile("enableNext", "false");
			writeFile("logs", ""); // Clear logs

			while (true) {
				if (readFile("clickedRun").replace("\n", "").equals("true")) {
					break;
				}
			}

			/* Kafka & Flume */

			writeLog("Disabling Run button");
			writeFile("enableRun", "false");

			writeLog("Starting KAFKA Producer");
			Kafka.Producer(datasetSample);
			wait(5);

			writeLog("Starting KAFKA Consumer");
			Kafka.Consumer(flume_folder + "temp/");

			writeLog("Transfering avro to HDFS via Flume");
			Main.wait(20);

			writeLog("Deleting avro.avro file from Flume");
			new File(flume_folder + "avro.avro").delete();

			writeLog("Deleting avro.avro.COMPLETED file from Flume");
			new File(flume_folder + "avro.avro.COMPLETED").delete();

			writeLog("Enabling next button");
			writeFile("enableNext", "True");

			while (true) {
				if (readFile("clickedNext").replace("\n", "").equals("true")) {
					break;
				}
			}

			writeFile("clickedRun", "false");
			writeFile("clickedNext", "false");
			writeFile("enableRun", "true");
			writeFile("enableNext", "false");
			writeFile("logs", ""); // Clear logs

			while (true) {
				if (readFile("clickedRun").replace("\n", "").equals("true")) {
					break;
				}
			}

			/* MapReduce */
			
			writeLog("Disabling Run button");
			writeFile("enableRun", "false");

			ArrayList<String> dates = new ArrayList<String>();
			ArrayList<String> countries = new ArrayList<String>();
			ArrayList<String> cities = new ArrayList<String>();
			ArrayList<String> values = new ArrayList<String>();
			
			writeLog("Deserializing and reading avro.avro");
			for (String JSONRow : avro.DeserializeAvro("extra/avro.avro")) {
				dates.add(JSONRow.replace("{", "").replace("}", "").replace("\"", "").split(",")[0].split(":")[1].substring(1));
				countries.add(JSONRow.replace("{", "").replace("}", "").replace("\"", "").split(",")[1].split(":")[1].substring(1));
				cities.add(JSONRow.replace("{", "").replace("}", "").replace("\"", "").split(",")[2].split(":")[1].substring(1));
				values.add(JSONRow.replace("{", "").replace("}", "").replace("\"", "").split(",")[3].split(":")[1].substring(1));
			}
			
			writeLog("Exporting data to dataMapReduce csv");
			try {
				FileWriter csv_file = new FileWriter("extra/dataMapReduce.csv", true);
				for (int i=0; i<dates.size(); i++) {
					csv_file.write(dates.get(i)+","+countries.get(i)+","+cities.get(i)+","+values.get(i)+"\n");
				}
				csv_file.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			Main.writeLog("Starting MapReduce job");
			My_Runner.run("extra/dataMapReduce.csv");
			
			writeLog("Data contains "+ My_Reducer.UStotalPollutionN +" US entries and "+ My_Reducer.INtotalPollutionN+" India entries.");
			writeLog("Country that emits the most pollutants is " + (My_Reducer.INtotalPollutionValue > My_Reducer.UStotalPollutionValue ? "India" : "United States"));
			writeLog("City that emits the most pollutants is " + My_Reducer.mostPollutionName);
			writeLog("City that emits the least pollutants is " + My_Reducer.leastPollutionName);
			writeLog("Average pollution of US is: " + (My_Reducer.UStotalPollutionValue / My_Reducer.UStotalPollutionN));
			writeLog("Average pollution of IN is: " + (My_Reducer.INtotalPollutionValue / My_Reducer.INtotalPollutionN));
			
			writeLog("Deleting avro.avro from extra");
			new File("extra/avro.avro").delete();
			
			writeLog("Deleting dataMapReduce.csv");
			new File("extra/dataMapReduce.csv").delete();
			
			writeLog("Enabling next button");
			writeFile("enableNext", "True");
			
			while (true) {
				if (readFile("clickedNext").replace("\n", "").equals("true")) {
					break;
				}
			}

			writeFile("clickedRun", "false");
			writeFile("clickedNext", "false");
			writeFile("enableRun", "true");
			writeFile("enableNext", "false");
			writeFile("logs", ""); // Clear logs

			while (true) {
				if (readFile("clickedRun").replace("\n", "").equals("true")) {
					break;
				}
			}
			
			/* Mahout */
			
			writeLog("Disabling Run button");
			writeFile("enableRun", "false");
			
			ArrayList<String> encodedCities = new ArrayList<String>();
			try {
				writeLog("Starting data preprocessing");
				for (int i=0; i<dates.size(); i++) {
					// 01/01/19 -> 1, 02/01/19 -> 2, ..., 31/05/21 -> 882
					dates.set(i, String.valueOf((int)((new SimpleDateFormat("yyyy-MM-dd").parse(dates.get(i)).getTime() - new SimpleDateFormat("yyyy-MM-dd").parse("2018-12-31").getTime()) / (1000*60*60*24l))));
					
					// Phoenix -> 1, Brooklyn -> 2, ...
					if (!encodedCities.contains(cities.get(i))) {
						encodedCities.add(cities.get(i));
					}
					String tempLog = "City with name \""+cities.get(i)+"\" is encoded with ";
					cities.set(i, String.valueOf(encodedCities.indexOf(cities.get(i))+1));
					writeLog(tempLog+cities.get(i));
				}
				
				writeLog("Exporting dataMahout csv");
				FileWriter csv_file = new FileWriter("extra/dataMahout.csv", true);
				for (int j=0; j<dates.size(); j++) {
					csv_file.write(dates.get(j)+" "+cities.get(j)+" "+values.get(j)+"\n");
				}
				csv_file.close();
				
				writeLog("Copying csv to Mahout input folder");
				Files.copy(new File("extra/dataMahout.csv").toPath(), new File("extra/Kmeans/Input/dataMahout.csv").toPath());
				
			} catch (Exception e) {
			    e.printStackTrace();
			}
			
			writeLog("Running Mahout");
			Mahout.run();
			
			writeLog("Deleting dataMahout.csv from extra and Mahout's input folder");
			new File("extra/dataMahout.csv").delete();
			new File("extra/Kmeans/Input/dataMahout.csv").delete();
			
			writeLog("Enabling restart button");
			writeFile("enableNext", "True");
			
		}
		
	}

	public static void CMDstart(String command) {
		writeLog("Running in cmd \"" + command + "\"");
		try {
			Runtime.getRuntime().exec("cmd /c start \"\" " + command);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String readFile(String filename) {

		StringBuilder toReturn = new StringBuilder();

		File file = new File(fullFilesPath + filename + ".temp");
		Scanner myReader;
		while (true) {
			try {
				myReader = new Scanner(file);
				break;
			} catch (FileNotFoundException e1) {
			}
		}

		while (myReader.hasNextLine()) {
			toReturn.append(myReader.nextLine() + "\n");
		}
		myReader.close();
		return toReturn.toString();
	}

	public static void writeFile(String filename, String text) {
		while (true) {
			try {
				FileWriter file = new FileWriter(fullFilesPath + filename + ".temp");
				file.write(text);
				file.close();
				break;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void writeLog(String text) {
		while (true) {
			try {
				FileWriter file = new FileWriter(fullFilesPath + "logs.temp", true);
				file.write(text + "\n");
				file.close();
				break;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void wait(int seconds) {
		writeLog("    Starting " + seconds + " second timeout");
		try {
			for (int i = 1; i < seconds; i++) {
				TimeUnit.SECONDS.sleep(1);
				if ((seconds-i) % 5 == 0) {
					writeLog("    " + (seconds - i) + " seconds remaining");
				}
			}
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
