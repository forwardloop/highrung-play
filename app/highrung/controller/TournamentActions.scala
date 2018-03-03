package highrung.controller

import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.mvc._

import scala.concurrent.Future

trait TournamentActions[P, T] extends Controller {

  protected def AnonymousTournamentAction(
    tournamentId: Int,
    action: (P, T) => Request[AnyContent] => Future[Result]) =
    UnauthedTournamentAction(tournamentId, (user, tournament) => implicit request =>
      action(user, tournament)(request)
    )

  protected def StandardUserTournamentAction(
    tournamentId: Int,
    action: (P, T) => Request[AnyContent] => Future[Result]) =
    AuthedPlayerTournamentAction(tournamentId, (user, tournament) => implicit request =>
      action(user, tournament)(request)
    )

  protected def NonMemberTournamentAction(
    tournamentId: Int,
    action: (P, T) => Request[AnyContent] => Future[Result]) =
    ConditionalAuthedPlayerTournamentAction(
      tournamentId,
      (player, tournament) => !isMember(player, tournament),
      action
    )

  protected def MemberTournamentAction(
    tournamentId: Int,
    action: (P, T) => Request[AnyContent] => Future[Result]) =
    ConditionalAuthedPlayerTournamentAction(
      tournamentId,
      (player, tournament) => isMember(player, tournament),
      action
    )

  protected def DirectorTournamentAction(
    tournamentId: Int,
    action: (P, T) => Request[AnyContent] => Future[Result]) =
    ConditionalAuthedPlayerTournamentAction(
      tournamentId,
      (player, tournament) => isDirector(player, tournament),
      action
    )

  private def AuthedPlayerTournamentAction(
    tournamentId: Int,
    action: (P, T) => Request[AnyContent] => Future[Result]) =
    AuthedAction { user =>
      implicit request =>
        PlayerTournamentResult(tournamentId, user, action)
    }

  private def ConditionalAuthedPlayerTournamentAction(
    tournamentId: Int,
    predicate: (P, T) => Boolean,
    action: (P, T) => Request[AnyContent] => Future[Result]) =
    AuthedPlayerTournamentAction(tournamentId, (user, tournament) => implicit request =>
      if (predicate(user, tournament)) action(user, tournament)(request)
      else Future(Forbidden)
    )

  private def UnauthedTournamentAction(
    tournamentId: Int,
    action: (P, T) => Request[AnyContent] => Future[Result]) =
    UnauthedAction { user =>
      implicit request =>
        PlayerTournamentResult(tournamentId, user, action)
    }

  private def PlayerTournamentResult(
    tournamentId: Int,
    player: P,
    asyncAction: (P, T) => Request[AnyContent] => Future[Result])(implicit request: Request[AnyContent]): Future[Result] =
    findById(tournamentId).flatMap {
      case Some(tournament) => asyncAction(player, tournament)(request)
      case None => Future(NotFound)
    }

  def AuthedAction(action: P => Request[AnyContent] => Future[Result]): Action[AnyContent]

  def UnauthedAction(action: P => Request[AnyContent] => Future[Result]): Action[AnyContent]

  def findById(tournamentId: Int): Future[Option[T]]

  def isMember(player: P, tournament: T): Boolean

  def isDirector(player: P, tournament: T): Boolean
}
