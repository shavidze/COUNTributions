package util

import munit.FunSuite

class MapCombinerTest extends FunSuite {

  test("combining empty maps should return empty map") {
    val actual = MapCombiner.combine(Map.empty[String, Double], Map.empty[String, Double])
    assertEquals(actual, Map.empty[String, Double])
  }

  test("combining maps with different keys should return the new map with both keys") {
    val m1 = Map("ZIO" -> 10.0)
    val m2 = Map("Cats" -> 10.0)
    val actual = MapCombiner.combine(m1 ,m2)
    assertEquals(actual, Map("ZIO" -> 10.0, "Cats" -> 10.0))
  }

  test("combining maps with same keys should return the new map with combined value") {
    val m1 = Map("Scala" -> 7.0)
    val m2 = Map("Scala" -> 3.0)
    val actual = MapCombiner.combine(m1, m2)
    assertEquals(actual, Map("Scala" -> 10.0))
  }

  test("combining maps with same and different keys should return the new map with all keys and combined values") {
    val m1 = Map("Technical death metal" -> 10.0, "Brutal death metal" -> 9.0)
    val m2 = Map("Melodic death metal" -> 8.0, "Technical death metal" -> 10.0)
    val actual = MapCombiner.combine(m1, m2)
    assertEquals(actual, Map("Technical death metal" -> 20.0, "Brutal death metal" -> 9.0, "Melodic death metal" -> 8.0))
  }

}
