package bearded.http

import com.sun.net.httpserver.{HttpExchange, HttpHandler, HttpContext, HttpServer}
import java.nio.charset.Charset
import java.net.InetSocketAddress

object MyHttpServer {
  def apply(port: Int): MyHttpServer = new MyHttpServer(port)
}

class MyHttpServer(port: Int) {

  type HttpHandlerFunction = (String) => (Int, String)

  def serve(root: String)(handler: HttpHandlerFunction) = {
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

        val path = uriPath.stripPrefix(contextPath).stripSuffix("/")

        try {
          val (code, content) = httpHandler(path)

          println(s"response: $code => $content")

          val query = exchange.getRequestURI.getQuery
          val body = if (query != null) {
            val callback = query.split("[&=]").tail.head
            callback + "(" + content + ")"
          } else {
            content
          }

          sendData(exchange, code, body)
        }
        catch {
          case e: Throwable => {
            e.printStackTrace()
            sendData(exchange, 500, "<html></body><h1>Server Error</h1></body></html>")
            throw e
          }
        }
        finally {
          exchange.close()
        }
      }
    })
  }


}
