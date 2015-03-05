package readers

import java.util.Date

import model.Resource

import scala.collection.immutable.Iterable
import scala.concurrent.duration.Duration

/**
 * Created by Andrea on 05/03/15.
 */
trait SparqlCachedReader extends SparqlReader{
  val validityTime : Duration
  private var _r : Iterable[Resource] = null
  private var _lastUpd : Date = new Date(0)
  def result : Iterable[Resource] = {
    if (_r == null || expired){
      _r = query()
      _lastUpd = now
    }
    _r
  }

  private def expired : Boolean = {
    val scadenza = new Date(_lastUpd.getTime() + validityTime.toMillis)
    now.after(scadenza)
  }

  private def now = new Date()
}
