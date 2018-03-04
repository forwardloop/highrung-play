package highrung.db

import org.joda.time.DateTime
import highrung.model.MatchComment

trait MatchCommentaryTable extends ColumnMapper {

  import driver.api._

  class MatchComments(tag: Tag) extends Table[MatchComment](tag, "MATCHCOMMENTARY") {
    def id = column[Int]("ID", O.PrimaryKey, O.AutoInc)
    def matchId = column[Int]("MATCHID")
    def playerId = column[Int]("PLAYERID")
    def text = column[String]("TEXT")
    def createdTs = column[DateTime]("CREATEDTIMESTAMP")

    def * = (
      id.?,
      matchId,
      playerId,
      text,
      createdTs
    ) <> (MatchComment.tupled, MatchComment.unapply)
  }
}