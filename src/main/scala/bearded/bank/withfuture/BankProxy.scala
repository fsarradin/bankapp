package bearded.bank.withfuture

import bearded.entity.Account
import concurrent.{future, Future}

class BankProxy(private val bankAccounts: Map[String, Account]) {

  val accounts = bankAccounts withDefault {
    accountNumber => throw new BankException(s"unknown account $accountNumber")
  }

  def accountByNumber(accountNumber: String): Future[Account] =
    future {
      Thread.sleep(2000)
      println(s"... get account $accountNumber")
      accounts(accountNumber)
    }

}
