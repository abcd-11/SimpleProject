import org.apache.spark._
import org.apache.spark.streaming._
import org.apache.spark.streaming.kafka010.KafkaUtils
import org.apache.commons.codec.StringDecoder
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.spark.streaming.dstream.{DStream, InputDStream}

object WordCount {
  def main(args: Array[String]) = {
    val conf = new SparkConf().setMaster("local[2]").setAppName("WordCount")
    val ssc = new StreamingContext(conf, Seconds(20))

    import org.apache.spark.streaming.kafka010._

    val preferredHosts = LocationStrategies.PreferConsistent
    val topics = List("test")
    import org.apache.kafka.common.serialization.StringDeserializer
    val kafkaParams = Map(
      "bootstrap.servers" -> "localhost:9092",
      "key.deserializer" -> classOf[StringDeserializer],
      "value.deserializer" -> classOf[StringDeserializer],
      "group.id" -> "spark-streaming-notes",
      "auto.offset.reset" -> "earliest"
    )
    import org.apache.kafka.common.TopicPartition
    val offsets = Map(new TopicPartition("test", 0) -> 2L)

    val dstream: InputDStream[ConsumerRecord[String, String]] = KafkaUtils.createDirectStream[String, String](
      ssc,
      preferredHosts,
      ConsumerStrategies.Subscribe[String, String](topics, kafkaParams, offsets))

   /* dstream.foreachRDD { rdd =>
      // Get the offset ranges in the RDD
      /*val offsetRanges: Array[OffsetRange] = rdd.asInstanceOf[HasOffsetRanges].offsetRanges
      for (o <- offsetRanges) {
        println(s"--------------${o.topic} ${o.partition} offsets: ${o.fromOffset} to ${o.untilOffset}")
      }*/
      val rddCollected: Array[ConsumerRecord[String, String]] = rdd.collect()
      rddCollected.foreach(record => "--------------"+println(record.key() + " :: " + record.value()))
    }*/

    val values: DStream[String] = dstream.map(record => record.value)
    val words: DStream[String] = values.flatMap(_.split(" "))
    val wordCountInitial: DStream[(String, Int)] = words.map(word => (word,1))
    val wordsCount: DStream[(String, Int)] = wordCountInitial.reduceByKey(_+_)
    wordsCount.print()

    ssc.start

    // the above code is printing out topic details every 5 seconds
    // until you stop it.

    //ssc.stop(stopSparkContext = false)
    ssc.awaitTermination()  // Wait for the computation to terminate
  }
}
