## The action constructed from an ActionRefiner instance doesn't work in develop mode. [#7614](https://github.com/playframework/playframework/issues/7614)

### Play Version: 2.6.1
### API: Scala
### Operating System: Windows 10
### JDK: Oracle 1.8.0_111
java version "1.8.0_111"
Java(TM) SE Runtime Environment (build 1.8.0_111-b14)
Java HotSpot(TM) 64-Bit Server VM (build 25.111-b14, mixed mode)
Paste the output from `java -version` at the command line.

### Expected Behavior
`HomeController.index` should works normally in dev mode while constructed from an ActionRefiner instance.

### Actual Behavior
#### Dev Mode
After sereral successful requests to http://localhost:9000/,  the later requests will be blocked.

#### Prod Mode
It works normally.

#### Code
The `index` action doesn't work in dev mode:
```
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
```

### Reproducible Test Case
This demo project will reproduce this issue:
https://github.com/joymufeng/play-scala-starter-example

