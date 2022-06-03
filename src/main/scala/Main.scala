import zio._
import zhttp.http._
import zhttp.service.Server

object Main extends App {

  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] =
    Server.start(8080, app).exitCode

  val app = Http.collect[Request] {
    case Method.GET -> _ / "org" / orgName / "contributors" => Response.text("TODO")
  }
}

