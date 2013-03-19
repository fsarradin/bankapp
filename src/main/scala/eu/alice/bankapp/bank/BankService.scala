package eu.alice.bankapp.bank

import eu.alice.bankapp.entity.Account


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
      yield {
        val account: Account = accountRepository.getAccount(bankName, accountNumber)
        account.balance
      }

    // compute the total balance
    val total: Double = balances.reduce(
      (subTotal, balance) => addBalances(subTotal, balance)
    )

    // build JSON response
    s"""{"total": "$total"}"""
  }


  def addBalances(balance1: Double, balance2: Double): Double = balance1 + balance2

}

object BankService {

  def apply(ownerAccounts: Map[String, List[String]],
            bankAccessors: Map[String, BankAccessor] = BankConnection.getBankAccessors): BankService
  = new BankService(AccountRepository(bankAccessors), ownerAccounts)

}
