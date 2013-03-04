package bearded.http

import com.sun.net.httpserver.{HttpExchange, HttpHandler, HttpContext, HttpServer}
import java.nio.charset.Charset
import java.net.InetSocketAddress
import concurrent._
import concurrent.ExecutionContext.Implicits.global
import scala.util.Success
import scala.util.Failure


object MyHttpServer {
  def apply(port: Int): MyHttpServer = new MyHttpServer(port)

  object Implicits {
    implicit def httpResponse2Future(response: (Int, String)): Future[(Int, String)] = future(response)
  }

}

class MyHttpServer(port: Int) {

  type HttpHandlerFunction = (String) => Future[(Int, String)]

  def serve(root: String)(handler: HttpHandlerFunction) {
    implicit val server = HttpServer.create(new InetSocketAddress(port), 0)

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
              println(s"response: $responseCode => $content")

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
              e.printStackTrace()
              sendData(exchange, 500, "<html></body><h1>Server Error</h1></body></html>")
              exchange.close()
            }
          }

        }
        catch {
          case e: Throwable => {
            e.printStackTrace()
            sendData(exchange, 500, "<html></body><h1>Server Error</h1></body></html>")
            exchange.close()

            throw e
          }
        }
      }
    })
  }


}
