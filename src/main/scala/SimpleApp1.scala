import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object SimpleApp1 {
  def main(args:Array[String]):Unit = {
    val sparkConf = new SparkConf().setAppName("SimpleApp1").setMaster("local")
    val sparkContext = new SparkContext(config = sparkConf)
    val data: Array[Int] = Array(1,2,3,4,5,6,7,8,9,10)
    val distdata: RDD[Int] = sparkContext.parallelize(data,3)
    //println("DistData is :: " + distdata.reduce((a,b)=>a+b))
    var counter = sparkContext.longAccumulator
    distdata.foreach(x=>counter.add(x))
    println("counter value::"+ counter)
  }
  //rdd.collect().foreach(println)
  // rdd.take(100).foreach(println)
}
