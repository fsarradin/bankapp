package bearded.bank

import bearded.entity.Account

package object base {

  type BankName = String
  type AccountNumber = String

  class AccountRepository(private val bankProxies: Map[BankName, BankProxy]) {

    def getAccount(bankName: BankName, accountNumber: AccountNumber): Account =
      if (!bankProxies.contains(bankName)) null
      else {
        val bankProxy: BankProxy = bankProxies(bankName)
        val account: Account = bankProxy.getAccountByNumber(accountNumber)
        account
      }

  }


  class BankProxy(private val bankAccounts: Map[AccountNumber, Account]) {

    def getAccountByNumber(accountNumber: AccountNumber): Account =
      if (!bankAccounts.contains(accountNumber)) null
      else bankAccounts(accountNumber)

  }


  class BankException(message: String) extends RuntimeException(message)

}
