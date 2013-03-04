package bearded.bank.withfuture

import concurrent._
import concurrent.ExecutionContext.Implicits.global
import bearded.entity.Account
import scala.util.Try

class AccountRepository(private val bankProxies: Map[String, BankProxy]) {

  val proxies = bankProxies withDefault {
    bankName => throw new BankException(s"unknown bank $bankName")
  }

  def getAccount(bankName: String, accountNumber: String): Future[Account] =
    for {
      bankProxy <- getBankProxy(bankName)
      account <- bankProxy.accountByNumber(accountNumber)
    } yield account

  private def getBankProxy(bankName: String): Future[BankProxy] = {
    val proxyPromise: Promise[BankProxy] = promise()
    proxyPromise.complete(Try(proxies(bankName)))
    proxyPromise.future
  }

}
