package me.fangjian.hackthon.service

import java.io.ByteArrayInputStream

import akka.util.Timeout
import me.fangjian.hackthon.Analyzer
import scala.collection.JavaConversions._
import me.fangjian.hackthon.db.DatabaseAccess
import org.json4s.NoTypeHints
import org.json4s.jackson.Serialization
import org.json4s.jackson.Serialization.{write => jsonWrite}
import spray.http.MediaTypes._
import spray.routing.HttpService


/**
 *
 * Created at 7/12/14
 * @author Jian Fang (jfang@rocketfuelinc.com)
 */
trait QueryService extends HttpService {
  val dao : DatabaseAccess
  import dao.profile.simple._

  implicit val formats = Serialization.formats(NoTypeHints)
  implicit val timeout : Timeout

  val routes = topNUser

  def sessionWrapper[T](f : (Session) => T) : T = dao.handler.withSession(f)

  def responseWrapper(handler : (Session) => AnyRef) = {
    rejectEmptyResponse{
      respondWithMediaType(`application/json`){
        complete{
          sessionWrapper(session => jsonWrite(handler(session)))
        }
      }
    }
  }

  def topNUser = post{path("query"){
    formFields("text", 'n.as[Int] ? 20){(text, n) =>
      responseWrapper{implicit session =>
        val desiredAttributes =
          Analyzer.jobFile.characterize(new ByteArrayInputStream(text.getBytes("utf-8"))).map{
            case (key, value) => (key.toString, value.toDouble)
          }.toMap[String, Double]
        dao.topNUser(n, desiredAttributes)
      }
    }
  }}
}
