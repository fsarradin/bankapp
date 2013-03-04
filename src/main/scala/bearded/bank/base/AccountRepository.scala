package bearded.bank.base

import bearded.entity.Account

class AccountRepository(private val bankProxies: Map[String, BankProxy]) {

  def getAccount(bankName: String, accountNumber: String): Account =
    if (!bankProxies.contains(bankName)) null
    else {
      val bankProxy: BankProxy = bankProxies(bankName)
      val account: Account = bankProxy.getAccountByNumber(accountNumber)
      account
    }

}
