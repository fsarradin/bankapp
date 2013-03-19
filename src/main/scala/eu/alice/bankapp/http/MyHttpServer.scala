package eu.alice.bankapp.http

import com.sun.net.httpserver.{HttpExchange, HttpHandler, HttpContext, HttpServer}
import java.nio.charset.Charset
import java.net.InetSocketAddress
import concurrent._
import concurrent.ExecutionContext.Implicits.global
import scala.util.Success
import scala.util.Failure


object MyHttpServer {
  def apply(port: Int, backlog: Int = 0): MyHttpServer = new MyHttpServer(port, backlog)

  object Implicits {
    implicit def httpResponse2Future(response: (Int, String)): Future[(Int, String)] = future{
      Thread.sleep(2000)
      response
    }
    implicit def intFuture2Future(response: (Int, Future[String])): Future[(Int, String)] = response._2.map((response._1, _))
  }

}

class MyHttpServer(port: Int, backlog: Int) {

  type HttpHandlerFunction = (String) => Future[(Int, String)]

  def serve(root: String)(handler: HttpHandlerFunction) {
    implicit val server = HttpServer.create(new InetSocketAddress(port), backlog)

    contextFor("/")(handler)

    println("Server running on port " + port + "...")
    server.start()
  }

  def contextFor(contextPath: String)(httpHandler: HttpHandlerFunction)(implicit server: HttpServer): HttpContext = {
    def sendData(exchange: HttpExchange, code: Int, body: String) {
      val response = body.getBytes(Charset.forName("UTF-8"))

      exchange.sendResponseHeaders(code, response.length)
      exchange.getResponseBody.write(response)
    }

    server.createContext(contextPath, new HttpHandler {
      def handle(exchange: HttpExchange) {
        val uriPath = exchange.getRequestURI.getPath
        println(s"request path: $uriPath")

        val path = uriPath.stripPrefix(contextPath).stripPrefix("/").stripSuffix("/")

        try {
          val futureResponse: Future[(Int, String)] = httpHandler(path)

          futureResponse.onComplete {
            case Success((responseCode, content)) => {
              println(s"response($uriPath): $responseCode => $content")

              val query = exchange.getRequestURI.getQuery
              val body = if (query != null) {
                val callback = query.split("[&=]").tail.head
                callback + "(" + content + ")"
              } else {
                content
              }

              sendData(exchange, responseCode, body)

              exchange.close()
            }
            case Failure(e) => {
              println(s"response($uriPath): 500")
              e.printStackTrace()
              sendData(exchange, 500, """{ "error": "Server Error" }""")
              exchange.close()
            }
          }

        }
        catch {
          case e: Throwable => {
            println(s"response($uriPath): 500")
            e.printStackTrace()
            sendData(exchange, 500, """{ "error": "Server Error" }""")
            exchange.close()

            throw e
          }
        }
      }
    })
  }


}
