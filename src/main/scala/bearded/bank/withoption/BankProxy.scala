package bearded.bank.withoption

import bearded.entity.Account

class BankProxy(private val bankAccounts: Map[String, Account]) {

  def getAccountByNumber(accountNumber: String): Option[Account] =
    bankAccounts.get(accountNumber)

}
