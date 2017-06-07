package techietester

import io.gatling.http.Predef._
import io.gatling.core.Predef._

/**
  * Created by gsinghsaini on 07.06.17.
  */
class ComputerDatabase extends Simulation {

  val httpConf = http.baseURL("http://computer-database.gatling.io")

  val csvFeeder = csv("computerNames.csv").random

  object LoadHomePage{
    val homePage = exec(http("load home page").get("/").check(status.is(200)))
  }

  object AddNewComputer{
    val newComp = repeat(4){
      feed(csvFeeder)
      exec(http("name-${companyName},introduced-${introducedDate},discontinued-${discontinuedDate},company-${companyID}").post("/computers").check(status.is(200)))}
    //.formParam("name", "1A").formParam("introduced", "2016-01-01").formParam("discontinued", "2017-01-01").formParam("company", "1"))}
    //exec(http("add new computer-${computerName}-${introducedDate}-${discontinuedDate}-${companyID}").post("/computers").check(status.is(200)))}
  }

  val scn = scenario("Basic Simulation")
    .exec(LoadHomePage.homePage)
    .pause(5)
    .exec(AddNewComputer.newComp)
    .pause(5)

  setUp(scn.inject(atOnceUsers(1))).protocols(httpConf)

}
