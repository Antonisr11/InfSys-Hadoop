# InfSys-Hadoop
I created an Information System in Java using Kafka, Flume, HDFS, MapReduce, Mahout, Tomcat &amp; JSP for UI for dataset analysis

## Requirements 

1. Eclipse
2. Java JDK & JRE
3. Hadoop HDFS
4. Apache Mahoot
5. Apache Flume
6. Apache Tomcat

## General

### Stage 1 - Opening programs 

When we run main a url opens in our browser. 

![image](https://user-images.githubusercontent.com/76475823/196285584-34a6fc7e-6df1-4da7-9f41-6c6607641953.png)

Then when we click run, main run all programs. This can take a while to complete - log screen will help us understand what is happening.

![image](https://user-images.githubusercontent.com/76475823/196285685-24aaf21d-e0d8-4fb1-b144-0045441f809b.png)

### Stage 2 - Kafka Producer & Consumer, Avro and Flume

In this stage, Kafka Producer selects 300 random lines from csv and sends them to Kafka Consumer. The latter saves them in Avro format to a file that Flume listens to.

![image](https://user-images.githubusercontent.com/76475823/196285739-a5284d79-5d8c-46b8-82f5-dd524f5cffea.png)

Then automatically Flume uploads files in HDFS with names flume_1 and flume_2 respectively (the dataflow goes from one source to multiple channels; Fan-Out Flow has Fan-Out).

![image](https://user-images.githubusercontent.com/76475823/196285805-9c972411-7d1e-4502-a16d-1a2b2d4dc505.png)

### Stage 3 - MapReduce

When we click run MapReduce runs. The log screen informs us which of the 2 countries emits more bursts, which city emits the most and the least pollutants, and the average pollution of each country. 

![image](https://user-images.githubusercontent.com/76475823/196285861-04ab997d-89ef-437c-9dad-a65840268221.png)

Every country's emits are saved to HDFS as well (first 2 letters can be IN for India or US for United States).

![image](https://user-images.githubusercontent.com/76475823/196285916-3b32d439-f16c-4496-bb3d-b474bd581b7f.png)

### Stage 4 - Mahoot

Moving to the last step, Mahoot. This step encodes all city names with a unique numeric ID in order to process them for analysis. We run K-means and separate data into two clusters. 

![image](https://user-images.githubusercontent.com/76475823/196285972-3aab9a85-79bf-48cd-98e6-4853eb7093da.png)

Log screen shows results after analysis. In vec we see 3 values for every record:
1. The first number stands for date and it shows how many days have passed since 01-01-2019
2. Second value shows the unique ID given to every city
3. Third value is the air pollution that this city emits

![image](https://user-images.githubusercontent.com/76475823/196286016-287db8f0-1f12-4840-8a7e-b20e3dad1502.png)

![image](https://user-images.githubusercontent.com/76475823/196286052-09cfca9b-8c80-4727-87d5-2d6758856122.png)

If we click Restart, we will go to Step 2
