package eu.alice.bankapp.bank.withoption

import eu.alice.bankapp.bank.BankAccessor
import eu.alice.bankapp.entity.Account


class BankProxy(bankAccessor: BankAccessor) {

  def getAccountByNumber(accountNumber: String): Option[Account] = {
    val account: Account = bankAccessor.getAccountByNumber(accountNumber)

    if (account == null)
      None
    else
      Some(account)
  }

}
