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
    val responsF = ws.url("http://api.sunrise-sunset.org/json?" +
      "lat=-1.291284&lng=36.821975&formatted=0").get()
    responsF.map { response => 
      val json = response.json
      val sunriseTimeStr = (json \ "results" \ "sunrise").as[String]
      val sunsetTimeStr = (json \ "results" \ "sunset").as[String]
      val sunriseTime = ZonedDateTime.parse(sunriseTimeStr)
      val sunsetTime = ZonedDateTime.parse(sunsetTimeStr)
      val formatter = DateTimeFormatter.ofPattern("HH:mm:ss").withZone(ZoneId.of("Africa/Nairobi"))
      val sunInfo = SunInfo(sunriseTime.format(formatter),
        sunsetTime.format(formatter))
      Ok(views.html.index(sunInfo))
    }
  }

  def versioned(path: String, file: Asset) = assets.versioned(path, file)
}
