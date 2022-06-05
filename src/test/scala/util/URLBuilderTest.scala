package util

import munit.FunSuite

class URLBuilderTest extends FunSuite {

  test("A simple test case for building organization url with default page value") {
    val actual = URLBuilder.buildRepositoriesURL("zio").value
    assertEquals(actual, "https://api.github.com/orgs/zio/repos?per_page=100&page=1")
  }

  test("A simple test case for building organization url with custom page value") {
    val actual = URLBuilder.buildRepositoriesURL("zio", 5).value
    assertEquals(actual, "https://api.github.com/orgs/zio/repos?per_page=100&page=5")
  }

  test("A simple test case for building contributors url with default page value") {
    val actual = URLBuilder.buildContributorsURL("zio", "zio").value
    assertEquals(actual, "https://api.github.com/repos/zio/zio/contributors?per_page=100&page=1")
  }

  test("A simple test case for building contributors url with custom page value") {
    val actual = URLBuilder.buildContributorsURL("zio", "zio", 10).value
    assertEquals(actual, "https://api.github.com/repos/zio/zio/contributors?per_page=100&page=10")
  }

}
