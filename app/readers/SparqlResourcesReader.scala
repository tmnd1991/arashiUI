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

import scala.collection.immutable.Iterable
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
      val arashi = ArashiPrefix[Rdf]
      val Objects = new Objects
      import Objects._

      val query = parseConstruct(s"""
                                CONSTRUCT { ?s ?p ?o }
                                WHERE {  GRAPH $graph { ?s ?p ?o } }""").get
      val answers: Rdf#Graph = endpoint.executeConstruct(query).getOrFail(30 seconds)
      List(answers.toPointedGraph.as[Resource])
      //List(answers.graph.toPG.as[Resource])
    /*
      val it = answers.iterator map { (row: Rdf#Solution) =>
        (
          row("s").get.as[Rdf#URI].get,
          row("p").get.as[Rdf#URI].get,
          row("o").get.as[String].get
        )
      }
      lazy val resources: Map[Rdf#URI, List[(Rdf#URI, Rdf#URI, String)]] = it.toList.groupBy(_._1)
      val rs: Iterable[Resource] = for (res <- resources) yield {
        Resource(
          res._1.toString,
          res._2.find(_._2 == arashi.unit) match{
            case Some(x :(Rdf#URI,Rdf#URI,String)) => Some(x._3)
            case _ => None
          },
          res._2.find(_._2 == arashi.sampleType).get._3,
          res._2.find(_._2 == arashi.name) match{
            case Some(x : (Rdf#URI,Rdf#URI,String)) => Some(x._3)
            case _ => None
          })
      }
      rs
    */
  }
}
