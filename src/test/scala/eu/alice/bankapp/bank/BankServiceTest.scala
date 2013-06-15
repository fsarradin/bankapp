package eu.alice.bankapp.bank

import eu.alice.bankapp.entity.Account
import concurrent.{Await, Future, future}
import concurrent.duration._
import concurrent.ExecutionContext.Implicits.global
import org.scalatest.FlatSpec
import org.scalatest.matchers.MustMatchers._

class BankServiceTest extends FlatSpec {

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

  "a bank" should "get total balance" in {
    val jsonFuture: Future[String] = bankService.totalBalance
    Await.ready(jsonFuture, 5000 milli)

    val json: String = jsonFuture.value.get.get

    json must include ("total")
    json must include ("1000")

    json must not (include ("error"))
    json must not (include ("Some"))
    json must not (include ("Try"))
    json must not (include ("Future"))
  }

}
