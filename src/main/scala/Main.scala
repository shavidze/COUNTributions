
import ContributorsRetriever.ContributorsEnv
import Utils.{GithubTokenLoader, Token}
import RepositoriesRetriever.RepositoriesEnv
import io.circe.generic.auto._
import io.circe.syntax.EncoderOps
import sttp.client3.{HttpClientSyncBackend, Identity, SttpBackend}
import zhttp.http._
import zhttp.service.{ChannelFactory, EventLoopGroup, Server}
import zio._
import zio.console._
import Utils._


object Main extends zio.App {

  val backend: SttpBackend[Identity, Any] = HttpClientSyncBackend()
  implicit val token: Token = GithubTokenLoader.loadToken

  private val httpApp = Http.collectZIO[zhttp.http.Request] {
    case Method.GET -> _ / "org" / organizationName / "contributors" =>
      for {
        _                <- putStrLn(s"fetching repositories for $organizationName")
        repositories     <- RepositoriesRetriever.fetchRepositories(organizationName)
        _                <- putStrLn(s"got repositories") *> putStrLn("fetching contributors in parallel")
        contributorsURLs <- ZIO.succeed(repositories.map(URLBuilder.buildContributorsURL(organizationName, _)))
        contributors     <- ZIO.foreachPar(contributorsURLs)(ContributorsRetriever.fetchContributors(_)).map(_.reduce(MapCombiner.combine))
        _                <- putStrLn("got contributors") *> putStrLn("processing contributors")
        result           <- ZIO.succeed(contributors.map(_.toResult))
        sortedResult     <- ZIO.succeed(result.toSeq.sortWith(_.contributions > _.contributions))
        resultAsJson     <- ZIO.succeed(sortedResult.asJson)
        _                <- putStrLn("done") *> putStrLn(s"result = $resultAsJson")
        response         <- ZIO.succeed(zhttp.http.Response.text(s"$resultAsJson"))
      } yield response
  }

  val httpServerLayers: ZLayer[Any, Nothing, ChannelFactory with EventLoopGroup] = ChannelFactory.auto ++ EventLoopGroup.auto(8)
  val repositoryLayer: ZLayer[Any, Nothing, RepositoriesEnv] = RepositoriesRetriever.live
  val contributorsLayer: ZLayer[Any, Nothing, ContributorsEnv] = ContributorsRetriever.live
  val httpAppLayer = httpServerLayers ++ repositoryLayer ++ contributorsLayer ++ Console.live

  override def run(args: List[String]): URIO[ZEnv, ExitCode] =
    Server.start(8080, httpApp).provideLayer(httpAppLayer).exitCode
}




