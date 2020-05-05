package risc.client.controller;

import com.jfoenix.controls.JFXScrollPane;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import risc.client.model.Territory;
import risc.client.view.ColorTheme;
import risc.client.model.TerritorySVG;
import risc.client.view.ViewFactory;
import risc.interfaces.game.Game;
import risc.interfaces.unit.CombatUnit;
import risc.socket.Action;
import risc.socket.GameClientSocketV2;

import java.net.URL;
import java.util.ResourceBundle;

public class InfoWindowController extends BaseController implements Initializable {
    @FXML
    private JFXScrollPane infoPane;


    @FXML
    private Pane outerPane;

    private TerritorySVG territory;

    public InfoWindowController(ViewFactory viewFactory, String fxmlName, GameClientSocketV2 gameClientSocketV2, TerritorySVG territory) {
        super(viewFactory, fxmlName, gameClientSocketV2);
        this.territory=territory;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeInfoPane();
    }

    private void initializeInfoPane() {
        infoPane.getStylesheets().addAll("client/src/main/resources/risc/client/view/css/scroll.css");
        infoPane.setContent(initializeInfoBox());

    }


    @FXML
    void buttonClickAction(ActionEvent event){
        Stage stage = (Stage) outerPane.getScene().getWindow();
        viewFactory.closeStage(stage);
        Action curAction = viewFactory.getGameWindowController().createNewCurAction();
        curAction.setSourceName(territory.getName());

        //TODO: find a better way to do the following code
        viewFactory.showActionWindow(viewFactory.getGameWindowController().getGameBoardPane().getScene().getWindow(), curAction);
    }

    private VBox initializeInfoBox() {
        VBox vBox = new VBox();
        //TODO: add the real information view
        Game game = gameClientSocketV2.getGame();

        // MOCK
        for (CombatUnit cu: game.getBoard().getTerritoryByID(territory.getTerrID()).getCombatUnit()) {

            Label unitInfo= new Label("Unit "+cu.getUnitID()+" : " + cu.getUnitLv());
            vBox.getChildren().add(unitInfo);
        }
        vBox.setSpacing(15); // items spacing
        vBox.setPadding(new Insets(20)); // top and bottom padding

        return vBox;
        // MOCK
    }


}
