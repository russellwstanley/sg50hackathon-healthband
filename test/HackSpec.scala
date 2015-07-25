
import controllers.{JsonSerialization, Location, AlarmState}
import org.scalatestplus.play.{OneAppPerSuite, PlaySpec}
import play.api.test._
import play.api.test.Helpers._
import play.api.libs.json._

class HacksSpec extends PlaySpec with OneAppPerSuite with JsonSerialization{

  "GET /healthcheck" should {
    "return ok" in {
      status(route(FakeRequest(GET,"/healthcheck")).get) must equal(OK)
    }



  }

  "Adding and getting a panicalarm" should {
    "work properly " in {
      val userid = "testuser"
      val alarm = AlarmState(0,Location(12345.6,23456.98))
      val postResult = route(FakeRequest(POST,s"/api/users/$userid/events/panic").withJsonBody(Json.toJson(alarm))).get
      status(postResult) must equal(CREATED)
      val getResult = route(FakeRequest(GET,s"/api/users/$userid/events/panic")).get
      contentAsJson(getResult).validate[List[AlarmState]].get must equal(List(alarm))
    }
  }
  "Adding and getting a fall alarm" should {
    "work properly " in {
      val userid = "testfalluser"
      val alarm = AlarmState(0,Location(12345.6,23456.98))
      val postResult = route(FakeRequest(POST,s"/api/users/$userid/events/fall").withJsonBody(Json.toJson(alarm))).get
      status(postResult) must equal(CREATED)
      val getResult = route(FakeRequest(GET,s"/api/users/$userid/events/fall")).get
      contentAsJson(getResult).validate[List[AlarmState]].get must equal(List(alarm))
    }
  }
  "Getting a users location" should {
    "return 404 if a user has had no update events" in {
      val userId = "testuser2"
      val getResult = route(FakeRequest(GET,s"/api/users/$userId/location")).get
      status(getResult) must equal(NOT_FOUND)
    }
    "return the location of the last event if the user has events" in {
      val userId = "testuser3"
      val location = Location(1.1,2.2)
      val alarm = AlarmState(0,location)
      val postResult = route(FakeRequest(POST,s"/api/users/$userId/events/panic").withJsonBody(Json.toJson(alarm))).get
      Thread.sleep(500)
      val getResult = route(FakeRequest(GET,s"/api/users/$userId/location")).get
      contentAsJson(getResult).validate[Location].get must equal(location)
    }
  }
}
