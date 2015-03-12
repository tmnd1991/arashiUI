package readers

import java.net.URL

import org.w3.banana.jena.JenaModule

/**
 * Created by tmnd91 on 12/03/15.
 */
class SparqlSampleJenaReader(val endpoint : URL) extends SparqlSamplesReader with JenaModule{
}
