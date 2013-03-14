package eu.alice.bankapp.bank

import eu.alice.bankapp.bank.base.AccountRepository


class BankService(accountRepository: AccountRepository, ownerAccounts: Map[String, Set[String]]) {

  /*
   * TOTAL BALANCE
   *
   */

  def totalBalance: String = {
    // get all balances
    val balances: Iterable[Double] =
      for {
        (bankName, accountNumbers) <- ownerAccounts
        accountNumber <- accountNumbers.toList
      }
      yield getAccount(bankName, accountNumber).balance

    // compute the total balance
    val totalBalance: Any = balances.reduce((subTotal, balance) => addBalances(subTotal, balance))

    // manage error and build JSON response
    if (totalBalance != null)
      s"""{"total": "$totalBalance"}"""
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
