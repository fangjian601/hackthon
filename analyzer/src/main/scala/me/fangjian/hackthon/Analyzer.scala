package me.fangjian.hackthon

import java.util.concurrent.atomic.AtomicInteger

import akka.actor.{Props, ActorRef, ActorSystem}
import akka.io.IO
import akka.pattern.ask
import akka.routing.FromConfig
import akka.util.Timeout
import com.typesafe.config.ConfigFactory
import io.derek.hackathon.characerizer.{JobProfileCharacterizer, GithubRepoCharacterizer}
import me.fangjian.hackthon.actor.{AnalysisActor, AnalysisHelper}
import me.fangjian.hackthon.db.DatabaseAccess
import me.fangjian.hackthon.service.ServiceActor
import spray.can.Http

import scala.concurrent.duration._
import scala.slick.driver.MySQLDriver


object Analyzer extends AnalysisHelper{
  val config = ConfigFactory.load()
  val characterizer = new GithubRepoCharacterizer()
  val jobFile = new JobProfileCharacterizer()

  val dao = getDAO(config.getString("hackthon-analyzer.db.host"),
    config.getInt("hackthon-analyzer.db.port"),
    config.getString("hackthon-analyzer.db.database"))
  implicit val system = ActorSystem("HackthonAnalyzerSystem")
  implicit val timeout = Timeout(config.getInt("hackthon-analyzer.timeout").seconds)

  def getDAO(host : String, port : Int, db : String) : DatabaseAccess = {
    new DatabaseAccess(MySQLDriver, s"jdbc:mysql://$host:$port/$db", "com.mysql.jdbc.Driver")
  }

  def startActor : ActorRef = {
    system.actorOf(Props(classOf[AnalysisActor], dao).withRouter(FromConfig), "analyzer")
  }

  def initDatabase() : Unit = {
    dao.handler.withSession{implicit session =>
      dao.createTables
    }
  }

  def startAnalyze(n : Int) : Unit = {
    val actorRef = startActor
    dao.handler.withSession{implicit session =>
      val actors = dao.topNActor(n)
      val repos = dao.reposByTopNActors(n)
      characterizeActors(actors, actorRef, new AtomicInteger(0), () => {
        system.shutdown()
      })
    }
  }

  def startService() : Unit = {
    val serviceActor = system.actorOf(Props(classOf[ServiceActor], dao, timeout), "HTTPServiceActor")
    IO(Http) ? Http.Bind(serviceActor,
      interface = config.getString("hackthon-analyzer.service.bind"),
      port = config.getInt("hackthon-analyzer.service.port"))
  }

  def main(args : Array[String]){
    initDatabase()
    //startAnalyze(3000)
    startService()
  }
}