package risc.client.controller.services;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import risc.socket.GameClientSocketV2;

import java.io.IOException;

public class ConnectService extends Service<ConnectResult> {

  GameClientSocketV2 gameClientSocketV2;

  public ConnectService(GameClientSocketV2 gameClientSocketV2) {
    this.gameClientSocketV2 = gameClientSocketV2;
  }

  @Override
  protected Task<ConnectResult> createTask() {
    return new Task<ConnectResult>(){
      @Override
      protected ConnectResult call() {
        try {
          gameClientSocketV2.connect();
        } catch (IOException e) {
          System.err.println("Cannot connect to server!");
          e.printStackTrace();
          return ConnectResult.FAIL_CONNECT;
        }

        try {
          int playerID = gameClientSocketV2.requestPlayerID();
          gameClientSocketV2.setPlayerID(playerID);
        } catch (ClassNotFoundException | IOException e) {
          System.err.println("Cannot request playerID!");
          e.printStackTrace();
          return ConnectResult.FAIL_RECEIVE_PLAYERID;
        }

        try {
          gameClientSocketV2.sendReady();
        } catch (IOException e) {
          e.printStackTrace();
          System.err.println("Cannot send ready signal!");
          return ConnectResult.FAIL_SEND_READY;
        }

        return ConnectResult.SUCCESS;
      }
    };
  }



}
