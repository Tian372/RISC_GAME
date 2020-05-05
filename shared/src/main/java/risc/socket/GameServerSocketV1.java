package risc.socket;

import risc.interfaces.game.Game;

import risc.interfaces.socket.GameServerSocket;


import risc.socket.actions.InitAction;
import risc.socket.actions.HandleClientSendInputAction;
import risc.socket.actions.SendGameToPlayerAction;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.*;

public class GameServerSocketV1 implements GameServerSocket {

  private ServerSocket server;
  private int connectedPlayerNum = 0;

  public List<Socket> getSocketList() {
    return socketList;
  }

  public void setSocketList(List<Socket> socketList) {
    this.socketList = socketList;
  }

  public List<Socket> socketList = new ArrayList<>();
  private List<SocketStream> socketStreams = new ArrayList<>();
  private Queue<Action> inputQueue = new ConcurrentLinkedQueue<>();
  private boolean[] notSendToPlayer;
  private final Game game;
  private ForkJoinPool forkJoinPool;


  public GameServerSocketV1(int port, Game game) throws IOException {
    int numPlayer = game.getAllPlayer().size();
    notSendToPlayer = new boolean[numPlayer];
    Arrays.fill(notSendToPlayer, false);
    server = new ServerSocket(port);
    this.game = game;
    forkJoinPool = new ForkJoinPool(numPlayer);
    System.out.println("[Server] Server started");
    System.out.println("[Server] Waiting for a client ...");
  }

  @Override
  public void sendGameToAllPlayers(Game game) {
    List<RecursiveAction> sendGameToAllPlayersActionList = new ArrayList<>();
    for (int i = 0; i < socketStreams.size(); i++) if (!notSendToPlayer[i]) {
      SendGameToPlayerAction sendGameToAllPlayersAction = new SendGameToPlayerAction(i, socketStreams.get(i), game);
      forkJoinPool.submit(sendGameToAllPlayersAction);
      sendGameToAllPlayersActionList.add(sendGameToAllPlayersAction);
      // update notSendToPlayer to true
      if (game.getPlayer(i).isFinished()) {
        notSendToPlayer[i] = true;
      }

    }
    sendGameToAllPlayersActionList.forEach(ForkJoinTask::join);
  }

  @Override
  public void handleAllClientsSendInput() throws InterruptedException {
    List<HandleClientSendInputAction> handleClientSendInputActionList = new ArrayList<>();
    inputQueue = new ConcurrentLinkedQueue<>();
    List<ForkJoinTask> futures = new ArrayList<>();
    for (int i = 0; i < socketStreams.size(); i++) if (!notSendToPlayer[i]) {
      System.out.println("[Test] Send to " + i);
      HandleClientSendInputAction handleClientSendInputAction = new HandleClientSendInputAction(i, socketStreams.get(i), game, inputQueue);

      futures.add(forkJoinPool.submit(handleClientSendInputAction));

      handleClientSendInputActionList.add(handleClientSendInputAction);
    }
    futures.forEach(ForkJoinTask::join);

  }


  @Override
  public void acceptConnection(int numPlayer) throws IOException {
    List<RecursiveAction> initActionList = new ArrayList<>();

    while (connectedPlayerNum < numPlayer) {
      Socket socket = server.accept();
      System.out.println("[Test] ip address " + socket.getInetAddress());
      System.out.println("[Server] new client comes in");
      socketList.add(socket);
      SocketStream socketStream = new SocketStream(socket);
      socketStreams.add(socketStream);
      final int playerID = connectedPlayerNum++;
      RecursiveAction initAction = new InitAction(playerID, socketStream);
      initActionList.add(initAction);
      forkJoinPool.submit(initAction);
    }
    initActionList.forEach(ForkJoinTask::join);

  }

  @Override
  public Queue<Action> getMessageQueue() {
    return inputQueue;
  }

  @Override
  public void close() throws IOException {
    server.close();
    socketList.forEach(socket -> {
      try {
        socket.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    });
  }
}
