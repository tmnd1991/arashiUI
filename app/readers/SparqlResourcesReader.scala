package readers

import java.net.URL
import java.util.Date
import scala.concurrent.duration._

import it.unibo.ing.utils._
import it.unibo.ing.utils.DateUtils._

import model.{Resource, Objects}
import model.Ontology.ArashiPrefix
import org.w3.banana._
import org.w3.banana.diesel._
import org.w3.banana.io._
import scala.util.Try
import scala.concurrent.duration.Duration

/**
 * Created by Antonio on 04/03/2015.
 */

trait SparqlResourcesReader extends SparqlReaderDependencies{ self =>
  import ops._
  import sparqlOps._
  import sparqlHttp.sparqlEngineSyntax._

  val endpoint : URL
  val graph : String


  def query(): Iterable[Resource] = {
    //return Iterable[Resource]()
    val start = new Date();
    val arashi = ArashiPrefix[Rdf]
    val Objects = new Objects
    import Objects._
    import Objects.ResourceBind._
    val sQuery = s"""PREFIX  xsd:  <http://www.w3.org/2001/XMLSchema#>
                     PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
                     PREFIX smacs: <http://ing.unibo.it/smacs/predicates#>
                     CONSTRUCT {
                        ?s smacs:unit ?unit .
                        ?s smacs:sampleType ?sampleType .
                        ?s smacs:id     ?s .
                        ?s rdf:type smacs:Resource
                     }
                     WHERE{
                        GRAPH $graph { ?s smacs:sampleType ?sampleType .
                                       ?s smacs:unit ?unit } .
                        OPTIONAL { ?s smacs:unit ?unit }
                     }"""
    val query = parseConstruct(sQuery).getOrElse(throw new Exception("cannot parse sparql query"))
    val resultGraph = endpoint.executeConstruct(query).getOrFail(30 seconds)
    val res = resultGraph.triples.collect{
      case Triple(resource, rdf.typ, arashi.Resource) =>{
        val pg = PointedGraph(resource, resultGraph)
        pg.as[Resource].toOption
      }
    }.flatten
    val end = new Date()
    println("lettura resources in" + (end.getTime - start.getTime) + " ms")
    return res
    //List(Resource("http://10.0.10.15:9875/memory/freePercentage",Some("%"),"gauge"))
  }
}
