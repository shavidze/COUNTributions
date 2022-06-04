import RepositoryRetriever.{RepositoryEnv, fetchRepositories}
import ContributorRetriever.{ContributorsEnv, fetchContributors}
import zio.*
import zhttp.http.*
import zhttp.service.{Client, Server}
import zio.console.{Console, getStrLn, putStrLn}

object Main extends App {

  type ServicesEnv = RepositoryEnv & ContributorsEnv

  private def organizationUrl(organization: String, page: Int): String = s"https://api.github.com/orgs/$organization/repos?per_page=100&page=$page"

  private def contributorsUrl(organization: String, repositoryName: String, page: Int): String = s"https://api.github.com/repos/$organization/$repositoryName/contributors?per_page=100&page=$page"

  val env: ZLayer[Any, Nothing, ServicesEnv] = RepositoryRetriever.live ++ ContributorRetriever.live

  override def run(args: List[String]) = Server.start(8080, http).provideCustomLayer(env).exitCode

  val http: Http[ServicesEnv with Console, Throwable, Request, Response] = Http.collectZIO[Request] {
    case Method.GET -> _ / "org" / orgName / "contributors" => {
      for {
        _    <- fetchRepositories("some organization", 1)
        _    <- fetchContributors("some repo name", 1)
        resp <- ZIO.succeed(Response.text("yes"))
      } yield resp
      
    }
  }

}

object RepositoryRetriever {

  type RepositoryEnv = Has[RepositoryRetriever.Service]

  trait Service {
    def fetchRepositories(organizationName: String, pageCount: Int): Task[Unit]
  }

  val live: ZLayer[Any, Nothing, RepositoryEnv] = ZLayer.succeed {
    new Service {
      override def fetchRepositories(organizationName: String, pageCount: Int): Task[Unit] = Task.succeed {
        println("fetching repositories...")
      }
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
      override def fetchContributors(repositoryName: String, pageCount: Int): Task[Unit] = Task.succeed {
        println("fetching contributors...")
      }
    }
  }

  def fetchContributors(repositoryName: String, pageCount: Int): ZIO[ContributorsEnv, Throwable, Unit] =
    ZIO.accessM(_.get.fetchContributors(repositoryName, pageCount))

}

