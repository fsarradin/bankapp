package eu.alice.bankapp.bank

import eu.alice.bankapp.entity.Account

class BankProxy(bankAccounts: Map[String, Account]) {

  def getAccountByNumber(accountNumber: String): Account = {
    if (!bankAccounts.contains(accountNumber)) null
    else bankAccounts(accountNumber)
  }

}
