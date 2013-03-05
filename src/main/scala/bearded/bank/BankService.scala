package bearded.bank

import base.{BankProxy, AccountRepository}
import bearded.entity.AliceProperties
import bearded.entity.AliceProperties._
import bearded.entity.Account
import collection.immutable.Iterable


class BankService(accountRepository: AccountRepository) {

  def principal: (Int, String) = {
    val (bankName, accountNumber) = AliceProperties.AlicePrincipal
    val balance = getAccount(bankName, accountNumber).balance

    (200, s"""{"balance": "$balance"}""")
  }

  def bankInfo: (Int, String) = {
    val bankInfos: Iterable[String] =
      for ((bankName, accountNumbers) <- AliceProperties.AliceAccounts)
      yield {
        val balances: List[Double] =
          for (accountNumber <- accountNumbers.toList)
          yield getAccount(bankName, accountNumber).balance

        s"""{"name": "$bankName", "balance": "${balances.sum}"}"""
      }

    (200, bankInfos.mkString("[", ",", "]"))
  }

  def totalBalance: (Int, String) = {
    val balances =
      for {
        (bankName, accountNumbers) <- AliceProperties.AliceAccounts
        accountNumber <- accountNumbers.toList
      }
      yield getAccount(bankName, accountNumber).balance

    (200, s"""{"total": "${balances.sum}"}""")
  }

  private def getAccount(bankName: String, accountNumber: String) =
    accountRepository.getAccount(bankName, accountNumber)

}

object BankService {

  def apply(): BankService = {
    val accountRepository = new AccountRepository(Map(

      "BGP" -> new BankProxy(Map(
        "CC-BGP-1" -> Account(Alice, 5000)
        , "CC-BGP-2" -> Account(Alice, 5000)
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
