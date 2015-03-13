name := """newArashiUI"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.1"

scalacOptions := Seq("-feature")

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  ws,
  "org.w3" % "jena_2.11" % "0.7.1",
  "it.unibo.ing" %% "utils" % "1.0" withSources() intransitive()
)
