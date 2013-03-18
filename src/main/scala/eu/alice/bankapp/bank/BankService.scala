package eu.alice.bankapp.bank

import eu.alice.bankapp.bank.base.AccountRepository
import collection.immutable.Iterable


class BankService(accountRepository: AccountRepository, ownerAccounts: Map[String, List[String]]) {

  /*
   * TOTAL BALANCE
   *
   */

  def totalBalance: String = {
    // get all balances
    val balances: Iterable[Double] =
      for {
        (bankName, accountNumbers) <- ownerAccounts
        accountNumber <- accountNumbers
      }
      yield getAccount(bankName, accountNumber).balance

    // compute the total balance
    val total: Any = balances.reduce(
      (subTotal, balance) => addBalances(subTotal, balance)
    )

    // manage error and build JSON response
    if (total != null)
      s"""{"total": "$total"}"""
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

  def apply(ownerAccounts: Map[String, List[String]],
            bankAccessors: Map[String, BankAccessor] = BankConnection.getBankAccessors): BankService
  = new BankService(AccountRepository(bankAccessors), ownerAccounts)

}
