package bearded.bank.base

import org.specs2.mutable.Specification
import bearded.entity.Account

class BaseTest extends Specification {

  val account: Account = Account("Owner", 1000.0)
  val bankAccounts = Map("Number" -> account)
  val bankProxy = new BankProxy(bankAccounts)

  "bankProxy" should {
    "get account by number" in {
      bankProxy.getAccountByNumber("Number") must be equalTo account
    }
    "complain when get account with bad number" in {
      bankProxy.getAccountByNumber("BadNumber") must beNull[Account]
    }
  }

}
