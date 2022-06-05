package util

import munit.FunSuite
import Utils._
import model.Result

class UtilsTest extends FunSuite {

  test("convert tuple of String and Double into Result case class instance") {
    val actual = ("meaningOfLife", 42.0).toResult
    assertEquals(actual, Result("meaningOfLife", 42))
  }

  test("build sttp partial request instance based on implicit token value") {
    implicit val token: Token = Token("GH_TOKEN")
    val url = "https://www.reddit.com/r/scala"
    val actual = RequestBuilder.build(url)
    val actualHeaderNames = actual.headers.map(_.name)
    val actualHeaderValues = actual.headers.map(_.value)
    assert(actualHeaderNames.contains("Authorization"))
    assert(actualHeaderValues.contains("token GH_TOKEN"))
  }

}
