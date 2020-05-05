package risc.client.view;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import risc.client.ChatClient;
import risc.client.controller.*;
import risc.client.controller.InfoWindowController;
import risc.client.model.TerritoryCircle;
import risc.client.model.TerritorySVG;
import risc.socket.Action;
import risc.socket.GameClientSocketV2;

import java.io.IOException;
import java.util.ArrayList;

public class ViewFactory {

  private GameClientSocketV2 gameClientSocketV2 = new GameClientSocketV2();

  private ArrayList<Stage> activeStages = new ArrayList<>();

  private ColorTheme colorTheme = ColorTheme.DEFAULT;
  private FontSize fontSize = FontSize.MEDIUM;

  private GameWindowController gameWindowController;

  private ChatClient chatClient;

  public ChatClient getChatClient() {
    return chatClient;
  }

  public void setChatClient(ChatClient chatClient) {
    this.chatClient = chatClient;
  }


  public ColorTheme getColorTheme() {
    return colorTheme;
  }

  public void setColorTheme(ColorTheme colorTheme) {
    this.colorTheme = colorTheme;
  }

  public FontSize getFontSize() {
    return fontSize;
  }

  public void setFontSize(FontSize fontSize) {
    this.fontSize = fontSize;
  }

  public GameWindowController getGameWindowController() {
    return gameWindowController;
  }

  public void showConnectWindow() {
    BaseController baseController = new ConnectWindowController(this, "ConnectWindow.fxml", gameClientSocketV2);
    initializeStage(baseController);
  }

  public void showOptionsWindow(Window ownerWindow) {
    BaseController controller = new OptionsWindowController(this, "OptionsWindow.fxml", gameClientSocketV2);
    initializeStageBlockingOwner(controller, ownerWindow);
  }

  public void showInfoWindow(Window ownerWindow, TerritorySVG territory){
    InfoWindowController controller=new InfoWindowController(this, "InfoWindow.fxml",gameClientSocketV2,territory);
    initializeStageBlockingOwner(controller, ownerWindow);
  }

  public void showGameWindow() {
    gameWindowController = new GameWindowController(this, "GameWindow.fxml", gameClientSocketV2);
    initializeStage(gameWindowController);
  }

  public void showActionWindow(Window ownerWindow, Action action) {
    BaseController controller = new ActionWindowController(this, "ActionWindow.fxml", gameClientSocketV2, action);
    initializeStageBlockingOwner(controller, ownerWindow);
  }

  public void showResultWindow(Window ownerWindow) {
    BaseController resultWindowController = new ResultWindowController(this, "ResultWindow.fxml", gameClientSocketV2, ownerWindow);
    initializeResultStage(resultWindowController, ownerWindow);
  }

  private void initializeResultStage(BaseController baseController, Window ownerWindow) {
    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(baseController.getFxmlName()));
    fxmlLoader.setController(baseController);
    Parent parent;
    try {
      parent = fxmlLoader.load();
    } catch (IOException e) {
      e.printStackTrace();
      return;
    }
    Scene scene = new Scene(parent);
    updateStyle(scene);
    Stage stage = new Stage();
    stage.setScene(scene);
    stage.show();
    activeStages.add(stage);
    stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
      public void handle(WindowEvent we) {
        ((Stage)ownerWindow).close();
        showConnectWindow();
      }
    });
  }

  private void initializeStage(BaseController baseController){
    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(baseController.getFxmlName()));
    fxmlLoader.setController(baseController);
    Parent parent;
    try {
      parent = fxmlLoader.load();
    } catch (IOException e) {
      e.printStackTrace();
      return;
    }
    Scene scene = new Scene(parent);
    updateStyle(scene);
    Stage stage = new Stage();
    stage.setScene(scene);
    stage.show();
    activeStages.add(stage);
  }

  private void initializeStageBlockingOwner(BaseController baseController, Window ownerWindow){
    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(baseController.getFxmlName()));
    fxmlLoader.setController(baseController);
    Parent parent;
    try {
      parent = fxmlLoader.load();
    } catch (IOException e) {
      e.printStackTrace();
      return;
    }

    Scene scene = new Scene(parent);
    updateStyle(scene);
    Stage stage = new Stage();
    stage.initModality(Modality.WINDOW_MODAL);
    stage.initOwner(ownerWindow);
    stage.setScene(scene);
    stage.show();
    activeStages.add(stage);

  }

  private void updateStyle(Scene scene){

    System.out.println("class: " + getClass().getResource(ColorTheme.getCssPath(colorTheme)));

    scene.getStylesheets().clear();
    scene.getStylesheets().add(getClass().getResource(ColorTheme.getCssPath(colorTheme)).toExternalForm());
    scene.getStylesheets().add(getClass().getResource(FontSize.getCssPath(fontSize)).toExternalForm());
  }

  public void updateAllStyles() {
    for (Stage stage: activeStages) {
      Scene scene = stage.getScene();
      updateStyle(scene);
    }
  }

  public  void closeStage(Stage stageToClose){
    stageToClose.close();
    activeStages.remove(stageToClose);
  }

  public GameClientSocketV2 getGameClientSocketV2() {
    return gameClientSocketV2;
  }

  public void setGameClientSocketV2(GameClientSocketV2 gameClientSocketV2) {
    this.gameClientSocketV2 = gameClientSocketV2;
  }
}
