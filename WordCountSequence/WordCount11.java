import java.io.IOException; 
import java.util.StringTokenizer;
import org.apache.hadoop.conf.Configuration; 
import org.apache.hadoop.fs.FileSystem; 
import org.apache.hadoop.fs.Path; 
import org.apache.hadoop.io.IntWritable; 
import org.apache.hadoop.io.LongWritable; 
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job; 
import org.apache.hadoop.mapreduce.Mapper; 
import org.apache.hadoop.mapreduce.Reducer; 
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat; 
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class WordCount11 {

public static class WordCountMapper 
extends Mapper < LongWritable, Text, Text, IntWritable >
{
	private final static IntWritable one = new IntWritable( 1); 
	private Text word = new Text();
	
	
	public void map( LongWritable key, Text value, Context context
	) throws IOException, 
	InterruptedException { 
		String str = value.toString();
		String itr = str.replaceAll("\\s","");
	    String starr[]=str.split(" ");
	

	List<String> stringList = new ArrayList<String>(Arrays.asList(starr));
	int setWCount=5;
	Text a1=null;
	String Sent="";
	
	for (int i = 0, j=0; i < starr.length; i++,j++) {

		if (setWCount + i <= starr.length) {
       
		    
	Sent=Sent+"\n"+stringList.subList(i, setWCount + i).toString().replaceAll("\\]", "").replaceAll("\\[", "").replaceAll(",", "") + "  " + i;
	System.err.println("In map " + stringList.subList(i, setWCount + i).toString().replaceAll("\\]", "").replaceAll("\\[", "").replaceAll(",", "") + "  " + j);

	IntWritable x = new IntWritable(i);		
			

		}
	}
	word.set(Sent);
	context.write(word, one);
	
	} 
	} 
	 
public static class WordCountReducer 
extends 
Reducer < Text, IntWritable, Text, IntWritable > { 
private IntWritable result = new IntWritable(); 
@Override 
public void reduce( Text key, Iterable < IntWritable > values, Context context 
) throws IOException, 
InterruptedException { 
int sum = 0; 
int arr[];

for (IntWritable val : values) { 
sum += val.get(); 
} 

context.write(key, new IntWritable(sum));

} 
}
public static void main( String[] args) throws Exception { 
Configuration conf = new Configuration();
Job job = Job.getInstance( conf, "word count");

job.setJarByClass(WordCount11.class);
FileInputFormat.addInputPath( job, new Path("input")); 
FileOutputFormat.setOutputPath( job, new Path("output")); 
job.setMapperClass( WordCountMapper.class); 
job.setCombinerClass( WordCountReducer.class); 
job.setReducerClass( WordCountReducer.class);
job.setOutputKeyClass( Text.class); 
job.setOutputValueClass( IntWritable.class);

System.exit( job.waitForCompletion( true) ? 0 : 1); 
} 
}