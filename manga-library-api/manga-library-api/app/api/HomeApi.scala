package api

import javax.inject.Inject

@Inject
class HomeApi extends Api {

  def home = Action {
    Ok
  }
}
