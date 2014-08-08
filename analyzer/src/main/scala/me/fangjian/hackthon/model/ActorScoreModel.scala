package me.fangjian.hackthon.model

import me.fangjian.hackthon.db.DatabaseProfile

import scala.util.Random

/**
 *
 * Created at 7/12/14
 * @author Jian Fang (jfang@rocketfuelinc.com)
 */
trait ActorScoreModel {
  this : DatabaseProfile =>
  case class ActorScore(actor : String, attribute : String, score : Double)
  import profile.simple._
  class ActorScores(tag : Tag) extends Table[ActorScore](tag, "actor_scores"){
    def actor = column[String]("actor")
    def attribute = column[String]("attribute")
    def score = column[Double]("score")
    def pKey = primaryKey("", (actor, attribute))
    def * = (actor, attribute, score) <> (ActorScore.tupled, ActorScore.unapply)
  }
  val actorScores = TableQuery[ActorScores]

  def topNUser(n : Int, desiredAttributes : Map[String, Double])(implicit session : Session) : Seq[Any] = {
    actorScores.filter(_.attribute inSet desiredAttributes.keys).list.groupBy(_.actor).map {
      case (actor, scores) =>
        (actor, scores, scores.map((score) => {
          score.score * desiredAttributes(score.attribute)
        }).sum)
    }.toSeq.sortBy(0 - _._3).take(n).map {
      case (actor, scores, _) =>
        Map(
          "user_id" -> actor,
          "attributes" -> scores.map((score) => {
            Map(
              "name" -> score.attribute,
              "score" -> (score.score + Random.nextDouble()) * 50
            )
          })
        )
    }.toSeq
  }
}
