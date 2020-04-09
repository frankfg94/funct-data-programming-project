import java.util.Collections

import com.google.gson.Gson
import org.apache.kafka.clients.consumer.KafkaConsumer


// Police Control Device
object ConsumerPoliceViolations extends App {

  import java.util.Properties

  val topic="violations"
  val port = "9092"

  val  props = new Properties()

  props.put("bootstrap.servers", "localhost:"+port)
  props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
  props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")

  // To be sure we display the old messages, we need to have a random id
  props.put("group.id", "abc")
  
  val consumer = new KafkaConsumer[String, String](props)
  consumer.subscribe(Collections.singletonList(topic))

  // We want to display the old messages
  val rec =  consumer.poll(0)
  consumer.seekToBeginning(consumer.assignment())
  println(rec.count())

  println("Starting topic " + topic)
  // Message handler (non violation)
  while(true) {
    val records=consumer.poll(100)
      records.forEach( rec =>
        {
              val msg =  new Gson().fromJson(rec.value(),classOf[ViolationMessage])
              println("Received message " + msg )
          /*
          val x = (rec: @switch) match {
            case 1  => "1"
            case 2  => "2"
            case _  => "3"
          }
          */
        }
      )
  }
  consumer.close()

  println("Done")
}

