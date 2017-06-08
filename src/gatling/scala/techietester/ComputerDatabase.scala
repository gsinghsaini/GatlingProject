package techietester

import io.gatling.http.Predef._
import io.gatling.core.Predef._

/**
  * Created by gsinghsaini on 07.06.17.
  */
class ComputerDatabase extends Simulation {

  val httpConf = http.baseURL("http://computer-database.gatling.io")

  val csvFeeder = csv("computerNames.csv").circular

  object LoadHomePage{
    val homePage = exec(http("load home page").get("/").check(status.is(200)))
  }

  object AddNewComputer{
    val newComp = repeat(4){
      feed(csvFeeder)
        .exec(http("add new computer")
          .post("/computers")
          .formParam("name", "${ComputerName}").formParam("introduced", "${IntroducedDate}").formParam("discontinued", "${DiscontinuedDate}").formParam("company", "${CompanyID}").check(status.is(200)))
    }
  }

  val scn = scenario("Basic Simulation")
    .exec(LoadHomePage.homePage)
    .pause(5)
    .exec(AddNewComputer.newComp)
    .pause(5)

  setUp(scn.inject(atOnceUsers(1))).protocols(httpConf)

}