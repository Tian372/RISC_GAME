package risc.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;


public class ChatClient {
  SocketChannel socketChannel;
  public ChatClient() throws IOException {
    InetSocketAddress chatServerAddr = new InetSocketAddress("localhost", 8081);
    socketChannel = SocketChannel.open(chatServerAddr);
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

  private CompletableFuture<Void> run() {
    return CompletableFuture.runAsync(() -> {
      ByteBuffer chatBuffer = ByteBuffer.allocate(1024);
      while (true) {
        int res = 0;
        try {
          res = socketChannel.read(chatBuffer);
          System.out.println("waiting message...");
          if (res == -1) {
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
          }
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    });
  }
}
