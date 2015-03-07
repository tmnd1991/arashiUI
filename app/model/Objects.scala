package model

/**
 * Created by Antonio on 04/03/2015.
 */


import java.net.URL

import model.Ontology.ArashiPrefix
import org.w3.banana._
import org.w3.banana.binder._
import scala.util._


class Objects[Rdf <: RDF](implicit
                          ops: RDFOps[Rdf],
                          recordBinder: RecordBinder[Rdf]){
  val arashi = ArashiPrefix[Rdf]
  import ops._
  import recordBinder._

  object ResourceBind{
    val clazz = URI("http://arashi/Resource")
    implicit val classUris = classUrisFor[Resource](clazz)
    val name = optional[String](arashi.name)
    val unit = optional[String](arashi.unit)
    val sampleType = property[String](arashi.sampleType)
    val id = property[String](arashi.id)
    implicit val container = URI("http://example.com/persons/")
    implicit val binder = pgbWithId[Resource](r => URI(r.id))(id, unit, sampleType, name)(Resource.apply, Resource.unapply)
  }
}