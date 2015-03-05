package model

/**
 * Created by Andrea on 05/03/15.
 */
case class ResourceTreeNode(id : String, parent : Option[ResourceTreeNode], text : String)
object ResourceTreeNode{
  import play.api.libs.json._
  implicit val resourceTreeNodeWrites = new Writes[ResourceTreeNode] {
    def writes(rtn: ResourceTreeNode) = Json.obj(
      "id" -> rtn.id,
      "parent" -> (rtn.parent match{
        case Some(x : ResourceTreeNode) => x.id
        case _ => "#"
      }),
      "text" -> rtn.text
    )
  }
  def convert(r : Resource) : ResourceTreeNode = ResourceTreeNode(r.id, None, r.name match{
    case Some(x : String) => x
    case _ => r.id
  })
}
