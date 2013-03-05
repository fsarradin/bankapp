package eu.alice.bankapp.bank


import eu.alice.bankapp.entity.AliceProperties._
import eu.alice.bankapp.entity.Account

object BankConnection {

  def getBankAccessors: Map[String, BankAccessor] = Map(

    "BGP" -> new BankAccessor(Map(
      "CC-BGP-1" -> Account(Alice, 5000)
      , "CC-BGP-2" -> Account(Alice, 5000)
      , "CC-BGP-42" -> Account(Alice, 3000)
    ))

    , "La Postale" -> new BankAccessor(Map(
      "CP-LPO-2" -> Account(Alice, 5000)
    ))

    , "Breizh Bank" -> new BankAccessor(Map(
      "CC-BRB-3" -> Account(Alice, 5000)
    ))

  )

}
