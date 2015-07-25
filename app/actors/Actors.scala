package actors

import akka.actor.{Props, Actor, ActorRef}
import controllers.AlarmState
import play.api.libs.concurrent.Akka
import scala.concurrent.ExecutionContext.Implicits.global
import play.api.Play.current


case class AlarmMsg(userId : String, alarm : AlarmState)
case class GetAlarmsMsg(userId : String)
case class ReportAlarmsMsg(sender : ActorRef)

class UsersActor extends Actor{


  var users : Map[String, ActorRef] = Map.empty

  override def receive: Receive = {
    case AlarmMsg(userId,alarm) => {
      if(!users.contains(userId)) users = users + (userId->Akka.system.actorOf(Props[UserActor],userId))
      users(userId) ! alarm
    }
    case getAlarms@GetAlarmsMsg(userId) => {
      if(!users.contains(userId)) users = users + (userId->Akka.system.actorOf(Props[UserActor],userId))
      users(userId) ! ReportAlarmsMsg(sender)
    }
  }
}

class UserActor extends Actor{

  var alarms : List[AlarmState] = List.empty

  override def receive: Actor.Receive = {
    case a : AlarmState => alarms = a :: alarms
    case ReportAlarmsMsg(sender) => sender ! alarms
  }
}
