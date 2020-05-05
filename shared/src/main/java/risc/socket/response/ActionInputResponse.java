package risc.socket.response;

import risc.interfaces.game.Game;
import risc.interfaces.socket.response.Response;

import java.io.Serializable;

public class ActionInputResponse implements Response, Serializable {
  private boolean isSuccess;
  private String errorMessage;

  public ActionInputResponse(boolean isSuccess, String errorMessage) {
    this.isSuccess = isSuccess;
    this.errorMessage = errorMessage;
  }

  @Override
  public boolean isSuccess() {
    return isSuccess;
  }

  @Override
  public String getErrorMessage() {
    return errorMessage;
  }

  @Override
  public String toString() {
    return isSuccess + " " + errorMessage;
  }

}
