import sbt.Package._
import sbt._
import Docker.autoImport.exposedPorts

scalaVersion := "2.12.4"

enablePlugins(DockerPlugin)
exposedPorts := Seq(8080)

libraryDependencies ++= Vector(
  Library.jackson_scala,
  Library.vertx_lang_scala,
  Library.vertx_web,
  Library.scalaTest % "test",
  // Uncomment for clustering
  // Library.vertx_hazelcast,

  //required to get rid of some warnings emitted by the scala-compile
  Library.vertx_codegen
)

packageOptions += ManifestAttributes(
  ("Main-Verticle",
   "scala:com.github.dokuroyh.vertx.scala.todomvc.HttpVerticle"))
