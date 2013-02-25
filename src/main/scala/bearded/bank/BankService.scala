package bearded.bank

import base.AccountRepository
import bearded.entity.AliceProperties


class BankService(accountRepository: AccountRepository) {

  def principal: String = {
    val (bankName, accountNumber) = AliceProperties.AlicePrincipal
    val balance = getAccount(bankName, accountNumber).balance
    s"""{"balance": "$balance"}"""
  }

  def bankInfo: String = {
    var bankInfos: Array[String] = new Array[String](0)

    for ((bankName, accountNumbers) <- AliceProperties.AliceAccounts) {
      var balance = 0.0
      for (accountNumber <- accountNumbers) {
        balance += getAccount(bankName, accountNumber).balance
      }
      bankInfos = bankInfos :+ s"""{"name": "$bankName", "balance": "$balance"}"""
    }

    bankInfos.mkString("[", ",", "]")
  }

  def totalBalance: String = {
    var total = 0.0

    for (bankName <- AliceProperties.AliceAccounts.keys) {
      for (accountNumber <- AliceProperties.AliceAccounts(bankName)) {
        total += getAccount(bankName, accountNumber).balance
      }
    }

    s"""{"total": "$total"}"""
  }

  private def getAccount(bankName: String, accountNumber: String) =
    accountRepository.getAccount(bankName, accountNumber)

}
