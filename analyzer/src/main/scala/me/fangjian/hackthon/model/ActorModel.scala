package me.fangjian.hackthon.model

import me.fangjian.hackthon.db.DatabaseProfile


/**
 *
 * Created at 7/12/14
 * @author Jian Fang (jfang@rocketfuelinc.com)
 */
trait ActorModel {
  this : DatabaseProfile =>
  case class Actor(actor : String, total : Int)
  import profile.simple._
  class Actors(tag : Tag) extends Table[Actor](tag, "actors"){
    def actor = column[String]("actor", O.PrimaryKey)
    def total = column[Int]("total")
    def * = (actor, total) <> (Actor.tupled, Actor.unapply)
  }
  val actors = TableQuery[Actors]

  def topNActor(n : Int)(implicit session : Session) : Seq[String] = {
    actors.sortBy(_.total.desc).map(_.actor).take(n).list
  }
}
