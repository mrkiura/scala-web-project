package services

import play.api.libs.ws.WSClient

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global


class WeatherService(wsClient: WSClient){
    def getTemperature(lat: Double, lon: Double): Future[Double] = {
        val weatherResponseF = wsClient.url("https://samples.openweathermap.org/data/2.5/" +
            "weather?lat=35&lon=139&appid=b6907d289e10d714a6e88b30761fae22").get()
        weatherResponseF.map { weatherResponse =>
        val weatherJson = weatherResponse.json
        val temperature = (weatherJson \ "main" \ "temp").as[Double]
        temperature
        }
    }
}