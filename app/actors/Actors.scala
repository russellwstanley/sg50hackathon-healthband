package actors

import akka.actor.{Props, Actor, ActorRef}
import controllers.{Location, AlarmState}
import play.api.libs.concurrent.Akka
import scala.concurrent.ExecutionContext.Implicits.global
import play.api.Play.current


case class PanicAlarmMsg(userId : String, alarm : AlarmState)
case class PanicAlarm(alarm : AlarmState)
case class GetPanicAlarmsMsg(userId : String)
case class ReportPanicAlarmsMsg(sender : ActorRef)

case class FallAlarmMsg(userId : String, alarm : AlarmState)
case class FallAlarm(alarm : AlarmState)
case class GetFallAlarmsMsg(userId : String)
case class ReportFallAlarmsMsg(sender : ActorRef)

case class GetLocationMsg(userId : String)
case class ReportLocationMsg(sender : ActorRef)

class UsersActor extends Actor{


  var users : Map[String, ActorRef] = Map.empty

  override def receive: Receive = {
    case PanicAlarmMsg(userId,alarm) => {
      if(!users.contains(userId)) users = users + (userId->Akka.system.actorOf(Props[UserActor],userId))
      users(userId) ! PanicAlarm(alarm)
    }
    case FallAlarmMsg(userId,alarm) => {
      if(!users.contains(userId)) users = users + (userId->Akka.system.actorOf(Props[UserActor],userId))
      users(userId) ! FallAlarm(alarm)
    }
    case GetPanicAlarmsMsg(userId) => {
      if(!users.contains(userId)) users = users + (userId->Akka.system.actorOf(Props[UserActor],userId))
      users(userId) ! ReportPanicAlarmsMsg(sender)
    }
    case GetFallAlarmsMsg(userId) => {
      if(!users.contains(userId)) users = users + (userId->Akka.system.actorOf(Props[UserActor],userId))
      users(userId) ! ReportFallAlarmsMsg(sender)
    }
    case GetLocationMsg(userId) => {
      if(!users.contains(userId)) users = users + (userId->Akka.system.actorOf(Props[UserActor],userId))
      users(userId) ! ReportLocationMsg(sender)
    }
  }
}

class UserActor extends Actor{

  var panicAlarms : List[AlarmState] = List.empty
  var fallAlarms : List[AlarmState] = List.empty

  var locations : List[Location] = List.empty

  override def receive: Actor.Receive = {
    case PanicAlarm(a) => {
      panicAlarms = a :: panicAlarms
      locations = a.location :: locations
    }
    case FallAlarm(a) => {
      fallAlarms = a :: fallAlarms
      locations = a.location :: locations
    }
    case ReportPanicAlarmsMsg(sender) => sender ! panicAlarms
    case ReportFallAlarmsMsg(sender) => sender ! fallAlarms
    case ReportLocationMsg(sender) => locations match {
      case Nil => sender ! None
      case head :: tail => sender ! Some(head)
    }
  }
}
