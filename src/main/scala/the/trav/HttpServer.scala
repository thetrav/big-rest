package the.trav

import unfiltered.netty.Http
import unfiltered.netty.cycle._

object HttpServer {
  def withServer(port: Int)(handler: Plan.Intent) {
    val server = Http(port).plan(Planify(handler))
    server.start()
    //readLine(s"server starting on $port enter to stop")
    //server.stop()
    println(s"server starting on $port")
   
  }
}
