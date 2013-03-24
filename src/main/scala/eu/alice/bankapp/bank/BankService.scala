package eu.alice.bankapp.bank

import eu.alice.bankapp.entity.Account
import collection.immutable.Iterable


class BankService(accountRepository: AccountRepository,
                  ownerAccounts: Map[String, List[String]]) {

  type Balance = BigDecimal

  /*
   * TOTAL BALANCE
   *
   */

  def totalBalance: String = {
    // get all balances
    val balances: Iterable[Balance] =
      for {
        (bankName, accountNumbers) <- ownerAccounts
        accountNumber <- accountNumbers
      }
      yield {
        val account: Account = accountRepository.getAccount(bankName, accountNumber)
        account.balance
      }

    // compute the total balance
    val total: Balance = balances.reduce(
      (subTotal, balance) => addBalances(subTotal, balance)
    )

    // build JSON response
    s"""{"total": "$total"}"""
  }

  def addBalances(balance1: Balance, balance2: Balance): Balance =
    balance1 + balance2

}

object BankService {

  def apply(ownerAccounts: Map[String, List[String]],
            bankAccessors: Map[String, BankProxy] = BankConnection.getBankAccessors): BankService
  = new BankService(AccountRepository(bankAccessors), ownerAccounts)

}
