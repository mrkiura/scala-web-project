import controllers.Application
import play.api.ApplicationLoader.Context
import play.api._
import play.api.libs.ws.ahc.AhcWSComponents
import play.api.mvc._
import router.Routes
import play.api.routing.Router
import com.softwaremill.macwire._
import _root_.controllers.AssetsComponents

import play.filters.HttpFiltersComponents
import services.{SunService, WeatherService}

import scala.concurrent.Future


class AppApplicationLoader extends ApplicationLoader {
    def load(context: Context) = {
        LoggerConfigurator(context.environment.classLoader).foreach { cfg =>
            cfg.configure(context.environment)
        }
        new AppComponents(context).application
    }
}

class AppComponents(context: Context) extends
    BuiltInComponentsFromContext(context) with AhcWSComponents
    with AssetsComponents with HttpFiltersComponents {
        private val log = Logger(this.getClass)
        override lazy val controllerComponents = wire[DefaultControllerComponents]
        lazy val prefix: String = "/"
        lazy val router: Router = wire[Routes]
        lazy val sunService = wire[SunService]
        lazy val weatherService = wire[WeatherService]
        lazy val applicationController = wire[Application]

        applicationLifecycle.addStopHook { () =>
            log.info("The app is about to stop")
            Future.successful(Unit)
        }

        val onStart = {
            log.info("The app is starting....")
        }
    } 