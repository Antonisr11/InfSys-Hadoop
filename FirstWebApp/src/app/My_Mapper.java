package app;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;

public class My_Mapper extends MapReduceBase implements Mapper<LongWritable, Text, Text, IntWritable> {
	private Text word = new Text();

	public void map(LongWritable key, Text value, OutputCollector<Text, IntWritable> output, Reporter reporter){
		// This method will run for every line of csv
		String line = value.toString(); //2019-01-01,US,San Antonio,42.0
		
		try {
			word.set(line.split(",")[1]+line.split(",")[2]);
			output.collect(word, new IntWritable((int)Double.parseDouble(line.split(",")[3])));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
