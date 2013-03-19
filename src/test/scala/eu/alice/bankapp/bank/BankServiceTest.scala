package eu.alice.bankapp.bank

import org.specs2.mutable.Specification
import eu.alice.bankapp.entity.Account
import concurrent.{Await, Future, future}
import concurrent.duration._
import concurrent.ExecutionContext.Implicits.global

class BankServiceTest extends Specification {

  val bankService: BankService = BankService(
    Map(
      "Bank" -> List("Number")
    ),
    Map(
      "Bank" -> new BankProxy(Map(
        "Number" -> Account("Owner", 1000.0)
      ))
    ))

  implicit def stringToFuture(s: String): Future[String] = future(s)

  "a bank" should {

    "get total balance" in {
      val jsonFuture: Future[String] = bankService.totalBalance
      Await.ready(jsonFuture, 5000 milli)

      val json: String = jsonFuture.value.get.get

      json must contain("total")
      json must contain("1000")
      json must not contain ("error")
      json must not contain ("Some")
      json must not contain ("Try")
      json must not contain ("Future")
    }

  }

}
