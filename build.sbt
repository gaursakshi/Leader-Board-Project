name := """Leader Board"""
organization := "com.example"

version := "1.0-SNAPSHOT"
//lazy val root = (project in file(".")).enablePlugins(PlayJava,PlayEbean)
lazy val root = (project in file(".")).enablePlugins(PlayJava, PlayEbean, PlayNettyServer).disablePlugins(PlayAkkaHttpServer)

scalaVersion := "2.13.5"
libraryDependencies += guice
libraryDependencies += javaJdbc
resolvers += "jitpack" at "https://jitpack.io"
libraryDependencies += "com.github.thibaultmeyer" % "play-rabbitmq-module" % "release~20.08"
libraryDependencies +="com.rabbitmq" % "amqp-client" % "2.8.1"
libraryDependencies ++= Seq(
  ws,
  filters,
  evolutions,
  javaJdbc,
  "org.postgresql" % "postgresql" % "42.1.1",
  "org.junit.jupiter" % "junit-jupiter-api" % "5.7.0" % Test,
  "org.junit.jupiter" % "junit-jupiter-engine" % "5.7.0" % Test,
)
// JUnit for testing
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "5.1.0" % Test

// Mockito for mocking
libraryDependencies += "org.mockito" % "mockito-core" % "3.12.4" % Test

// Play Framework test utilities
libraryDependencies += "com.typesafe.play" %% "play-test" % "2.8.10" % Test

// Optional: ScalaTest for additional testing capabilities
libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.10" % Test


libraryDependencies += "com.rabbitmq" % "amqp-client" % "5.12.0"

libraryDependencies +="org.projectlombok" % "lombok" % "1.18.2"








