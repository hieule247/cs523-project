package cs523.streaming.kafka;

import java.io.IOException;
import java.util.*;

import org.apache.spark.SparkConf;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.*;
import org.apache.spark.streaming.kafka010.*;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;

import cs523.utils.HBaseTable;

public class SparkStreaming {
	public static void main(String[] args) throws Exception
	{
		try {
			JavaStreamingContext streamingContext = new JavaStreamingContext(
					new SparkConf().setAppName("SparkStreaming").setMaster("local[*]"), Durations.seconds(2));
			
			Map<String, Object> kafkaParams = new HashMap<>();
			kafkaParams.put("bootstrap.servers", "localhost:9092");
			kafkaParams.put("key.deserializer", StringDeserializer.class);
			kafkaParams.put("value.deserializer", StringDeserializer.class);
			kafkaParams.put("group.id", "group");
			kafkaParams.put("auto.offset.reset", "latest");
			kafkaParams.put("enable.auto.commit", false);

			Collection<String> topics = Arrays.asList("cs523-project");

			JavaInputDStream<ConsumerRecord<String, String>> stream =
			  KafkaUtils.createDirectStream(
			    streamingContext,
			    LocationStrategies.PreferConsistent(),
			    ConsumerStrategies.<String, String>Subscribe(topics, kafkaParams)
			  );
			
			// Create HBase table
			HBaseTable.createTable();
			
			// Insert data into HBase from Kafka
			stream.foreachRDD(rdd -> rdd.foreach(x -> insertDataIntoHbase(x.value())));		
			
			streamingContext.start();
			streamingContext.awaitTermination();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private static void insertDataIntoHbase(String record) throws IOException{
		List<String> fieldValues = Arrays.asList(record.split(","));
		HBaseTable.addData(fieldValues);		
	}
}
