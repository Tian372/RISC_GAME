package risc.interfaces.action;

import org.junit.jupiter.api.Test;
import risc.interfaces.board.GameBoard;
import risc.interfaces.exceptions.InsufficientUnitNumException;
import risc.interfaces.exceptions.InvalidUnitException;
import risc.interfaces.exceptions.ResourceException;
import risc.interfaces.exceptions.UnconnectedException;
import risc.interfaces.player.HumanPlayer;
import risc.interfaces.player.Player;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class MoveActionTest {

    @Test
    void applyAction() throws ResourceException, InsufficientUnitNumException, UnconnectedException, InvalidUnitException {
        ArrayList<Player> playerList = new ArrayList<>();
        playerList.add(new HumanPlayer(0, "t"));
        playerList.add(new HumanPlayer(1, "t"));
        playerList.add(new HumanPlayer(2, "t"));
        //playerList.add(new HumanPlayer(2, "t"));
        //playerList.add(new HumanPlayer(2, "t"));
        GameBoard gameBoard = new GameBoard(playerList, 3);

        MoveAction zero2one = new MoveAction(gameBoard.getTerritory("Adwick"), gameBoard.getTerritory("LLandaff"), gameBoard.getTerritory("Adwick").getCombatUnitID());
        zero2one.applyAction();

        assertEquals(zero2one.compareTo(zero2one), 0);
    }
}