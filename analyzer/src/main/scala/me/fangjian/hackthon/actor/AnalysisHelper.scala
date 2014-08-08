package me.fangjian.hackthon.actor

import java.util.concurrent.atomic.AtomicInteger

import akka.actor.ActorRef
import me.fangjian.hackthon.Analyzer
import me.fangjian.hackthon.db.DatabaseAccess

import collection.JavaConversions._

/**
 *
 * Created at 7/12/14
 * @author Jian Fang (jfang@rocketfuelinc.com)
 */
trait AnalysisHelper {
  val dao : DatabaseAccess

  import dao._
  import dao.profile.simple._

  def characterizeRepo(name : String, owner : String) : Unit = {
    dao.handler.withSession{implicit session =>
      dao.repoByNameAndOwner(name, owner).foreach((repo) => {
        val results = Analyzer.characterizer.characterize(repo.toMap)
        dao.repoScores ++= results.map{
          case (attribute, score) => RepoScore(name, owner, attribute, score)
        }
      })
    }
  }

  def characterizeRepos(repos : Seq[(String, String)], actor : ActorRef,
                        counter : AtomicInteger, callback : () => Unit) : Unit = {
    val length = repos.length
    counter.set(length)
    repos.foreach{
      case(name, owner) =>
        actor ! FetchRepo(name, owner, () => {
          val remains = counter.decrementAndGet()
          if((length - remains) % 1000 == 0){
            println(s"analyzed ${length - remains}/${length} repos")
          }
          if(remains == 0){
            println("repos analyzing finished")
            callback()
          }
        })
    }
  }

  def characterizeActor(actor : String) : Unit = {
    dao.handler.withSession{implicit session =>
      dao.actorScores ++= aggregateRepoScores(dao.repoScoresByActor(actor)).map{
        case (attribute, score) => ActorScore(actor, attribute, score)
      }
    }
  }

  def characterizeActors(actors : Seq[String], actorRef : ActorRef,
                         counter : AtomicInteger, callback : () => Unit) : Unit = {
    val length = actors.length
    counter.set(length)
    actors.foreach((actor) => {
      actorRef ! FetchActor(actor, () => {
        val remain = counter.decrementAndGet()
        if((length - remain) % 1000 == 0){
          println(s"analyzed ${length - remain}/${length} actors")
        }
        if(remain == 0){
          println("actor analyzing finished")
          callback()
        }
      })
    })
  }

  def aggregateRepoScores(repoScores : Seq[(RepoScore, Int)]) : Seq[(String, Double)] = {
    repoScores.groupBy(_._1.attribute).map{
      case (attribute, scores) =>
        val rawScore : Double = scores.map{
          case (s, amount) => s.score * amount
        }.sum
        val maxAmount : Int = scores.map(_._2).sum
        (attribute, rawScore / maxAmount)
    }.toSeq
  }
}

