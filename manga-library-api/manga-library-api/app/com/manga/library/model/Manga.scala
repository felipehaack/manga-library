package com.manga.library.model

import com.wix.accord.dsl._
import play.api.libs.json.Json

case class Manga(
                  name: String
                )

object Manga {
  implicit val format = Json.format[Manga]

  implicit val Validator = validator[Manga] { v =>
    v.name is notBlank
    v.name.length is between(1, 50)
  }
}
