package bearded.bank.withoption

import bearded.entity.Account
import bearded.bank.BankAccessor

class AccountRepository(private val bankProxies: Map[String, BankProxy]) {

  def getAccount(bankName: String, accountNumber: String): Option[Account] =
    for {
      bankProxy <- bankProxies.get(bankName)
      account <- bankProxy.getAccountByNumber(accountNumber)
    } yield account

}

object AccountRepository {
  def apply(accessors: Map[String, BankAccessor]): AccountRepository = {
    val bankProxies: Map[String, BankProxy] = for {
      (bankName, accessor) <- accessors
    } yield (bankName, new BankProxy(accessor))

    new AccountRepository(bankProxies)
  }
}
