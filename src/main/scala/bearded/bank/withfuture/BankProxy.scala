package bearded.bank.withfuture

import bearded.entity.Account
import concurrent.{Promise, Future}
import util.Try

class BankProxy(private val bankAccounts: Map[String, Account]) {

  val accounts = bankAccounts withDefault {
    accountNumber => throw new BankException(s"unknown account $accountNumber")
  }

  def accountByNumber(accountNumber: String): Future[Account] = {
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
