import java.util.Collections

import com.google.gson.Gson
import org.apache.kafka.clients.consumer.KafkaConsumer

import scala.annotation.switch


// Police Control Device
object ConsumerPoliceViolations extends App {
  def echo(code: String, info : String) : String = code match {
    case "10" => info + " Speed violation"
    case "20" => info + " Hydrant violation"
    case "30" => info + " Parking violation"
    case "40" => info + " Driver violation"
    case "50" => info + " Standing violation"
    case "100" => info + " Human action required"
  }

  import java.util.Properties

  val topic="violations2"
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
          val vCode = msg.violationCode.toInt
          val info ="Number : " + msg.violationCode +  msg.message.locationLon+ "/" + msg.message.locationLat + " Plate  : "  + msg.plateId + " Violation : "
          if (vCode == -1)
          {
            println(info +"need Human verification")
          }
          else if(vCode < 20)
            {
              println(info +"Speed violation")

            }
          else if (vCode < 40)
            {
              println(info +"Hydrant violation")

            }
          else if (vCode < 60)
            {
              println(info +"Parking violation")

            }
          else if (vCode < 80)
            {
              println(info +"Driver violation")

            }
          else
            {
              println(info +"Unknown violations")
            }

        }
      )


  }
  consumer.close()

  println("Done")
}




