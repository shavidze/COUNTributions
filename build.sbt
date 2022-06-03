ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.1.2"

libraryDependencies += "dev.zio" %% "zio" % "1.0.14"
libraryDependencies += "org.http4s" %% "http4s-blaze-server" % "0.23.11"
libraryDependencies += "org.http4s" %% "http4s-circe" % "0.23.11"
libraryDependencies += "org.http4s" %% "http4s-dsl" % "0.23.11"

lazy val root = (project in file("."))
  .settings(
    name := "scalac_assignment"
  )
