package readers

import java.util.Date

/**
 * Created by tmnd91 on 10/03/15.
 */
trait SparqlSamplesReader {


  val begin = """"2015-03-03T05:22:29Z"^^xsd:dateTime"""
  val end   = """"2015-03-03T05:44:02Z"^^xsd:dateTime"""
  val query = s"""
    PREFIX  xsd:  <http://www.w3.org/2001/XMLSchema#>
    SELECT ?g ?dateTime WHERE{
      GRAPH ?g {} .
      BIND( xsd:dateTime(strafter(str(?g),"http://stormsmacs/tests/")) as ?dateTime )
      FILTER( $begin <= ?dateTime && ?dateTime <= $end)
  }"""
}
