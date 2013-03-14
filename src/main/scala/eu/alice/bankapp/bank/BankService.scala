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


  def addBalances(balance1: Double, balance2: Double): Double =
    if (balance1 == null || balance2 == null) null.asInstanceOf[Double]
    else balance1 + balance2


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
