/**
  * Created by tajgeer on 24.06.17.
  */
case class Step(
  location :String,
  status :String,
  timestamp :Int,
  attention: Boolean
)

class Response(
  packcode :String,
  carrier :String,
  steps :List[Step],
  stepsCount :Int,
  url :String,
  requestTime :Int,
  delivered: Boolean
)

case class Package(
  status :Boolean,
  code :String,
  response :Response,
  processingTime :Double
)