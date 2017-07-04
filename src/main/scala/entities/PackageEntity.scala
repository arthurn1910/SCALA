package entities

import javax.persistence._

/**
  * Created by arthurn on 24.06.17.
  */
@Entity
@Table(name = "package", schema = "public")
class PackageEntity {
  private var id = 0
  private var trackNumber = ""
  private var status = false
  private var carrier = ""

  @Id
  @Column(name = "id", nullable = false)
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  def getId: Int = id

  def setId(id: Int): Unit = {
    this.id = id
  }

  @Basic
  @Column(name = "carrier", nullable = false)
  def getCarrier: String = carrier

  def setCarrier(carrier :String): Unit = {
    this.carrier = carrier
  }

  @Basic
  @Column(name = "track_number", nullable = false, length = -1)
  def getTrackNumber: String = trackNumber

  def setTrackNumber(trackNumber: String): Unit = {
    this.trackNumber = trackNumber
  }

  @Basic
  @Column(name = "status", nullable = false)
  def isStatus: Boolean = status

  def setStatus(status: Boolean): Unit = {
    this.status = status
  }

  override def equals(o: Any): Boolean = {
    if (this.equals(o)) return true
    if (o == null || (getClass ne o.getClass)) return false
    val that = o.asInstanceOf[PackageEntity]
    if (id != that.id) return false
    if (status != that.status) return false
    if (if (trackNumber != null) !(trackNumber == that.trackNumber)
    else that.trackNumber != null) return false
    true
  }

  override def hashCode: Int = {
    var result = id
    result = 31 * result + (if (trackNumber != null) trackNumber.hashCode
    else 0)
    result = 31 * result + (if (status) 1
    else 0)
    result
  }
}