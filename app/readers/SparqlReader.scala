package readers

import java.util.Date

import model.{Resource, Objects}
import model.Ontology.ArashiPrefix
import org.w3.banana._, syntax._, diesel._
import java.net.URL
import java.util.concurrent.TimeUnit
import it.unibo.ing.utils._

import scala.collection.immutable.Iterable
import scala.concurrent.duration.Duration

/**
 * Created by Antonio on 04/03/2015.
 */

trait SparqlReader extends SparqlReaderDependencies{ self =>
  import ops._
  import sparqlOps._
  import sparqlHttp.sparqlEngineSyntax._
  val endpoint : URL
  val graph : String
  lazy val result = query()
  import DateUtils._
  def query(): Iterable[Resource] = {
      val arashi = ArashiPrefix[Rdf]
      val Objects = new Objects
      import Objects._

      val query = parseSelect(s"""
                                SELECT DISTINCT ?s ?p ?o
                                {  GRAPH $graph { ?s ?p ?o } }""").get
      val answers: Rdf#Solutions = endpoint.executeSelect(query).getOrFail(Duration(30, TimeUnit.SECONDS))
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
  }
}
