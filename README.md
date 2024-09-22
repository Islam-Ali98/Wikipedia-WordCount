# Wikipedia-WordCount
This project processes a large number of Wikipedia pages to count the frequency of words using Hadoop. It extracts the top 100 most frequent words while handling special characters.

## Project Overview

The WordCount project is designed to process a large set of Wikipedia articles using Hadoop's MapReduce framework. The goal is to count the frequency of words that meet specific criteria and extract the top 100 most frequent words.

### How It Works

1. **Mapping Phase**:
   - The `Map` class processes input text data by reading each line and splitting it into tokens (words).
   - It uses a regular expression to filter tokens, ensuring that only words with a length between 5 and 25 characters are considered.
   - For each valid word, the mapper emits a key-value pair where the key is the word and the value is `1`.

2. **Reducing Phase**:
   - The `Reduce` class aggregates the counts of each word emitted by the mappers.
   - It maintains a hashmap to store the total count for each word.
   - After processing all input, it sorts the hashmap by frequency and outputs the top 100 most frequent words along with their counts.

3. **Cleanup**:
   - During the cleanup phase of the reducer, the sorted results are written to the context, ensuring that the output contains only the top 100 words.

This project effectively demonstrates the use of Hadoop for processing large datasets and extracting meaningful insights from text data.

## Running the WordCount Job

The project includes a `run.sh` script to compile and execute the WordCount MapReduce job for multiple Wikipedia subfolders. 
