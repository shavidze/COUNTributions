import sttp.client3.{Identity, RequestT, UriContext, basicRequest}

object Utils {

  object GithubTokenLoader {
    def loadToken: Token = Token(scala.util.Properties.envOrElse("GH_TOKEN", ""))
  }

  case class Token(value: String)

  object RequestBuilder {
    def build(url: String)(implicit token: Token): RequestT[Identity, Either[String, String], Any] = {
      basicRequest.get(uri"$url")
        .header("Accept", "application/vnd.github.v3+json")
        .header("Authorization", s"token ${token.value}")
    }
  }

  implicit class TupleToResultConverter(tup: (String, Double)) {
    def toResult: Result = Result(tup._1, tup._2)
  }

}

