package risc.client.controller;

import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import risc.client.view.ViewFactory;
import risc.interfaces.game.Game;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.util.NodeQueryUtils.hasText;

class OptionsWindowControllerTest extends ApplicationTest {
  @Override
  public void start(Stage stage) throws Exception {
    ViewFactory viewFactory = new ViewFactory();
    viewFactory.getGameClientSocketV2().setGame(new Game(2));
    viewFactory.showGameWindow();
    ((Stage)viewFactory.getGameWindowController().getErrorLabel().getScene().getWindow()).close();
    viewFactory.showOptionsWindow(viewFactory.getGameWindowController().getErrorLabel().getScene().getWindow());
  }

  @Test
  public void testClickCircle () {
    clickOn("#fontSizePicker");
  }

}