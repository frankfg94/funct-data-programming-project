import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types.{LongType, StringType, StructField, StructType}
//import org.apache.spark.sql.SparkSession

object CsvParser extends App{


  println("Creating dataset..." )

//  val pathOfCsv = "/home/frank/Desktop/tickets.csv"
  val pathOfCsv = "/home/frank/Desktop/tickets.csv"
  val spark = SparkSession.builder
    .master("local[*]")
    .appName("Spark Word Count")
    .getOrCreate()

  val t0 = System.nanoTime()
  val df = spark.read
    .option("header", "true")
    .csv(pathOfCsv)
  val t1 = System.nanoTime()
  println("Finished creating dataset" )
  println("Total Load time : " + ((t1 - t0).toFloat/1000000000) + " seconds")
  //println(df.count()+ " rows in total")

  // Dataframe but with only the necessary columns
  val filteredDataframe = df.select("Summons Number", "Plate ID", "Vehicle Body Type", "Violation Code", "Registration State")
  // Replace NaN values
  val filteredDfNotNull = filteredDataframe.na.fill("Unknown")
  //filteredDfNotNull.show(10)

  // We import implicits to support case classes
  import  spark.implicits._

  // Next, we can assign each row to a variable of the violation message class
  val violationDataset = filteredDfNotNull.map { row =>
    ViolationMessage(
      row.getString(0).toLong, // Summons Number
      row.getString(1), // ViolationCode
      row.getString(2), // Plate ID
      row.getString(3), // Vehicle Body Type
      row.getString(4), // Registration State
      null) // Message
  }

   violationDataset.show(100)
/*
  val nbCar = df.groupBy($"Vehicle Make").count.orderBy($"count".desc)
  nbCar.show()
  val nbVehicleType = filteredDfNotNull.groupBy($"Vehicle Body Type").count.orderBy($"count".desc)
  nbVehicleType.show()
  val nbViolationType = filteredDfNotNull.groupBy($"Violation Code").count.orderBy($"count".desc)
  nbViolationType.show()
*/
  ProducerDrone.LoadDataSetOfViolations(violationDataset,spark)
}

