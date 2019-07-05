package services

import model.SunInfo
import java.time.{ZoneId, ZonedDateTime}
import java.util.Date
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import play.api.libs.ws.WSClient

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global


class SunService(wsClient: WSClient) {
    def getSunInfo(lat: Double, lon: Double): Future[SunInfo] = {
        val sunshineResponseF = wsClient.url("http://api.sunrise-sunset.org/json?" +
            "lat=-1.291284&lng=36.821975&formatted=0").get()
        sunshineResponseF.map { sunshineResponse =>
        val sunshineJson = sunshineResponse.json
        val sunriseTimeStr = (sunshineJson \ "results" \ "sunrise").as[String]
        val sunsetTimeStr = (sunshineJson \ "results" \ "sunset").as[String]
        val sunriseTime = ZonedDateTime.parse(sunriseTimeStr)
        val sunsetTime = ZonedDateTime.parse(sunsetTimeStr)
        val formatter = DateTimeFormatter.ofPattern("HH:mm:ss").withZone(ZoneId.of("Africa/Nairobi"))
        val sunInfo = SunInfo(sunriseTime.format(formatter),
            sunsetTime.format(formatter))
        sunInfo
        }
    }
}