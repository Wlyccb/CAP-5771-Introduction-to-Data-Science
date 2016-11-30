package pagerank;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;

import org.apache.hadoop.conf.*;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;


public class PageRank {
	
	 private static String numberOfPages;
	
	public static void main(String[] args) throws Exception {
		PageRank pageranking = new PageRank();
		
        String[] outputpath = {"/temp/iter1out","/temp/iter2out","/temp/iter3out","/temp/iter4out","/temp/iter5out","/temp/iter6out","/temp/iter7out","/temp/iter8out","/temp/num_nodes"};
        String[] res = {"/temp/iter1.out","/temp/iter8.out"};
        
        pageranking.countN(args[0],args[1]+outputpath[8]);
        
        Path src3 = new Path (args[1]+outputpath[8]);
        Path des3 = new Path (args[1]+"/num_nodes");
        Configuration conf3 = new Configuration();
        FileUtil.copyMerge(src3.getFileSystem(conf3), src3, des3.getFileSystem(conf3), des3, false, conf3,""); 
        
        
        Path pt=new Path(args[1]+"/num_nodes");
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(new URI(args[1]),conf);
        BufferedReader br=new BufferedReader(new InputStreamReader(fs.open(pt)));
        numberOfPages = br.readLine();
        
        pageranking.CalRank1(args[0],args[1]+outputpath[0]);
        for (int i=0;i<7;i++) {
        	pageranking.CalRank8(args[1]+outputpath[i],args[1]+outputpath[i+1]);
        }
        
        pageranking.ranksort(args[1]+outputpath[0],args[1]+res[0]);
        pageranking.ranksort(args[1]+outputpath[7],args[1]+res[1]);
        
        
        Path src1 = new Path (args[1]+res[0]);
        Path src2 = new Path (args[1]+res[1]);
        
        Path des1 = new Path (args[1]+"/iter1.out");
        Path des2 = new Path (args[1]+"/iter8.out");
        
        Configuration conf1 = new Configuration();
        Configuration conf2 = new Configuration();
        
        FileUtil.copyMerge(src1.getFileSystem(conf1), src1, des1.getFileSystem(conf1), des1, false, conf1,""); 
        FileUtil.copyMerge(src2.getFileSystem(conf2), src2, des2.getFileSystem(conf2), des2, false, conf2,"");  
 
    }
	

	public void countN (String inputpath, String outputpath) throws Exception{
		Configuration conf = new Configuration();
		Job job = Job.getInstance(conf, "job1");
		
		job.setJobName("CountN");
		job.setJarByClass(PageRank.class);
		
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(IntWritable.class);
		
		job.setOutputKeyClass(IntWritable.class);
		job.setOutputValueClass(NullWritable.class);
		
		job.setMapperClass(CountMap.class);
		job.setReducerClass(CountReduce.class);
		
		FileInputFormat.addInputPath(job, new Path(inputpath));
	    FileOutputFormat.setOutputPath(job, new Path(outputpath));
	    job.waitForCompletion(true);
	}
	
	public void CalRank1 (String inputpath, String outputpath) throws Exception {
		Configuration conf = new Configuration();
		conf.set("NumberOfPages", numberOfPages);
		Job job = Job.getInstance(conf, "job2");
		
		job.setJobName("CalRank");
		job.setJarByClass(PageRank.class);
		
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(Text.class);
		
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		
		job.setMapperClass(PageRankMap.class);
		job.setReducerClass(PageRankReduce.class);
		
		FileInputFormat.addInputPath(job, new Path(inputpath));
	    FileOutputFormat.setOutputPath(job, new Path(outputpath));
	    job.waitForCompletion(true);
	}
	
	public void CalRank8 (String inputpath, String outputpath) throws Exception {
		Configuration conf = new Configuration();
		conf.set("NumberOfPages", numberOfPages);
		Job job = Job.getInstance(conf, "job3");
		
		job.setJobName("CalRank");
		job.setJarByClass(PageRank.class);
		
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(Text.class);
		
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		
		job.setMapperClass(PageRankMap2.class);
		job.setReducerClass(PageRankReduce2.class);
		
		FileInputFormat.addInputPath(job, new Path(inputpath));
	    FileOutputFormat.setOutputPath(job, new Path(outputpath));
	    job.waitForCompletion(true);
	}
	
	public void ranksort (String inputpath, String outputpath) throws Exception {
		Configuration conf = new Configuration();
		conf.set("NumberOfPages", numberOfPages);
		Job job = Job.getInstance(conf, "job4");
	
		job.setJobName("RankSort");
		job.setJarByClass(PageRank.class);
		
		job.setNumReduceTasks(1);
		
		job.setMapOutputKeyClass(DoubleWritable.class);
		job.setMapOutputValueClass(Text.class);
		
		job.setSortComparatorClass(DescDoubleComparator.class);
		
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(DoubleWritable.class);
		
		job.setMapperClass(RankSortMap.class);
		job.setReducerClass(RankSortReduce.class);
		
		FileInputFormat.addInputPath(job, new Path(inputpath));
	    FileOutputFormat.setOutputPath(job, new Path(outputpath));
	    job.waitForCompletion(true);
	}
	
	
	
	
	
}