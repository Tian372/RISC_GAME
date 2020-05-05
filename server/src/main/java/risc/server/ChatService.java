package risc.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class ChatService {

  Selector selector;
  ServerSocketChannel chatServerSocket;
  Map<String, String> ipToName;
  public ChatService(Map<String, String> ipToName) throws IOException {
    this.ipToName = ipToName;
    // Selector: multiplexor of SelectableChannel objects
    selector = Selector.open(); // selector is open here

    // ServerSocketChannel: selectable channel for stream-oriented listening sockets
    chatServerSocket = ServerSocketChannel.open();
    chatServerSocket.getLocalAddress();
    // Binds the channel's socket to a local address and configures the socket to listen for connections
    chatServerSocket.bind(new InetSocketAddress(8081));
//        chatServerSocket.bind(new InetSocketAddress("127.0.0.1", 8081));


    // Adjusts this channel's blocking mode.
    chatServerSocket.configureBlocking(false);

    SelectionKey selectKy = chatServerSocket.register(selector,  SelectionKey.OP_ACCEPT);

    System.out.println("Chat service is ready..");
    // Infinite loop..
    // Keep server running
  }

//  public static void main(String[] args) throws IOException {
//    ChatService chatService = new ChatService();
//    chatService.run().join();
//  }

  public CompletableFuture<Void> run() {
    return CompletableFuture.runAsync(() -> {
      while (true) {
        try {
          selector.select();

          Set<SelectionKey> socketKeys = selector.selectedKeys();
          Iterator<SelectionKey> keyIterator = socketKeys.iterator();

          while (keyIterator.hasNext()) {
            SelectionKey key = keyIterator.next();
            if (key.isAcceptable()) {
              SocketChannel chatClient = chatServerSocket.accept();

              chatClient.configureBlocking(false);

              chatClient.register(selector, SelectionKey.OP_READ);
              System.out.println("Connection Accepted: " + chatClient.getRemoteAddress() + "\n");
//              byte[] messageByteArr = new String("Hello from server").getBytes();
//              ByteBuffer buffer = ByteBuffer.wrap(messageByteArr);
//              chatClient.write(buffer);
//              buffer.clear();

            } else if (key.isReadable()) {
              handleRead(key);
            }
            keyIterator.remove();
          }
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    });

  }



  private void handleRead(SelectionKey key) throws IOException {
    SocketChannel chatClient = (SocketChannel) key.channel();
    ByteBuffer chatBuffer = ByteBuffer.allocate(256);
    int res = chatClient.read(chatBuffer);
    if (res == -1) {
      chatClient.close();
    } else {
      StringBuilder messageSB = new StringBuilder(new String(chatBuffer.array()).trim());
      chatBuffer.clear();
      while (chatClient.read((chatBuffer)) > 0) {
        messageSB.append(new String(chatBuffer.array()).trim());
        chatBuffer.clear();
      }
      String message = messageSB.toString();
      System.out.println("Message received: " + message);
      System.out.println(ipToName);
      message = "[" + ipToName.get(((SocketChannel)key.channel()).getRemoteAddress().toString().split(":")[0])+ "]: " + message;
      for (SelectionKey k : selector.keys()) if (k != key && (k.interestOps() & SelectionKey.OP_ACCEPT) == 0 ) {
        byte[] messageByteArr = new String(message).getBytes();
        ByteBuffer buffer = ByteBuffer.wrap(messageByteArr);
        System.out.println("broadcast to : " + ((SocketChannel)k.channel()).getRemoteAddress());
        ((SocketChannel) k.channel()).write(buffer);
        buffer.clear();
      }

    }
  }

}