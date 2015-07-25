
name := "50Hacks"

version := "1.0-SNAPSHOT"


lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  "org.scalatestplus" %% "play" % "1.2.0" % "test",
  cache,
  ws
)
