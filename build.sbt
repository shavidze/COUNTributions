ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.1.2"

val ZHTTPVersion = "1.0.0.0-RC27"

libraryDependencies += "dev.zio" %% "zio" % "1.0.14"
libraryDependencies += "org.http4s" %% "http4s-circe" % "0.23.11"
libraryDependencies ++= Seq(
  "io.d11" %% "zhttp" % ZHTTPVersion,
  "io.d11" %% "zhttp-test" % ZHTTPVersion % Test
)


lazy val root = (project in file("."))
  .settings(
    name := "scalac_assignment"
  )
