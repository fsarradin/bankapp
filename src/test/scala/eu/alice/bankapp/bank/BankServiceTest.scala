package eu.alice.bankapp.bank

import org.specs2.mutable.Specification
import eu.alice.bankapp.entity.Account
import concurrent.{Await, Future, future}
import concurrent.duration._
import concurrent.ExecutionContext.Implicits.global

class BankServiceTest extends Specification {

  val (principalBank, principalAccount): (String, String) = ("PrincipalBank", "PrincipalNumber")

  val bankServiceWithPrincipal: BankService = BankService((principalBank, principalAccount),
    Map(
      principalBank -> Set(principalAccount)
    ),
    Map(
      principalBank -> new BankAccessor(Map(
        principalAccount -> Account("Owner", 1000.0)
      ))
    ))

  val bankServiceWithoutPrincipal: BankService = BankService((principalBank, principalAccount),
    Map(
      principalBank -> Set(principalAccount)
    ),
    Map(
      "Bank" -> new BankAccessor(Map(
        "Number" -> Account("Owner", 1000.0)
      ))
    ))

  implicit def stringToFuture(s: String): Future[String] = future(s)

  "bank service with principal" should {

    "get principal account" in {
      val jsonFuture: Future[String] = bankServiceWithPrincipal.principalBalance
      Await.ready(jsonFuture, 5000 milli)

      val json: String = jsonFuture.value.get.get

      json must contain("balance")
      json must contain("1000")
      json must not contain ("error")
      json must not contain ("Some")
      json must not contain ("Try")
      json must not contain ("Future")
    }

    "get balance by bank" in {
      val jsonFuture: Future[String] = bankServiceWithPrincipal.balanceByBank
      Await.ready(jsonFuture, 5000 milli)

      val json: String = jsonFuture.value.get.get

      json must contain("name")
      json must contain(principalBank)
      json must contain("balance")
      json must contain("1000")
      json must not contain("error")
      json must not contain ("Some")
      json must not contain ("Try")
      json must not contain ("Future")
    }

    "get totla balance" in {
      val jsonFuture: Future[String] = bankServiceWithPrincipal.totalBalance
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

  "bank service without principal" should {

    "get principal account" in {
      val jsonFuture: Future[String] = bankServiceWithoutPrincipal.principalBalance
      Await.ready(jsonFuture, 5000 milli)

      val json: String = jsonFuture.value.get.get

      json must not contain("balance")
      json must not contain("1000")
      json must contain("error")
    }

    "get balance by bank" in {
      val jsonFuture: Future[String] = bankServiceWithoutPrincipal.balanceByBank
      Await.ready(jsonFuture, 5000 milli)

      val json: String = jsonFuture.value.get.get

      json must contain("name")
      json must contain(principalBank)
      json must not contain("balance")
      json must not contain("1000")
      json must contain ("error")
    }

    "get totla balance" in {
      val jsonFuture: Future[String] = bankServiceWithoutPrincipal.totalBalance
      Await.ready(jsonFuture, 5000 milli)

      val json: String = jsonFuture.value.get.get

      json must not contain("total")
      json must not contain("1000")
      json must contain ("error")
    }

  }

}
