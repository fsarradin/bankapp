package bearded

import bank.BankService
import http.MyHttpServer
import bearded.http.MyHttpServer.Implicits._


object BankWebApp {

  def main(args: Array[String]) {
    val bankService: BankService = BankService()

    MyHttpServer(8080)
      .serve("/") {
        case "banks" => bankService.bankInfo
        case "total" => bankService.totalBalance
        case "principal" => bankService.principal

        case badPath => (404, badPath + " Not Found")
      }

  }

}
