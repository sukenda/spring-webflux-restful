package auth

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.concurrent.duration.DurationInt
import scala.language.postfixOps

/**
 * https://www.james-willett.com/gatling-load-testing-complete-guide/
 */
class SignupSimulation extends Simulation {

  object randomStringGenerator {
     def randomString(length: Int): String = scala.util.Random.alphanumeric.filter(_.isLetter).take(length).mkString
  }

  private val httpConf = http
    .baseUrl("http://167.99.70.48:9090")
    .header(HttpHeaderNames.Accept, "application/json")
    .header(HttpHeaderNames.ContentType, "application/json")

  private val maps = Iterator.continually(Map(
    "username" -> randomStringGenerator.randomString(10),
    "password" -> randomStringGenerator.randomString(10),
    "email" -> randomStringGenerator.randomString(10).concat("@gmail.com"),
    "profileName" -> randomStringGenerator.randomString(15),
    "roles" -> randomStringGenerator.randomString(6).toUpperCase,
  ))

  private val scn = scenario("SignupSimulation")
    .feed(maps)
    .exec(http("Signup")
      .post("/auth/signup")
      .body(ElFileBody("auth/register.json")).asJson
      .check(status.is(200))
    )

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
