package bearded.bank

import bearded.entity.AliceProperties
import base.AccountRepository


class BankService(accountRepository: AccountRepository) {

  def principalBalance: (Int, String) = {
    val (bankName, accountNumber) = AliceProperties.AlicePrincipal
    val balance: Double = getAccount(bankName, accountNumber).balance

    (200, s"""{"balance": "$balance"}""")
  }

  def balanceByBank: (Int, String) = {
    val balancesByBankJson: Iterable[String] =
      for ((bankName, accountNumbers) <- AliceProperties.AliceAccounts)
      yield {
        val balances: List[Double] =
          for (accountNumber <- accountNumbers.toList)
          yield getAccount(bankName, accountNumber).balance

        s"""{"name": "$bankName", "balance": "${balances.sum}"}"""
      }

    (200, balancesByBankJson.mkString("[", ",", "]"))
  }

  def totalBalance: (Int, String) = {
    val balances: Iterable[Double] =
      for {
        (bankName, accountNumbers) <- AliceProperties.AliceAccounts
        accountNumber <- accountNumbers.toList
      }
      yield getAccount(bankName, accountNumber).balance

    (200, s"""{"total": "${balances.sum}"}""")
  }

  private def getAccount(bankName: String, accountNumber: String) =
    accountRepository.getAccount(bankName, accountNumber)

}

object BankService {

  def apply(): BankService = new BankService(AccountRepository(BankConnection.getBankAccessors))

}
