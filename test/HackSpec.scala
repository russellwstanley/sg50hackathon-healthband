
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

  "Adding and getting an alarm" should {
    "work properly " in {
      val userid = "testuser"
      val alarm = AlarmState(0,Location(12345,23456))
      val postResult = route(FakeRequest(POST,s"/users/$userid/events/alarms").withJsonBody(Json.toJson(alarm))).get
      status(postResult) must equal(CREATED)
      val getResult = route(FakeRequest(GET,s"/users/$userid/events/alarms")).get
      contentAsJson(getResult).validate[List[AlarmState]].get must equal(List(alarm))
    }
  }
}
