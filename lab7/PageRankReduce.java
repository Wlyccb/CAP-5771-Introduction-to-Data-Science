package pagerank;

import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class PageRankReduce extends Reducer<Text,Text,Text,Text> {
	private static final double damping = 0.85;
	private static Long N;
	public void reduce(Text key, Iterable<Text> values,Context context) throws IOException, InterruptedException{
		StringBuilder sb = new StringBuilder();
		Iterator<Text> it = values.iterator();
		double Sum_Score = 0;
		Configuration conf = context.getConfiguration();
    	N=Long.valueOf(conf.get("NumberOfPages"));
		while (it.hasNext()){
			String value  = it.next().toString();
			if (value.startsWith("|"))  {
				sb.append(value.substring(1));
				sb.append("\t");
			}
			else {
				String[] split = value.split("\\t");
				double score = (double)1.0/N;
				int Count_Links = Integer.valueOf(split[2]);
				Sum_Score += (score/Count_Links);
			}
		}
		double newRank = damping * Sum_Score + (1-damping)/N;
		context.write(key, new Text(newRank + "\t" + sb.toString()));
	}
}