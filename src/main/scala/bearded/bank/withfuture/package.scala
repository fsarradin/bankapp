package bearded.bank

import concurrent.ExecutionContext.Implicits.global
import concurrent.{Promise, Future, promise}
import bearded.entity.Account
import util.Try

package object withfuture {

  type BankName = String
  type AccountNumber = String


  class AccountRepository(private val bankProxies: Map[BankName, BankProxy]) {

    val proxies = bankProxies withDefault {
      bankName => throw new BankException(s"unknown bank $bankName")
    }

    def getAccount(bankName: BankName, accountNumber: AccountNumber): Future[Account] =
      for {
        bankProxy <- getBankProxy(bankName)
        account <- bankProxy.accountByNumber(accountNumber)
      } yield account

    private def getBankProxy(bankName: BankName): Future[BankProxy] = {
      val proxyPromise: Promise[BankProxy] = promise()

      proxyPromise.complete(Try(proxies(bankName)))

      proxyPromise.future
    }

  }


  class BankProxy(private val bankAccounts: Map[AccountNumber, Account]) {

    val accounts = bankAccounts withDefault {
      accountNumber => throw new BankException(s"unknown account $accountNumber")
    }

    def accountByNumber(accountNumber: AccountNumber): Future[Account] = {
      val accountPromise: Promise[Account] = scala.concurrent.promise()

      new Thread(new Runnable {
        def run() {
          Thread.sleep(2000)
          println(s"... get account $accountNumber")
          accountPromise.complete(Try(accounts(accountNumber)))
        }
      }).start()

      accountPromise.future
    }

  }


  class BankException(message: String) extends RuntimeException(message)

}
