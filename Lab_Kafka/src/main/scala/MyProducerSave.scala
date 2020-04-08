


object MyProducerSave extends App {
 import java.util.Properties

 import org.apache.kafka.clients.producer._

  val port = "9092"
  val props = new Properties()
  props.put("bootstrap.servers", "localhost:"+port)

  props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
  props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")

  val producer = new KafkaProducer[String, String](props)

  val topic = "scala"

  for (i <- 1 to 5) {
   val record = new ProducerRecord(topic, "key", s"hello this is the message $i")
   producer.send(record)
  }
  println("Done")
  producer.close()

}