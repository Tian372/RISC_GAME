package risc.socket.actions;

import risc.interfaces.socket.GameServerSocket;

import java.io.*;
import java.util.concurrent.RecursiveAction;

public class InitAction extends RecursiveAction {

  public int playerID;
  private GameServerSocket.SocketStream socketStream;

  public InitAction(int playerID, GameServerSocket.SocketStream socketStream) {
    this.playerID = playerID;
    this.socketStream = socketStream;
  }


  @Override
  protected void compute() {

    try {
      socketStream.out.reset();
      socketStream.out.writeObject(String.valueOf(playerID));
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(1);
    }

    String line = "";
    try {
      while (!line.equals("ready")) {
        line = (String) socketStream.in.readObject();
      }
    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }
}
