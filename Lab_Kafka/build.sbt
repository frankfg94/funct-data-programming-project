name := "Lab_kafka"

version := "0.1"


libraryDependencies += "org.scala-lang" % "scala-library" % "2.11.12"

// NE surtout pas changer la version
// https://mvnrepository.c  om/artifact/org.scala-lang/scala-library


// https://mvnrepository.com/artifact/com.google.code.gson/gson
libraryDependencies += "com.google.code.gson" % "gson" % "2.8.6"

// https://mvnrepository.com/artifact/org.apache.spark/spark-core
//libraryDependencies += "org.apache.spark" %% "spark-core" % "2.4.4"
// https://mvnrepository.com/artifact/org.apache.spark/spark-mllib

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "2.4.4",
  // https://mvnrepository.com/artifact/org.apache.spark/spark-sql
  "org.apache.spark" %% "spark-sql" % "2.4.4",
  "org.apache.spark" %% "spark-mllib" % "2.4.4" % "runtime",
  "org.apache.spark" %% "spark-sql-kafka-0-10" % "2.4.4",
  "org.apache.spark" %% "spark-streaming-kafka-0-10" % "2.4.4",
  "org.apache.kafka" % "kafka-clients" % "0.11.0.1",
  "org.apache.spark" %% "spark-hive" % "2.4.4" % "provided"
)
