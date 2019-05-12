package xyz.sigmalab.turl.test

import org.scalatest._
import cats._
import cats.implicits._
import cats.syntax._
import cats.effect._
import cats.effect.implicits._
import io.circe.Decoder.Result
import io.circe.{Decoder, HCursor, Json}
import org.http4s._
import org.http4s.client._
import org.http4s.headers._
import org.http4s.circe._
import org.http4s.circe.CirceEntityDecoder._
import com.typesafe.config.{Config, ConfigFactory}

case class APIKey(key: String, secret: String)

case class BearerAccessToken(value: String)

object BearerAccessToken {
  implicit val circeDecoder = new Decoder[BearerAccessToken] {
    override def apply(c: HCursor): Result[BearerAccessToken] = {
      println(">>>","HERE :)", c)
      for {
        // tokenType <- c.downField("token_type").as[String]
        tokenValue <- c.downField("access_token").as[String]
      } yield BearerAccessToken(tokenValue)
    }
  }
  // implicit val circeDecoderHttp4s = jsonDecoder[IO]
}

object InitWork {

  def APIKeyFromConfig() = {
    val config = ConfigFactory.load()
    APIKey(
      config.getString("twitter.apikey.key"),
      config.getString("twitter.apikey.secret")
    )
  }

  /**
    *
    * {"token_type":"bearer","access_token":"AAAAAAAAAAAAAAAAAAAAAHVK%2BQAAAAAA5L4cU8lkmJ%2F7Ilu1v%2FHzhfTAF8U%3D10dUvd21KdJQwLDPYeYUXt5PeV5zkZoOYdSvOSBZidA3YUBlmi"}
    */
  def getToken(apikey: APIKey)(implicit client: Client[IO]):  IO[BearerAccessToken] = {

    val body = {
      val form = UrlForm(
        "grant_type" -> "client_credentials"
      )
      val entity: IO[Entity[IO]] = UrlForm.entityEncoder[IO].toEntity(form)
      entity.unsafeRunSync.body
    }

    val apikey = APIKeyFromConfig()

    val req = Request(
      Method.POST,
      Uri.unsafeFromString("https://api.twitter.com/oauth2/token"),
      HttpVersion.`HTTP/2.0`,
      Headers(
        Authorization(BasicCredentials(apikey.key, apikey.secret))
      ),
      body
    ).withContentType(
      `Content-Type`.apply(MediaType.`application/x-www-form-urlencoded`)
    )

    client.expect[BearerAccessToken](req)
  }
}

class InitWork extends AsyncFlatSpec with MustMatchers {

  /*
  it must "server-?" in {

    import cats.effect._
    // import cats.effect._

    import org.http4s._
    // import org.http4s._

    import org.http4s.dsl.io._
    // import org.http4s.dsl.io._

    import org.http4s.server.blaze._
    // import org.http4s.server.blaze._

    import scala.concurrent.ExecutionContext.Implicits.global
    // import scala.concurrent.ExecutionContext.Implicits.global

    val service = HttpRoutes.of[IO] {
      case GET -> Root / "hello" / name =>
        Ok(s"Hello, $name.")
    }
    // service: org.http4s.HttpRoutes[cats.effect.IO] = Kleisli(org.http4s.HttpRoutes$$$Lambda$41860/596099131@75f875c8)

    val builder = BlazeBuilder[IO].bindHttp(8080, "localhost").mountService(service, "/").start
    // builder: cats.effect.IO[org.http4s.server.Server[cats.effect.IO]] = IO$1861786547

    val server = builder.unsafeRunSync
    // server: org.http4s.server.Server[cats.effect.IO] = BlazeServer(/127.0.0.1:8080)

    ???
  }*/

  def returnString(url: String) = {

    import cats._, cats.effect._, cats.implicits._
    import cats.effect._
    import org.http4s._
    import org.http4s.client._
    import org.http4s.client.blaze._
    import org.http4s.client.dsl.io._
    import org.http4s.headers._
    import org.http4s.MediaType

    val httpClient = Http1Client[IO]().unsafeRunSync()

    val key = "b6wRXEIi0BS77dLJmMktmskTB"
    val secret = "cJDvf7P1vwkO34TaR39bOjgmEWnqib0SycThXVKQS9ZgsA1S53"
    val xey = "YjZ3UlhFSWkwQlM3N2RMSm1Na3Rtc2tUQjpjSkR2ZjdQMXZ3a08zNFRhUjM5Yk9qZ21FV25xaWIwU3ljVGhYVktRUzlaZ3NBMVM1Mw=="

    val form = UrlForm(
      "grant_type" -> "client_credentials"
    )

    val entity: IO[Entity[IO]] = UrlForm.entityEncoder[IO].toEntity(form)

    val req = Request[IO](
      Method.POST,
      Uri.unsafeFromString("https://api.twitter.com/oauth2/token"),
      HttpVersion.`HTTP/2.0`,
      Headers(
        Authorization(BasicCredentials(key, secret))
      ),
      entity.unsafeRunSync().body
    ).withContentType(
      `Content-Type`.apply(MediaType.`application/x-www-form-urlencoded`)
    )

    httpClient.expect[String](req)
  }

  it must "init-client" in {

    import cats.effect._

    import org.http4s.client.blaze._
    // import org.http4s.client.blaze._

    import org.http4s.client._
    // import org.http4s.client._

    implicit val httpClient: Client[IO] = Http1Client[IO]().unsafeRunSync
    // httpClient: org.http4s.client.Client[cats.effect.IO] = Client(Kleisli(org.http4s.client.blaze.BlazeClient$$$Lambda$41889/699463954@4f942244),IO$1886267402)

    InitWork.getToken(InitWork.APIKeyFromConfig()).map { tk =>
      info(tk.toString)
      println(">>>", tk)
      assert(true, "?")
    }.unsafeToFuture()
  }

}
