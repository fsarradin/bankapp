package bearded.bank.withoption

import bearded.entity.Account

class AccountRepository(private val bankProxies: Map[String, BankProxy]) {

  def getAccount(bankName: String, accountNumber: String): Option[Account] =
    for {
      bankProxy <- bankProxies.get(bankName)
      account <- bankProxy.getAccountByNumber(accountNumber)
    } yield account

}
