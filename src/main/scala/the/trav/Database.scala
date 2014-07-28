package the.trav

import java.sql.{DriverManager, Connection}

object Database {

  def withDatabase(program: PersonStore => Unit) {

    Class.forName("org.h2.Driver")
    val conn = DriverManager.getConnection("jdbc:h2:~/test", "sa", "")
    try {
      conn.prepareCall("DROP TABLE IF EXISTS PEOPLE").executeUpdate()
      conn.prepareCall( "CREATE TABLE PEOPLE ( NAME varchar(255) NOT NULL, YEARS int NOT NULL)").executeUpdate()
      val personStore = PersonStore(conn)

      personStore.add(Person("first person", 1))
      program(personStore)

    } finally conn.close()
  }

  case class PersonStore(conn: Connection) {
    def all: List[Person] = {
      val call = conn.prepareCall("select * from PEOPLE")
      val rs = call.executeQuery
      var list = List[Person]()
      while(rs.next) {
        list = list :+ Person(rs.getString("NAME"), rs.getInt("YEARS"))
      }
      list
    }

    def add(person: Person) {
      conn.prepareCall(s"INSERT INTO PEOPLE (NAME, YEARS) values ('${person.name}', ${person.years})").executeUpdate()
    }

    def delete(name: String) {
      conn.prepareCall(s"DELETE FROM PEOPLE WHERE NAME = '$name'").executeUpdate()
    }
  }
}
