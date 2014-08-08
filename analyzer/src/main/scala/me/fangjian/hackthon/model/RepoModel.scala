package me.fangjian.hackthon.model

import java.sql.Blob

import me.fangjian.hackthon.db.DatabaseProfile

trait RepoModel{
  this : DatabaseProfile =>
  case class Repo(name : String, owner : String, language : String,
                  homepage : Option[Blob], amount : Int, description : Option[Blob]){
    def toMap : Map[String, String] = {
      val homepageValue = homepage match {
        case Some(b) => new String(b.getBytes(1, b.length.toInt))
        case _ => ""
      }
      val descriptionValue = description match{
        case Some(d) => new String(d.getBytes(1, d.length.toInt))
        case _ => ""
      }
      Map[String, String](
        "name" -> name,
        "owner" -> owner,
        "language" -> language,
        "homepage" -> homepageValue,
        "description" -> descriptionValue
      )
    }
  }
  import profile.simple._
  class Repos(tag : Tag) extends Table[Repo](tag, "repos"){
    def name = column[String]("name")
    def owner = column[String]("owner")
    def language = column[String]("language")
    def homepage = column[Blob]("homepage", O.Nullable)
    def amount = column[Int]("amount")
    def description = column[Blob]("description", O.Nullable)
    def pKey = primaryKey("", (name, owner))

    def * = (name, owner, language, homepage.?, amount, description.?) <> (Repo.tupled, Repo.unapply)
  }
  val repos = TableQuery[Repos]

  def repoByNameAndOwner(name : String, owner : String)(implicit session : Session) : Option[Repo] = {
    repos.filter(_.name === name).filter(_.owner === owner).firstOption
  }
}