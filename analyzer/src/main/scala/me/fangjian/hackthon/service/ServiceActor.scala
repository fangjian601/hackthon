package me.fangjian.hackthon.service

import akka.actor.{ActorRefFactory, ActorLogging, Actor}
import akka.util.Timeout
import com.gettyimages.spray.swagger.SwaggerHttpService
import com.wordnik.swagger.model.ApiInfo
import me.fangjian.hackthon.db.DatabaseAccess

/**
 *
 * Created at 7/12/14
 * @author Jian Fang (jfang@rocketfuelinc.com)
 */
class ServiceActor(override val dao : DatabaseAccess,  override val timeout : Timeout)
  extends Actor with ActorLogging with QueryService{
  override def actorRefFactory: ActorRefFactory = context
  override def receive = runRoute(routes ~
    get {
      pathPrefix("") { pathEndOrSingleSlash {
        getFromResource("GitMatch-app/index.html")
      }
      } ~
        getFromResourceDirectory("GitMatch-app")
    })
}