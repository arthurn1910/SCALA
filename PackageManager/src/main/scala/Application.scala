/**
  * Created by tajgeer on 24.06.17.
  */

import java.io.IOException

import org.hibernate.boot.registry._
import org.hibernate.boot._

import scalafx.application.JFXApp
import java.net.URL
import java.util
import java.util.concurrent.{ScheduledThreadPoolExecutor, TimeUnit}
import javafx.scene.{control => jfxsc}
import javafx.scene.{layout => jfxsl}
import javafx.{event => jfxe}
import javafx.{fxml => jfxf}
import javafx.{scene => jfxs}

import scalafx.Includes._
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene


object Application extends JFXApp
{
  val resource = getClass.getResource("FXML/appView.fxml")

  if(resource == null)
  {
    throw new IOException("Cannot load resource view.fxml")
  }

  val root : jfxs.Parent = jfxf.FXMLLoader.load(resource)

  stage = new PrimaryStage()
  {
    title = "Parcel Tracker"
    scene  = new Scene(root)
  }
}
