import zio._

object Main extends zio.App {

  override def run(args: List[String]): URIO[ZEnv, ExitCode] = app.exitCode

  val app: ZIO[Any, Throwable, Unit] = ZIO.succeed(())

}
