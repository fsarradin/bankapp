package eu.alice.bankapp.bank.withfuture

import concurrent.{future, Future}
import concurrent.ExecutionContext.Implicits.global
import eu.alice.bankapp.bank.{BankException, BankAccessor}
import eu.alice.bankapp.entity.Account

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
