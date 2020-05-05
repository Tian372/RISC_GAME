package risc.client.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import risc.client.view.ViewFactory;
import risc.socket.GameClientSocketV2;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

public class ResultWindowController extends BaseController implements Initializable {

  @FXML
  private Label Result;

  @FXML
  private ImageView resultImage;

  private Window gameWindow;

  public ResultWindowController(ViewFactory viewFactory, String fxmlName, GameClientSocketV2 gameClientSocketV2, Window gameWindow) {
    super(viewFactory, fxmlName, gameClientSocketV2);
    this.gameWindow = gameWindow;
  }

    public ResultWindowController(ViewFactory viewFactory, String fxmlName, GameClientSocketV2 gameClientSocketV2) {
    super(viewFactory, fxmlName, gameClientSocketV2);
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    if (gameClientSocketV2.getPlayer().isWin()) {
      Result.setText("You win!");
      resultImage.setImage(new Image("file:src/main/resources/risc/client/view/assets/win.jpeg"));
    } else {
      Result.setText("You lose!");
      resultImage.setImage(new Image("file:src/main/resources/risc/client/view/assets/lose.jpeg"));
    }

  }

}
