import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

import com.google.gson.Gson

// TODO
// Variables techniques (ex : batterie)



case class ImageData(imageId : Int, data : Array[Byte])

object DroneTest extends App
{


  // Le drone va envoyer 5 messages chaque seconde
                val m1 = Message(1, 1, 1, LocalDateTime.now.plus(1, ChronoUnit.SECONDS).toString, "1")
                val m2 = Message(1, 1, 1, LocalDateTime.now.plus(2, ChronoUnit.SECONDS).toString, "1")
                val m3 = Message(1, 1, 1, LocalDateTime.now.plus(3, ChronoUnit.SECONDS).toString, "1")
                val m4 = Message(1, 1, 1,LocalDateTime.now.plus(4, ChronoUnit.SECONDS).toString, "1")
                val m5 = Message(2, 2, 2, LocalDateTime.now.plus(5, ChronoUnit.SECONDS).toString, "1")

                // create a JSON string from the Person, then print it
               // sendMsgToSoftware(m1)
                /*sendMsgToSoftware(m2)
                sendMsgToSoftware(m3)
                sendMsgToSoftware(m4)
                sendMsgToSoftware(m5)
                */
                ProducerDrone.beginDronePatrol(10,10,10000,"1")

                //MyProducerDrone.MoveDroneTo(1,1,50,100,true)
                def sendMsgToSoftware(message: Message): Unit = {
                        print("Sending " + new Gson().toJson(message))
                        ProducerDrone.sendDataToPoliceSoftware(message,"standard")
                }

                def sendViolationMsgToSoftware(vm: ViolationMessage) : Unit = {
                        print("Sending " + new Gson().toJson(vm))
                        ProducerDrone.sendDataToPoliceSoftware(vm, "violations2")
                }

}
