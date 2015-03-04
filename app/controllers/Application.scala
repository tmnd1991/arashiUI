package controllers

import java.io.{StringReader, ByteArrayInputStream, InputStreamReader}

import org.w3.banana.jena.{JenaModule, JenaOps, Jena}
import org.w3.banana.jena.io.{JenaRDFReader, JenaRDFWriter, JenaAnswerOutput}
import play.api._
import play.api.mvc._
import org.w3.banana._, syntax._, diesel._
import scala.util._

object Application extends Controller{

//object Application extends Controller with RDFModule with RDFOpsModule with RDFXMLWriterModule{
  def index = Action {
    Ok(views.html.index())
  }

}