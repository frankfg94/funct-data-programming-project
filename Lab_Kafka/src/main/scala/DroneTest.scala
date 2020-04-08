import java.sql.{Date, Time}
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.Calendar

import com.google.gson.Gson

// TODO
// Variables techniques (ex : batterie)

case class Message(locationLon : BigDecimal,
        locationLat : BigDecimal,
        locationAlt : BigDecimal,
        time : String,
        droneId : String)

case class ImageData(imageId : Int, data : Array[Byte])
//val drones: List[Drone] = List(drone0, drone1, drone2)


case class ViolationMessage(
                           summonNumber : Long,
                           violationCode : String,
                           plateId : String,
                           vehicleBodyType: String,
                           registrationState : String,
                           message : Message)

/*
case class Drone(
                longitude : BigDecimal,
                latitude : BigDecimal,
                speed : Float,
                temperature : Float,
                hddSize : Long
                )
*/
// Producer d'exemple

object DroneTest extends App
{

                // Le drone va envoyer 5 messages chaque seconde
                val m1 = Message(1, 1, 1, LocalDateTime.now.plus(1, ChronoUnit.SECONDS).toString, "1")
                val m2 = Message(1, 1, 1, LocalDateTime.now.plus(2, ChronoUnit.SECONDS).toString, "1")
                val m3 = Message(1, 1, 1, LocalDateTime.now.plus(3, ChronoUnit.SECONDS).toString, "1")
                val m4 = Message(1, 1, 1,LocalDateTime.now.plus(4, ChronoUnit.SECONDS).toString, "1")
                val m5 = Message(2, 2, 2, LocalDateTime.now.plus(5, ChronoUnit.SECONDS).toString, "1")

                // create a JSON string from the Person, then print it
                sendMsgToSoftware(m1)
                /*sendMsgToSoftware(m2)
                sendMsgToSoftware(m3)
                sendMsgToSoftware(m4)
                sendMsgToSoftware(m5)
                */
                MyProducerDrone.beginDronePatrol(10,10,10000,"1")

                //MyProducerDrone.MoveDroneTo(1,1,50,100,true)
                def sendMsgToSoftware(message: Message): Unit = {
                        print("Sending " + new Gson().toJson(message))
                        MyProducerDrone.sendDataToPoliceSoftware(message)
                }
        }
