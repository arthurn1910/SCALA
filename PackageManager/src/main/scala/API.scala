/**
  * Created by tajgeer on 24.06.17.
  */

import java.net.URL
import org.json4s._
import org.json4s.native.JsonMethods._

object API {

  val API_KEY = "$2y$10$M0.HwHAgWwgHeGky18qncu6Zmu8JE4nOzV6qyEqzjxNyIOQTaUR3K"
  val USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11"

  def getPackage(packcode: String): Either[Boolean, JValue] = {

    val connection = new URL("https://api.sledzkuriera.pl/package?key=" + API_KEY + "&packcode=" + packcode).openConnection
    connection.setRequestProperty("User-Agent", USER_AGENT)
    connection.connect

    val content = scala.io.Source.fromInputStream(connection.getInputStream).mkString

    if (connection.getInputStream != null)
      connection.getInputStream.close()

    implicit val formats = DefaultFormats
    val json = parse(content)

    if ((json \ "status").equals(JBool(false))) {
      Left(false)
    }
    else {
      Right(json)
    }
  }
}
