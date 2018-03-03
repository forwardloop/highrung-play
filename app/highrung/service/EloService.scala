package highrung.service

import com.google.inject.Inject
import highrung.db.EloTable
import highrung.model.Elo
import org.joda.time.DateTime
import play.api.db.slick.{ HasDatabaseConfig, DatabaseConfigProvider }
import slick.jdbc.JdbcProfile
import forwardloop.glicko2s.Glicko2

import scala.concurrent.Future

class EloService @Inject() (protected val dbConfigProvider: DatabaseConfigProvider)
    extends { val dbConfig = dbConfigProvider.get[JdbcProfile] } with HasDatabaseConfig[JdbcProfile] with EloTable {

  import profile.api._
  val Elos = TableQuery[Elos]

  def createDefault(playerId: Int) = dbConfig.db.run {
    DBIO.seq(
      Elos += Elo(
        None,
        playerId,
        None,
        Glicko2.NewPlayerRatingG1,
        Glicko2.NewPlayerRatingDeviationG1,
        Glicko2.NewPlayerVolatilityG1,
        DateTime.now()
      )
    )
  }

  def save(elo: Elo) = dbConfig.db.run {
    DBIO.seq(
      Elos += elo
    )
  }

  def save(elos: Seq[Elo]) = dbConfig.db.run {
    DBIO.seq(
      Elos ++= elos
    )
  }

  def findAllByPlayer(playerId: Int): Future[Seq[Elo]] = dbConfig.db.run {
    Elos
      .filter(elo => elo.playerId === playerId)
      .result
  }

  def findByPlayer(playerId: Int): Future[Elo] = dbConfig.db.run {
    Elos
      .filter(elo => elo.playerId === playerId)
      .sortBy(_.createdTs.desc)
      .result
      .head
  }

  def findByPlayers(playersIds: Seq[Int]): Future[Seq[Elo]] = {
    val maxQuery =
      Elos
        .groupBy {
          _.playerId
        }
        .map {
          case (playerId, elo) =>
            playerId -> elo.map(_.id).max
        }
    dbConfig.db.run {
      val result =
        for {
          elo <- Elos if elo.playerId inSet playersIds
          m <- maxQuery if (elo.playerId === m._1 && elo.id === m._2)
        } yield elo
      result.result
    }
  }

  def deleteAll() = dbConfig.db.run {
    Elos.delete
  }

  def deleteForMatch(matchId: Int) = dbConfig.db.run {
    Elos
      .filter(elo => (elo.matchId === matchId))
      .delete
  }
}