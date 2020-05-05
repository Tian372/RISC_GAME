package risc.client.controller;


import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import risc.client.controller.services.AnimationService;
import risc.client.controller.services.ConnectResult;
import risc.client.controller.services.ConnectService;
import risc.client.controller.services.ReceiveGameService;
import risc.client.view.ViewFactory;
import risc.interfaces.game.Game;
import risc.socket.GameClientSocketV2;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;


public class ConnectWindowController extends BaseController implements Initializable {

  public ConnectWindowController(ViewFactory viewFactory, String fxmlName, GameClientSocketV2 gameClientSocketV2) {
    super(viewFactory, fxmlName, gameClientSocketV2);
  }

  @FXML
  private Pane connectPane;

  @FXML
  private JFXTextField hostnameField;

  @FXML
  private JFXTextField portField;

  @FXML
  private Label errorLabel;

  @FXML
  private JFXButton optionButton;

  @FXML
  private JFXButton startButton;

  @FXML
  void optionButtonAction(ActionEvent event) {
    viewFactory.showOptionsWindow(errorLabel.getScene().getWindow());
  }

  private boolean isConnected = false;

  @FXML
  void startButtonAction() {

    if (isConnected) {
      return;
    }

    System.out.println("startButtonAction!!");
    if (validateFields()) {
      gameClientSocketV2.setHostName(hostnameField.getText());
      gameClientSocketV2.setPortNumber(Integer.parseInt(portField.getText()));
      ConnectService connectService = new ConnectService(gameClientSocketV2);
      connectService.start();
      connectService.setOnSucceeded(event -> {
        ConnectResult connectResult= connectService.getValue();
        switch (connectResult) {
          case SUCCESS:
            isConnected = true;
            System.out.println("connected!");
            errorLabel.setText("connected! wait for other player to connect..");
            ReceiveGameService receiveGameService = new ReceiveGameService(gameClientSocketV2);
            receiveGameService.start();
            receiveGameService.setOnSucceeded(eventRecvGame -> {
              Game game = receiveGameService.getValue();
              if (game == null) {
                errorLabel.setText("cannot receive game!");
                return;
              }
              try {
                viewFactory.getChatClient().setHostname(hostnameField.getText());
                viewFactory.getChatClient().setPortNum(Integer.parseInt(portField.getText()) + 1);
                viewFactory.getChatClient().run();
                String line = gameClientSocketV2.receiveStart();
                System.out.println(line);
              } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
              }
              Stage stage = (Stage) errorLabel.getScene().getWindow();
              viewFactory.showGameWindow();
              viewFactory.closeStage(stage);
              isConnected = false;
            });
            return;

          case FAIL_CONNECT:
            errorLabel.setText("cannot connect to server!");
            return;
          case FAIL_RECEIVE_PLAYERID:
          case FAIL_SEND_READY:
          case FAIL_RECEIVE_GAME:
            errorLabel.setText("server failed!");
            return;
          default:
            return;
        }
      });

    }
  }

  private boolean validateFields() {
    if(hostnameField.getText().isEmpty()) {
      errorLabel.setText("Please fill hostname");
      return false;
    }
    if(portField.getText().isEmpty()) {
      errorLabel.setText("Please fill port number");
      return false;
    }
    try {
      int portNum = Integer.valueOf(portField.getText());
      if (portNum < 1001) {
        errorLabel.setText("port number must be larger than 1000");
        return false;
      }
    } catch (NumberFormatException e) {
      errorLabel.setText("port number should be number");
      return false;
    }
    return true;
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    hostnameField.setText("localhost");
    portField.setText("8080");

    //AnimationService.moveAnimation(connectPane,150,150,250,150);
//    AnimationService.upgradeAnimation(connectPane,1,2,100,200);
//    AnimationService.upgradeAnimation(connectPane,3,4,250,200);
//    AnimationService.upgradeAnimation(connectPane,5,6,400,200);
//    AnimationService.boomAnimation(connectPane,200,160);
    ArrayList<Integer> attackLevel=new ArrayList<>();
    attackLevel.add(1);attackLevel.add(2);attackLevel.add(3);attackLevel.add(4);attackLevel.add(5);attackLevel.add(6);;
    AnimationService.attackAnimation(connectPane,attackLevel,100,50,240);
    //AnimationService.winAnimation(connectPane,100,100);
  }

}
