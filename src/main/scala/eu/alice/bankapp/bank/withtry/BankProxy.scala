package eu.alice.bankapp.bank.withtry

import util.{Failure, Success, Try}
import eu.alice.bankapp.bank.{BankException, BankAccessor}
import eu.alice.bankapp.entity.Account

class BankProxy(bankAccessor: BankAccessor) {

  def accountByNumber(accountNumber: String): Try[Account] = {
    val account: Account = bankAccessor.getAccountByNumber(accountNumber)

    if (account == null)
      Failure(new BankException(s"unknown account $accountNumber"))
    else
      Success(account)
  }

}
