import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.{Collections, UUID}

import com.google.gson.Gson
import org.apache.kafka.clients.consumer.KafkaConsumer


// Police Control Device

object ConsumerPoliceStandard extends App {

  import java.util.Properties

  val topic="standard"
  val port = "9092"

  val  props = new Properties()

  props.put("bootstrap.servers", "localhost:"+port)
  props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
  props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")

  // To be sure we display the old messages, we need to have a random id
  props.put("group.id", UUID.randomUUID().toString())
  
  val consumer = new KafkaConsumer[String, String](props)
  consumer.subscribe(Collections.singletonList(topic))

  // We want to display the old messages
  consumer.poll(0)
  consumer.seekToBeginning(consumer.assignment())
  println("Starting topic " + topic)




    // Message handler (non violation)
    while(true){
      val records=consumer.poll(100)
       records.forEach( rec =>
          {
            val msg =  new Gson().fromJson(rec.value(),classOf[Message])
            println("Received message from drone n°"+ msg.droneId + " MSG : " + msg + " displayed at " + DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm").format(LocalDateTime.now) )

          }
        )
  }
  //consumer.close()

  println("Done")
}

