package controllers

import java.net.{MalformedURLException, UnknownHostException, URL}

import actors._
import akka.actor.{Props, Actor}
import akka.actor.Actor.Receive
import akka.util.Timeout
import dataproviders.DengueDataProvider
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
case class Heartrate(bpm : Int, location : Location)
case class User(id : String)

trait JsonSerialization{

  implicit val locationWrites = Json.writes[Location]
  implicit val alarmStateWrites = Json.writes[AlarmState]
  implicit val locationReads = Json.reads[Location]
  implicit val alarmStateReads = Json.reads[AlarmState]
  implicit val heartrateReads = Json.reads[Heartrate]
  implicit val heartrateWrites = Json.writes[Heartrate]


}


object Application extends Controller with JsonSerialization{

  lazy val usersActor = Akka.system.actorOf(Props[UsersActor], "usersmanager")
  implicit val timeout : akka.util.Timeout = Timeout(4, SECONDS)
  val dengueData = new DengueDataProvider()

  def healthcheck = Action{
    Ok("hello")
  }

  def reportHeartrate(id : String) = Action(parse.json){
    request => {
      usersActor ! HeartrateMsg(id, request.body.validate[Heartrate].get)
      Created("ok")
    }
  }

  def getHeartrate(id : String) = Action.async{
    (usersActor ? GetHeartrateMsg(id)).mapTo[List[Heartrate]]
      .map(heartrate => Ok(Json.toJson(heartrate)))

  }

  def addFallAlarm(id : String) = Action(parse.json){
    request => {
      usersActor ! FallAlarmMsg(id, request.body.validate[AlarmState].get)
      Created("ok")
    }
  }

  def addPanicAlarm(id : String) = Action(parse.json){
    request => {
      usersActor ! PanicAlarmMsg(id, request.body.validate[AlarmState].get)
      Created("ok")
    }
  }

  def getFallAlarms(id : String) = Action.async{
    (usersActor ? GetFallAlarmsMsg(id)).mapTo[List[AlarmState]]
      .map(alarms => Ok(Json.toJson(alarms)))
  }

  def getPanicAlarms(id : String) = Action.async{
    (usersActor ? GetPanicAlarmsMsg(id)).mapTo[List[AlarmState]]
      .map(alarms => Ok(Json.toJson(alarms)))
  }

  def userDashboard(id : String) = Action{
    Ok(views.html.userDashboard(id))
  }

  def userDengue(id : String) = Action{
    Ok(views.html.userDengue(id))
  }

  def getUserLocation(id : String) = Action.async{
    (usersActor ? GetLocationMsg(id)).mapTo[Option[Location]].map{
      case None => NotFound("no location found")
      case Some(location) => Ok(Json.toJson(location))
    }
  }

  def getDengueClusters(format : Option[String]) = Action{
    Ok(dengueData.getDengueXml).as("text/xml");
  }


}