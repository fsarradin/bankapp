package eu.alice.bankapp.entity

object AliceProperties {

  val Alice = "Alice"

  val AliceAccounts: Map[String, List[String]] = Map(
    "BGP" -> List("CC-BGP-1", "#CC-BGP-2", "CC-BGP-42")
    , "La Postale" -> List("CP-LPO-2")
    , "#Breizh Bank" -> List("CC-BRB-3")
  )

}
