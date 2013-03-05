package bearded.bank.withfuture

import bearded.entity.Account
import concurrent.{future, Future}
import concurrent.ExecutionContext.Implicits.global

class BankProxy(private val bankAccounts: Map[String, Account]) {

  val accounts = bankAccounts withDefault {
    accountNumber => throw new BankException(s"unknown account $accountNumber")
  }

  def accountByNumber(accountNumber: String): Future[Account] =
    future {
      Thread.sleep(2000)
      accounts(accountNumber)
    }

}
