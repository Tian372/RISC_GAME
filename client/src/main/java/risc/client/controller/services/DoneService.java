package risc.client.controller.services;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import risc.socket.Action;
import risc.socket.GameClientSocketV2;

import java.io.IOException;

public class DoneService extends Service<Void> {

  GameClientSocketV2 gameClientSocketV2;
  Action action;

  public DoneService(GameClientSocketV2 gameClientSocketV2, Action action) {
    this.gameClientSocketV2 = gameClientSocketV2;
    this.action = action;
  }

  @Override
  protected Task<Void> createTask() {
    return new Task<Void>() {
      @Override
      protected Void call() {
        try {
          gameClientSocketV2.sendInputObj(action);
        } catch (IOException e) {
          System.err.println("Cannot send done!");
          e.printStackTrace();
        }
        return null;
      }
    };
  }
}
