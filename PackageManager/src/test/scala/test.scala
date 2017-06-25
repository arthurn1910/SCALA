/**
  * Created by tajgeer on 25.06.17.
  */
import org.scalatest._

class test extends FlatSpec with Matchers {

  "API" should "return package info for valid packcode" in {

    val api = API.getPackage("16592873837")
    var p: org.json4s.JObject = null

    api match {
      case Right(x) => {
        p = x.asInstanceOf[org.json4s.JObject]
      }
      case Left(x) => {
        p = null
      }
    }

    p should not be null

    val paczka = new Paczka(p)

    paczka.getCarrier() should be ("DHL")
    paczka.getPackcode() should be ("16592873837")
    assert(paczka.getStepsCount() >= 0)

  }

  "API" should "return false where no package found" in {

    val api = API.getPackage("XYZ")

    assert(api === Left(false))

  }
}
