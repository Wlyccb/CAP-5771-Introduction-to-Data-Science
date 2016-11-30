package pagerank;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class RankSortReduce extends Reducer<DoubleWritable, Text, Text, DoubleWritable> {

    private static Long N;

    public void reduce (DoubleWritable key, Iterable<Text> values,Context context) throws IOException, InterruptedException {
    	
    	Configuration conf = context.getConfiguration();
    	N=Long.valueOf(conf.get("NumberOfPages"));
    	final double threshold = 5.0/N;
	    if (key.get() >= threshold) {
	    	for(Text val:values){
	    		key.get();
	    		context.write(val,new DoubleWritable(key.get()));
	    	}
	    }
    }

}