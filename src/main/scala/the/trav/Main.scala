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

      case GET(Path("/people.html")) =>
      val script = """
      $(function() {
        $("#go").click(function() {
          $.get("http://thetrav-test.apigee.net/people", function(data) {
            var sum = _.reduce(data, function(accum, person) { return accum + person.years}, 0);
            $("#output").html(data.length+" elements with "+ (sum/data.length) + " average years");
          });
        });
      });
      """
        Ok ~> Html(
<html>
  <head>
    <title>yo</title>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
    <script src="http://underscorejs.org/underscore-min.js"></script>
    <script>{xml.Unparsed(script)}</script>
  </head>
  <body>
    <input type="button" value="Go" id="go"/>
    <div id="output"></div>
  </body>
</html>
)
    }
  }
}

