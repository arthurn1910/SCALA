package API
/**
  * Created by tajgeer on 25.06.17.
  */
import org.json4s.DefaultFormats
import org.json4s.JsonAST.JObject

case class Step(
   location :String,
   status :String,
   timestamp :Int,
   attention: Boolean
 )

class Package(val paczka :JObject) {

  implicit val formats = DefaultFormats

  def getPackcode(): String = (paczka \ "response" \ "packcode").extract[String]
  def getCarrier(): String = (paczka \ "response" \ "carrier").extract[String]
  def getSteps(): List[Step] = (paczka \ "response" \ "steps").extract[List[Step]]
  def getStepsCount(): Int = (paczka \ "response" \ "stepsCount").extract[Int]
  def isDelivered(): Boolean = (paczka \ "response" \ "delivered").extract[Boolean]
  def url(): String = (paczka \ "response" \ "url").extract[String]
}