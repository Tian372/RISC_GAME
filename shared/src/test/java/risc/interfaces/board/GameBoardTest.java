package risc.interfaces.board;

import org.junit.jupiter.api.Test;
import risc.interfaces.action.AttackAction;
import risc.interfaces.action.MoveAction;
import risc.interfaces.exceptions.InsufficientUnitNumException;
import risc.interfaces.exceptions.InvalidUnitException;
import risc.interfaces.exceptions.ResourceException;
import risc.interfaces.exceptions.UnconnectedException;
import risc.interfaces.game.Game;
import risc.interfaces.player.HumanPlayer;
import risc.interfaces.player.Player;
import risc.interfaces.territory.Territory;


import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class GameBoardTest {

    @Test
    void isAdjacent() throws ResourceException, InsufficientUnitNumException, UnconnectedException, InvalidUnitException {

            ArrayList<Player> playerList = new ArrayList<>();
            playerList.add(new HumanPlayer(0, "t"));
            playerList.add(new HumanPlayer(1, "t"));
            playerList.add(new HumanPlayer(2, "t"));
            playerList.add(new HumanPlayer(2, "t"));
            playerList.add(new HumanPlayer(2, "t"));
            GameBoard gameBoard = new GameBoard(playerList, 3);

            //MoveAction zero2one = new MoveAction(gameBoard.getTerritory("Adwick"), gameBoard.getTerritory("LLandaff"), gameBoard.getTerritory("Adwick").getCombatUnitID());
            //AttackAction f20 = new AttackAction(gameBoard.getTerritory("Asaph"), gameBoard.getTerritory("Adwick"), gameBoard.getTerritory("Asaph").getCombatUnitID());
            //f20.applyAction();

            //assertFalse(zero2one.isConnect(gameBoard.getTerritory("Adwick"), gameBoard.getTerritory("LLandaff")));


            assertNull(gameBoard.getTerritoryByID(123456789));

            assertNotNull(gameBoard.getTerritoryByID(gameBoard.getTerritory("Adwick").getID()));

            assertTrue(gameBoard.hasTerr("Adwick"));
            assertFalse(gameBoard.hasEnoughUnit("a", "d",0));

            assertTrue(gameBoard.isAdjacent(gameBoard.getTerritory("Adwick"), gameBoard.getTerritory("LLandaff")));

           //System.out.println(Arrays.deepToString(gameBoard));

            System.out.println(playerList.get(3).getAllTerritory().size());

    }



}