package risc.client.controller.services;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import risc.interfaces.game.Game;
import risc.interfaces.socket.response.Response;
import risc.socket.Action;
import risc.socket.GameClientSocketV2;
import risc.socket.response.ActionInputResponse;

import java.io.IOException;

public class ValidateActionService extends Service<Response> {

  GameClientSocketV2 gameClientSocketV2;
  Action action;
  public ValidateActionService(GameClientSocketV2 gameClientSocketV2, Action action) {
    this.action = action;
    this.gameClientSocketV2 = gameClientSocketV2;
  }

  @Override
  protected Task<Response> createTask() {
    return new Task<Response>() {
      @Override
      protected Response call() {
        // MOCK:
//        System.out.println("Started validating input");
//        return new ActionInputResponse(false, "cannot connect to server!");
        // TODO: uncomment
        try {
          return gameClientSocketV2.validateInput(action);
        } catch (IOException | ClassNotFoundException e) {
          System.err.println("Cannot validate input!");
          e.printStackTrace();
          return null;
        }
      }
    };
  }
}
