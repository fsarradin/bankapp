package eu.alice.bankapp.entity

object AliceProperties {

  val Alice = "Alice"

  val AlicePrincipal: (String, String) = ("BGP", "#CC-BGP-1")

  val AliceAccounts: Map[String, Set[String]] = Map(
    "BGP" -> Set("CC-BGP-1", "#CC-BGP-2", "CC-BGP-42")
    , "La Postale" -> Set("CP-LPO-2")
    , "#Breizh Bank" -> Set("CC-BRB-3")
  )

}
