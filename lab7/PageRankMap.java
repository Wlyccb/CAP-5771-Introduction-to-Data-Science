package pagerank;

import java.io.IOException;
import java.util.StringTokenizer;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;


public class PageRankMap extends Mapper<Object, Text, Text, Text>{
	  private Text link = new Text();
	  private Text title = new Text();
	  public void map(Object key, Text value, Context context
             ) throws IOException, InterruptedException {
		  StringTokenizer itr = new StringTokenizer(value.toString());
		  int count = itr.countTokens()-1;
		  title.set(itr.nextToken());
		  while (itr.hasMoreTokens()) {
			  String temp = new String(itr.nextToken());
				  link.set(temp);
				  context.write(link, new Text ("1.0" + "\t" + title + "\t" + count));
				  context.write(title,new Text("|"+link));
		  }
	  }	
}
