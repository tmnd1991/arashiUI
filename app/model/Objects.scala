package model

/**
 * Created by Antonio on 04/03/2015.
 */

import java.util.Date

import model.Ontology.ArashiPrefix
import org.w3.banana._
import org.w3.banana.binder._
import scala.util._
import org.w3.banana.RDF


class Objects[Rdf <: RDF](implicit
                          ops: RDFOps[Rdf],
                          recordBinder: RecordBinder[Rdf]){
  val arashi = ArashiPrefix[Rdf]
  val rdf = RDFPrefix[Rdf]
  val xsd = XSDPrefix[Rdf]
  import ops._
  import recordBinder._
  import org.w3.banana.binder._

  object ResourceBind{
    val clazz = arashi("Resource")
    implicit val classUris = classUrisFor[Resource](clazz)
    val id = property[String](arashi.id)
    val unit = optional[String](arashi.unit)
    val sampleType = property[String](arashi.sampleType)
    implicit val container = URI("http://stormsmacs/Resources")
    implicit val binder = pgb[Resource](id, unit, sampleType)(Resource.apply, Resource.unapply)
  }

  object SampleBind{
    import org.joda.time.DateTime
    import org.w3.banana.binder.JodaTimeBinders._
    val clazz = arashi.sampleType
    implicit val classUris = classUrisFor[Sample](clazz)
    val value = property[String](rdf.value)
    val date = property[DateTime](xsd.dateTime)
    implicit val container = URI("http://stormsmacs/Samples")
    implicit val binder = pgb[Sample](value, date)(Sample.apply, Sample.unapply)
  }
}