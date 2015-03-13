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
    val samples =
      if (id == "")
        reader.query(new URL("http://10.0.10.15:9875/memory/freePercentage"),new Date(from),new Date(to))
      else
        reader.query(new URL(id),DateUtils.parse("2015-03-08T05:22:29Z"),DateUtils.parse("2015-03-09T05:42:29Z"))
    Ok(Json.toJson(samples))
  }
}