package highrung.service

import highrung.db.MatchCommentaryTable
import highrung.model.MatchComment
import com.google.inject.Inject
import org.joda.time.DateTime
import play.api.db.slick.{ DatabaseConfigProvider, HasDatabaseConfig }
import slick.jdbc.JdbcProfile

import scala.concurrent.Future

class MatchService @Inject() (protected val dbConfigProvider: DatabaseConfigProvider)
    extends { val dbConfig = dbConfigProvider.get[JdbcProfile] } with HasDatabaseConfig[JdbcProfile] with MatchCommentaryTable {

  import profile.api._
  val MatchComments = TableQuery[MatchComments]

  def createComment(
    matchId: Int,
    playerId: Int,
    text: String) = dbConfig.db.run {
    DBIO.seq(
      MatchComments += MatchComment(
        None,
        matchId,
        playerId,
        text,
        DateTime.now()
      ))
  }

  def updateCommentsMatchId(
    commentIds: Seq[Int],
    newMatchId: Int) = dbConfig.db.run {
    MatchComments
      .filter(_.id.inSet(commentIds))
      .map(c => c.matchId)
      .update(newMatchId)
  }

  def findComments(matchId: Int): Future[Seq[MatchComment]] = dbConfig.db.run {
    MatchComments
      .filter(_.matchId === matchId)
      .result
  }

  def findComments(matchIds: Seq[Int]): Future[Seq[MatchComment]] = dbConfig.db.run {
    MatchComments
      .filter(_.matchId.inSet(matchIds))
      .result
  }
}