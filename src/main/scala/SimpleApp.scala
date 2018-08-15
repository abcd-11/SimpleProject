import org.apache.spark
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.{Dataset, KeyValueGroupedDataset, SparkSession}
import org.apache.spark.rdd.{PairRDDFunctions, RDD}

object SimpleApp {
  def main(args: Array[String]) {
    val logFile = "C:\\hadoop\\spark-2.3.1-bin-hadoop2.7\\README.md" // Should be some file on your system
    val spark = SparkSession.builder.appName("Simple Application").master("local").getOrCreate()
    import spark.implicits._
    val logData: Dataset[String] = spark.read.textFile(logFile).cache()
    /*val numAs = logData.filter(line => line.contains("a")).count()
    val numBs = logData.filter(line => line.contains("b")).count()
    println(s"Lines with a: $numAs, Lines with b: $numBs")*/
    val words: Dataset[(String, Int)] = logData.flatMap(line => line.split(" ")).map(word => (word,1))
    val wordsGrouped: KeyValueGroupedDataset[String, (String, Int)] = words.groupByKey(_._1)
    val reduceGroups: Dataset[(String, (String, Int))] = wordsGrouped.reduceGroups((a, b)=>(a._1,a._2+b._2))
    val result = reduceGroups.map(_._2)
    result.collect().foreach(println(_))
    spark.stop()
  }
}