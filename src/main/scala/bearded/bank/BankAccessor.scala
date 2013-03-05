package bearded.bank

import bearded.entity.Account

class BankAccessor(bankAccounts: Map[String, Account]) {

  def getAccountByNumber(accountNumber: String): Account = {
    if (!bankAccounts.contains(accountNumber)) null
    else bankAccounts(accountNumber)
  }

}
