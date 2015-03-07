package model

import java.net.URL

/**
 * Created by Andrea on 05/03/15.
 */
class ResourceTreeNode(_id : String, val children : Boolean = true){
  val text = takeProtocolAway(_id)

  val id = text.hashCode

  val parentText : String = {
    val splittedAndReversed = text.reverse.split("/")
    if (splittedAndReversed.length > 1)
      text.take(text.length - (splittedAndReversed.head.length+1))
    else
      "#"
  }

  val parentId : Int = parentText.hashCode

  override def equals(x : Any) : Boolean = x match{
    case xx : ResourceTreeNode => this.id == xx.id
    case _ => false
  }

  override def hashCode() : Int = id.hashCode

  private def takeProtocolAway(s : String) : String = {
    try{
      val u = new URL(s)
      s.substring(u.getProtocol.length + 3)
    }
    catch{
      case _ : Throwable => s
    }
  }

}
object ResourceTreeNode{
  import play.api.libs.json._
  implicit val resourceTreeNodeWrites = new Writes[ResourceTreeNode] {
    def writes(rtn: ResourceTreeNode) = Json.obj(
      "id" -> rtn.id,
      "parent" -> (if (rtn.parentText == "#") "#" else rtn.parentId),
      "text" -> (if (rtn.parentText == "#") rtn.text else rtn.text.substring(rtn.parentText.length)),
      "children" -> rtn.children,
      "parentText" -> rtn.parentText
    )
  }
  def convert(r : Resource, first : Boolean) : ResourceTreeNode = {
    new ResourceTreeNode(r.id.toString,!first)
  }
  def convertFamily(resourceTreeNode: ResourceTreeNode, first : Boolean) : List[ResourceTreeNode] = {
    if (resourceTreeNode.parentId == "#".hashCode)
      List(resourceTreeNode)
    else{
      convertFamily(new ResourceTreeNode(resourceTreeNode.parentText,true),false) :+ resourceTreeNode
    }

  }
}
