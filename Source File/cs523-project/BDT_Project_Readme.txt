I. Install Kafka
	1. Download Kafka:
		- Latest  version: https://kafka.apache.org/downloads
		- Project version: https://archive.apache.org/dist/kafka/2.4.1/kafka_2.12-2.4.1.tgz
	2. Extract and change current directory to
		$ cd /home/cloudera/kafka_2.12-2.4.1

II. Start Zookeeper and Kafka
	1. Run properties files:
		1.1 $ ./bin/zookeeper-server-start.sh config/zookeeper.properties
		1.2 $ ./bin/kafka-server-start.sh config/server.properties
	2. Create topic
		$ ./bin/kafka-topics.sh --create --zookeeper localhost:2181 --topic cs523-project --replication-factor 1 --partitions 1
	3. Check topic existed: cs523-project
		3.1 $ ./bin/kafka-topics.sh --list --zookeeper localhost:2181
		3.2 $ ./bin/kafka-topics.sh --describe --zookeeper localhost:2181 --topic cs523-project

III. Run Kafka Producer (Terminal 1)
	1. Start console shell (Terminal)
	2. Run command
		$ ./bin/kafka-console-producer.sh --broker-list localhost:9092 --topic cs523-project

IV. Run Kafka Consumer (Terminal 2)
	1. Start console shell (Terminal)
	2. Run command
		$ ./bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic cs523-project --from-beginning

V. Notes - If Can not create HBase table
	1. Check HBase Service Start.
		- $ service --status-all
	2. If not start --> Need re-start
	// --Start HBase Service.
	- $ sudo service hbase-master start
	- $ sudo service hbase-regionserver start

###SPARK STREAMING & SPARK SQL

#Start spark streaming and producer
java -cp "target/cs523-project-0.0.1-SNAPSHOT-jar-with-dependencies.jar:/home/cloudera/workspace/cs523-project/libs/*" cs523.streaming.kafka.SparkProcess
java -cp "target/cs523-project-0.0.1-SNAPSHOT-jar-with-dependencies.jar:/home/cloudera/workspace/cs523-project/libs/*" cs523.utils.KafkaProducer

#Start spark sql
java -cp "target/cs523-project-0.0.1-SNAPSHOT-jar-with-dependencies.jar:/home/cloudera/workspace/cs523-project/libs/*" cs523.sparksql.SparkSQLReportCreation


/// ------ CREATE TABLE AND REFERENCE TO HBASE
1. Run script file 
	sh script/Hive_CreateTableAndQueryData.sh

#sudo service hbase-master start | sudo service hbase-regionserver start

