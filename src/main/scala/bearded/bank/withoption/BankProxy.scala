package bearded.bank.withoption

import bearded.entity.Account
import bearded.bank.BankAccessor

class BankProxy(bankAccessor: BankAccessor) {

  def getAccountByNumber(accountNumber: String): Option[Account] = {
    val account: Account = bankAccessor.getAccountByNumber(accountNumber)

    if (account == null)
      None
    else
      Some(account)
  }

}
