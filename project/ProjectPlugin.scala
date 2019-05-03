package xyz.sigmalab.turl.build

import sbt._
import sbt.Keys._

object ProjectPlugin extends AutoPlugin {

  object autoImport {

  }

  override def projectSettings = Seq(

    scalaVersion := "2.12.8",
    organization := "xyz.sigmalab",
    name := "turl",
    version := "0.1.0-SNAPSHOT",

    libraryDependencies ++= Deps.Http4sClient.all,
    libraryDependencies ++= Deps.ScalaTest.allForTest,
    libraryDependencies ++= Deps.Circe.all,

    resolvers += Resolver.sonatypeRepo("snapshots")
  )

}
