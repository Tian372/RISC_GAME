package risc.interfaces.game;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import risc.interfaces.player.Player;
import risc.interfaces.socket.response.Response;
import risc.socket.Action;
import risc.socket.ActionType;

import java.io.IOException;

public class TestGame {
  @Test
  public void test_Game() {


    Action act;
    Game game=new Game(3);

    game.startOneTurn();
    act=new Action();
    act.setActionType(ActionType.ALLY);
    act.setPlayerID(1);
    act.setAllyID(2);
    game.validate(act);
    game.applyAllOrders();
    Player p1=game.getPlayer(1);
    Player p2=game.getPlayer(2);
    System.out.println("Test1");
    System.out.println(p1.hasAlly());
    System.out.println(p2.hasAlly());

    game.startOneTurn();
    act=new Action();
    act.setActionType(ActionType.ALLY);
    act.setPlayerID(1);
    act.setAllyID(2);
    game.validate(act);
    act=new Action();
    act.setActionType(ActionType.ALLY);
    act.setPlayerID(2);
    act.setAllyID(1);
    game.validate(act);
    game.applyAllOrders();
    p1=game.getPlayer(1);
    p2=game.getPlayer(2);
    System.out.println("Test2");
    System.out.println(p1.hasAlly());
    System.out.println(p2.hasAlly());
    System.out.println(p1.isAlliance(p2));
    System.out.println(p2.isAlliance(p1));
    game.startOneTurn();

  }

  @Test
  public void test_Game2() {
    Action act;
    Game game=new Game(3);

    game.startOneTurn();
    act=new Action();
    act.setActionType(ActionType.ALLY);
    act.setPlayerID(1);
    act.setAllyID(2);
    game.validate(act);

    act=new Action();
    act.setActionType(ActionType.ALLY);
    act.setPlayerID(1);
    act.setAllyID(0);
    Response rs=game.validate(act);
    System.out.println("Response is:");
    System.out.println(rs.isSuccess());
    System.out.println(rs.getErrorMessage());



  }


  @Test
  public void test_Game3() {
    Action act;
    Game game=new Game(3);

    game.startOneTurn();
    act=new Action();
    act.setActionType(ActionType.ALLY);
    act.setPlayerID(1);
    act.setAllyID(2);
    game.validate(act);
    act=new Action();
    act.setActionType(ActionType.ALLY);
    act.setPlayerID(2);
    act.setAllyID(1);
    game.validate(act);
    game.applyAllOrders();
    Player p1=game.getPlayer(1);
    Player p2=game.getPlayer(2);
    System.out.println("Test2");
    System.out.println(p1.hasAlly());
    System.out.println(p2.hasAlly());
    System.out.println(p1.isAlliance(p2));
    System.out.println(p2.isAlliance(p1));
    game.startOneTurn();
    act=new Action();
    act.setPlayerID(1);
    act.setSourceName(game.getBoard().getTerritoryByID(4).getTerritoryName());
    act.setTargetName(game.getBoard().getTerritoryByID(0).getTerritoryName());
    act.setUnitIDList(game.getBoard().getTerritoryByID(4).getCombatUnitID());
    act.setActionType(ActionType.ATTACK);
    game.validate(act);

    act=new Action();
    act.setPlayerID(2);
    act.setSourceName(game.getBoard().getTerritoryByID(7).getTerritoryName());
    act.setTargetName(game.getBoard().getTerritoryByID(0).getTerritoryName());
    act.setUnitIDList(game.getBoard().getTerritoryByID(7).getCombatUnitID());
    act.setActionType(ActionType.ATTACK);

    game.validate(act);

    game.applyAllOrders();


    System.out.println("test combine:");
    System.out.println(game.getBoard().getTerritoryByID(0).getCombatUnitID());
    System.out.println(game.getBoard().getTerritoryByID(4).getCombatUnitID());
    System.out.println(game.getBoard().getTerritoryByID(7).getCombatUnitID());


  }

  @Test
  public void testJackson() throws IOException {
    Game game = new Game(1);
    ObjectMapper objectMapper = new ObjectMapper();
    String jsonInString = objectMapper.writeValueAsString(game);
    System.out.println(jsonInString);

  }

}
