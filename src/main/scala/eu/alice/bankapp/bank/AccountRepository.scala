package eu.alice.bankapp.bank

import eu.alice.bankapp.entity.Account

class AccountRepository(bankAccessors: Map[String, BankAccessor]) {

  def getAccount(bankName: String, accountNumber: String): Account =
  {
    val bankAccessor: BankAccessor = getBankAccessor(bankName)
    val account: Account = getAccountByNumber(bankAccessor, accountNumber)
    account
  }


  def getBankAccessor(bankName: String): BankAccessor =
  {
    if (!bankAccessors.contains(bankName))
      throw new BankException(s"unknown bank name $bankName")
    else
      bankAccessors(bankName)
  }

  def getAccountByNumber(bankAccessor: BankAccessor, accountNumber: String): Account =
  {
    val account: Account = bankAccessor.getAccountByNumber(accountNumber)

    if (account == null)
      throw new BankException(s"unknown account number $accountNumber")
    else
      account
  }


}

object AccountRepository {
  def apply(accessors: Map[String, BankAccessor]): AccountRepository = new AccountRepository(accessors)
}
