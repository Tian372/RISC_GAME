package risc.client.controller.services;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import risc.interfaces.game.Game;
import risc.socket.GameClientSocketV2;

import java.io.IOException;

public class ReceiveGameService extends Service<Game> {

  GameClientSocketV2 gameClientSocketV2;

  public ReceiveGameService(GameClientSocketV2 gameClientSocketV2) {
    this.gameClientSocketV2 = gameClientSocketV2;
  }

  @Override
  protected Task<Game> createTask() {
    return new Task<Game>() {
      @Override
      protected Game call() {
        try {
          return gameClientSocketV2.receiveGame();
        } catch (IOException | ClassNotFoundException e) {
          System.err.println("Cannot receive game!");
          e.printStackTrace();
          return null;
        }
      }
    };
  }

}
