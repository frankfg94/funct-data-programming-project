import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.Properties

import com.google.gson.Gson
import kafka.utils.Json
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import org.apache.spark.sql.Dataset


object MyProducerDrone extends App {
  def LoadDataSetOfViolations(vMsgs: Dataset[ViolationMessage]) : Unit = {
    val port = "9092"
    val props = new Properties()
    props.put("bootstrap.servers", "localhost:" + port)
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    val producer = new KafkaProducer[String, String](props)

    val topic = "violations"
    println("Starting to load data into Kafka")
    /*
        TODO : Enlever le foreach
    vMsgs.foreach(vMsg => {
      val record = new ProducerRecord(topic, vMsg.summonNumber.toString, new Gson().toJson(vMsg))
      producer.send(record)
    })
     */
    println("Data loaded into kafka")
    producer.close()
  }


  import java.util.Properties

 import org.apache.kafka.clients.producer._

  def beginDronePatrol(long : BigDecimal, lat : BigDecimal, periodMilliseconds : Int, droneId : String) : Unit = {
    val rnd = new scala.util.Random
    val t = new java.util.Timer()
    val task = new java.util.TimerTask {
      def run() = {
        val m = Message(long + rnd.nextInt(1) - rnd.nextInt(1), lat + rnd.nextInt(1) - rnd.nextInt(-1), 1, LocalDateTime.now.toString, droneId)
        DroneTest.sendMsgToSoftware(m)
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

 def sendDataToPoliceSoftware(m: Message): Unit = {
  val port = "9092"
  val props = new Properties()
  props.put("bootstrap.servers", "localhost:" + port)
  props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
  props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
  val producer = new KafkaProducer[String, String](props)

  // Interessant, on peut avoir plusieurs types pour un producer producer
  val topic = "t2"


  val record = new ProducerRecord(topic, m.droneId, new Gson().toJson(m))
  producer.send(record)

  println("Done")
  producer.close()

 }


}




