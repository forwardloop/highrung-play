package highrung.db

import java.sql.Timestamp
import highrung.model.Elo
import org.joda.time.DateTime
import slick.jdbc.JdbcProfile

trait EloTable {

  protected val driver: JdbcProfile
  import driver.api._

  implicit val timestampMapper = MappedColumnType.base[DateTime, Timestamp](
    dt => new Timestamp(dt.getMillis),
    ts => new DateTime(ts.getTime)
  )

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
