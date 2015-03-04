package readers

import java.net.URL

import org.w3.banana.jena.JenaModule

/**
 * Created by Antonio on 04/03/2015.
 */
class SparqlJenaReader(val endpoint : URL,
                       val graph : String) extends SparqlReader with JenaModule
