package bearded.bank.withtry

import util.Try
import bearded.entity.Account

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
