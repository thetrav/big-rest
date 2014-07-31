package the.trav

import scala.slick.driver.PostgresDriver.simple._
import scala.slick.jdbc.meta.MTable

import org.apache.commons.dbcp2.BasicDataSource


object DB {

  def defaultDataSource(settings: String): BasicDataSource = {
    val dbUri = new java.net.URI(settings)
    val parts = dbUri.getUserInfo().split(":")

    val ds = new BasicDataSource
    ds.setDriverClassName("org.postgresql.Driver")
    ds.setUsername(parts(0))
    ds.setPassword(parts(1))
    ds.setMaxTotal(10)
    ds.setInitialSize(10)
    ds.setValidationQuery("SELECT 1")
    new java.io.File("target").mkdirs // ensure that folder for database exists

    val host = dbUri.getHost
    val port = dbUri.getPort
    val path = dbUri.getPath
    ds.setUrl(s"jdbc:postgresql://$host:$port$path")
    ds
  }

  def withDatabase(program: PersonStore => Unit)(implicit dataSource: BasicDataSource){
    val db = Database.forDataSource(dataSource)

    class People(tag: Tag) extends Table[(String, Int)](tag, "PEOPLE") {
      def name = column[String]("NAME")
      def years = column[Int]("YEARS")
      // Every table needs a * projection with the same type as the table's type parameter
      def * = (name, years)
    }
    val people = TableQuery[People] 

    db withSession { implicit session =>
      if (MTable.getTables("PEOPLE").list().isEmpty) {
        people.ddl.create
        people += ("first person", 1)
      }
    }

    val personStore = new PersonStore {
      def all: List[Person] = {
        db withSession { implicit session =>
          people.list.map(Person.tupled)
        }
      }

      def add(person: Person) {
        db withSession { implicit session =>
          people += (person.name, person.years)
        }
      }

      def delete(name: String) {
        db withSession { implicit session =>
          people.filter(_.name.like(name)).delete
        }
      }
    }
    program(personStore)
  }

  trait PersonStore {
    def all: List[Person]
    def add(person: Person): Unit
    def delete(name: String): Unit
  }
}
