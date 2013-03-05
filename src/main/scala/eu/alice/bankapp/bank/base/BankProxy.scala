package eu.alice.bankapp.bank.base

import eu.alice.bankapp.bank.BankAccessor
import eu.alice.bankapp.entity.Account

class BankProxy(bankAccessor: BankAccessor) {

  def getAccountByNumber(accountNumber: String): Account =
    bankAccessor.getAccountByNumber(accountNumber)

}
