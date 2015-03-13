package model

/**
 * Created by Antonio on 04/03/2015.
 */

import com.sun.org.apache.bcel.internal.classfile.Unknown
import model.Ontology.ArashiPrefix
import scala.util._
import org.w3.banana._
import org.w3.banana.binder._

class Objects[Rdf <: RDF](implicit
                          ops: RDFOps[Rdf],
                          recordBinder: RecordBinder[Rdf]){
  val arashi = ArashiPrefix[Rdf]
  val rdf = RDFPrefix[Rdf]
  val xsd = XSDPrefix[Rdf]
  import ops._
  import recordBinder._
/*
  object URIBind {
    implicit val binder: PGBinder[Rdf, java.net.URI] = new PGBinder[Rdf, java.net.URI] {
      def fromPG(pointed: PointedGraph[Rdf]): Try[java.net.URI] = Try(new java.net.URI(pointed.toString))
      def toPG(uri: java.net.URI): PointedGraph[Rdf] = PointedGraph.apply(makeUri(uri.toString))
    }
  }*/


  object ResourceBind{
    implicit val binderURI: PGBinder[Rdf, java.net.URI] = new PGBinder[Rdf, java.net.URI] {
      def fromPG(pointed: PointedGraph[Rdf]): Try[java.net.URI] = Try(new java.net.URI(pointed.pointer.toString))
      def toPG(uri: java.net.URI): PointedGraph[Rdf] = PointedGraph.apply(makeUri(uri.toString))
    }
    val clazz = arashi("Resource")
    implicit val classUris = classUrisFor[Resource](clazz)
    val id = property[java.net.URI](arashi.id)
    val unit = optional[String](arashi.unit)
    val sampleType = property[String](arashi.sampleType)
    implicit val container = URI("http://stormsmacs/Resources")
    implicit val binder: PGBinder[Rdf, Resource] = pgb[Resource](id, unit, sampleType)(Resource.apply, Resource.unapply)
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