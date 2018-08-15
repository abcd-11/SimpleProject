name := "Simple Project"

version := "0.1"

scalaVersion := "2.11.8"

lazy val exckudeJpountz = ExclusionRule(organization = "net.jpountz.lz4", name = "lz4")

libraryDependencies += "org.apache.spark" %% "spark-sql" % "2.3.1"

libraryDependencies += "org.apache.spark" % "spark-streaming_2.11" % "2.3.1"

// https://mvnrepository.com/artifact/org.apache.spark/spark-streaming-kafka
libraryDependencies += "org.apache.spark" % "spark-streaming-kafka-0-10_2.11" % "2.3.1" excludeAll(exckudeJpountz)