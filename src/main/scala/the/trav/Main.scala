package the.trav

import unfiltered.response._
import unfiltered.request._
import org.json4s._
import org.json4s.jackson.JsonMethods._
import org.json4s.jackson.Serialization
import org.json4s.jackson.Serialization.write

object Main extends App {
  implicit val formats = Serialization.formats(NoTypeHints)

  DB.withDatabase { personStore =>
    HttpServer.withServer(80) {
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
    }
  }
}

