
import org.scalatestplus.play.{OneAppPerSuite, PlaySpec}
import play.api.test._
import play.api.test.Helpers._

class HacksSpec extends PlaySpec with OneAppPerSuite{

  "GET /healthcheck" should {
    "return ok" in {
      status(route(FakeRequest(GET,"/healthcheck")).get) must equal(OK)
    }



  }
}
