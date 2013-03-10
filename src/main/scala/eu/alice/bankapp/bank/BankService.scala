package eu.alice.bankapp.bank

import eu.alice.bankapp.bank.base.AccountRepository
import eu.alice.bankapp.entity.Account


class BankService(accountRepository: AccountRepository, ownerPrincipal: (String, String), ownerAccounts: Map[String, Set[String]]) {

  /*
   * PRINCIPAL BALANCE
   *
   */

  def principalBalance: String = {
    val (bankName, accountNumber) = ownerPrincipal
    val account: Account = getAccount(bankName, accountNumber)

    if (account == null) s"""{"error": "unknown bank name or account number"}"""
    else s"""{"balance": "${account.balance}"}"""

  }


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
   * BALANCE BY BANK
   *
   */

  def balanceByBank: String = {
    val balancesByBankJson: Iterable[String] =
      for (bankName <- ownerAccounts.keys)
      yield {
        val accounts: List[Account] = accountsFor(bankName)

        if (accounts == null) s"""{"name": "$bankName", "error": "unknown bank name or account number"}"""
        else {
          val balances = accounts.map(_.balance)
          s"""{"name": "$bankName", "balance": "$balances"}"""
        }
      }

    balancesByBankJson.mkString("[", ",", "]")
  }

  def accountsFor(bankName: String): List[Account] = {
    val accounts: List[Account] =
      for {
        accountNumbers <- ownerAccounts.get(bankName).toList
        accountNumber <- accountNumbers
      }
      yield getAccount(bankName, accountNumber)

    if (accounts.contains(null)) null
    else accounts
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
