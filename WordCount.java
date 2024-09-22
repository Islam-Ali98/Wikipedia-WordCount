import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.List;

public class WordCount extends Configured implements Tool {

    public static class Map extends Mapper<LongWritable, Text, Text, IntWritable> {

        private final static IntWritable one = new IntWritable(1);
        private Text word = new Text();

        @Override
        public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

            final String regex1 = "^[a-z]{5,25}";
            final Pattern pattern = Pattern.compile(regex1, Pattern.MULTILINE);

            String line = value.toString().toLowerCase();
            String[] tokens = line.split("[^a-z]");
            for (String token : tokens) {
                final Matcher matcher = pattern.matcher(token);
                if(matcher.find()){
                    word.set(token);
                    context.write(word, one);
                }
            }
        }
    }

    public static class Reduce extends Reducer<Text, IntWritable, Text, IntWritable> {
        private IntWritable result = new IntWritable();
        private HashMap<String,Integer> m = new HashMap<>();

        @Override
        public void reduce(Text key, Iterable<IntWritable> values, Context context)
                throws IOException, InterruptedException {

            int sum = 0;
            for (IntWritable value : values){
                sum += value.get();
            }
            m.put(key.toString(), sum);
        }

        @Override
        protected void cleanup(Context context) throws IOException, InterruptedException {
            List<HashMap.Entry<String,Integer>> entryList = new ArrayList<>(m.entrySet());

            // Sort the list based on values (strings) in reverse order,
            // with keys as secondary criteria for sorting
            entryList.sort((e1, e2) -> {
                int cmp = Integer.compare(e2.getValue(), e1.getValue()); // Sort by value in reverse order
                if (cmp != 0) {
                    return cmp; // If values are different, return the result of comparing values
                }
                // If values are the same, compare keys to retain all entries with duplicate values
                return e1.getKey().compareTo(e2.getKey());
            });

            // Iterate over the sorted list and print entries
            int count=0;
            for (HashMap.Entry<String,Integer> entry : entryList) {
                String x = entry.getKey();
                Integer y = entry.getValue();
                context.write(new Text(x), new IntWritable(y));
                count++;
                if(count>=100) {
                    break;
                }
            }
        }
    }

    @Override
    public int run(String[] args) throws Exception {
        Job job = Job.getInstance(new Configuration(), "WordCount");
        job.setJarByClass(WordCount.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        job.setMapperClass(Map.class);
        //The combiner class seems to create problems with counts (according to documentation)
        //job.setCombinerClass(Reduce.class);
        job.setReducerClass(Reduce.class);

        job.setInputFormatClass(TextInputFormat.class);
        job.setOutputFormatClass(TextOutputFormat.class);

        FileInputFormat.setInputPaths(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        job.waitForCompletion(true);
        return 0;
    }

    public static void main(String[] args) throws Exception {
        int ret = ToolRunner.run(new Configuration(), new WordCount(), args);
        System.exit(ret);
    }
}
