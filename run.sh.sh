#!/bin/bash
module load lang/Java/1.8.0_241

export JAVA_HOME=/opt/apps/resif/iris-rhel8/2020b/broadwell/software/Java/1.8.0_241/
export HADOOP_HOME=~/hadoop-3.3.5
export PATH=~/hadoop-3.3.5/bin:$PATH

javac -classpath $HADOOP_HOME/share/hadoop/common/hadoop-common-3.3.5.jar:$HADOOP_HOME/share/hadoop/mapreduce/hadoop-mapreduce-client-core-3.3.5.jar:$HADOOP_HOME/share/hadoop/common/lib/commons-logging-1.1.3.jar WordCount_3.java

jar -cvf WordCount.jar *.class

SUBFOLDERS=(AA AB AC AD AE AF AG AH AI AJ AK)

# Loop through each subfolder
for SUBFOLDER in "${SUBFOLDERS[@]}"
do
    # Input and output paths
    INPUT_PATH="Wikipedia-En-41784-Articles/$SUBFOLDER"
    OUTPUT_PATH="WordCount_$SUBFOLDER"

    hadoop fs -rm -r ~/$OUTPUT_PATH


    # Run the WordCount MapReduce job
    hadoop jar WordCount.jar WordCount ~/$INPUT_PATH ~/$OUTPUT_PATH

    
done