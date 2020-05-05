package risc.client.controller;

import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import risc.client.view.ViewFactory;
import risc.interfaces.game.Game;
import risc.socket.Action;

class ActionWindowControllerTest extends ApplicationTest {
  @Override
  public void start(Stage stage) throws Exception {
    ViewFactory viewFactory = new ViewFactory();
    viewFactory.getGameClientSocketV2().setGame(new Game(2));
    viewFactory.showGameWindow();
    ((Stage)viewFactory.getGameWindowController().getErrorLabel().getScene().getWindow()).close();
    Action action = new Action();
    action.setSourceName("Asaph");
    action.setTargetName("Adwick");
    viewFactory.showActionWindow(viewFactory.getGameWindowController().getErrorLabel().getScene().getWindow(), action);
  }

  @Test
  public void testClickCircle () {
    clickOn("#upgradeTab");
    clickOn("#upgradeButton");
  }
  @Test
  public void testAttackButton () {
    lookup("#attackTab");
    clickOn("#attackTab");
    clickOn("#attackButton");
    lookup("#attackAction");
    lookup("#moveAction");
  }

  @Test
  public void testClick(){
    clickOn("#attackTab");
    clickOn("#attackButton");

  }

}