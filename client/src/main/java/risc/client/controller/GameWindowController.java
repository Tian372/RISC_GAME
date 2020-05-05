package risc.client.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import risc.client.controller.services.AnimationService;
import risc.client.controller.services.DoneService;
import risc.client.controller.services.ReceiveGameService;
import risc.client.controller.services.ValidateActionService;
import risc.client.model.Territory;
import risc.client.model.TerritorySVG;
import risc.client.view.ViewFactory;
import risc.interfaces.game.Game;
import risc.interfaces.player.Player;
import risc.interfaces.socket.response.Response;
import risc.socket.Action;
import risc.socket.ActionType;
import risc.socket.GameClientSocketV2;

import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;


public class GameWindowController extends BaseController implements Initializable {

    @FXML
    private Label playerName;

    @FXML
    private Label foodPoint;

    @FXML
    private Label techPoint;

    /*
    @FXML
    private Button doneButton;
    */
    @FXML
    private Pane gameBoardPane;

    @FXML
    private Pane upperPane;


    @FXML
    private Label errorLabel;

    @FXML
    private JFXButton chatBtn;

    @FXML
    private JFXDrawer chatDrawer;

    private State actionState = State.CHOOSE_ACTION;
    @FXML
    private AnchorPane gamePane;


  private Action curAction;
  public Action prevAction;

  public int flag;


  private Map<Integer, Color> colorMap = new HashMap<>(){{
    put(0, Color.GREENYELLOW);
    put(1, Color.ORANGERED);
    put(2, Color.SKYBLUE);
    put(3, Color.BLACK);
    put(4, Color.LIGHTGREY);
  }};

    public GameWindowController(ViewFactory viewFactory, String fxmlName, GameClientSocketV2 gameClientSocketV2) {
        super(viewFactory, fxmlName, gameClientSocketV2);
    }

  @Override
  public void initialize(URL location, ResourceBundle resources) {

//    BackgroundImage myBI= new BackgroundImage(new Image("file:src/main/resources/risc/client/view/assets/world.png"),
//        BackgroundRepeat.SPACE, BackgroundRepeat.SPACE, BackgroundPosition.CENTER,
//        new BackgroundSize(1.0, 1.0, true, true, true, true));
////then you set to your node
//    gamePane.setBackground(new Background(myBI));
    setUpPlayerInfoPane();
    initializeUpperPane();
    initializeGameBoard();
  }


    private void setUpPlayerInfoPane() {
        playerName.setText(gameClientSocketV2.getPlayer().getName());
    }


    public static enum State {
        //State when player hasn't chosen any action
        CHOOSE_ACTION,

        //State when player has chosen src but hasn't chosen target for Attack/Move Action
        CHOOSE_TARGET,

        //State when player has clicked "Done" and is waiting for the next input
        DONE
    }


    public void setState(State state) {
        this.actionState = state;
    }

  public void showUpgradeAnimation(Action act){
    Pane pane=viewFactory.getGameWindowController().getGameBoardPane();
    TerritorySVG srcSVG=viewFactory.getGameWindowController().nameToTerr.get(act.getSourceName());
    Game game = gameClientSocketV2.getGame();
    int id1=act.getUnitIDList().get(0);
    String level1Str=game.getBoard().getTerritory(act.getSourceName()).getOneUnit(id1).getUnitLv();
    int level2=Integer.parseInt(act.getLevelList().get(0).substring(2))+1;
    int level1=level2-1;
    System.out.println("level");
    System.out.println(level1);
    System.out.println(level2);
    AnimationService.upgradeAnimation(pane,level1,level2,
        (srcSVG.getBoundsInParent().getMinX()+srcSVG.getBoundsInParent().getMaxX())/2-70,
        (srcSVG.getBoundsInParent().getMinY()+srcSVG.getBoundsInParent().getMaxY())/2-70);

  }

    public JFXTextArea getChatLog() {
        return chatLog;
    }

    public void setChatLog(JFXTextArea chatLog) {
        this.chatLog = chatLog;
    }

    public JFXTextField getChatMsg() {
        return chatMsg;
    }

    public void setChatMsg(JFXTextField chatMsg) {
        this.chatMsg = chatMsg;
    }

    JFXTextArea chatLog;
    JFXTextField chatMsg;

    private void initializeUpperPane() {
        chatDrawer.open();

        foodPoint.setText("" + gameClientSocketV2.getPlayer().getFoodResource());
        techPoint.setText("" + gameClientSocketV2.getPlayer().getTechResource());
        Button doneButton = initializeButton();
        upperPane.getChildren().add(doneButton);
//        upperPane.toFront();
        VBox chatPane = new VBox(0);


        chatLog = new JFXTextArea();
        chatLog.setEditable(false);
        chatLog.prefHeight(350);
        chatLog.prefWidth(300);
        chatLog.setStyle("-fx-background-color: #bdc3c7;");


        chatMsg = new JFXTextField();
        chatMsg.setEditable(true);
        chatMsg.prefHeight(100);
        chatMsg.prefWidth(300);
        chatMsg.setStyle("-fx-background-color: white;");

        JFXButton sendBtn = new JFXButton("Send");
        sendBtn.prefHeight(100);
        sendBtn.prefWidth(300);
        sendBtn.setStyle("-fx-background-color: #3498db;");


        chatPane.getChildren().addAll(chatLog, chatMsg, sendBtn);

        chatDrawer.setSidePane(chatPane);

        chatBtn.setOnMouseClicked((e) -> {

            if (chatDrawer.isShown()) {
                chatDrawer.close();
            } else {
                chatDrawer.open();
            }

        });

        sendBtn.setOnMouseClicked((e) -> {
            String msg = chatMsg.getText();
            chatLog.appendText("[Me]: " + msg + "\n");
            try {
                viewFactory.getChatClient().sendMsg(msg);
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            //alliance message is like : "Alliance:3", where 3 is the playerid
            if(msg.length() > 9 && msg.substring(0,9).equals("Alliance:")) {
                //TODO: add alliance
                Action tmp=new Action();
                tmp.setActionType(ActionType.ALLY);
                tmp.setPlayerID(gameClientSocketV2.getPlayerID());
                String allyStr=msg.substring(9);
                boolean allyFlag=true;
                if(allyStr.equals("Green")){
                    tmp.setAllyID(0);
                }
                else if(allyStr.equals("Red")){
                    tmp.setAllyID(1);
                }
                else if(allyStr.equals("Blue")){
                    tmp.setAllyID(2);
                }
                else{
                    allyFlag=false;
                }

                if(allyFlag) {
                    ValidateActionService validateActionService = new ValidateActionService(gameClientSocketV2, tmp);
                    validateActionService.start();
                    validateActionService.setOnSucceeded(validateEvent -> {
                        Response response = validateActionService.getValue();
                        if (response == null) {
                            errorLabel.setText("Response is null!");
                        } else {
                            errorLabel.setText(response.getErrorMessage());
                        }
                    });
                }
            }

        });


    }

    public void refresh() {
        upperPane.getChildren().removeAll();
        initializeUpperPane();

//        gameBoardPane.getChildren().removeAll();
//        initializeGameBoard();
    }

    private void refreshColor() {
      Game game = gameClientSocketV2.getGame();
      for (Player player : game.getAllPlayer()) {
        Color color = colorMap.get(player.getID());
        for (risc.interfaces.territory.Territory territory : player.getAllTerritory()) {
          nameToTerr.get(territory.getTerritoryName()).setStroke(color);
        }
      }
    }




  double orgSceneX;
  double orgSceneY;
  double orgTranslateX;
  double orgTranslateY;

  class AnimatedZoomOperator {

    private Timeline timeline;

    public AnimatedZoomOperator() {
      this.timeline = new Timeline(60);
    }

    public void zoom(Node node, double factor, double x, double y) {
      // determine scale
      double oldScale = node.getScaleX();
      double scale = oldScale * factor;
      double f = (scale / oldScale) - 1;

      // determine offset that we will have to move the node
      Bounds bounds = node.localToScene(node.getBoundsInLocal());
      double dx = (x - (bounds.getWidth() / 2 + bounds.getMinX()));
      double dy = (y - (bounds.getHeight() / 2 + bounds.getMinY()));

      // timeline that scales and moves the node
      timeline.getKeyFrames().clear();
      System.out.println("factor: " + factor);
      System.out.println("oldScale:" + oldScale);
      System.out.println("scale: " + scale);

      timeline.getKeyFrames().addAll(
          new KeyFrame(Duration.millis(200), new KeyValue(node.translateXProperty(), node.getTranslateX() - f * dx)),
          new KeyFrame(Duration.millis(200), new KeyValue(node.translateYProperty(), node.getTranslateY() - f * dy)),
          new KeyFrame(Duration.millis(200), new KeyValue(node.scaleXProperty(), scale)),
          new KeyFrame(Duration.millis(200), new KeyValue(node.scaleYProperty(), scale))
      );
      timeline.play();
    }
  }

  AnimatedZoomOperator zoomOperator = new AnimatedZoomOperator();

  private TerritorySVG initializeSVG(String name, JSONObject terrInfo, Color fillColor, Color strokeColor) {
    Game game = gameClientSocketV2.getGame();
    TerritorySVG territory = TerritorySVG.SVGBuilder.newInstance()
        .name(name)
        .content((String) terrInfo.get("svg"))
        .fillColor(fillColor)
        .strokeColor(strokeColor)
        .terrID(game.getBoard().getTerritory(name).getID())
        .create();

    territory.setOnMouseClicked(event -> {
      if (actionState == State.CHOOSE_ACTION) {
        viewFactory.showInfoWindow(gameBoardPane.getScene().getWindow(), territory);
        errorLabel.setText("");
      } else if (actionState == State.CHOOSE_TARGET) {
        curAction.setTargetName(((Territory) event.getSource()).getName());

        boolean validateResult=validateAction();
        if(validateResult&&prevAction.getActionType()==ActionType.MOVE){
          TerritorySVG srcSVG=nameToTerr.get(prevAction.getSourceName());
          TerritorySVG tarSVG=nameToTerr.get(prevAction.getTargetName());

          AnimationService.moveAnimation(gameBoardPane,
                  (srcSVG.getBoundsInParent().getMinX()+srcSVG.getBoundsInParent().getMaxX())/2,
                  (srcSVG.getBoundsInParent().getMinY()+srcSVG.getBoundsInParent().getMaxY())/2,
                  (tarSVG.getBoundsInParent().getMinX()+tarSVG.getBoundsInParent().getMaxX())/2,
                  (tarSVG.getBoundsInParent().getMinY()+tarSVG.getBoundsInParent().getMaxY())/2);
        }
        else if (prevAction.getActionType()==ActionType.ATTACK){
          TerritorySVG srcSVG=nameToTerr.get(prevAction.getSourceName());
          TerritorySVG tarSVG=nameToTerr.get(prevAction.getTargetName());


          ArrayList<Integer> arr=new ArrayList<>();
          for (int id:prevAction.getUnitIDList()) {
            String levelStr=game.getBoard().getTerritory(prevAction.getSourceName()).getOneUnit(id).getUnitLv();
            int level=Integer.parseInt(levelStr.substring(6))+1;
            arr.add(level);
          }

          AnimationService.attackAnimation(gameBoardPane,arr,
                  (tarSVG.getBoundsInParent().getMinX()+tarSVG.getBoundsInParent().getMaxX())/2-140,
                  (tarSVG.getBoundsInParent().getMinY()+tarSVG.getBoundsInParent().getMaxY())/2-50,
                  (tarSVG.getBoundsInParent().getMinX()+tarSVG.getBoundsInParent().getMaxX())/2-60
                  );
        }
        }


      else if (actionState == State.DONE){
        //TODO: write what should happen when it is already DONE
        errorLabel.setText("Please wait for other players");
      }

    });
    return territory;
  }

  private void initializeGameBoard() {

    gameBoardPane.setOnScroll(event -> {
      System.out.println("X: " + event.getDeltaX());
      System.out.println("Y: " + event.getDeltaY());
      if (event.getDeltaY() == 0) {
        return;
      }
      double zoomFactor = 1.2;

      if (event.getDeltaY() < 0) {
        // zoom out
        zoomFactor = 1 / zoomFactor;
      }
      zoomOperator.zoom(gameBoardPane, zoomFactor, event.getSceneX(), event.getSceneY());

    });

    gameBoardPane.setOnMousePressed(event -> {
      orgSceneX = event.getSceneX();
      orgSceneY = event.getSceneY();
      orgTranslateX = ((Pane)(event.getSource())).getTranslateX();
      orgTranslateY = ((Pane)(event.getSource())).getTranslateY();
    });

    gameBoardPane.setOnMouseDragged(event -> {
      double offsetX = event.getSceneX() - orgSceneX;
      double offsetY = event.getSceneY() - orgSceneY;

      double newTranslateX = orgTranslateX + offsetX;
      double newTranslateY = orgTranslateY + offsetY;

      gameBoardPane.setTranslateX(newTranslateX);
      gameBoardPane.setTranslateY(newTranslateY);

    });

        JSONParser parser = new JSONParser();

//    ImageView background = new ImageView(new Image("file:src/main/resources/risc/client/view/assets/territory.jpg", 1920, 1080, false, true));
//    background.setLayoutX(-500);
//    background.setLayoutY(-500);
//
//    gameBoardPane.getChildren().add(background);
    Game game = gameClientSocketV2.getGame();
//    BackgroundImage myBI= new BackgroundImage(new Image(),
//        BackgroundRepeat.SPACE, BackgroundRepeat.SPACE, BackgroundPosition.CENTER,
//        new BackgroundSize(4.0, 4.0, true, true, true, true));
//    //then you set to your node
//    gameBoardPane.setBackground(new Background(myBI));

////
//    JSONParser parser = new JSONParser();
//
//    //TODO: change the "5" in "for(int i=2;i<=5;i++){" to the actual number of players
//    //draw lines for connection (MOCK)
//    try {
//      JSONArray jLocation = (JSONArray) parser.parse(new FileReader("src/main/java/risc/client/model/territoriesLocation.json"));
//      JSONObject jConnection = (JSONObject) parser.parse(new FileReader("src/main/java/risc/client/model/territoriesConnect.json"));
//
//
//      for(int i=2;i<=game.getAllPlayer().size();i++){
//        JSONArray jarr= (JSONArray) jConnection.get(Integer.toString(i));
//        for(int j=0;j<jarr.size();j++){
//
//
//          JSONArray lineInfo=(JSONArray)jarr.get(j);
//          long startID=(long)lineInfo.get(0);
//          long endID=(long)lineInfo.get(1);
//          JSONObject startInfo= (JSONObject) jLocation.get((int) startID);
//          JSONObject endInfo= (JSONObject) jLocation.get((int) endID);
//
//
//          Line line=new Line();
//          line.setStartX((Double) startInfo.get("centerX"));
//          line.setStartY((Double) startInfo.get("centerY"));
//          line.setEndX((Double) endInfo.get("centerX"));
//          line.setEndY((Double) endInfo.get("centerY"));
//          gameBoardPane.getChildren().add(line);
//
//        }
//      }
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//
//    // MOCK
////    for (int i = 0; i < 5; i++) {
////      for (int j = 0; j < 3; j++) {
////        try {
////          JSONArray jsonArray = (JSONArray) parser.parse(new FileReader("src/main/java/risc/client/model/territoriesLocation.json"));
////          JSONObject terrInfo = (JSONObject) jsonArray.get(i * 3 + j);
////          Territory terrNode = initializeTerritory("test" + (i*3+j), terrInfo, colorMap.get(i));
////          gameBoardPane.getChildren().add(terrNode);
////        } catch (IOException | ParseException e) {
////          e.printStackTrace();
////        }
////      }
////    }
//
//    //MOCK
//
//

    ArrayList<Player> playerList = game.getAllPlayer();
    for (Player player : playerList) {
      for (risc.interfaces.territory.Territory territory : player.getAllTerritory()) {
        try {
          JSONArray jsonArray = (JSONArray) parser.parse(new FileReader(
                  "src/main/java/risc/client/model/territoriesSVG.json"));

          JSONObject terrInfo = (JSONObject) jsonArray.get(territory.getID());
          TerritorySVG terrNode = initializeSVG(territory.getTerritoryName(), terrInfo, Color.SLATEBLUE, colorMap.get(player.getID()));
          nameToTerr.put(territory.getTerritoryName(), terrNode);
          gameBoardPane.getChildren().add(terrNode);
        } catch (IOException | ParseException e) {
          e.printStackTrace();
        }
      }
    }
  }

  Map<String, TerritorySVG> nameToTerr = new HashMap<>();

    private Button initializeButton() {
        int r = 35;
        JFXButton doneButton = new JFXButton("Done!");

        //<Button fx:id="doneButton" layoutX="951.0" layoutY="21.0" mnemonicParsing="false" onAction="#doneButtonAction" prefHeight="43.0" prefWidth="83.0" text="Done!" />
        doneButton.setLayoutX(951.0);
        doneButton.setLayoutY(10.0);
        doneButton.setShape(new Circle(r));
        doneButton.setMinSize(2 * r, 2 * r);
        doneButton.setMaxSize(2 * r, 2 * r);
        doneButton.setId("DoneButton");

        String color1 = "#E6EE19";
        String color2 = "#DAE115";
        String color3 = "#D1D80B";

        //#E6EE19 is RGB value for a yellow
        doneButton.setStyle("-fx-background-color:" + color1);

        //set the button to a darker color when the mouse is on this button
        doneButton.setOnMouseEntered(event -> {
            doneButton.setStyle("-fx-background-color: " + color2);
        });

        //set the button to a even darker color when it is pressed, and set back when the mouse release
        doneButton.setOnMousePressed(event -> {
            doneButton.setStyle("-fx-background-color: " + color3);
        });
        doneButton.setOnMouseReleased(event -> {
            doneButton.setStyle("-fx-background-color: " + color2);

            //after Done button is clicked, the current turn is Done

            if (actionState == State.DONE) {
                return;
            }
            actionState = State.DONE;
            //TODO send the Done message to the server
            curAction = new Action();
            curAction.setPlayerID(gameClientSocketV2.getPlayerID());
            curAction.setActionType(ActionType.DONE);
            DoneService doneService = new DoneService(gameClientSocketV2, curAction);
            doneService.start();
            doneService.setOnSucceeded(sendInputSucceedEvent -> {
                errorLabel.setText("Your input is sent, wait for other users...");
            });
            try {
                // receive start for a new turn
                System.out.println(gameClientSocketV2.receiveInputValidationResponse());
            } catch (Exception e) {
                e.printStackTrace();
            }
            ReceiveGameService receiveGameService = new ReceiveGameService(gameClientSocketV2);
            receiveGameService.start();
            receiveGameService.setOnSucceeded(receivedGameEvent -> {
                Game game = receiveGameService.getValue();
                if (game == null) {
                    errorLabel.setText("Cannot receive game!");
                } else {
                    this.gameClientSocketV2.setGame(game);
                    setState(State.CHOOSE_ACTION);
                    this.refresh();
                    this.refreshColor();
                    errorLabel.setText("received game!");

          if (gameClientSocketV2.getPlayer().isFinished()) {
            Pane pane = new Pane();
            if(gameClientSocketV2.getPlayer().isWin()){
            AnimationService.winAnimation(pane,0,0);
            }
            else{
                AnimationService.loseAnimation(pane,0,0);
            }

            Popup popup = new Popup();
            popup.getContent().add(pane);
            popup.show((Stage)gameBoardPane.getScene().getWindow());
          } else {
            try {
              String line = gameClientSocketV2.receiveStart();
              System.out.println(line);
            } catch (IOException | ClassNotFoundException e) {
              e.printStackTrace();
            }
          }

                }
            });
        });

        //set the button to the original color when the mouse move away
        doneButton.setOnMouseExited(event -> {
            doneButton.setStyle("-fx-background-color: " + color1);
        });

        return doneButton;
    }

  public Action createNewCurAction() {
    curAction=new Action();
    curAction.setPlayerID(gameClientSocketV2.getPlayerID());
    return curAction;
  }

  private boolean validateAction(){
    Game game = gameClientSocketV2.getGame();
    boolean flag=true;
    try {
      game.validate(curAction);
    }
    catch(Exception e){
      flag=false;
    }
    ValidateActionService validateActionService = new ValidateActionService(gameClientSocketV2, curAction);
    validateActionService.start();
    validateActionService.setOnSucceeded(validateEvent -> {
      Response response = validateActionService.getValue();
      if (response == null) {
        errorLabel.setText("Response is null!");
      } else {
        errorLabel.setText(response.getErrorMessage());
      }
    });
    refresh();
    actionState = State.CHOOSE_ACTION;
    prevAction=curAction;
    curAction = new Action();
    curAction.setPlayerID(gameClientSocketV2.getPlayerID());
//        viewFactory.showOptionsWindow(gameBoardPane.getScene().getWindow());
    return flag;
  }

  public Pane getGameBoardPane() {
    return gameBoardPane;
  }

  public State getActionState(){
    return actionState;
  }

  public Action getCurAction() {
    return curAction;
  }

  public void setCurAction(Action curAction) {
    this.curAction = curAction;
  }

  public Label getErrorLabel() {
    return errorLabel;
  }

  public void setErrorLabel(Label errorLabel) {
    this.errorLabel = errorLabel;
  }


}

