package risc.client;

import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import risc.client.view.ViewFactory;

import static javafx.scene.input.KeyCode.A;
import static javafx.scene.input.KeyCode.CONTROL;
import static org.junit.jupiter.api.Assertions.*;

class LauncherTest extends ApplicationTest {
  @Override
  public void start(Stage stage) throws Exception {
    ViewFactory viewFactory = new ViewFactory();
    viewFactory.showConnectWindow();
  }

  @Test
  public void testEnglishInput () {
    clickOn("#hostnameFied");
    push(CONTROL, A);
    write("localhost");
    clickOn("#portField");
    push(CONTROL, A);
    write("8080");
    clickOn("#startButton");
  }

}