package bearded.bank.withoption

import org.specs2.mutable.Specification
import bearded.entity.Account
import bearded.bank.BankAccessor

class OptionTest extends Specification {

  val account: Account = Account("Owner", 1000.0)
  val bankAccounts = new BankAccessor(Map("Number" -> account))
  val bankProxy = new BankProxy(bankAccounts)

  "bankProxy" should {
    "get account by number" in {
      bankProxy.getAccountByNumber("Number") must beSome(account)
    }
    "complain when get account with bad number" in {
      bankProxy.getAccountByNumber("BadNumber") must beNone
    }
  }

}
