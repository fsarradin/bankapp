package bearded.bank.withfuture

import bearded.entity.Account
import concurrent.{future, Future}
import concurrent.ExecutionContext.Implicits.global
import bearded.bank.BankAccessor

class BankProxy(bankAccessor: BankAccessor) {

  def accountByNumber(accountNumber: String): Future[Account] =
    future {
      Thread.sleep(2000)

      val account: Account = bankAccessor.getAccountByNumber(accountNumber)

      if (account == null)
        throw new BankException(s"unknown account $accountNumber")
      else
        account
    }

}
