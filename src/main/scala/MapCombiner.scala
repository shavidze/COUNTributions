object MapCombiner {
  type Count = Map[String, Double]

  def combine(x: Map[String, Double], y: Count): Count = {
    val xDefault = x.withDefaultValue(0.0)
    val yDefault = y.withDefaultValue(0.0)
    val uniqueKeys = x.keys.toSet.union(y.keys.toSet)
    uniqueKeys.map { k => (k -> (xDefault(k) + yDefault(k))) }.toMap
  }
}