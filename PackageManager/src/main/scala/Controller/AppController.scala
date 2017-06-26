package Controller

/**
  * Created by Konrad on 25.06.2017.
  */

import java.net.URL
import java.util
import javafx.scene.{control => jfxsc, layout => jfxsl}
import javafx.{event => jfxe, fxml => jfxf}

import entities.{PackageEntity, TrackEntity}
import org.hibernate.boot.MetadataSources
import org.hibernate.boot.registry.{StandardServiceRegistry, StandardServiceRegistryBuilder}

import scala.collection.JavaConverters._
import scala.util.control.Breaks
import scalafx.Includes._
import scalafx.collections.ObservableBuffer

class AppController extends jfxf.Initializable
{
  @jfxf.FXML
  private var parcelNumberList: jfxsc.ListView[String] = _
  @jfxf.FXML
  private var stepsList: jfxsc.ListView[String] = _
  @jfxf.FXML
  private var parcelCodeLabel: jfxsc.Label = _
  @jfxf.FXML
  private var isDeliveredLabel: jfxsc.Label = _
  @jfxf.FXML
  private var parcelNumberTextField: jfxsc.TextField = _

  var parcelList:List[Any] = _
  var trackList:List[Any] = _

  @jfxf.FXML
  private def trackNewParcel(event: jfxe.ActionEvent): Unit =
  {
    println("new parcel with number " + parcelNumberTextField.getText + " tracked")
  }

  @jfxf.FXML
  private def deleteParcel(event : jfxe.ActionEvent) : Unit =
  {
    println("parcel number " + parcelNumberList.getSelectionModel.getSelectedItem + " deleted")
  }

  @jfxf.FXML
  private def refresh(event : jfxe.ActionEvent) : Unit =
  {
    println("refreshed ")
  }

  @jfxf.FXML
  private def showDetails(event : jfxe.ActionEvent) : Unit =
  {
    val selectedNumber = parcelNumberList.getSelectionModel.getSelectedItem

    var parcelId = 0
    var loop = new Breaks

    loop.breakable
    {
      for (parcel <- parcelList)
      {
        if (parcel.asInstanceOf[PackageEntity].getTrackNumber == selectedNumber)
        {
          parcelCodeLabel.setText(selectedNumber)
          if (parcel.asInstanceOf[PackageEntity].isStatus)
            isDeliveredLabel.setText("Delivered")
          else
            isDeliveredLabel.setText("Not delivered")

          parcelId = parcel.asInstanceOf[PackageEntity].getId
          loop.break()
        }
      }
    }

    for(track <- trackList)
    {
      if(track.asInstanceOf[TrackEntity].getPackageId == parcelId)
        stepsList.getItems.add(track.asInstanceOf[TrackEntity].getDate + track.asInstanceOf[TrackEntity].getLocation)
    }
  }

  def initialize(url: URL, rb: util.ResourceBundle): Unit =
  {
    val registry: StandardServiceRegistry = new StandardServiceRegistryBuilder().configure.build
    val database = new MetadataSources( registry ).buildMetadata().buildSessionFactory()

    val session = database.openSession()
    session.beginTransaction()
    parcelList = session.createQuery("FROM PackageEntity").list().asScala.toList
    trackList = session.createQuery("FROM TrackEntity").list().asScala.toList
    session.close()

    for(parcel <- parcelList)
      parcelNumberList.getItems.add(parcel.asInstanceOf[PackageEntity].getTrackNumber)
  }
}
