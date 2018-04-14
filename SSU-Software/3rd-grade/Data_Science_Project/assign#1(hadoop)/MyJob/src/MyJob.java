import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.apache.hadoop.mapred.KeyValueTextInputFormat;
import org.apache.hadoop.mapred.TextOutputFormat;

public class MyJob extends Configured implements Tool{
	
	
    public static class MapClass extends MapReduceBase
        implements Mapper{

		@Override
		public void map(Object key, Object value, OutputCollector output, Reporter arg3) throws IOException {
			// TODO Auto-generated method stub
			 output.collect(value, key);
		}
        
    }
    
    
    public static class Reduce extends MapReduceBase
        implements Reducer{
	    	boolean title = true;
	    	int sum = 0;
	    	int largest = 0;
	    	int smallest = 99999;
	    	double mean = 0;
	    	int number = 0;
	    	int count = 0;
	    	String largest_cite = "";
	    	String smallest_cite = "";
	    	String temp = " ";
	    	String csv = "";
			@Override
			public void reduce(Object key, Iterator values, OutputCollector output, Reporter reporter) throws IOException {
				 // TODO Auto-generated method stub
				if(!title){
				csv = "";	 
				count = 0;
			
				temp = key.toString();
		        while (values.hasNext()){
		        	if(csv.length() > 0 ) 
		        		csv += ",";
		        	csv += values.next().toString();
		        	count++;
		       }
		        
		       number++;
		       sum += count;
		       
	           if(largest < count ){
	         	  largest = count;
	         	  largest_cite = temp;
	           }
	           
	           if(smallest > count){
	         	  smallest = count;
	         	  smallest_cite = temp;
	           }
	           
	           if(number != 0){
	        	   mean =(double)sum/number;
	           }
	          csv += "\n The count is " + count + " and sum is "  +sum + " and mean is " + mean +"\n"
	        		  + "smallest is " + smallest_cite + " and count is " + smallest +"\n"
	        		  + "largest is " + largest_cite + " and count is " + largest + "\n";
	          
	          output.collect(key, new Text(csv));
			}
			else{
		        while (values.hasNext()){
		        	if(csv.length() > 0 ) 
		        		csv += ",";
		        	csv += values.next().toString();
		        }
		        output.collect(key, new Text(csv));
				title = false;
			}
		}
		
    }
    
    public int run(String[] arg0) throws Exception {
        // TODO Auto-generated method stub
        Configuration conf = getConf();
        JobConf job = new JobConf(conf, MyJob.class);
        
        Path in = new Path(arg0[0]);
        Path out = new Path(arg0[1]);
        FileInputFormat.setInputPaths(job, in);
        FileOutputFormat.setOutputPath(job, out);
        
        job.setJobName("cite[20150250]");
        job.setJarByClass(MyJob.class);
        job.setMapperClass(MapClass.class);
        job.setReducerClass(Reduce.class);
        
        job.setInputFormat(KeyValueTextInputFormat.class);
        job.setOutputFormat(TextOutputFormat.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);
        job.set("key.value.separator.in.input.line", ",");
        JobClient.runJob(job);
        
        return 0;
    }
    
    public static void main( String[] args) throws Exception {
        int res = ToolRunner.run(new Configuration(), new MyJob(), args);
        System.exit(res);
    }

}