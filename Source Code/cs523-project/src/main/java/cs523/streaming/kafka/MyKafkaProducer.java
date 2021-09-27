package cs523.streaming.kafka;

import java.io.BufferedReader;
import java.io.FileReader;
import java.net.URISyntaxException;
import java.util.Properties;
import java.util.UUID;
import java.util.stream.Stream;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

public class MyKafkaProducer {
	private static String KafkaBrokerEndpoint = "localhost:9092";
    private static String KafkaTopic = "cs523-project";
      
    private Producer<String, String> ProducerProperties(){
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaBrokerEndpoint);
        properties.put(ProducerConfig.CLIENT_ID_CONFIG, "MyKafkaProducer");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        return new KafkaProducer<String, String> (properties);
    }

    public void PublishMessages(String filePath) throws URISyntaxException{
    	
        final Producer<String, String> csvProducer = ProducerProperties();
        
        try {
        	Stream<String> FileStream = new BufferedReader(new FileReader(filePath)).lines().skip(1);
            FileStream.forEach(line -> {
            	System.out.println(line);
            	
                final ProducerRecord<String, String> csvRecord = new ProducerRecord<String, String>(
                        KafkaTopic, UUID.randomUUID().toString(), line);

                csvProducer.send(csvRecord, (metadata, exception) -> {
                    if (metadata != null){
                        System.out.println("CsvData: -> "+ csvRecord.key()+" | "+ csvRecord.value());
                    }
                    else {
                        System.out.println("Error Sending Csv Record -> "+ csvRecord.value());
                    }
                });
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    

}
