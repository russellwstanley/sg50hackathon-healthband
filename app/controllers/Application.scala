package controllers

import java.net.{MalformedURLException, UnknownHostException, URL}

import actors.{GetAlarmsMsg, AlarmMsg, UsersActor}
import akka.actor.{Props, Actor}
import akka.actor.Actor.Receive
import akka.util.Timeout
import play.api._
import play.api.libs.json.Json
import play.api.mvc._
import scala.concurrent.ExecutionContext.Implicits.global
import play.api.libs.concurrent.Akka
import play.api.Play.current
import akka.pattern.ask
import java.util.concurrent.TimeUnit.SECONDS

import scala.concurrent.Future


case class Location(latitude : Double, longitude : Double)
case class AlarmState(state : Int, location : Location)
case class User(id : String)

trait JsonSerialization{

  implicit val locationWrites = Json.writes[Location]
  implicit val alarmStateWrites = Json.writes[AlarmState]
  implicit val locationReads = Json.reads[Location]
  implicit val alarmStateReads = Json.reads[AlarmState]

}


object Application extends Controller with JsonSerialization{

  lazy val usersActor = Akka.system.actorOf(Props[UsersActor], "usersmanager")
  implicit val timeout : akka.util.Timeout = Timeout(4, SECONDS)

  def healthcheck = Action{
    Ok("hello")
  }

  def addAlarm(id : String) = Action(parse.json){
    request => {
      usersActor ! AlarmMsg(id, request.body.validate[AlarmState].get)
      Created("ok")
    }
  }

  def getAlarms(id : String) = Action.async{
    (usersActor ? GetAlarmsMsg(id)).mapTo[List[AlarmState]]
      .map(alarms => Ok(Json.toJson(alarms)))
  }

  def userDashboard(id : String) = Action{
    Ok(views.html.UserDashboard(id))
  }

  def overviewDashboard() = Action{
    Ok(views.html.OverviewDashboard("hello"))
  }

}