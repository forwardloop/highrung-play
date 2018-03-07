name := "highrung-play"

version := "0.9.2"

scalaVersion := "2.11.12"

lazy val highrungPlay = (project in file(".")).enablePlugins(PlayScala)

libraryDependencies ++= Seq(
  "com.github.forwardloop" %% "highrung-model" % "0.9.4",
  "com.github.forwardloop" %% "glicko2s" % "0.9.3",
  "com.typesafe.play" %% "play-slick" % "2.1.0"
)

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
sonatypeProfileName := "com.github.forwardloop"
organization := "com.github.forwardloop"

// Add sonatype repository settings
publishTo := Some(
  if (isSnapshot.value)
    Opts.resolver.sonatypeSnapshots
  else
    Opts.resolver.sonatypeStaging
)