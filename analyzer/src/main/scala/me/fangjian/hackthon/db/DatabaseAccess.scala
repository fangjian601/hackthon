package me.fangjian.hackthon.db

import me.fangjian.hackthon.model._

import scala.slick.driver.JdbcProfile
import scala.slick.jdbc.meta.MTable

/**
 *
 * Created at 7/12/14
 * @author Jian Fang (jfang@rocketfuelinc.com)
 */
class DatabaseAccess(override val profile : JdbcProfile,
                     override val url : String,
                     override val driverClass : String) extends DatabaseProfile
  with RepoModel with RepoActorModel
  with RepoScoreModel with ActorScoreModel with ActorModel{

  import profile.simple._

  def createTables(implicit session : Session) : Unit = {
    val tables = Map(
      (actors.baseTableRow.tableName, actors.ddl),
      (repos.baseTableRow.tableName, repos.ddl),
      (repoActors.baseTableRow.tableName, repoActors.ddl),
      (repoScores.baseTableRow.tableName, repoScores.ddl),
      (actorScores.baseTableRow.tableName, actorScores.ddl)
    )
    tables.keys.foreach((name) => {
      if(MTable.getTables(name).list.isEmpty){
        tables(name).create
      }
    })
  }
}
