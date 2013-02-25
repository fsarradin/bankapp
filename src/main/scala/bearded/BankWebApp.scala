package bearded

import bank.BankService
import bank.base.{BankProxy, AccountRepository}
import entity.Account
import entity.AliceProperties.Alice
import http.MyHttpServer


object BankWebApp {

  var bankService: BankService = null

  def main(args: Array[String]) {
    injectDependencies()

    MyHttpServer(8080).serve("/") {
      case "banks" => (200, bankService.bankInfo)
      case "total" => (200, bankService.totalBalance)
      case "principal" => (200, bankService.principal)

      case badPath => (404, badPath + " Not Found")
    }

  }

  def injectDependencies() {
    val accountRepository = new AccountRepository(Map(

      "BGP" -> new BankProxy(Map(
        "CC-BGP-1" -> Account(Alice, 5000)
        , "CC-BGP-2" -> Account("Tom", 3000)
        , "CC-BGP-42" -> Account(Alice, 3000)
      ))

      , "La Postale" -> new BankProxy(Map(
        "CP-LPO-2" -> Account(Alice, 5000)
      ))

      , "Breizh Bank" -> new BankProxy(Map(
        "CC-BRB-3" -> Account(Alice, 5000)
      ))

    ))

    bankService = new BankService(accountRepository)
  }
}
