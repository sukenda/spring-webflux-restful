package auth

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.concurrent.duration.DurationInt
import scala.language.postfixOps

/**
 * https://www.james-willett.com/gatling-load-testing-complete-guide/
 */
class TokenSimulation extends Simulation {

  object randomStringGenerator {
    def randomString(length: Int): String = scala.util.Random.alphanumeric.filter(_.isLetter).take(length).mkString
  }

  private val httpConf = http
    .baseUrl("http://167.99.70.48:9090")
    .header(HttpHeaderNames.Accept, "application/json")
    .header(HttpHeaderNames.ContentType, "application/json")

  private val scn = scenario("TokenSimulation")
    .exec(http("Token")
      .post("/auth/token")
      .body(ElFileBody("auth/login.json")).asJson
    ).pause(10)

  setUp(
    scn.inject(
      nothingFor(5 seconds),
      atOnceUsers(10),
      rampUsers(50) during (30 second)
    ).protocols(httpConf)
  ).maxDuration(1 minute)
    .assertions(
      //global.responseTime.max.lt(300),
      global.successfulRequests.percent.gt(95)
    )
}
