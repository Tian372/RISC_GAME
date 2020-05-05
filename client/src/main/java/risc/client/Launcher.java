package risc.client;

import javafx.application.Application;
import javafx.stage.Stage;
import risc.client.view.ViewFactory;

public class Launcher extends Application {

  public ChatClient chatClient;

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage stage) throws Exception {


    ViewFactory viewFactory = new ViewFactory();
    chatClient = new ChatClient(viewFactory);
    viewFactory.setChatClient(chatClient);
    viewFactory.showConnectWindow();
//    viewFactory.showGameWindow();
  }

}
