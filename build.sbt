name := "highrung-play"

version := "1.0"

scalaVersion := "2.11.8"

lazy val highrungPlay = (project in file(".")).enablePlugins(PlayScala)

libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.4" % "test"



