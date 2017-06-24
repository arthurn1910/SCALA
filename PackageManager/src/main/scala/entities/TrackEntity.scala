package entities

import javax.persistence._

/**
  * Created by arthurn on 24.06.17.
  */
@Entity
@Table(name = "track", schema = "public")
class TrackEntity {
  private var id = 0
  private var date = 0
  private var location = ""
  private var status = ""
  private var packageId = 0

  @Id
  @Column(name = "id", nullable = false)
  def getId: Int = id

  def setId(id: Int): Unit = {
    this.id = id
  }

  @Basic
  @Column(name = "date", nullable = false)
  def getDate: Int = date

  def setDate(date: Int): Unit = {
    this.date = date
  }

  @Basic
  @Column(name = "location", nullable = false, length = -1)
  def getLocation: String = location

  def setLocation(location: String): Unit = {
    this.location = location
  }

  @Basic
  @Column(name = "status", nullable = false, length = -1)
  def getStatus: String = status

  def setStatus(status: String): Unit = {
    this.status = status
  }

  override def equals(o: Any): Boolean = {
    if (this.equals(o)) return true
    if (o == null || (getClass ne o.getClass)) return false
    val that = o.asInstanceOf[TrackEntity]
    if (id != that.id) return false
    if (if (date != null) !(date == that.date)
    else that.date != null) return false
    if (if (location != null) !(location == that.location)
    else that.location != null) return false
    if (if (status != null) !(status == that.status)
    else that.status != null) return false
    true
  }

  override def hashCode: Int = {
    var result = id
    result = 31 * result + (if (date != null) date.hashCode
    else 0)
    result = 31 * result + (if (location != null) location.hashCode
    else 0)
    result = 31 * result + (if (status != null) status.hashCode
    else 0)
    result
  }

  def getPackageId: Int = packageId

  def setPackageId(packageId: Int): Unit = {
    this.packageId = packageId
  }
}