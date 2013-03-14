package eu.alice.bankapp.bank

import eu.alice.bankapp.bank.base.AccountRepository


class BankService(accountRepository: AccountRepository, ownerAccounts: Map[String, Set[String]]) {

  /*
   * TOTAL BALANCE
   *
   */

  def totalBalance: String = {
    val balances: Iterable[Double] =
      for {
        (bankName, accountNumbers) <- ownerAccounts
        accountNumber <- accountNumbers.toList
      }
      yield getAccount(bankName, accountNumber).balance

    val sum: Any = balances.reduce((subTotal, balance) => addBalances(subTotal, balance))

    if (sum != null)
      s"""{"total": "$sum"}"""
    else
      s"""{"error": "unknown bank name or account number"}"""
  }


  def addBalances(v1: Double, v2: Double) =
    if (v1 == null || v2 == null) null
    else v1 + v2


  /*
   * OTHERS...
   *
   */

  private def getAccount(bankName: String, accountNumber: String) =
    accountRepository.getAccount(bankName, accountNumber)

}

object BankService {

  def apply(ownerAccounts: Map[String, Set[String]],
            bankAccessors: Map[String, BankAccessor] = BankConnection.getBankAccessors): BankService
  = new BankService(AccountRepository(bankAccessors), ownerAccounts)

}
