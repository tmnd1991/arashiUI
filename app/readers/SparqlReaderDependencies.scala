package readers

import org.w3.banana._

/**
 * Created by Antonio on 04/03/2015.
 */
trait SparqlReaderDependencies extends RDFModule
with RecordBinderModule
with RDFOpsModule
with SparqlOpsModule
with SparqlHttpModule{
  implicit val sparqlOps: SparqlOps[Rdf]
}