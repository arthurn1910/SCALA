/**
  * Created by tajgeer on 24.06.17.
  */

import org.hibernate.boot.registry._
import org.hibernate.boot._

import scalafx.application._
import scalafx.geometry._
import scalafx.scene._

object Application {

  val registry: StandardServiceRegistry = new StandardServiceRegistryBuilder().configure.build
  val database = new MetadataSources( registry ).buildMetadata().buildSessionFactory()

  def main(args: Array[String]): Unit = {

    val paczka = API.getPackage("16592873837")
    var p: Package = null

    paczka match {
      case Right(x) => {
        p = new Package(x.asInstanceOf[org.json4s.JObject])
      }
      case Left(x) => {
        p = null
        println("Paczka nie została odnaleziona!")
      }
    }

    // Zapis przesyłki (wraz ze statusami) do bazy
    /*val pack = new PackageEntity
    pack.setTrackNumber(p.getPackcode())
    pack.setStatus(p.isDelivered())

    val session = database.openSession()
    session.beginTransaction()
    session.persist(pack)
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

    session.close()*/

    // jakieś informacje o znalezionej przesyłce
    if (p != null)
    {
      println("Numer przesyłki: " + p.getPackcode())
      println("Liczba statusów: " + p.getStepsCount())
      println("Obsługujący kurier: " + p.getCarrier())

      val steps: List[Step] = p.getSteps()
      for (step <- steps) {
        println(step.timestamp + " | " + step.location + " | " + step.status)
      }
    }
  }
}
