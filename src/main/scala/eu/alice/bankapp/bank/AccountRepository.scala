package eu.alice.bankapp.bank

import eu.alice.bankapp.entity.Account

class AccountRepository(bankProxies: Map[String, BankProxy]) {

  def getAccount(bankName: String, accountNumber: String): Account =
  {
    val bankProxy: BankProxy = getBankProxy(bankName)
    val account: Account = getAccountByNumber(bankProxy, accountNumber)
    account
  }


  def getBankProxy(bankName: String): BankProxy =
  {
    if (!bankProxies.contains(bankName))
      throw new BankException(s"unknown bank name $bankName")
    else
      bankProxies(bankName)
  }

  def getAccountByNumber(bankProxy: BankProxy, accountNumber: String): Account =
  {
    val account: Account = bankProxy.getAccountByNumber(accountNumber)

    if (account == null)
      throw new BankException(s"unknown account number $accountNumber")
    else
      account
  }


}

object AccountRepository {
  def apply(accessors: Map[String, BankProxy]): AccountRepository = new AccountRepository(accessors)
}
