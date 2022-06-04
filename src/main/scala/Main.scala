import RepositoryRetriever.{RepositoryEnv, fetchRepositories}
import ContributorRetriever.{ContributorsEnv, fetchContributors}
import zio.*
import zhttp.http.*
import zhttp.service.Server
import zio.console.putStrLn

object Main extends App {

  val env: ZLayer[Any, Nothing, RepositoryEnv & ContributorsEnv] = RepositoryRetriever.live ++ ContributorRetriever.live

  override def run(args: List[String]) = Server.start(8080, http).provideLayer(env).exitCode

  val http: Http[Any, Nothing, Request, Response] = Http.collect[Request] {
    case Method.GET -> _ / "org" / orgName / "contributors" => ???
  }

}

object RepositoryRetriever {

  type RepositoryEnv = Has[RepositoryRetriever.Service]

  trait Service {
    def fetchRepositories(organizationName: String, pageCount: Int): Task[Unit]
  }

  val live: ZLayer[Any, Nothing, RepositoryEnv] = ZLayer.succeed {
    new Service {
      override def fetchRepositories(organizationName: String, pageCount: Int): Task[Unit] = Task.succeed(())
    }
  }

  def fetchRepositories(organizationName: String, pageCount: Int): ZIO[RepositoryEnv, Throwable, Unit] =
    ZIO.accessM(_.get.fetchRepositories(organizationName, pageCount))

}

object ContributorRetriever {

  type ContributorsEnv = Has[ContributorRetriever.Service]

  trait Service {
    def fetchContributors(repositoryName: String, pageCount: Int): Task[Unit]
  }

  val live: ZLayer[Any, Nothing, ContributorsEnv] = ZLayer.succeed {
    new Service {
      override def fetchContributors(repositoryName: String, pageCount: Int): Task[Unit] = Task.succeed(())
    }
  }

  def fetchContributors(repositoryName: String, pageCount: Int): ZIO[ContributorsEnv, Throwable, Unit] =
    ZIO.accessM(_.get.fetchContributors(repositoryName, pageCount))

}

