package eu.alice.bankapp.bank

import eu.alice.bankapp.entity.Account

class AccountRepository(bankAccessors: Map[String, BankAccessor]) {

  def getAccount(bankName: String, accountNumber: String): Account =
  {
    val bankAccessor: BankAccessor = bankAccessors(bankName)
    val account: Account = bankAccessor.getAccountByNumber(accountNumber)
    account
  }

}

object AccountRepository {
  def apply(accessors: Map[String, BankAccessor]): AccountRepository = new AccountRepository(accessors)
}
