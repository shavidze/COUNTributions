import Utils.{RequestBuilder, Token}
import Main.{backend, organizationUrl}
import io.circe.generic.auto._
import sttp.client3.circe.asJson
import zio.{Has, Task, ZIO, ZLayer}

import scala.annotation.tailrec

object RepositoriesRetriever {

  type RepositoriesEnv = Has[RepositoriesRetriever.Service]

  trait Service {
    def fetchRepositories(organizationName: String)(implicit token: Token): Task[Vector[String]]
  }

  val live: ZLayer[Any, Nothing, RepositoriesEnv] = ZLayer.succeed {
    new Service {
      override def fetchRepositories(organizationName: String)(implicit token: Token): Task[Vector[String]] = Task.effect {
          @tailrec
          def loop(page: Int, responses: Int, accumulatedRepositories: Vector[String]): Vector[String] = {
            if (responses < 100) accumulatedRepositories
            else {
              val url = organizationUrl(organizationName, page)
              val jsonResponse = RequestBuilder.build(url)
                .response(asJson[Vector[Repository]])
                .send(backend)
              val repositoryNames = jsonResponse.body.fold[Vector[String]](_ => Vector.empty, _.flatMap(_.name))
              val responseCount = repositoryNames.size
              if (responseCount < 100) accumulatedRepositories ++: repositoryNames
              else loop(page + 1, 100, accumulatedRepositories ++: repositoryNames)
            }
          }

          loop(1, 100, Vector.empty)
        }
      }
    }

  val test: ZLayer[Any, Nothing, RepositoriesEnv] = ZLayer.succeed {
    new Service {
      override def fetchRepositories(organizationName: String)(implicit token: Token): Task[Vector[String]] = Task.succeed {
        Vector("scURLa", "GithubScraper", "ZIOPlayground", "InterIO")
      }
    }
  }
  def fetchRepositories(organizationName: String)(implicit token: Token): ZIO[RepositoriesEnv, Throwable, Vector[String]] =
    ZIO.accessM(_.get.fetchRepositories(organizationName))

}