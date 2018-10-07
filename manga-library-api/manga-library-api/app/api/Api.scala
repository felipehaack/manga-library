package api

import javax.inject.Inject

import com.manga.library.model.Error
import com.wix.accord.Descriptions
import com.wix.accord.transform.ValidationTransform.TransformedValidator
import play.api.libs.json.{Json, Reads, Writes}
import play.api.mvc.InjectedController

import scala.concurrent.ExecutionContext

trait Api extends InjectedController {
  @Inject implicit var ec: ExecutionContext = _

  implicit class EnrichStatus(status: Status) {
    def toJson[B: Writes](obj: B) = {
      status.apply(Json.toJson(obj))
    }
  }

  def fromJson[A: Reads](validator: TransformedValidator[A]) = {
    parse.json.validate { jsValue =>
      jsValue.validate[A].asEither match {
        case Left(syntaxErrors) =>
          val errors = syntaxErrors.toList.flatMap {
            case (jsPath, violations) => violations.map { violation =>
              Error(jsPath.toString(), violation.message)
            }
          }
          Left(BadRequest.toJson(errors))
        case Right(obj) =>
          val errors = validator(obj).toFailure.map { failure =>
            failure.violations.map { violation =>
              Error(Descriptions.render(violation.path), violation.constraint)
            }
          }
          errors
            .map(_ => Left(BadRequest.toJson(errors)))
            .getOrElse(Right(obj))
      }
    }
  }
}
