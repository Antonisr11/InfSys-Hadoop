package app;

import java.io.IOException;
import java.util.Iterator;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;

public class My_Reducer extends MapReduceBase implements Reducer<Text, IntWritable, Text, IntWritable>{
	
	public static String mostPollutionName = "";
	public static int mostPollutionValue = Integer.MIN_VALUE;
	public static String leastPollutionName = "";
	public static int leastPollutionValue = Integer.MAX_VALUE;
	
	public static int UStotalPollutionValue = 0;
	public static int UStotalPollutionN = 0;
	public static int INtotalPollutionValue = 0;
	public static int INtotalPollutionN = 0;
	
	public void reduce(Text key, Iterator<IntWritable> values, OutputCollector <Text, IntWritable> output, Reporter reporter) throws IOException{
		
		int sum = 0;
		while (values.hasNext()) {
			sum += values.next().get();
		}
		
		if (sum>mostPollutionValue) {
			mostPollutionName=key.toString().substring(2, key.toString().length());
			mostPollutionValue = sum;
		}
		
		if (sum<leastPollutionValue) {
			leastPollutionName = key.toString().substring(2, key.toString().length());
			leastPollutionValue = sum;
		}
		
		if (key.toString().substring(0, 2).equals("US")) {
			//US
			UStotalPollutionValue+=sum;
			UStotalPollutionN++;
		}
		else {
			//IN
			INtotalPollutionValue+=sum;
			INtotalPollutionN++;
		}
		output.collect(key, new IntWritable(sum));
	}
}
