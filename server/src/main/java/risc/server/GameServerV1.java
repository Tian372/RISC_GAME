package risc.server;

import risc.interfaces.game.Game;
import risc.interfaces.server.GameServer;
import risc.interfaces.socket.GameServerSocket;
import risc.socket.GameServerSocketV1;

import java.io.IOException;
import java.net.Socket;
import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class GameServerV1 implements GameServer {

  GameServerSocket gameServerSocket;
  ChatService chatService;
  Game game;
  int numPlayer;

  public GameServerV1(int port, int numPlayer) throws IOException {
    this.numPlayer = numPlayer;
    this.game = new Game(numPlayer);
    this.gameServerSocket = new GameServerSocketV1(port, game);
  }

  public void run() throws IOException, InterruptedException {


    System.out.println("[Server step 1] start to accept connect...");
    gameServerSocket.acceptConnection(numPlayer);
    System.out.println("[Server step 1 Done] all player are connected and ready!");

    System.out.println("[Server step 2] send initial game to players");
    gameServerSocket.sendGameToAllPlayers(game);
    List<Socket> socketList = gameServerSocket.getSocketList();
    List<String> name = Arrays.asList("Green", "Red", "Blue", "Orange", "Purple");
    Map<String, String> ipToName = new HashMap<>();

    for (int i = 0; i < socketList.size(); i++) {
      ipToName.put(socketList.get(i).getRemoteSocketAddress().toString().split(":")[0], name.get(i));
    }

    this.chatService = new ChatService(ipToName);
    chatService.run();
    System.out.println("[Server step 3] while game is not end, keep going");
    while (!game.isFinished()) {
      System.out.println("[Server step 3.1] start one turn");
      game.startOneTurn();

      System.out.println("[Server step 3.2 Done] notify all the players to receive input");
      gameServerSocket.handleAllClientsSendInput();

      System.out.println("[Server step 3.3] apply all orders");
      game.applyAllOrders();
      // notify child to send game

      System.out.println("[Server step 3.4] send game to players");
      gameServerSocket.sendGameToAllPlayers(game);

    }
    System.out.println("[Server 4] game ends");
    gameServerSocket.close();
  }

}
