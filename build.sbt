name := "highrung-play"

version := "0.9.0"

scalaVersion := "2.11.8"

lazy val highrungPlay = (project in file(".")).enablePlugins(PlayScala)

libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.4" % "test"

// POM settings for Sonatype
homepage := Some(url("https://github.com/forwardloop/highrung-play"))
scmInfo := Some(ScmInfo(url("https://github.com/forwardloop/highrung-play"),
                            "git@github.com:forwardloop/highrung-play.git"))
developers := List(Developer("forwardloop",
                             "Kris",
                             "support@squashpoints.com",
                             url("https://github.com/forwardloop")))
licenses += ("GPLv3", url("https://www.gnu.org/licenses/gpl-3.0.en.html"))
publishMavenStyle := true
sonatypeProfileName := "com.highrung"
organization := "com.highrung"

// Add sonatype repository settings
publishTo := Some(
  if (isSnapshot.value)
    Opts.resolver.sonatypeSnapshots
  else
    Opts.resolver.sonatypeStaging
)


