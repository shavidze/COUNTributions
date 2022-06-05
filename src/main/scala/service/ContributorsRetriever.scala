package service

import model.{Contributor, ContributorsURL}
import sttp.client3.circe.asJson
import sttp.client3.quick.backend
import util.Utils.{RequestBuilder, Token}
import io.circe.generic.auto._
import zio.{Has, Task, ZIO, ZLayer}

import scala.annotation.tailrec

object ContributorsRetriever {

  type ContributorsEnv = Has[ContributorsRetriever.Service]

  trait Service {
    def fetchContributors(url: ContributorsURL)(implicit token: Token): Task[Map[String, Double]]
  }

  val live: ZLayer[Any, Nothing, ContributorsEnv] = ZLayer.succeed {
    new Service {

      override def fetchContributors(url: ContributorsURL)(implicit token: Token): Task[Map[String, Double]] = Task.effect {

        @tailrec
        def loop(url: ContributorsURL, responses: Int, accumulatedContributions: Map[String, Double]): Map[String, Double] = {
          if (isLastRequest(responses)) accumulatedContributions
          else {
            val response = RequestBuilder.build(url.value)
              .response(asJson[Vector[Contributor]])
              .send(backend)
            val contributions = response.body.fold[Map[String, Double]](_ => Map.empty, _.map(contributor => (contributor.login.fold("")(identity), contributor.contributions.fold(0.0)(identity))).toMap)
            val responseCount = contributions.size
            val combinedContributions = accumulatedContributions ++ contributions
            if (isLastRequest(responseCount)) combinedContributions
            else loop(url.copy(url.organization, url.repositoryName, url.page + 1), 100, combinedContributions)
          }
        }

        loop(url, 100, Map.empty)
      }

      def isLastRequest: Int => Boolean = _ < 100
    }

  }

  val test: ZLayer[Any, Nothing, ContributorsEnv] = ZLayer.succeed {
    new Service {
      override def fetchContributors(url: ContributorsURL)(implicit token: Token): Task[Map[String, Double]] = Task.succeed {
        Map("user1" -> 12.0, "user2" -> 13.0, "user3" -> 5.0)
      }

    }
  }

  def fetchContributors(url: ContributorsURL)(implicit token: Token): ZIO[ContributorsEnv, Throwable, Map[String, Double]] =
    ZIO.accessM(_.get.fetchContributors(url))

}
