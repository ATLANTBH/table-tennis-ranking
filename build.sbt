name := """tennis-ranking"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava, PlayEbean)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs,
  "org.webjars.bower" % "jquery" % "2.1.4",
  "org.webjars" % "bootstrap" % "3.3.5",
  "org.postgresql" % "postgresql" % "42.4.0"
)
