package post

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.concurrent.duration.DurationInt
import scala.language.postfixOps

/**
 * https://www.james-willett.com/gatling-load-testing-complete-guide/
 */
class CreatePostSimulation extends Simulation {

  object randomStringGenerator {
    def randomString(length: Int): String = scala.util.Random.alphanumeric.filter(_.isLetter).take(length).mkString
  }

  private val httpConf = http
    .baseUrl("http://167.99.70.48:9090")
    .header(HttpHeaderNames.Authorization, "Bearer eyJhbGciOiJIUzUxMiJ9.eyJjcmVhdGVkIjoxNjA1MTU5NDkzNjQ0LCJyb2xlcyI6WyJBRE1JTiJdLCJ1c2VybmFtZSI6InN1a2VuZGEiLCJzdWIiOiJzdWtlbmRhIiwiaWF0IjoxNjA1MTU5NDkzLCJleHAiOjE2MDUxODgyOTN9.R3scr3hOqzjuyosNXhUGQIEAcJ9pPOg0KynEZ3WIefxkmvMTS3rlIj2Wsa9udJ-mHvJFdSEjUke4U9qcGgVnOw")
    .header(HttpHeaderNames.Accept, "application/json")
    .header(HttpHeaderNames.ContentType, "application/json")

  private val maps = Iterator.continually(Map(
    "title" -> randomStringGenerator.randomString(5),
    "name" -> randomStringGenerator.randomString(5),
  ))

  private val scn = scenario("CreatePostSimulation")
    .feed(maps)
    .exec(http("CreatePost")
      .post("/posts")
      .body(ElFileBody("post/post.json")).asJson
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
