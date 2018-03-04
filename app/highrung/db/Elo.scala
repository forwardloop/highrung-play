package highrung.db

import highrung.model.Elo
import org.joda.time.DateTime

trait EloTable extends ColumnMapper {

  import driver.api._

  class Elos(tag: Tag) extends Table[Elo](tag, "ELO") {
    def id = column[Int]("ID", O.PrimaryKey, O.AutoInc)
    def playerId = column[Int]("PLAYERID")
    def matchId = column[Int]("MATCHID")
    def glickoRating = column[Double]("GLICKORATING")
    def glickoRatingDeviation = column[Double]("GLICKORATINGDEVIATION")
    def glickoVolatility = column[Double]("GLICKOVOLATILITY")
    def createdTs = column[DateTime]("CREATEDTS")

    def * = (
      id.?,
      playerId,
      matchId.?,
      glickoRating,
      glickoRatingDeviation,
      glickoVolatility,
      createdTs
    ) <> (Elo.tupled, Elo.unapply)
  }
}
