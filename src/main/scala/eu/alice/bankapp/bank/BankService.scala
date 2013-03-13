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

    if (balances.toList.contains(null))
      s"""{"error": "unknown bank name or account number"}"""
    else
      s"""{"total": "${balances.sum}"}"""
  }


  /*
   * OTHERS...
   *
   */

  private def getAccount(bankName: String, accountNumber: String)=
    accountRepository.getAccount(bankName, accountNumber)

}

object BankService {

  def apply(ownerAccounts: Map[String, Set[String]],
            bankAccessors: Map[String, BankAccessor] = BankConnection.getBankAccessors): BankService
  = new BankService(AccountRepository(bankAccessors), ownerAccounts)

}
