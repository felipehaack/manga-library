package api

import javax.inject.Inject

import com.manga.library.model.Error
import com.wix.accord.transform.ValidationTransform.TransformedValidator
import com.wix.accord.{Descriptions, Failure}
import play.api.libs.json.{Json, JsonValidationError, Reads, Writes}
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
            case (jsPath, violations) =>
              val parseViolationToErrorModel = (violations: Seq[JsonValidationError]) => {
                violations.map { violation =>
                  Error(jsPath.toString(), violation.message)
                }
              }
              parseViolationToErrorModel.apply(violations)
          }
          Left(BadRequest.toJson(errors))
        case Right(obj) =>
          val inCaseFailureParseToErrorModel = (failure: Failure) => {
            failure.violations.map { violation =>
              Error(Descriptions.render(violation.path), violation.constraint)
            }
          }
          val errorsOpt = validator(obj).toFailure.map(inCaseFailureParseToErrorModel.apply)
          errorsOpt
            .map(errors => Left(BadRequest.toJson(errors)))
            .getOrElse(Right(obj))
      }
    }
  }
}
