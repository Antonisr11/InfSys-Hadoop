package app;

//import org.apache.hadoop.conf.Configuration;
//import org.apache.hadoop.fs.FSDataInputStream;
//import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
//import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.TextInputFormat;
import org.apache.hadoop.mapred.TextOutputFormat;

public class My_Runner {
	
	public static void run(String inputDatasetCSV){
		JobConf conf = new JobConf(My_Runner.class);
		conf.setJobName("WordCount");
		
		conf.setOutputKeyClass(Text.class);
		conf.setOutputValueClass(IntWritable.class);
		
		conf.setMapperClass(My_Mapper.class);
		conf.setCombinerClass(My_Reducer.class);
		conf.setReducerClass(My_Reducer.class);
		
		conf.setInputFormat(TextInputFormat.class);
		conf.setOutputFormat(TextOutputFormat.class);
		
		String outputPath= "hdfs://localhost:9000/MyOutPutt";
		int count=1;
		
		outerWhile: while (true) {
			try {
				FileInputFormat.setInputPaths(conf, new Path(inputDatasetCSV));
				FileOutputFormat.setOutputPath(conf, new Path(outputPath+String.valueOf(count)));
				JobClient.runJob(conf);
				//http://localhost:9870/explorer.html
				Main.writeLog("Job was successful and stored at output path: "+outputPath+String.valueOf(count)+")");
			    break outerWhile;
			}
			catch (org.apache.hadoop.mapred.FileAlreadyExistsException e) {
				count++;
			}
			catch (Exception e) {
				e.printStackTrace();
				Main.writeLog("Job was NOT successful");
			}
		}
	}
}
