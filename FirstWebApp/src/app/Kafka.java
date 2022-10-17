package app;

import java.util.Properties;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import java.util.ArrayList;
import java.util.Arrays;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import java.io.*;

public class Kafka {
	public static ArrayList<String> dataset = new ArrayList<String>();

	public static void Producer(int entries) {
		Properties props = new Properties();
		props.put("bootstrap.servers", "localhost:9092");
		props.put("acks", "all");
		props.put("retries", 0);
		props.put("batch.size", 16384);
		props.put("linger.ms", 1);
		props.put("buffer.memory", 33554432);
		props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

		readCSV();

		Producer<String, String> producer = new KafkaProducer<>(props);
		for (int j = 0; j < entries; j++) {
			int randomNumber = (int) (Math.random() * (dataset.size() + 1));
			producer.send(new ProducerRecord<String, String>("my_first_topics", dataset.get(randomNumber)));
			Main.writeLog("  Sent:" + dataset.get(randomNumber));
		}
		producer.close();
	}

	public static void Consumer(String exportPath) {
		Properties props = new Properties();
		props.put("bootstrap.servers", "localhost:9092");
		props.put("group.id", "group-1");
		props.put("enable.auto.commit", "true");
		props.put("auto.commit. interval.ms", "1000");
		props.put("auto.offset.reset", "earliest");
		props.put("session.timeout.ms", "30000");
		props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

		KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(props);
		kafkaConsumer.subscribe(Arrays.asList("my_first_topics"));
		boolean receivedMessages = false;
		ArrayList<avro> avros = new ArrayList<avro>();
		while (!receivedMessages) {
			ConsumerRecords<String, String> records = kafkaConsumer.poll(100);
			for (ConsumerRecord<String, String> record : records) {
				avro temAvro = new avro();
				temAvro.setDate(record.value().split(",")[0]);
				temAvro.setCountry(record.value().split(",")[1]);
				temAvro.setCity(record.value().split(",")[2]);
				temAvro.setValue(Double.parseDouble(record.value().split(",")[3]));
				avros.add(temAvro);

				Main.writeLog("  offset = " + record.offset() + ", value = " + record.value());
				receivedMessages = true;
			}
		}
		Main.writeLog("Exporting avro file at " + exportPath);
		avro.SerializeAvros(avros, exportPath);
		
		Main.writeLog("Exporting avro file at /extra");
		avro.SerializeAvros(avros, "extra");
	}

	public static void readCSV() {
		dataset.clear();
		try (BufferedReader br = new BufferedReader(new FileReader("dataset.csv"))) {
			String line = "";
			while ((line = br.readLine()) != null) {
				dataset.add(line);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		dataset.remove(0);
	}
}