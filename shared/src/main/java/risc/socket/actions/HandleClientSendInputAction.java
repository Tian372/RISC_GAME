package risc.socket.actions;

import risc.interfaces.game.Game;
import risc.interfaces.socket.GameServerSocket;
import risc.interfaces.socket.response.Response;
import risc.socket.Action;
import risc.socket.ActionType;

import java.io.*;
import java.util.Queue;
import java.util.concurrent.RecursiveAction;

public class HandleClientSendInputAction extends RecursiveAction {

  public int playerID;
  private GameServerSocket.SocketStream socketStream;
  private final Game game;
  private Queue<Action> inputQueue;
  public HandleClientSendInputAction(int playerID, GameServerSocket.SocketStream socketStream, Game game, Queue<Action> inputQueue) {
      this.playerID = playerID;
      this.socketStream = socketStream;
      this.game = game;
      this.inputQueue = inputQueue;
  }

  @Override
  protected void compute() {

    try {
      socketStream.out.reset();
      socketStream.out.writeObject("start");
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(1);
    }

    Action action;
    do {
      try {
        action = (Action) socketStream.in.readObject();
        Response res = null;
        synchronized (game) {
           res = game.validate(action);
        }
        if (res.isSuccess()) {
          inputQueue.offer(action);
        } else {
        }
        socketStream.out.reset();
        socketStream.out.writeObject(res);

      } catch (IOException | ClassNotFoundException e) {
        e.printStackTrace();
        return;
      }
    } while (action.getActionType() != ActionType.DONE);

  }

}
