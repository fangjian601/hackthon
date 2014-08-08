package me.fangjian.hackthon.model

import me.fangjian.hackthon.db.DatabaseProfile

/**
 *
 * Created at 7/12/14
 * @author Jian Fang (jfang@rocketfuelinc.com)
 */
trait RepoScoreModel {
  this : DatabaseProfile with RepoActorModel =>
  case class RepoScore(name : String, owner : String, attribute : String, score : Double)
  import profile.simple._
  class RepoScores(tag : Tag) extends Table[RepoScore](tag, "repo_scores"){
    def name = column[String]("name", O.NotNull)
    def owner = column[String]("owner", O.NotNull)
    def attribute = column[String]("attribute", O.NotNull)
    def score = column[Double]("score", O.NotNull)
    def pKey = primaryKey("", (name, owner, attribute))
    def * = (name, owner, attribute, score) <> (RepoScore.tupled, RepoScore.unapply)
  }
  val repoScores = TableQuery[RepoScores]

  def repoScoresByActor(actor : String)(implicit session : Session) : Seq[(RepoScore, Int)] = {
    (for{
      ra <- repoActors
      rs <- repoScores if ra.name === rs.name && ra.owner === rs.owner && ra.actor === actor
    } yield (rs, ra.amount)).list
  }
}
