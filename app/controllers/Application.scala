package controllers

import controllers.Assets.Asset
import javax.inject._
import play.api.mvc._
import java.util.Date
import java.text.SimpleDateFormat
import scala.concurrent.Future

class Application @Inject() (components: ControllerComponents, assets: Assets)
    extends AbstractController(components) {
  def index = Action.async {
    val date = new Date()
    val dateStr = new SimpleDateFormat().format(date)
    Future.successful {Ok(views.html.index(dateStr))}
  }

  def versioned(path: String, file: Asset) = assets.versioned(path, file)
}
