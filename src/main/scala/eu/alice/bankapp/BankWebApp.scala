package eu.alice.bankapp

import bank.BankService
import entity.AliceProperties
import http.MyHttpServer
import http.MyHttpServer.Implicits._


object BankWebApp {

  def main(args: Array[String]) {
    val bankService: BankService = BankService(AliceProperties.AliceAccounts)

    MyHttpServer(8080)
      .serve("/") {
      case "total" => (200, bankService.totalBalance)

      case badPath => (404, badPath + " Not Found")
    }

  }

}
