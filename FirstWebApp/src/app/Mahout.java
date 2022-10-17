package app;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.mahout.clustering.conversion.InputDriver;
import org.apache.mahout.clustering.kmeans.KMeansDriver;
import org.apache.mahout.clustering.kmeans.RandomSeedGenerator;
import org.apache.mahout.common.HadoopUtil;
import org.apache.mahout.common.distance.DistanceMeasure;
import org.apache.mahout.common.distance.EuclideanDistanceMeasure;
import org.apache.mahout.utils.clustering.ClusterDumper;

public class Mahout {

	static Configuration conf = new Configuration();
	static Path input = new Path("extra/Kmeans/Input");
	static Path output = new Path("extra/Kmeans/Output");
	static DistanceMeasure measure = new EuclideanDistanceMeasure();
	static int k = 2;
	static double convergenceDelta = 0.5;
	static int maxIterations = 10;

	public static void run() {
		
		try {
			HadoopUtil.delete(conf, output);
			
			Path directoryContainingConvertedInput = new Path(output, "KmeansOutputData");
			InputDriver.runJob(input, directoryContainingConvertedInput, "org.apache.mahout.math.RandomAccessSparseVector");

			Path clusters = new Path(output, "random-seeds");
			clusters = RandomSeedGenerator.buildRandom(conf, directoryContainingConvertedInput, clusters, k, measure);

			KMeansDriver.run(conf, directoryContainingConvertedInput, clusters, output, convergenceDelta, maxIterations, true, 0.0, false);

			Path outGlob = new Path(output, "clusters-*-final");
			Path clusteredPoints = new Path(output, "clusteredPoints");

			ClusterDumper clusterDumper = new ClusterDumper(outGlob, clusteredPoints);
			clusterDumper.printClusters(null);
			
			String results = clusterDumper.getClusterIdToPoints().toString();
			StringBuilder tempStringBuilder = new StringBuilder();
			
			String identifier1 = results.substring(1).split("]]")[0].replace("],", "]\n")+"]";
			tempStringBuilder.append("\nIdentifier VL-"+identifier1.split("=")[0]+"\n");
			tempStringBuilder.append(" "+identifier1.split("=")[1].substring(1)+"\n");
			
			String identifier2 = results.substring(1).split("]]")[1].replace("],", "]\n")+"]".replace("}", "");
			tempStringBuilder.append("\nIdentifier VL-"+identifier2.split("=")[0].substring(2)+"\n");
			tempStringBuilder.append(" "+identifier2.split("=")[1].substring(1)+"\n");
			
			Main.writeLog(tempStringBuilder.toString());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
