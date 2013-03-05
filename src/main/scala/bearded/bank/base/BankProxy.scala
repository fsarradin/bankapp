package bearded.bank.base

import bearded.entity.Account
import bearded.bank.BankAccessor

class BankProxy(bankAccessor: BankAccessor) {

  def getAccountByNumber(accountNumber: String): Account =
    bankAccessor.getAccountByNumber(accountNumber)

}
