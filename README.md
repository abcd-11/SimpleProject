# SimpleProject

Steps to setup Kafka

1) bin\windows\zookeeper-server-start.bat config\zookeeper.properties
2) bin\windows\kafka-server-start.bat config\server.properties
3) bin\windows\kafka-topics.bat --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic test
4) bin\windows\kafka-console-producer.bat --broker-list localhost:9092 --topic test --property parse.key=true --property key.separator=:

5) Import "Simple Project" in your IDE.
6) Run WordCount.scala and enter some message in kafka producer in "key1:value1" format.
