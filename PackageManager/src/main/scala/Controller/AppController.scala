package Controller

/**
  * Created by Konrad on 25.06.2017.
  */

import API._
import java.net.URL
import java.util
import javafx.scene.{control => jfxsc, layout => jfxsl}
import javafx.scene.control._
import javafx.scene.control.Alert._
import javafx.{event => jfxe, fxml => jfxf}
import java.util.Date
import java.text.SimpleDateFormat
import javafx.beans.property.SimpleStringProperty
import javafx.collections.{FXCollections, ObservableList}
import javafx.scene.control.cell.PropertyValueFactory

import entities.{PackageEntity, TrackEntity}
import org.hibernate.boot.MetadataSources
import org.hibernate.boot.registry.{StandardServiceRegistry, StandardServiceRegistryBuilder}

import scala.collection.JavaConverters._
import scala.util.control.Breaks
import scalafx.Includes._
import scalafx.collections.ObservableBuffer
import scalafx.scene.input.MouseEvent

class TableStep(d :String, l :String, s :String) {
  val date: SimpleStringProperty = new SimpleStringProperty(d)
  val location: SimpleStringProperty = new SimpleStringProperty(l)
  val status: SimpleStringProperty = new SimpleStringProperty(s)

  def getDate(): String = date.get()
  def getLocation(): String = location.get()
  def getStatus(): String = status.get()
}

class AppController extends jfxf.Initializable {
  val registry: StandardServiceRegistry = new StandardServiceRegistryBuilder().configure.build
  val database = new MetadataSources(registry).buildMetadata().buildSessionFactory()
  var activePackage: Integer = null

  @jfxf.FXML
  private var parcelNumberList: jfxsc.ListView[String] = _
  @jfxf.FXML
  private var parcelCodeLabel: jfxsc.Label = _
  @jfxf.FXML
  private var isDeliveredLabel: jfxsc.Label = _
  @jfxf.FXML
  private var carrierLabel: jfxsc.Label = _
  @jfxf.FXML
  private var parcelNumberTextField: jfxsc.TextField = _
  @jfxf.FXML
  private var stepsTable: jfxsc.TableView[TableStep] = _
  @jfxf.FXML
  private var cDate: TableColumn[TableStep, String] = _
  @jfxf.FXML
  private var cLocation: TableColumn[TableStep, String] = _
  @jfxf.FXML
  private var cStatus: TableColumn[TableStep, String] = _

  var parcelList: List[Any] = _
  var trackList: List[Any] = _

  @jfxf.FXML
  private def trackNewParcel(event: jfxe.ActionEvent): Unit = {
    val parcel = API.getPackage(parcelNumberTextField.getText)
    var p: Package = null

    parcel match {
      case Right(x) => {
        p = new Package(x.asInstanceOf[org.json4s.JObject])
      }
      case Left(x) => {
        p = null

        val alert = new Alert(AlertType.ERROR)
        alert.setTitle("Error")
        alert.setHeaderText("Package not found!")
        alert.showAndWait()
      }
    }

    if (p != null) {
      var session = database.openSession()
      session.beginTransaction()

      val pack = new PackageEntity
      pack.setTrackNumber(p.getPackcode())
      pack.setStatus(p.isDelivered())
      pack.setCarrier(p.getCarrier())

      session.save(pack)
      session.flush()
      session.getTransaction.commit()

      val steps: List[Step] = p.getSteps()
      for (step <- steps) {
        session.beginTransaction()

        val track = new TrackEntity
        track.setDate(step.timestamp)
        track.setLocation(step.location)
        track.setStatus(step.status)
        track.setPackageId(pack.getId)

        session.persist(track)
        session.getTransaction.commit()
      }

      session.close()

      val alert = new Alert(AlertType.INFORMATION)
      alert.setTitle("Success")
      alert.setHeaderText("Package was added!")
      alert.showAndWait()

      refresh(event)
    }
  }

  @jfxf.FXML
  private def deleteParcel(event: jfxe.ActionEvent): Unit = {
    val selected = parcelNumberList.getSelectionModel.getSelectedItem

    if (selected == null) {
      return
    }

    val alert = new Alert(AlertType.CONFIRMATION)
    alert.setTitle("Confirmation")
    alert.setHeaderText("Are you sure, that you want to remove package #" + selected + "?")

    val result = alert.showAndWait
    if (result.get eq ButtonType.OK) {
      val session = database.openSession()
      session.beginTransaction()

      var id = 0
      var loop = new Breaks

      loop.breakable {
        for (parcel <- parcelList) {
          if (parcel.asInstanceOf[PackageEntity].getTrackNumber == selected) {
            id = parcel.asInstanceOf[PackageEntity].getId
            loop.break()
          }
        }
      }

      if (id > 0) {
        var q = session.createQuery("DELETE FROM PackageEntity WHERE id=" + id)
        q.executeUpdate()

        q = session.createQuery("DELETE FROM TrackEntity WHERE package_id=" + id)
        q.executeUpdate()
      }

      if (id == activePackage) {
        carrierLabel.setText("")
        parcelCodeLabel.setText("")
        isDeliveredLabel.setText("")
        stepsTable.getItems().clear()
      }

      session.close()

      refresh(event)
    }
  }

  @jfxf.FXML
  private def refresh(event: jfxe.ActionEvent): Unit = {
    val session = database.openSession()
    session.beginTransaction()
    parcelList = session.createQuery("FROM PackageEntity").list().asScala.toList
    trackList = session.createQuery("FROM TrackEntity").list().asScala.toList
    session.close()

    parcelNumberList.getItems().clear()

    for (parcel <- parcelList)
      parcelNumberList.getItems.add(parcel.asInstanceOf[PackageEntity].getTrackNumber)
  }

  @jfxf.FXML
  private def showDetails(event: javafx.scene.input.MouseEvent): Unit = {
    val selectedNumber = parcelNumberList.getSelectionModel.getSelectedItem

    var parcelId = 0
    var loop = new Breaks

    loop.breakable {
      for (parcel <- parcelList) {
        if (parcel.asInstanceOf[PackageEntity].getTrackNumber == selectedNumber) {
          parcelCodeLabel.setText(selectedNumber)
          if (parcel.asInstanceOf[PackageEntity].isStatus)
            isDeliveredLabel.setText("Delivered")
          else
            isDeliveredLabel.setText("Not delivered")

          carrierLabel.setText(parcel.asInstanceOf[PackageEntity].getCarrier)

          parcelId = parcel.asInstanceOf[PackageEntity].getId
          loop.break()
        }
      }
    }

    activePackage = parcelId

    stepsTable.getItems().clear()
    var data: ObservableList[TableStep] = FXCollections.observableArrayList()

    for (track <- trackList) {
      if (track.asInstanceOf[TrackEntity].getPackageId == parcelId) {
        val item = track.asInstanceOf[TrackEntity]

        val ts = item.getDate * 1000L
        val df = new SimpleDateFormat("dd.MM.yyyy, HH:mm")
        val date = df.format(ts)

        data.add(new TableStep(date, item.getLocation, item.getStatus))
      }

      stepsTable.setItems(data)

    }
  }

  def monitor(): Unit = {

    for (parcel <- parcelList) {

      var pack: Package = null

      val p = parcel.asInstanceOf[PackageEntity]
      val steps = trackList.count(_.asInstanceOf[TrackEntity].getPackageId == p.getId)

      val api = API.getPackage(p.getTrackNumber)
      api match {
        case Right(x) => {
            pack = new Package(x.asInstanceOf[org.json4s.JObject])
        }
        case Left(x) => {
            pack = null

        }
      }

      if (pack != null) {
        if (pack.getStepsCount() > steps) {
          println("Updating #" + p.getTrackNumber + "...")

          val session = database.openSession()
          session.beginTransaction()

          var q = session.createQuery("DELETE FROM TrackEntity WHERE package_id=" + p.getId)
          q.executeUpdate()

          session.flush()
          session.getTransaction.commit()

          val steps: List[Step] = pack.getSteps()
          for (step <- steps) {
            session.beginTransaction()

            val track = new TrackEntity
            track.setDate(step.timestamp)
            track.setLocation(step.location)
            track.setStatus(step.status)
            track.setPackageId(p.getId)

            session.persist(track)
            session.getTransaction.commit()
          }

          trackList = session.createQuery("FROM TrackEntity").list().asScala.toList

          session.close()

          val alert = new Alert(AlertType.INFORMATION)
          alert.setTitle("Information")
          alert.setHeaderText("Your package #" + p.getTrackNumber + " has new status!")
          alert.showAndWait()

        }
      }
    }
  }

  def initialize(url: URL, rb: util.ResourceBundle): Unit = {
    val session = database.openSession()
    session.beginTransaction()
    parcelList = session.createQuery("FROM PackageEntity").list().asScala.toList
    trackList = session.createQuery("FROM TrackEntity").list().asScala.toList
    session.close()

    for (parcel <- parcelList)
      parcelNumberList.getItems.add(parcel.asInstanceOf[PackageEntity].getTrackNumber)

    cDate.setCellValueFactory(new PropertyValueFactory[TableStep, String]("Date"))
    cLocation.setCellValueFactory(new PropertyValueFactory[TableStep, String]("Location"))
    cStatus.setCellValueFactory(new PropertyValueFactory[TableStep, String]("Status"))

    val monitoring = new Runnable() {
      def run(): Unit = {
        while (true) {
          monitor()
          Thread.sleep(10000)
        }
      }
    }

    val thread = new Thread(monitoring)
    thread.start()
  }
}
