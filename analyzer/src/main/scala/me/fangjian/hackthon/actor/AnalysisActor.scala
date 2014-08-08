package me.fangjian.hackthon.actor

import akka.actor.{Actor, ActorLogging}
import me.fangjian.hackthon.db.DatabaseAccess

/**
 *
 * Created at 7/12/14
 * @author Jian Fang (jfang@rocketfuelinc.com)
 */

case class FetchRepo(name : String, owner : String, callback : () => Unit)
case class FetchActor(actor : String, callback : () => Unit)

class AnalysisActor(override val dao : DatabaseAccess) extends Actor
  with ActorLogging with AnalysisHelper{
  def receive = {
    case FetchRepo(name, owner, callback) =>
      characterizeRepo(name, owner)
      callback()
    case FetchActor(actor, callback) =>
      characterizeActor(actor)
      callback()
  }
}
