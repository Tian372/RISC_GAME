package risc.socket.actions;

import risc.interfaces.exceptions.InvalidUnitException;
import risc.interfaces.game.Game;
import risc.interfaces.socket.GameServerSocket;

import java.io.*;
import java.util.concurrent.RecursiveAction;

public class SendGameToPlayerAction extends RecursiveAction {

  private Game game;
  public int playerID;
  private GameServerSocket.SocketStream socketStream;
  public SendGameToPlayerAction(int playerID, GameServerSocket.SocketStream socketStream, Game game) {
    this.playerID = playerID;
    this.socketStream = socketStream;
    this.game = game;
  }

  @Override
  protected void compute() {
    try {
      socketStream.out.reset();
      socketStream.out.writeObject(game);
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(1);
    }

  }
}
