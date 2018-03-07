package highrung.db

import org.joda.time.DateTime
import highrung.model.Message

trait MessagesTable extends ColumnMapper {

  import driver.api._

  class Messages(tag: Tag) extends Table[Message](tag, "MESSAGE") {
    def id = column[Int]("ID", O.PrimaryKey, O.AutoInc)
    def parentId = column[Int]("PARENTID")
    def tournamentId = column[Int]("LADDERID")
    def subject = column[String]("SUBJECT")
    def text = column[String]("TEXT")
    def updateBy = column[Int]("UPDATEBY")
    def updateTs = column[DateTime]("UPDATETIME")
    def deleted = column[String]("DELETED")

    def * = (
      id.?,
      parentId,
      tournamentId,
      subject,
      text,
      updateBy,
      updateTs,
      deleted
    ) <> (Message.tupled, Message.unapply)
  }
}