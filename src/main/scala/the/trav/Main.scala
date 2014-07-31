package the.trav

import unfiltered.response._
import unfiltered.request._
import org.json4s._
import org.json4s.jackson.JsonMethods._
import org.json4s.jackson.Serialization
import org.json4s.jackson.Serialization.write
import scala.util.Random

object Main extends App {
  implicit val formats = Serialization.formats(NoTypeHints)
  import DB._
  import HttpServer._
  implicit val dataSource = defaultDataSource(System.getenv("DATABASE_URL"))
  withDatabase { personStore =>
    withServer(System.getenv("PORT").toInt) {
      case GET(Path("/people")) =>
        val json = write(personStore.all)
        Ok ~> JsonContent ~> ResponseString(json)

      case DELETE(Path(Seg("people" :: name :: Nil))) =>
        personStore.delete(name)
        Accepted

      case req @ POST(Path("/people")) =>
        val body = Body.string(req)
        val person = parse(body).extract[Person]
        personStore.add(person)
        Created

      case POST(Path(Seg("people" :: count :: Nil))) =>
        (1 to count.toInt).foreach { i =>
          personStore.add(Person(s"${Random.nextInt}", Math.abs(Random.nextInt)))
        }
        Created
    }
  }
}

