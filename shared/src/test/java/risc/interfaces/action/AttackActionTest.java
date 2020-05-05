package risc.interfaces.action;

import org.junit.jupiter.api.Test;
import risc.interfaces.board.GameBoard;
import risc.interfaces.exceptions.InsufficientUnitNumException;
import risc.interfaces.exceptions.InvalidUnitException;
import risc.interfaces.exceptions.ResourceException;
import risc.interfaces.exceptions.UnconnectedException;
import risc.interfaces.player.HumanPlayer;
import risc.interfaces.player.Player;
import risc.interfaces.territory.Territory;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class AttackActionTest {

    @Test
    void applyAction() throws ResourceException, InsufficientUnitNumException, UnconnectedException, InvalidUnitException {

        ArrayList<Player> playerList = new ArrayList<>();
        playerList.add(new HumanPlayer(0, "t"));
        playerList.add(new HumanPlayer(1, "t"));
        playerList.add(new HumanPlayer(2, "t"));

        //playerList.add(new HumanPlayer(2, "t"));
        //playerList.add(new HumanPlayer(2, "t"));


        GameBoard gameBoard = new GameBoard(playerList, 3);

        //MoveAction zero2one = new MoveAction(gameBoard.getTerritory("Adwick"), gameBoard.getTerritory("LLandaff"), gameBoard.getTerritory("Adwick").getCombatUnitID());

        try {
            AttackAction f20fail = new AttackAction(gameBoard.getTerritory("Asaph"), gameBoard.getTerritory("LLandaff"), gameBoard.getTerritory("Asaph").getCombatUnitID());
        }catch (Exception e){
            assertEquals("Two territories are not adjacent.",e.getMessage());
        }
        AttackAction f20 = new AttackAction(gameBoard.getTerritory("Asaph"), gameBoard.getTerritory("Adwick"), gameBoard.getTerritory("Asaph").getCombatUnitID());
        f20.applyAction();

        assertEquals(f20.compareTo(f20), 0);
        //assertEquals(gameBoard.getTerritory("Asaph").getOwner().getID(), gameBoard.getTerritory("Adwick").getOwner().getID());


    }
}