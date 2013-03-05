package bearded.bank.base

import bearded.entity.Account
import bearded.bank.BankAccessor

class AccountRepository(bankProxies: Map[String, BankProxy]) {

  def getAccount(bankName: String, accountNumber: String): Account =
    if (!bankProxies.contains(bankName)) null
    else {
      val bankProxy: BankProxy = bankProxies(bankName)
      val account: Account = bankProxy.getAccountByNumber(accountNumber)
      account
    }

}

object AccountRepository {
  def apply(accessors: Map[String, BankAccessor]): AccountRepository = {
    val bankProxies: Map[String, BankProxy] = for {
      (bankName, accessor) <- accessors
    } yield (bankName, new BankProxy(accessor))

    new AccountRepository(bankProxies)
  }
}
