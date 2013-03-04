package bearded.bank

import base.{BankProxy, AccountRepository}
import bearded.entity.AliceProperties
import bearded.entity.AliceProperties._
import bearded.entity.Account


class BankService(accountRepository: AccountRepository) {

  def principal: (Int, String) = {
    val (bankName, accountNumber) = AliceProperties.AlicePrincipal
    val balance = getAccount(bankName, accountNumber).balance

    (200, s"""{"balance": "$balance"}""")
  }

  def bankInfo: (Int, String) = {
    var bankInfos: Array[String] = new Array[String](0)

    for ((bankName, accountNumbers) <- AliceProperties.AliceAccounts) {
      var balance = 0.0
      for (accountNumber <- accountNumbers) {
        balance += getAccount(bankName, accountNumber).balance
      }
      bankInfos = bankInfos :+ s"""{"name": "$bankName", "balance": "$balance"}"""
    }

    (200, bankInfos.mkString("[", ",", "]"))
  }

  def totalBalance: (Int, String) = {
    var total = 0.0

    for (bankName <- AliceProperties.AliceAccounts.keys) {
      for (accountNumber <- AliceProperties.AliceAccounts(bankName)) {
        total += getAccount(bankName, accountNumber).balance
      }
    }

    (200, s"""{"total": "$total"}""")
  }

  private def getAccount(bankName: String, accountNumber: String) =
    accountRepository.getAccount(bankName, accountNumber)

}

object BankService {

  def apply(): BankService = {
    val accountRepository = new AccountRepository(Map(

      "BGP" -> new BankProxy(Map(
        "CC-BGP-1" -> Account(Alice, 5000)
        , "CC-BGP-42" -> Account(Alice, 3000)
      ))

      , "La Postale" -> new BankProxy(Map(
        "CP-LPO-2" -> Account(Alice, 5000)
      ))

      , "Breizh Bank" -> new BankProxy(Map(
        "CC-BRB-3" -> Account(Alice, 5000)
      ))

    ))

    new BankService(accountRepository)
  }

}
