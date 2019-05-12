package xyz.sigmalab.turl.build

import sbt._

object Deps {

  object Http4sClient {

    val org = "org.http4s"
    // val ver = "1.0.0-SNAPSHOT"
    val ver = "0.18.23"

    val dsl = org %% "http4s-dsl" % ver
    val blazeServer = org %% "http4s-blaze-server" % ver
    val blazeClient = org %% "http4s-blaze-client" % ver
    val circe = org %% "http4s-circe" % ver

    val all = Seq(dsl, blazeServer, blazeClient, circe)

  }

  object Circe {

    val org = "io.circe"
    val ver = "0.10.0"

    val core = org %% "circe-core" % ver
    val generic = org %% "circe-generic" % ver
    val parser = org %% "circe-parser" % ver
    
    val all = Seq(core, generic, parser)

  }

  object ScalaTest {

    val org = "org.scalatest"
    var ver = "3.0.5"

    val scalatest = org %% "scalatest" % ver

    val all = Seq(scalatest)
    val allForTest = all.map(_ % Test)

  }

  object TypesafeCofnig {
    val org = "com.typesafe"
    val ver = "1.3.3"
    val config = org % "config" % ver
    val all = Seq(config)
  }

}

