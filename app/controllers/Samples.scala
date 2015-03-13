package controllers

import java.net.URL
import java.util.Date

import it.unibo.ing.utils.DateUtils
import play.api.mvc._
import readers.SparqlSampleJenaReader
import play.api.libs.json.Json

/**
 * Created by tmnd91 on 12/03/15.
 */
object Samples extends Controller{
  val reader = new SparqlSampleJenaReader(new URL("http://137.204.57.150:3030/ds/query"))

  def samplesOf(id : String, from : Long, to : Long) = Action{
    val samples = reader.query(new URL(id),new Date(from),new Date(to))
    Ok(Json.toJson(samples))
  }
}