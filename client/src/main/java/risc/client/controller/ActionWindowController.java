package risc.client.controller;

import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import risc.client.controller.services.ValidateActionService;
import risc.client.model.UnitCheckBox;
import risc.client.view.ViewFactory;
import risc.interfaces.game.Game;
import risc.interfaces.socket.response.Response;
import risc.interfaces.unit.CombatUnit;
import risc.socket.Action;
import risc.socket.ActionType;
import risc.socket.GameClientSocketV2;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class ActionWindowController extends BaseController implements Initializable {

    @FXML
    private ScrollPane movePane;

    @FXML
    private ScrollPane attackPane;

    @FXML
    private ScrollPane upgradePane;

    @FXML
    private Label moveSourceName;

    @FXML
    private Label attackSourceName;

    @FXML
    private Label upgradeUnitName;

    @FXML
    private JFXButton moveButton;

    @FXML
    private JFXButton attackButton;

    @FXML
    private Button upgradeButton;
    //the levels that will be displayed in level boxes
    private final List possibleLevels = Arrays.asList("lv1", "lv2", "lv3", "lv4", "lv5", "lv6");


    private List<UnitCheckBox> unitRadioButtonAttackList = new ArrayList<>();
    private List<UnitCheckBox> unitRadioButtonMoveList = new ArrayList<>();
    private List<UnitCheckBox> unitRadioButtonUpgradeList = new ArrayList<>();
    private List<ComboBox> comboBoxList = new ArrayList<>();


    private Action action;

    //TODO extract the common code in attackAction, moveAction and upgradeAction
    @FXML
    void attackAction(ActionEvent event) {
        Stage stage = (Stage) movePane.getScene().getWindow();
        viewFactory.closeStage(stage);
        if (unitRadioButtonAttackList.stream().anyMatch(CheckBox::isSelected)) {
            viewFactory.getGameWindowController().setState(GameWindowController.State.CHOOSE_TARGET);
            unitRadioButtonAttackList.forEach(unitRadioButton -> {
                if (unitRadioButton.isSelected()) {
                    action.getUnitIDList().add(unitRadioButton.getUnitID());
                }
            });
        }
        action.setActionType(ActionType.ATTACK);
    }

    @FXML
    void moveAction(ActionEvent event) {
        Stage stage = (Stage) movePane.getScene().getWindow();
        viewFactory.closeStage(stage);
        if (unitRadioButtonMoveList.stream().anyMatch(CheckBox::isSelected)) {
            viewFactory.getGameWindowController().setState(GameWindowController.State.CHOOSE_TARGET);
            unitRadioButtonMoveList.forEach(unitRadioButton -> {
                if (unitRadioButton.isSelected()) {
                    action.getUnitIDList().add(unitRadioButton.getUnitID());
                }
            });
        }
        action.setActionType(ActionType.MOVE);
    }

    @FXML
    void upgradeAction(ActionEvent event) {
        Stage stage = (Stage) movePane.getScene().getWindow();
        viewFactory.closeStage(stage);
        if (unitRadioButtonUpgradeList.stream().anyMatch(CheckBox::isSelected)) {
            viewFactory.getGameWindowController().setState(GameWindowController.State.CHOOSE_ACTION);

            for (int i = 0; i < unitRadioButtonUpgradeList.size(); i++) {
                UnitCheckBox unitRadioButton = unitRadioButtonUpgradeList.get(i);
                ComboBox<String> comboBox = comboBoxList.get(i);
                if (unitRadioButton.isSelected()) {
                    action.getUnitIDList().add(unitRadioButton.getUnitID());
                    String level = comboBox.getSelectionModel().getSelectedItem();
                    action.getLevelList().add(level);
                }
            }
        }
        boolean flag = true;
        action.setActionType(ActionType.UPGRADE);
        Game game = gameClientSocketV2.getGame();
        try {
            game.validate(action);
        } catch (Exception e) {
            flag = false;
        }


        final GameWindowController gameWindowController = viewFactory.getGameWindowController();
        ValidateActionService validateActionService = new ValidateActionService(gameClientSocketV2, action);
        validateActionService.start();
        validateActionService.setOnSucceeded(validateEvent -> {
            Response response = validateActionService.getValue();
            if (response == null) {
                gameWindowController.getErrorLabel().setText("Response is null!");
            } else {
                gameWindowController.getErrorLabel().setText(response.getErrorMessage());
            }
        });
        viewFactory.getGameWindowController().refresh();
        if (flag) {
            viewFactory.getGameWindowController().prevAction = action;
            viewFactory.getGameWindowController().showUpgradeAnimation(action);
        } else {
            viewFactory.getGameWindowController().flag = 1;
        }

    /*
    Action newAction = new Action();
    newAction.setPlayerID(gameClientSocketV2.getPlayerID());
    gameWindowController.setCurAction(newAction);

     */

    }

    public ActionWindowController(ViewFactory viewFactory, String fxmlName, GameClientSocketV2 gameClientSocketV2, Action action) {
        super(viewFactory, fxmlName, gameClientSocketV2);
        this.action = action;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeMovePane();
        initializeAttackPane();
        initializeUpgradePane();
    }


    //TODO extract the common code in initializeAttackVBox, initializeMoveVBox and initializeUpgradeVBox
    private VBox initializeAttackVBox() {
        VBox vBox = new VBox();

        // TODO: uncomment
        Game game = gameClientSocketV2.getGame();
        for (CombatUnit cu : game.getBoard().getTerritory(action.getSourceName()).getCombatUnit()) {
            UnitCheckBox unitRadioButton = new UnitCheckBox("Unit :" + cu.getUnitLv(), cu.getUnitID());
            unitRadioButtonAttackList.add(unitRadioButton);
            vBox.getChildren().add(unitRadioButton);
        }

        // MOCK
//    for (int i = 0; i < 20; i++) {
//      UnitRadioButton unitRadioButton = new UnitRadioButton("test " + i, i);
//      unitRadioButtonAttackList.add(unitRadioButton);
//      vBox.getChildren().add(unitRadioButton);
//    }
        // MOCK

        vBox.setSpacing(10); // items spacing
        vBox.setPadding(new Insets(20)); // top and bottom padding
        return vBox;
    }

    private VBox initializeMoveVBox() {
        VBox vBox = new VBox();

        // TODO: uncomment
        Game game = gameClientSocketV2.getGame();

        int id = 0;
        for (CombatUnit cu : game.getBoard().getTerritory(action.getSourceName()).getCombatUnit()) {
            UnitCheckBox unitRadioButton = new UnitCheckBox("Unit :" + cu.getUnitLv(), cu.getUnitID());
            unitRadioButton.setId("MoveUnit" + id);
            unitRadioButtonMoveList.add(unitRadioButton);
            vBox.getChildren().add(unitRadioButton);
        }

        vBox.setSpacing(10); // items spacing
        vBox.setPadding(new Insets(20)); // top and bottom padding
        return vBox;
    }


    private VBox initializeUpgradeVBox() {
        VBox vBox = new VBox();

        // TODO: uncomment
        Game game = gameClientSocketV2.getGame();

        // MOCK
        int id = 0;
        for (CombatUnit cu : game.getBoard().getTerritory(action.getSourceName()).getCombatUnit()) {
            HBox hBox = new HBox();
            UnitCheckBox unitRadioButton = new UnitCheckBox("Unit :" + cu.getUnitLv(), cu.getUnitID());
            ComboBox<String> unitComboBox = new ComboBox<>();
            unitRadioButton.setId("AttackUnit" + id);
            //get 3 from "level 3"
            int level = Integer.parseInt(cu.getUnitLv().substring(6));
            unitComboBox.setItems(FXCollections.observableArrayList(possibleLevels.subList(level, 6)));
            unitComboBox.getSelectionModel().selectFirst();

            hBox.getChildren().add((unitRadioButton));
            hBox.getChildren().add((unitComboBox));
            hBox.setSpacing(10);
            hBox.setAlignment(Pos.CENTER);

            unitRadioButtonUpgradeList.add(unitRadioButton);
            comboBoxList.add(unitComboBox);

            vBox.getChildren().add(hBox);

        }

        vBox.setSpacing(15); // items spacing
        vBox.setPadding(new Insets(20)); // top and bottom padding
        return vBox;
    }

    private void initializeMovePane() {
        moveSourceName.setText(action.getSourceName());
        movePane.setContent(initializeMoveVBox());
        movePane.setPannable(true);
    }

    private void initializeAttackPane() {
        attackSourceName.setText(action.getSourceName());
        attackPane.setContent(initializeAttackVBox());
        attackPane.setPannable(true);
    }

    private void initializeUpgradePane() {
        upgradePane.setContent(initializeUpgradeVBox());
        upgradePane.setPannable(true);
    }


}
