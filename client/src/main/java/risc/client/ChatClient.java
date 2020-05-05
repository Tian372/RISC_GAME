package risc.client;

import risc.client.view.ViewFactory;
import risc.interfaces.game.Game;

import javax.swing.text.View;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;


public class ChatClient {
  SocketChannel socketChannel;
  ViewFactory viewFactory;
  InetSocketAddress chatServerAddr;

  public String getHostname() {
    return hostname;
  }

  public void setHostname(String hostname) {
    this.hostname = hostname;
  }

  public int getPortNum() {
    return portNum;
  }

  public void setPortNum(int portNum) {
    this.portNum = portNum;
  }

  String hostname;
  int portNum;

  public ViewFactory getViewFactory() {
    return viewFactory;
  }

  public void setViewFactory(ViewFactory viewFactory) {
    this.viewFactory = viewFactory;
  }

  public ChatClient(ViewFactory viewFactory) throws IOException {
    this.viewFactory = viewFactory;
  }

  public ChatClient() throws IOException {
  }

  public static void main(String[] args) throws IOException, InterruptedException {
    ChatClient chatClient = new ChatClient();
    chatClient.run();
    log("Connecting to Server on port 1111...");
    System.out.println("I am " + chatClient.socketChannel.getLocalAddress());
    Scanner scanner = new Scanner(System.in);
    while (true) {
      String input = scanner.nextLine();
      chatClient.sendMsg(input);
    }
  }

  public void sendMsg(String input) throws IOException {
    byte[] message = input.getBytes();
    ByteBuffer buffer = ByteBuffer.wrap(message);
    this.socketChannel.write(buffer);
    log("sending: " + input);
    buffer.clear();
  }

  private static void log(String str) {
    System.out.println(str);
  }

  public CompletableFuture<Void> run() throws IOException {
    chatServerAddr = new InetSocketAddress(hostname, portNum);
    socketChannel = SocketChannel.open(chatServerAddr);
    return CompletableFuture.runAsync(() -> {
      ByteBuffer chatBuffer = ByteBuffer.allocate(1024);
      while (true) {
        int res = 0;
        try {
          res = socketChannel.read(chatBuffer);
          System.out.println("waiting message...");
          if (res == -1) {
            System.out.println("close");
            socketChannel.close();
            return;
          } else {
            StringBuilder messageSB = new StringBuilder(new String(chatBuffer.array()).trim());
            chatBuffer.clear();
            chatBuffer.put(new byte[1024]);
            chatBuffer.clear();
//            while (socketChannel.read((chatBuffer)) > 0) {
//              messageSB.append(new String(chatBuffer.array()).trim());
//              chatBuffer.clear();
//            }
            String message = messageSB.toString();
            System.out.println("Message received: " + message);
            Game game = viewFactory.getGameClientSocketV2().getGame();
            int playerID = viewFactory.getGameClientSocketV2().getPlayerID();
            viewFactory.getGameWindowController().getChatLog().appendText(message + "\n");

          }
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    });
  }
}
