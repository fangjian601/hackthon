package me.fangjian.hackthon.db

import me.fangjian.hackthon.Analyzer

import scala.slick.driver.JdbcProfile
import scala.slick.jdbc.JdbcBackend._

/**
 *
 * Created at 7/12/14
 * @author Jian Fang (jfang@rocketfuelinc.com)
 */
trait DatabaseProfile {
  val profile : JdbcProfile
  val driverClass : String
  val url : String

  private val user = Analyzer.config.getString("hackthon-analyzer.db.user")
  private val password = Analyzer.config.getString("hackthon-analyzer.db.password")

  def handler : Database = {
    Database.forURL(
      url = url,
      user = user,
      password = password,
      driver = driverClass
    )
  }
}
