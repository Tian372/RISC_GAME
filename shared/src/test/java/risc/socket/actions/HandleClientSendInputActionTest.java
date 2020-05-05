package risc.socket.actions;

import org.junit.jupiter.api.Test;
import risc.interfaces.game.Game;
import risc.interfaces.socket.GameServerSocket;
import risc.socket.Action;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayDeque;
import java.util.Queue;

import static org.junit.jupiter.api.Assertions.*;

class HandleClientSendInputActionTest {
  @Test
  void test() throws IOException {
    GameServerSocket.SocketStream socketStream = null;
    try {
    socketStream = new GameServerSocket.SocketStream(new Socket());
    } catch (Exception ignore) {

    }
    Game game = new Game(2);
    Queue<Action> q = new ArrayDeque<>();
    HandleClientSendInputAction handleClientSendInputAction = new HandleClientSendInputAction(0, socketStream, game, q);

  }
}