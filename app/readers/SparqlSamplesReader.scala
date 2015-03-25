package readers

import java.util.Date
import java.net.URL
import org.joda.time.DateTime

import scala.concurrent.duration._
import org.w3.banana._, io._, binder._, jena._, diesel._

import it.unibo.ing.utils.DateUtils
import model.Ontology.ArashiPrefix
import model.{Objects, Sample}

/**
 * @author Antonio Murgia
 * @version 10/03/15
 */
trait SparqlSamplesReader extends SparqlReaderDependencies{
  self =>
  import ops._
  import sparqlOps._
  import sparqlHttp.sparqlEngineSyntax._

  val endpoint : URL

  def query(resource : URL, begin : Date, end : Date) : List[Sample] = {
    val startExec = new Date()
    val arashi = ArashiPrefix[Rdf]
    val Objects = new Objects
    import Objects._
    import Objects.SampleBind._
    val beginString = DateUtils.format(begin)
    val endString   = DateUtils.format(end)
    val resString   = resource.toString()
    val sString = s"""
      PREFIX  xsd:  <http://www.w3.org/2001/XMLSchema#>
      PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
      PREFIX smacs: <http://ing.unibo.it/smacs/predicates#>
      CONSTRUCT {
        ?g rdf:value ?o .
        ?g xsd:dateTime ?dateTime .
        ?g rdf:type smacs:sampleType
      }
      WHERE{
        VALUES ?x {<$resString>} .
        GRAPH ?g { ?x rdf:value ?o} .
        BIND( xsd:dateTime(strafter(str(?g),"http://stormsmacs/tests/")) as ?dateTime ) .
        FILTER( "$beginString"^^xsd:dateTime <= ?dateTime &&
                "$endString"^^xsd:dateTime >= ?dateTime )
      }"""

    val query = parseConstruct(sString).getOrElse(throw new Exception("cannot parse sparql query"))
    val resultGraph = endpoint.executeConstruct(query).getOrFail(30 seconds)
    val samples = resultGraph.triples.collect {
      case Triple(sample, rdf.typ, arashi.sampleType) =>
        val pg = PointedGraph(sample, resultGraph)
        pg.as[Sample].toOption
    }.flatten
    val res = samples.toList.sortBy(_.date)
    val endExec = new Date()
    println("lettura samples in" + (endExec.getTime - startExec.getTime) + " ms")
    return res
  }

  implicit class RichDateTime(x : DateTime) extends Ordered[DateTime]{
    override def compare(that: DateTime): Int = {
      x.getMillis.compareTo(that.getMillis)
    }
  }

}
