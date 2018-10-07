import play.core.PlayVersion
import sbt._

object Dependencies extends AutoPlugin {
  val play = "com.typesafe.play" %% "play" % PlayVersion.current
  val playJson = "com.typesafe.play" %% "play-json" % PlayVersion.current
  val playTest = "com.typesafe.play" %% "play-test" % PlayVersion.current

  // Persistence
  val postgres = "org.postgresql" % "postgresql" % "42.1.4"
  val relate = "com.lucidchart" %% "relate" % "2.0.1"
  val hikari = "com.zaxxer" % "HikariCP" % "2.7.2"
  val liquibase = "org.liquibase" % "liquibase-core" % "3.5.3"

  // Validation
  object Accord {
    private val version = "0.7.1"
    val core = "com.wix" %% "accord-core" % version excludeAll ExclusionRule(organization = "org.scalatest")
  }

  // Test
  val scalatest = "org.scalatest" %% "scalatest" % "3.0.4"
  val scalatestPlay = "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2"
  val mockito = "org.mockito" % "mockito-core" % "2.12.0"
  val guiceTestkit = "com.google.inject.extensions" % "guice-testlib" % "4.1.0"
}
