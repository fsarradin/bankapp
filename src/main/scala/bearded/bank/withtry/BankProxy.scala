package bearded.bank.withtry

import bearded.entity.Account
import util.Try

class BankProxy(private val bankAccounts: Map[String, Account]) {

  val accounts = bankAccounts withDefault {
    accountNumber => throw new BankException(s"unknown account $accountNumber")
  }

  def accountByNumber(accountNumber: String): Try[Account] =
    Try(accounts(accountNumber))

}
