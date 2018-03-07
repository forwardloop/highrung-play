package highrung.service

import slick.jdbc.JdbcProfile
import com.google.inject.Inject
import org.joda.time.DateTime
import highrung.db.MessagesTable
import highrung.model.Message
import scala.concurrent.Future
import play.api.db.slick.{ HasDatabaseConfig, DatabaseConfigProvider }

class MessageService @Inject() (protected val dbConfigProvider: DatabaseConfigProvider)
    extends { val dbConfig = dbConfigProvider.get[JdbcProfile] } with MessagesTable with HasDatabaseConfig[JdbcProfile] {

  import profile.api._
  val Messages = TableQuery[Messages]

  def saveMessage(tournamentId: Int, participantId: Int, subject: String, content: String) = db.run {
    DBIO.seq(
      Messages += Message(
        id = None,
        parentId = tournamentId,
        tournamentId,
        subject,
        content,
        updatedBy = participantId)
    )
  }

  def deleteMessage(participantId: Int, messageId: Int) = db.run {
    val query = for {
      m <- Messages if m.id === messageId
    } yield (m.deleted, m.updateBy, m.updateTs)
    query.update(("Y", participantId, new DateTime()))
  }

  def getMessages(tournamentId: Int): Future[Seq[Message]] = db.run {
    Messages
      .filter(m => (m.tournamentId === tournamentId && m.deleted === "N"))
      .result
  }
}