package risc.interfaces.socket.response;

import risc.interfaces.game.Game;

public interface Response {
  boolean isSuccess();
  String getErrorMessage();
}
