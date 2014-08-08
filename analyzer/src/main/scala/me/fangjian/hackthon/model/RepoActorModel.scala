package me.fangjian.hackthon.model

import me.fangjian.hackthon.db.DatabaseProfile

/**
 *
 * Created at 7/12/14
 * @author Jian Fang (jfang@rocketfuelinc.com)
 */
trait RepoActorModel {
  this : DatabaseProfile with ActorModel =>
  case class RepoActor(name : String, owner : String, actor : String, amount : Int)
  import profile.simple._
  class RepoActors(tag : Tag) extends Table[RepoActor](tag, "repo_actors"){
    def name = column[String]("name")
    def owner = column[String]("owner")
    def actor = column[String]("actor")
    def amount = column[Int]("amount")
    def pKey = primaryKey("", (name, owner, actor))
    def * = (name, owner, actor, amount) <> (RepoActor.tupled, RepoActor.unapply)
  }
  val repoActors = TableQuery[RepoActors]

  def actorRepos(actor : String)(implicit session : Session) : Seq[(String, String, Int)] = {
    repoActors.filter(_.actor === actor).list.groupBy((repo) =>{
      (repo.name, repo.owner)
    }).map{
      case ((name, owner), repos) => (name, owner, repos.head.amount)
    }.toSeq
  }

  def reposByTopNActors(n : Int)(implicit session : Session) : Seq[(String, String)] = {
    (for{
      a <- actors.sortBy(_.total.desc).map(_.actor).take(n)
      r <- repoActors if r.actor === a
    } yield (r.name, r.owner)).groupBy((repo) => (repo._1, repo._2)).map(_._1).list
  }
}
