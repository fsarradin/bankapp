package bearded.bank

import util.Try
import bearded.entity.Account

package object withtry {

  type BankName = String
  type AccountNumber = String

  class AccountRepository(private val bankProxies: Map[BankName, BankProxy]) {

    val proxies = bankProxies withDefault {
      bankName => throw new BankException(s"unknown bank $bankName")
    }

    def getAccount(bankName: BankName, accountNumber: AccountNumber): Try[Account] =
      for {
        bankProxy <- Try(proxies(bankName))
        account <- bankProxy.accountByNumber(accountNumber)
      } yield account

  }


  class BankProxy(private val bankAccounts: Map[AccountNumber, Account]) {

    val accounts = bankAccounts withDefault {
      accountNumber => throw new BankException(s"unknown account $accountNumber")
    }

    def accountByNumber(accountNumber: AccountNumber): Try[Account] =
      Try(accounts(accountNumber))

  }


  class BankException(message: String) extends RuntimeException(message)

}
