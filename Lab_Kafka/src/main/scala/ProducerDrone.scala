import java.time.LocalDateTime
import java.util.{Properties, UUID}

import com.google.gson.Gson
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import org.apache.spark.sql.{Dataset, SparkSession}


object ProducerDrone extends App {
  def LoadDataSetOfViolations(vMsgs: Dataset[ViolationMessage],  spark: SparkSession) : Unit = {

    val port = "9092"
    val props = new Properties()
    props.put("bootstrap.servers", "localhost:" + port)
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    val producer = new KafkaProducer[String, String](props)

    val topic = "violations"
    println("Starting to load data into Kafka")
    val t0 = System.nanoTime()
    import org.apache.spark.sql.functions.{concat, lit}
    import spark.implicits._ // << add this

    val ds = vMsgs.select(
      concat($"vehicleBodyType", lit(" "),
      $"violationCode", lit(" "),
      $"registrationState", lit(" "),
      $"plateId", lit(" "),
      $"summonNumber").alias("value"))

    ds.show(5)

      ds.write
      .format("kafka")
      .option("checkpointLocation", "checkpoint")
      .option("kafka.bootstrap.servers", "localhost:9092")
        .option("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
        .option("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
      .option("topic", "testTopic")
      .save()

/*
    vMsgs.take(100).foreach { m =>
      println("sending " + new Gson().toJson(m))
      producer.send(new ProducerRecord(topic, new Gson().toJson(m)))
    }*/
    val t1 = System.nanoTime()
    println("Data loaded into kafka " + ((t1 - t0).toFloat/1000000000) + " seconds")
  }


  import java.util.Properties

  import org.apache.kafka.clients.producer._

  def beginDronePatrol(long : BigDecimal, lat : BigDecimal, periodMilliseconds : Int, droneId : String) : Unit = {
    val rnd = new scala.util.Random
    val t = new java.util.Timer()
    val task = new java.util.TimerTask {
      def run() = {
        val randomViolationCode = rnd.nextInt(100) - 1
        val m = Message(long + rnd.nextInt(1) - rnd.nextInt(1), lat + rnd.nextInt(1) - rnd.nextInt(1), 1, LocalDateTime.now.toString, droneId)
        // 1 chance out of 10 to trigger a violationMsg
        if(rnd.nextInt(10) >= 1) {
          val vm = ViolationMessage(1, randomViolationCode.toString, UUID.randomUUID().toString(),"SUBN","NY",m)
          DroneTest.sendViolationMsgToSoftware(vm)
        }
        else {
          DroneTest.sendMsgToSoftware(m)
        }
      }
    }
    t.schedule(task, periodMilliseconds, periodMilliseconds)
  }

 def MoveDroneTo(long : BigDecimal,lat : BigDecimal, destLong : BigDecimal, destLat : BigDecimal, sendMessages : Boolean = true) : Unit = {

   if (sendMessages) {
     val m = Message(long, lat, 1, LocalDateTime.now.toString, "1")
     DroneTest.sendMsgToSoftware(m)
     Thread.sleep(100)
   }

   if(lat == destLat && long == destLong)
     {
       println("This drone reached its destination")
       return
     }


   if (long < destLong && lat < destLat) {
     MoveDroneTo(long + 0.1, lat + 0.1, destLong, destLat)
   }
   else if (long > destLong && lat > destLat) {
     MoveDroneTo(long - 0.1, lat - 0.1, destLong, destLat)
   }

   if (long < destLong) {
     if (destLong > 0)
       MoveDroneTo(long + 0.1, lat, destLong, destLat)
     else
       MoveDroneTo(long - 0.1, lat, destLong, destLat)
   }
   else if (lat < destLat) {
     if (destLat > 0)
       MoveDroneTo(long, lat + 0.1, destLong, destLat)
     else
       MoveDroneTo(long, lat - 0.1, destLong, destLat)
   }
 }

 def sendDataToPoliceSoftware(m: Message,  topicName : String): Unit = {
  val port = "9092"
  val props = new Properties()
  props.put("bootstrap.servers", "localhost:" + port)
  props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
  props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
  val producer = new KafkaProducer[String, String](props)


  val record = new ProducerRecord(topicName, m.droneId, new Gson().toJson(m))
  producer.send(record)

  println("Done")
  producer.close()

 }

  def sendDataToPoliceSoftware(m: ViolationMessage,  topicName : String): Unit = {
    val port = "9092"
    val props = new Properties()
    props.put("bootstrap.servers", "localhost:" + port)
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    val producer = new KafkaProducer[String, String](props)


    val record = new ProducerRecord(topicName, m.message.droneId, new Gson().toJson(m))
    producer.send(record)

    println("Done")
    producer.close()

  }



}




