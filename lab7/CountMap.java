package pagerank;

import java.io.IOException;

import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Mapper;


public class CountMap extends Mapper<LongWritable, Text, Text, IntWritable> {
	private final static IntWritable one = new IntWritable(1);
    private Text word = new Text("Totala_Lines");
    public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
    	context.write(word, one);
    }
}
