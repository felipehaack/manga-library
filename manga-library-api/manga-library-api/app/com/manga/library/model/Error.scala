package com.manga.library.model

import play.api.libs.json.Json

case class Error(
                  code: String,
                  msg: String
                )

object Error {
  implicit val format = Json.format[Error]
}
