package controllers

import java.net.{MalformedURLException, UnknownHostException, URL}

import play.api._
import play.api.libs.json.Json
import play.api.mvc._
import scala.concurrent.ExecutionContext.Implicits.global

case class Location(lat : Int, long : Int)
case class AlarmState(state : Int, location : Location)

object Application extends Controller {

  def healthcheck = Action{
    Ok("hello")
  }

  def addAlarm = Action{
    Ok("hello")
  }

}