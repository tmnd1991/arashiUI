package model

import java.util.Date

import org.joda.time.DateTime

/**
 * Created by tmnd91 on 09/03/15.
 */
case class Sample(value : String, date : DateTime)
object Sample {

  import play.api.libs.json._

  implicit val resourceWrites = new Writes[Sample] {
    def writes(sample: Sample) = Json.obj(
      "date" -> sample.date,
      "value" -> sample.value
    )
  }
  /*
    implicit val resourceReads: Reads[Resource] = (
      (JsPath \ "id").read[String] and
        (JsPath \ "unit").read[String] and
        (JsPath \ "sampleType").read[String] and
        (JsPath \ "name").read[Option[String]]
      )(Resource.apply _)
      */
}
