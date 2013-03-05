package eu.alice.bankapp.bank.withtry

import util.Try
import eu.alice.bankapp.entity.Account
import eu.alice.bankapp.bank.BankAccessor

class AccountRepository(private val bankProxies: Map[String, BankProxy]) {

  val proxies = bankProxies withDefault {
    bankName => throw new BankException(s"unknown bank $bankName")
  }

  def getAccount(bankName: String, accountNumber: String): Try[Account] =
    for {
      bankProxy <- Try(proxies(bankName))
      account <- bankProxy.accountByNumber(accountNumber)
    } yield account

}

object AccountRepository {
  def apply(accessors: Map[String, BankAccessor]): AccountRepository = {
    val bankProxies: Map[String, BankProxy] = for {
      (bankName, accessor) <- accessors
    } yield (bankName, new BankProxy(accessor))

    new AccountRepository(bankProxies)
  }
}
