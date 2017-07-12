package controllers

import javax.inject._
import akka.stream.Materializer
import play.api.mvc._
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class HomeController @Inject()(cc: ControllerComponents)(implicit ec: ExecutionContext, mat: Materializer) extends AbstractController(cc) {
  val userAction = new UserAction(new BodyParsers.Default())

  class UserRequest[A](val user: String, request: Request[A]) extends WrappedRequest[A](request)
  class UserAction @Inject()(val parser: BodyParsers.Default)(implicit val ec: ExecutionContext) extends ActionBuilder[UserRequest, AnyContent] with ActionRefiner[Request, UserRequest] {
    def executionContext = ec
    def refine[A](input: Request[A]) = {
      println("refine ...")
      Future.successful(Left(Results.Ok("Left result.")))
    }
  }

  def index = userAction { implicit request: Request[AnyContent] =>
    Ok("ok")
  }
}
