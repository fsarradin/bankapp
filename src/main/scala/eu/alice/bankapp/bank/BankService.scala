package eu.alice.bankapp.bank

import eu.alice.bankapp.bank.base.AccountRepository


class BankService(accountRepository: AccountRepository, ownerPrincipal: (String, String), ownerAccounts: Map[String, Set[String]]) {

  /*
   * PRINCIPAL BALANCE
   *
   */

  def principalBalance: String = {
    val (bankName, accountNumber) = ownerPrincipal
    val balance: Double = getAccount(bankName, accountNumber).balance

    s"""{"balance": "$balance"}"""
  }


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

    s"""{"total": "${balances.sum}"}"""
  }


  /*
   * BALANCE BY BANK
   *
   */

  def balanceByBank: String = {
    val balancesByBankJson: Iterable[String] =
      for (bankName <- ownerAccounts.keys)
      yield totalBalanceFor(bankName)

    balancesByBankJson.mkString("[", ",", "]")
  }

  def totalBalanceFor(bankName: String): String = {
    val balances: List[Double] =
      for {
        accountNumbers <- ownerAccounts.get(bankName).toList
        accountNumber <- accountNumbers
      }
      yield getAccount(bankName, accountNumber).balance

    s"""{"name": "$bankName", "balance": "${balances.sum}"}"""
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
