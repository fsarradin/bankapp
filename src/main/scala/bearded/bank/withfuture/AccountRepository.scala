package bearded.bank.withfuture

import concurrent._
import concurrent.ExecutionContext.Implicits.global
import bearded.entity.Account
import bearded.bank.BankAccessor

class AccountRepository(private val bankProxies: Map[String, BankProxy]) {

  val proxies = bankProxies withDefault {
    bankName => throw new BankException(s"unknown bank $bankName")
  }

  def getAccount(bankName: String, accountNumber: String): Future[Account] =
    for {
      bankProxy <- getBankProxy(bankName)
      account <- bankProxy.accountByNumber(accountNumber)
    } yield account

  private def getBankProxy(bankName: String): Future[BankProxy] =
    future {
      proxies(bankName)
    }

}

object AccountRepository {
  def apply(accessors: Map[String, BankAccessor]): AccountRepository = {
    val bankProxies: Map[String, BankProxy] = for {
      (bankName, accessor) <- accessors
    } yield (bankName, new BankProxy(accessor))

    new AccountRepository(bankProxies)
  }
}
