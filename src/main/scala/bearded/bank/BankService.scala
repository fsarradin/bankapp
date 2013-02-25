package bearded.bank

import base.AccountRepository
import bearded.entity.AliceProperties


class BankService(accountRepository: AccountRepository) {

  def principal: String = {
    s"""{"balance": "0.0"}"""
  }

  def bankInfo: String = {
    s"""[{"name": "N/A", "balance": "0.0"}]"""
  }

  def totalBalance: String = {
    s"""{"total": "0.0"}"""
  }

  private def getAccount(bankName: String, accountNumber: String) =
    accountRepository.getAccount(bankName, accountNumber)

}
