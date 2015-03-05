package readers

import java.net.URL

import org.w3.banana.jena.JenaModule

import scala.concurrent.duration.Duration
import scala.concurrent.duration._

/**
 * Created by Antonio on 04/03/2015.
 */
class SparqlJenaReader(val endpoint : URL,
                       val graph : String,
                       val validityTime : Duration = 1 minutes) extends SparqlCachedReader with JenaModule
