package risc.interfaces.action;

import org.junit.jupiter.api.Test;
import risc.interfaces.board.GameBoard;
import risc.interfaces.exceptions.MaxLevelException;
import risc.interfaces.exceptions.ResourceException;
import risc.interfaces.exceptions.WrongLevelException;
import risc.interfaces.player.HumanPlayer;
import risc.interfaces.player.Player;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class UpgradeActionTest {

    @Test
    void applyAction() throws ResourceException, MaxLevelException, WrongLevelException {

        ArrayList<Player> playerList = new ArrayList<>();
        playerList.add(new HumanPlayer(0, "t"));
        playerList.add(new HumanPlayer(1, "t"));
        playerList.add(new HumanPlayer(2, "t"));

        //playerList.add(new HumanPlayer(2, "t"));
        //playerList.add(new HumanPlayer(2, "t"));


        GameBoard gameBoard = new GameBoard(playerList, 3);

        //MoveAction zero2one = new MoveAction(gameBoard.getTerritory("Adwick"), gameBoard.getTerritory("LLandaff"), gameBoard.getTerritory("Adwick").getCombatUnitID());
        //AttackAction f20 = new AttackAction(gameBoard.getTerritory("Asaph"), gameBoard.getTerritory("Adwick"), gameBoard.getTerritory("Asaph").getCombatUnitID());
        //f20.applyAction();

        int unitID = gameBoard.getTerritory("Adwick").getCombatUnitID().get(0);
        try{
            UpgradeAction upgradeFail = new UpgradeAction(gameBoard.getTerritory("Adwick"),unitID, 6);
        }catch(Exception e){
            assertEquals(e.getMessage(), "Do not have enough tech resource to upgrade.");
        }


        UpgradeAction upgrade = new UpgradeAction(gameBoard.getTerritory("Adwick"),unitID, 3);
        assertEquals(upgrade.getPriorityTag(), 0);
        assertEquals(upgrade.compareTo(upgrade), 0);
        assertEquals(gameBoard.getTerritory("Adwick").getOneUnit(unitID).getUnitLv(), "Level 3");
    }
}