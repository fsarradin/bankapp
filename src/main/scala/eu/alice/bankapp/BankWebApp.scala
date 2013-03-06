package eu.alice.bankapp

import bank.BankService
import entity.AliceProperties
import http.MyHttpServer
import http.MyHttpServer.Implicits._


object BankWebApp {

  def main(args: Array[String]) {
    val bankService: BankService = BankService(AliceProperties.AlicePrincipal, AliceProperties.AliceAccounts)

    MyHttpServer(8080)
      .serve("/") {
      case "banks" => (200, bankService.balanceByBank)
      case "total" => (200, bankService.totalBalance)
      case "principal" => (200, bankService.principalBalance)

      case badPath => (404, badPath + " Not Found")
    }

  }

}
