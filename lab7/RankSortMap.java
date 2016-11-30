package pagerank;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;


public class RankSortMap extends Mapper<Object, Text, DoubleWritable, Text>{
	  private Text title = new Text();
	  private DoubleWritable rank = new DoubleWritable();
	  public void map(Object key, Text value, Context context
           ) throws IOException, InterruptedException {
		  StringTokenizer itr = new StringTokenizer(value.toString());
		  title.set(itr.nextToken());
		  rank.set(Double.parseDouble(itr.nextToken()));
		  context.write(rank,title);
	  }	
}