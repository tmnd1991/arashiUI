package readers

import java.net.URL
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

    val arashi = ArashiPrefix[Rdf]
    val Objects = new Objects
    import Objects._
    import Objects.ResourceBind._
    val sQuery = s"""PREFIX  xsd:  <http://www.w3.org/2001/XMLSchema#>
                     PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
                     PREFIX smacs: <http://ing.unibo.it/smacs/predicates#>
                     CONSTRUCT {
                        ?s smacs:unit ?o .
                        ?s smacs:sampleType ?o .
                        ?s smacs:id     ?s .
                        ?s rdf:type smacs:Resource
                     }
                     WHERE{
                        GRAPH $graph { ?s ?p ?o } .
                        OPTIONAL { ?s smacs:unit ?o }
                     }"""
    val query = parseConstruct(sQuery).getOrElse(throw new Exception("cannot parse sparql query"))
    val resultGraph = endpoint.executeConstruct(query).getOrFail(30 seconds)
    val x: Iterable[Resource] = resultGraph.triples.collect{
      case Triple(resource, rdf.typ, arashi.Resource) =>{
        val pg = PointedGraph(resource, resultGraph)
        pg.as[Resource].toOption
      }
    }.flatten
    x
    //List(Resource("http://10.0.10.15:9875/memory/freePercentage",Some("%"),"gauge"))
  }
}
