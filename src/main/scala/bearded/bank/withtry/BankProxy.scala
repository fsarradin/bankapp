package bearded.bank.withtry

import bearded.entity.Account
import util.{Failure, Success, Try}
import bearded.bank.BankAccessor

class BankProxy(bankAccessor: BankAccessor) {

  def accountByNumber(accountNumber: String): Try[Account] = {
    val account: Account = bankAccessor.getAccountByNumber(accountNumber)

    if (account == null)
      Failure(new BankException(s"unknown account $accountNumber"))
    else
      Success(account)
  }

}
