package actors

import akka.actor.{Props, Actor, ActorRef}
import controllers.{Heartrate, Location, AlarmState}
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

case class HeartrateMsg(userId : String, heartrate : Heartrate)
case class GetHeartrateMsg(userId : String)
case class ReportHeartrateMsg(sender : ActorRef)

case class GetLocationMsg(userId : String)
case class ReportLocationMsg(sender : ActorRef)

class UsersActor extends Actor{


  var users : Map[String, ActorRef] = Map.empty

  def createUserIfNeeded(userId : String): Unit ={
    if(!users.contains(userId)) users = users + (userId->Akka.system.actorOf(Props[UserActor],userId))

  }

  override def receive: Receive = {
    case HeartrateMsg(userId, heartrate) => {
      createUserIfNeeded(userId)
      users(userId) ! heartrate
    }
    case PanicAlarmMsg(userId,alarm) => {
      createUserIfNeeded(userId)
      users(userId) ! PanicAlarm(alarm)
    }
    case FallAlarmMsg(userId,alarm) => {
      createUserIfNeeded(userId)
      users(userId) ! FallAlarm(alarm)
    }
    case GetHeartrateMsg(userId) => {
      createUserIfNeeded(userId)
      users(userId) ! ReportHeartrateMsg(sender)
    }
    case GetPanicAlarmsMsg(userId) => {
      createUserIfNeeded(userId)
      users(userId) ! ReportPanicAlarmsMsg(sender)
    }
    case GetFallAlarmsMsg(userId) => {
      createUserIfNeeded(userId)
      users(userId) ! ReportFallAlarmsMsg(sender)
    }
    case GetLocationMsg(userId) => {
      createUserIfNeeded(userId)
      users(userId) ! ReportLocationMsg(sender)
    }
  }
}

class UserActor extends Actor{

  var panicAlarms : List[AlarmState] = List.empty
  var fallAlarms : List[AlarmState] = List.empty
  var heartrates : List[Heartrate] = List.empty

  var locations : List[Location] = List.empty

  override def receive: Actor.Receive = {
    case h : Heartrate => {
      heartrates = h :: heartrates
      locations = h.location :: locations
    }
    case PanicAlarm(a) => {
      panicAlarms = a :: panicAlarms
      locations = a.location :: locations
    }
    case FallAlarm(a) => {
      fallAlarms = a :: fallAlarms
      locations = a.location :: locations
    }
    case ReportHeartrateMsg(sender) => sender ! heartrates
    case ReportPanicAlarmsMsg(sender) => sender ! panicAlarms
    case ReportFallAlarmsMsg(sender) => sender ! fallAlarms
    case ReportLocationMsg(sender) => locations match {
      case Nil => sender ! None
      case head :: tail => sender ! Some(head)
    }
  }
}
