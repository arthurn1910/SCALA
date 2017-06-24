/**
  * Created by tajgeer on 24.06.17.
  */

import entities._
import org.hibernate.boot.registry._
import org.hibernate.boot._

object Application {

  val registry: StandardServiceRegistry = new StandardServiceRegistryBuilder().configure.build
  val database = new MetadataSources( registry ).buildMetadata().buildSessionFactory()

  def main(args: Array[String]): Unit = {
    val paczka = API.getPackage("16592873837")
    var p = org.json4s.JObject();

    paczka match {
      case Right(x) => {
        p = x.asInstanceOf[org.json4s.JObject]
      }
      case Left(x) => {
        p = null
        println("Paczka nie została odnaleziona!")
      }
    }

    /*
    val pack = new PackageEntity
    pack.setTrackNumber("16592873837")
    pack.setStatus(false)

    val session = database.openSession()
    session.beginTransaction()
    session.persist(pack)
    session.getTransaction.commit()
    session.close()
    */

    println(p)
  }
}
