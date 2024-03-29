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
import services._


class Application (components: ControllerComponents,
  assets: Assets, ws: WSClient, sunService: SunService, weatherService: WeatherService)
  extends AbstractController(components) {
    def index = Action.async {
      val lat = -1.291284
      val lon = 36.821975
      val sunInfoF = sunService.getSunInfo(lat, lon)
      val temperatureF = weatherService.getTemperature(lat, lon)
      for {
        sunInfo <- sunInfoF
        temperature <- temperatureF
      } yield {
        Ok(views.html.index(sunInfo, temperature))
      }
    }
    def versioned(path: String, file: Asset) = assets.versioned(path, file)
}
