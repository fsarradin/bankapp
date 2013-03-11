package eu.alice.bankapp.bank

import eu.alice.bankapp.bank.base.AccountRepository
import eu.alice.bankapp.entity.Account


class BankService(accountRepository: AccountRepository, ownerPrincipal: (String, String), ownerAccounts: Map[String, Set[String]]) {

  /*
   * TOTAL BALANCE
   *
   */

  def totalBalance: String = {
    val accounts: Iterable[Account] =
      for {
        (bankName, accountNumbers) <- ownerAccounts
        accountNumber <- accountNumbers.toList
      }
      yield getAccount(bankName, accountNumber)

    if (accounts.toList.contains(null)) s"""{"error": "unknown bank name or account number"}"""
    else {
      val balances = accounts.map(_.balance)
      s"""{"total": "${balances.sum}"}"""
    }
  }


  /*
   * OTHERS...
   *
   */

  private def getAccount(bankName: String, accountNumber: String) = accountRepository.getAccount(bankName, accountNumber)

}

object BankService {

  def apply(ownerPrincipal: (String, String),
            ownerAccounts: Map[String, Set[String]],
            bankAccessors: Map[String, BankAccessor] = BankConnection.getBankAccessors): BankService
  = new BankService(AccountRepository(bankAccessors), ownerPrincipal, ownerAccounts)

}
