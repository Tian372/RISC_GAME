package risc.client.controller;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import risc.client.view.ViewFactory;
import risc.interfaces.game.Game;

import java.util.Objects;

import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.util.NodeQueryUtils.hasText;

class GameWindowControllerTest extends ApplicationTest {
  @Override
  public void start(Stage stage) throws Exception {
    ViewFactory viewFactory = new ViewFactory();
    viewFactory.getGameClientSocketV2().setGame(new Game(2));
    viewFactory.showGameWindow();
  }

  @Test
  public void testClickCircle () {
    verifyThat("#foodPoint", hasText("100"));
    verifyThat("#techPoint", hasText("100"));
    verifyThat("#Adwick", Objects::nonNull);
    clickOn("#Adwick");

    clickOn("#DoneButton");
  }

  @Test
  public void testClickDoneButton () {
    clickOn("#DoneButton");
  }




}