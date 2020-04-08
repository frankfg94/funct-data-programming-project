import java.time.{Duration, LocalDateTime}
import java.time.format.DateTimeFormatter
import java.util
import java.util.{Collections, Optional, UUID}

import com.google.gson.Gson
import org.apache.kafka.clients.admin.{AdminClient, AlterConfigOp, AlterConfigsOptions, AlterConfigsResult, AlterPartitionReassignmentsOptions, AlterPartitionReassignmentsResult, AlterReplicaLogDirsOptions, AlterReplicaLogDirsResult, Config, CreateAclsOptions, CreateAclsResult, CreateDelegationTokenOptions, CreateDelegationTokenResult, CreatePartitionsOptions, CreatePartitionsResult, CreateTopicsOptions, CreateTopicsResult, DeleteAclsOptions, DeleteAclsResult, DeleteConsumerGroupOffsetsOptions, DeleteConsumerGroupOffsetsResult, DeleteConsumerGroupsOptions, DeleteConsumerGroupsResult, DeleteRecordsOptions, DeleteRecordsResult, DeleteTopicsOptions, DeleteTopicsResult, DescribeAclsOptions, DescribeAclsResult, DescribeClusterOptions, DescribeClusterResult, DescribeConfigsOptions, DescribeConfigsResult, DescribeConsumerGroupsOptions, DescribeConsumerGroupsResult, DescribeDelegationTokenOptions, DescribeDelegationTokenResult, DescribeLogDirsOptions, DescribeLogDirsResult, DescribeReplicaLogDirsOptions, DescribeReplicaLogDirsResult, DescribeTopicsOptions, DescribeTopicsResult, ElectLeadersOptions, ElectLeadersResult, ExpireDelegationTokenOptions, ExpireDelegationTokenResult, ListConsumerGroupOffsetsOptions, ListConsumerGroupOffsetsResult, ListConsumerGroupsOptions, ListConsumerGroupsResult, ListPartitionReassignmentsOptions, ListPartitionReassignmentsResult, ListTopicsOptions, ListTopicsResult, NewPartitionReassignment, NewPartitions, NewTopic, RecordsToDelete, RemoveMembersFromConsumerGroupOptions, RemoveMembersFromConsumerGroupResult, RenewDelegationTokenOptions, RenewDelegationTokenResult}
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.{ElectionType, Metric, MetricName, TopicPartition, TopicPartitionReplica}
import org.apache.kafka.common.acl.{AclBinding, AclBindingFilter}
import org.apache.kafka.common.config.ConfigResource

import scala.annotation.switch


// Police Control Device

object MyConsumerPolice extends App {

  /*
  def SetDronePath(value: Int, locationLong: BigDecimal, locationLat: BigDecimal) : Unit = {
      println("New destination for the Drone : "  + locationLong + " / " + locationLat)
      //MyProducerDrone.MoveDroneTo(value,locationLong,locationLat)
  }
*/


  def MoveToManhantann() : Unit = {
    //SetDronePath(1,40.758896,-73.985130)
  }


  import java.util.Properties

  val topic="t2"
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

  // Message handler (non violation)
  while(true){
    val records=consumer.poll(100)
      records.forEach( rec =>
        {
              val msg =  new Gson().fromJson(rec.value(),classOf[Message])
              println("Received message from drone n°"+ msg.droneId + " MSG : " + msg + " displayed at " + DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm").format(LocalDateTime.now) )
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

