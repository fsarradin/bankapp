package bearded.bank.base

import bearded.entity.Account

class BankProxy(private val bankAccounts: Map[String, Account]) {

  def getAccountByNumber(accountNumber: String): Account =
    if (!bankAccounts.contains(accountNumber)) null
    else bankAccounts(accountNumber)

}
