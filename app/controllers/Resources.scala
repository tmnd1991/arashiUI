package controllers

import org.w3.banana._, syntax._, diesel._
import org.w3.banana.jena.JenaModule
import org.w3.banana.jena.io.JenaRDFWriter
import play.api._
import play.api.mvc._
import readers.SparqlJenaReader
import java.net.URL

/**
 * Created by Antonio on 03/03/2015.
 */
object Resources extends Controller{
  /*
  val objects = new model.Objects()
  import objects._
  import ops._
  val resource = Resource("http://stosmacasad/asdf","ms","gauge")
  val x = resource.toPG.
  val s = JenaRDFWriter.n3Writer.asString(x,"")
  val ss = """<http://stosmacasad/asdf> <http://ing.unibo.it/smacs/predicates#id> "http://stosmacasad/asdf" ; <http://ing.unibo.it/smacs/predicates#sampleType> "gauge" ; <http://ing.unibo.it/smacs/predicates#unit> """
  */
  def allResources = Action{
    val reader = new SparqlJenaReader(new URL("http://137.204.57.150:3030/ds/query"), "<http://stormsmacs/tests/Resources>")
    Ok(reader.result.toList.toString())
  }
}
