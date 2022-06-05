ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.8"

val ZHTTPVersion = "1.0.0.0-RC27"

libraryDependencies += "dev.zio" %% "zio" % "1.0.14"
libraryDependencies += "com.softwaremill.sttp.client3" %% "circe" % "3.6.2"

val circeVersion = "0.14.1"

libraryDependencies ++= Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser"
).map(_ % circeVersion)

libraryDependencies ++= Seq(
  "io.d11" %% "zhttp" % ZHTTPVersion,
  "io.d11" %% "zhttp-test" % ZHTTPVersion % Test
)


lazy val root = (project in file("."))
  .settings(
    name := "scalac_assignment"
  )
