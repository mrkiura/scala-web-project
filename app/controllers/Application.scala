package controllers

import controllers.Assets.Asset
import javax.inject._
import play.api.mvc._
import java.time.{ZoneId, ZonedDateTime}
import java.util.Date
import java.text.SimpleDateFormat
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import play.api.libs.ws.WSClient
import model._
import java.time.format.DateTimeFormatter


class Application @Inject() (components: ControllerComponents,
  assets: Assets, ws: WSClient) extends AbstractController(components) {
  def index = Action.async {
    val sunshineResponseF = ws.url("http://api.sunrise-sunset.org/json?" +
      "lat=-1.291284&lng=36.821975&formatted=0").get()
    val weatherResponseF = ws.url("https://samples.openweathermap.org/data/2.5/" +
      "weather?lat=35&lon=139&appid=b6907d289e10d714a6e88b30761fae22").get()
    
    for {
      sunshineResponse <- sunshineResponseF
      weatherResponse <- weatherResponseF
    } yield {
      val sunshineJson = sunshineResponse.json
      val sunriseTimeStr = (sunshineJson \ "results" \ "sunrise").as[String]
      val sunsetTimeStr = (sunshineJson \ "results" \ "sunset").as[String]
      val sunriseTime = ZonedDateTime.parse(sunriseTimeStr)
      val sunsetTime = ZonedDateTime.parse(sunsetTimeStr)
      val formatter = DateTimeFormatter.ofPattern("HH:mm:ss").withZone(ZoneId.of("Africa/Nairobi"))
      val sunInfo = SunInfo(sunriseTime.format(formatter),
        sunsetTime.format(formatter))
      val weatherJson = weatherResponse.json
      val temperature = (weatherJson \ "main" \ "temp").as[Double]
      Ok(views.html.index(sunInfo, temperature))
    }
  }

  def versioned(path: String, file: Asset) = assets.versioned(path, file)
}
