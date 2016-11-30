package pagerank;

import java.io.IOException;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Reducer;

public class CountReduce extends Reducer<Text, IntWritable,IntWritable,NullWritable> {
   public void reduce(Text key, Iterable<IntWritable> values,Context context) throws IOException, InterruptedException{
     int sum = 0;
     for(IntWritable val:values){
       sum += val.get();
     }
     context.write(new IntWritable(sum),NullWritable.get());
   }
}