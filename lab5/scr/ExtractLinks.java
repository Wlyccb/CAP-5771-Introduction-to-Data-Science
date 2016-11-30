package wikipedia;

import java.io.IOException;
import java.util.List;
import java.util.StringTokenizer;

import java.util.Collection; 
import java.util.HashSet;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Reducer.Context;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;



public class ExtractLinks {
	 

  public static class Map
       extends Mapper<LongWritable, Text, Text, Text>{

    public void map(LongWritable key, Text value, Context context
                    ) throws IOException, InterruptedException {
    	Logger.getLogger(Map.class.getName()).log(Level.SEVERE,"start map");
    	try {
    		String page = value.toString();
    		Document document = DocumentHelper.parseText(page);
    		Element root = document.getRootElement();
    		Text title = new Text(root.elementTextTrim("title").replace(" ","_"));
    		List<Element> list = root.elements("revision");
    		String text = "";
    		for (Element ele : list) {
    			text = ele.elementTextTrim("text");
    		}
    		Pattern pattern = Pattern.compile("\\[\\[([^\\]]+?)(\\||\\]\\])");
    		Matcher matcher = pattern.matcher(text);
    		context.write(title, new Text("!@#$"));
			while (matcher.find()) {
				context.write(new Text(matcher.group(1).replace(" ","_")),title);
			}	
    	}
    	catch (DocumentException e) {
    		Logger.getLogger(Map.class.getName()).log(Level.SEVERE, "DocumentException",e);
    	}
      }
    }
 

  public static class Reduce
       extends Reducer<Text,Text,Text,Text> {
	  private boolean isnotredlink;

    public void reduce(Text key, Iterable<Text> values,
                       Context context
                       ) throws IOException, InterruptedException {
    	isnotredlink = false;
    	StringBuilder sb = new StringBuilder();
    	Iterator<Text> it = values.iterator();
    	while (it.hasNext()){
    		String value  = it.next().toString();
    		if (value.equals("!@#$")) {
    			isnotredlink = true;
    		}	
    		sb.append(value);
    		sb.append("\t");
    	}
    	if (isnotredlink) {
    		context.write(key, new Text(sb.toString()));
    	}
    	}
  }
  
  public static class Map2
  extends Mapper<Object, Text, Text, Text>{
	  private Text title = new Text();
	  private Text page = new Text();
	  public void map(Object key, Text value, Context context
               ) throws IOException, InterruptedException {
		  StringTokenizer itr = new StringTokenizer(value.toString());
		  title.set(itr.nextToken());
		  while (itr.hasMoreTokens()) {
			  String temp = new String(itr.nextToken());
			  if(temp.equals("!@#$")) context.write(title, new Text(""));
			  else {
				  page.set(temp);
				  context.write(page, title);
			  }
		  }
	  }	
}


public static class Reduce2
  extends Reducer<Text,Text,Text,Text> {
// boolean isnotreslink = false;

public void reduce(Text key, Iterable<Text> values,
                  Context context
                  ) throws IOException, InterruptedException {
	StringBuilder sb = new StringBuilder();
	Iterator<Text> it = values.iterator();
	Collection links = new HashSet(); 
	while (it.hasNext()){
		String value  = it.next().toString();
		if (links.contains(value) || value.equals("")) continue;
		else {
			links.add(value);
			sb.append(value);
			sb.append("\t");
		}
	}
	context.write(key, new Text(sb.toString()));
	}
}
  private static final String OUTPUT_PATH = "output/temp";

  public static void main(String[] args) throws Exception {
    Configuration conf = new Configuration();
    conf.set("xmlinput.start", "<page>");
    conf.set("xmlinput.end", "</page>");
    Job job1 = Job.getInstance(conf, "job1");
    job1.setJarByClass(ExtractLinks.class);
    job1.setMapperClass(Map.class);
    job1.setReducerClass(Reduce.class);
    job1.setInputFormatClass(XMLInputFormat.class);
    job1.setOutputKeyClass(Text.class);
    job1.setOutputValueClass(Text.class);
    FileInputFormat.addInputPath(job1, new Path(args[0]));
    FileOutputFormat.setOutputPath(job1, new Path(OUTPUT_PATH));
    
    job1.waitForCompletion(true);
    
    Configuration conf2 = new Configuration();
    Job job2 = Job.getInstance(conf2, "job2");
    job2.setJarByClass(ExtractLinks.class);
    job2.setMapperClass(Map2.class);
    job2.setReducerClass(Reduce2.class);
    job2.setOutputKeyClass(Text.class);
    job2.setOutputValueClass(Text.class);
    FileInputFormat.addInputPath(job2, new Path(OUTPUT_PATH));
    FileOutputFormat.setOutputPath(job2, new Path(args[1]));
    System.exit(job2.waitForCompletion(true) ? 0 : 1);
  }
}
