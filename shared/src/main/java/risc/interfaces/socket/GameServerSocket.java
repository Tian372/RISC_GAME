package risc.interfaces.socket;

import risc.interfaces.game.Game;
import risc.interfaces.socket.response.Response;
import risc.socket.Action;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.Queue;

public interface GameServerSocket {

  public static class SocketStream {
    final public ObjectInputStream in;
    final public ObjectOutputStream out;
    public SocketStream(Socket socket) throws IOException {
      in = new ObjectInputStream(socket.getInputStream());
      out = new ObjectOutputStream(socket.getOutputStream());
    }
  }

  public List<Socket> getSocketList();
  void acceptConnection(int numPlayer) throws IOException;
  Queue<Action> getMessageQueue();
  void handleAllClientsSendInput() throws InterruptedException;
  void sendGameToAllPlayers(Game game);
  void close() throws IOException;
}
