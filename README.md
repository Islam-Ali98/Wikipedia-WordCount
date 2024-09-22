# Wikipedia-WordCount
This project processes a large number of Wikipedia pages to count the frequency of words using Hadoop. It extracts the top 100 most frequent words while handling special characters.

## Features

- Utilizes Hadoop for distributed processing of data
- Splits words to eliminate special characters like `$`, `%`, etc.
- Outputs the top 100 most frequent words from the processed Wikipedia pages
## Running the WordCount Job

The project includes a `run.sh` script to compile and execute the WordCount MapReduce job for multiple Wikipedia subfolders. 
