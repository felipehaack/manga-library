name := "manga-library"
version := "1.0"
scalaVersion in ThisBuild := "2.12.7"

val api = Project("manga-library-api", file("manga-library-api"))

val root = Project("manga-library", file("."))
  .aggregate(api)