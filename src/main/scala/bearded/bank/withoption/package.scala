package bearded.bank

import bearded.entity.Account

package object withoption {

  type BankName = String
  type AccountNumber = String

  class AccountRepository(private val bankProxies: Map[BankName, BankProxy]) {

    def getAccount(bankName: BankName, accountNumber: AccountNumber): Option[Account] =
      for {
        bankProxy <- bankProxies.get(bankName)
        account <- bankProxy.getAccountByNumber(accountNumber)
      } yield account

  }


  class BankProxy(private val accounts: Map[AccountNumber, Account]) {

    def getAccountByNumber(accountNumber: AccountNumber): Option[Account] =
      accounts.get(accountNumber)

  }

}
