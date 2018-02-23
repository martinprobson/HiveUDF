name := "HiveUDF"

organization := "net.martinprobson"

version := "0.1-SNAPSHOT"

description := "HiveUDF Example (scala)"

scalaVersion := "2.12.2"

// Enables publishing to Maven repo
publishMavenStyle := true

// Use local Maven repo to resolve
resolvers += Resolver.mavenLocal

libraryDependencies ++= Seq(
  "org.scala-lang"  % "scala-library" % "2.12.2",
  "org.apache.hive" % "hive-exec" % "1.2.2" % "provided",
  "org.apache.hadoop" % "hadoop-client" % "2.7.3" % "provided",
  "org.scalatest" %% "scalatest" % "3.0.4" % "test",
  "org.scalactic" %% "scalactic" % "3.0.4" % "test",
  "org.slf4j" % "slf4j-api" % "1.7.5",
  "org.slf4j" % "slf4j-log4j12" % "1.7.25",
  "org.clapper" %% "grizzled-slf4j" % "1.3.1") 

