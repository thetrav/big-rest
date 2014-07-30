package the.trav

import scala.slick.driver.PostgresDriver.simple._
import scala.slick.jdbc.meta.MTable

object DB {

def dbUrl = {
  val dbUri = new java.net.URI(System.getenv("DATABASE_URL"))
  val parts = dbUri.getUserInfo().split(":")

  val username = parts(0)
  val password = parts(1)
  val host = dbUri.getHost
  val port = dbUri.getPort
  val path = dbUri.getPath
  s(username, password, "jdbc:postgresql://$host:$port$path")
}


  def withDatabase(program: PersonStore => Unit) {
    Database.forURL(dbUrl._3, driver = "org.postgresql.Driver", user = dbUrl._1, password = dbUrl._2) withSession { implicit session =>
      class People(tag: Tag) extends Table[(String, Int)](tag, "PEOPLE") {
        def name = column[String]("NAME")
        def years = column[Int]("YEARS")
        // Every table needs a * projection with the same type as the table's type parameter
        def * = (name, years)
      }
      val people = TableQuery[People]


      if (MTable.getTables("PEOPLE").list().isEmpty) {
        people.ddl.create
      }

      people += ("first person", 1)
      val personStore = new PersonStore {
        def all: List[Person] = {
          people.list.map(Person.tupled)
        }

        def add(person: Person) {
          people += (person.name, person.years)
        }

        def delete(name: String) {
          people.filter(_.name.like(name)).delete
        }
      }
      program(personStore)
    }
  }

  trait PersonStore {
    def all: List[Person]
    def add(person: Person): Unit
    def delete(name: String): Unit
  }
}
