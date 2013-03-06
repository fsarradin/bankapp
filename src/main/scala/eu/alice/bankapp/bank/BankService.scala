package eu.alice.bankapp.bank

import eu.alice.bankapp.bank.base.AccountRepository


class BankService(accountRepository: AccountRepository, ownerPrincipal: (String, String), ownerAccounts: Map[String, Set[String]]) {

  def principalBalance: String = {
    val (bankName, accountNumber) = ownerPrincipal
    val balance: Double = getAccount(bankName, accountNumber).balance

    s"""{"balance": "$balance"}"""
  }

  def balanceByBank: String = {
    val balancesByBankJson: Iterable[String] =
      for ((bankName, accountNumbers) <- ownerAccounts)
      yield {
        val balances: List[Double] =
          for (accountNumber <- accountNumbers.toList)
          yield getAccount(bankName, accountNumber).balance

        s"""{"name": "$bankName", "balance": "${balances.sum}"}"""
      }

    balancesByBankJson.mkString("[", ",", "]")
  }

  def totalBalance: String = {
    val balances: Iterable[Double] =
      for {
        (bankName, accountNumbers) <- ownerAccounts
        accountNumber <- accountNumbers.toList
      }
      yield getAccount(bankName, accountNumber).balance

    s"""{"total": "${balances.sum}"}"""
  }


  private def getAccount(bankName: String, accountNumber: String) = accountRepository.getAccount(bankName, accountNumber)

}

object BankService {

  def apply(ownerPrincipal: (String, String),
            ownerAccounts: Map[String, Set[String]],
            bankAccessors: Map[String, BankAccessor]= BankConnection.getBankAccessors): BankService
  = new BankService(AccountRepository(bankAccessors), ownerPrincipal, ownerAccounts)

}
