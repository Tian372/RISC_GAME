package risc.client.controller;

import risc.client.view.ViewFactory;
import risc.socket.GameClientSocketV2;

public abstract class BaseController {

  protected ViewFactory viewFactory;
  private String fxmlName;
  protected GameClientSocketV2 gameClientSocketV2;

  public BaseController(ViewFactory viewFactory, String fxmlName, GameClientSocketV2 gameClientSocketV2) {
    this.viewFactory = viewFactory;
    this.fxmlName = fxmlName;
    this.gameClientSocketV2 = gameClientSocketV2;
  }

  public String getFxmlName() {
    return fxmlName;
  }

  public GameClientSocketV2 getGameClientSocketV2() {
    return gameClientSocketV2;
  }

  public void setGameClientSocketV2(GameClientSocketV2 gameClientSocketV2) {
    this.gameClientSocketV2 = gameClientSocketV2;
  }
}
