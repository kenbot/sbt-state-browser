name := "sbt-state-browser"

organization := "kenbot"

version := "0.1"

scalaVersion := "2.9.2"

sbtPlugin := true

libraryDependencies ++= Seq(
  "org.scala-lang" % "scala-swing" % "2.9.2",
  "com.github.benhutchison" % "scalaswingcontrib" % "1.3")
