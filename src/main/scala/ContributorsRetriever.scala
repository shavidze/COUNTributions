import Main.{backend, contributorsUrl}
import sttp.client3.circe.asJson
import zio.{Has, Task, ZIO, ZLayer}
import io.circe.generic.auto._

import scala.annotation.tailrec


object ContributorsRetriever {

  type ContributorsEnv = Has[ContributorsRetriever.Service]

  trait Service {
    def fetchContributors(organizationName: String, repositoryName: String)(implicit token: Token): Task[Map[String, Double]]
  }

  val live: ZLayer[Any, Nothing, ContributorsEnv] = ZLayer.succeed {
    new Service {
      override def fetchContributors(organizationName: String, repositoryName: String)(implicit token: Token): Task[Map[String, Double]] = Task.effect {
        @tailrec
        def loop(page: Int, responses: Int, accumulatedContributions: Map[String, Double]): Map[String, Double] = {
          if (isLastRequest(responses)) accumulatedContributions
          else {
            val url = contributorsUrl(organizationName, repositoryName, page)
            val response = RequestBuilder.build(url)
              .response(asJson[Vector[Contributor]])
              .send(backend)
            val contributions = response.body.fold[Map[String, Double]](_ => Map.empty, _.map(contributor => (contributor.login.fold("")(identity), contributor.contributions.fold(0.0)(identity))).toMap)
            val responseCount = contributions.size
            val combinedContributions = accumulatedContributions ++ contributions
            if (isLastRequest(responseCount)) combinedContributions
            else loop(page + 1, 100, combinedContributions)
          }
        }

        loop(1, 100, Map())
      }

      def isLastRequest: Int => Boolean = _ < 100
    }

  }

  val test: ZLayer[Any, Nothing, ContributorsEnv] = ZLayer.succeed {
    new Service {
      override def fetchContributors(organizationName: String, repositoryName: String)(implicit token: Token): Task[Map[String, Double]] = Task.succeed {
        Map("user1" -> 12.0, "user2" -> 13.0, "user3" -> 5.0)
      }
    }
  }

  def fetchContributors(organizationName: String, repositoryName: String)(implicit token: Token): ZIO[ContributorsEnv, Throwable, Map[String, Double]] =
    ZIO.accessM(_.get.fetchContributors(organizationName, repositoryName))
}