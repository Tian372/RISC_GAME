package risc.interfaces.factory;

import risc.interfaces.action.AttackAction;
import risc.interfaces.action.GameAction;
import risc.interfaces.action.MoveAction;
import risc.interfaces.action.UpgradeAction;
import risc.interfaces.board.GameBoard;
import risc.interfaces.player.Player;
import risc.interfaces.territory.Territory;
import risc.socket.Action;
import risc.socket.ActionType;

import java.io.Serializable;
import java.util.ArrayList;

public class ActionFactory implements Serializable {
  GameBoard gameBoard;
  ArrayList<Player> allPlayer;

  public ActionFactory(GameBoard gameBoard,ArrayList<Player> allPlayer){
    this.gameBoard=gameBoard;
    this.allPlayer=allPlayer;
  }

  //retrun an action object for the given message
  //if it is a "Done" message from the player, will return null
  //will throw relevant exception if the message is illegal
  public GameAction generateAction(Action act,ArrayList<AttackAction> allAttackAction) throws Exception{

    if(act.getActionType()==ActionType.MOVE){
      return generateMoveAction(act);
    }
    
    else if(act.getActionType()==ActionType.ATTACK){
      return generateAttackAction(act,allAttackAction);
    }
    else if(act.getActionType()==ActionType.UPGRADE){
      return generateUpgradeAction(act);
    }
    else{
       throw new WrongActionStringException("command type doesn't exist"); }
  }

  //TODO: find a better way to implement this method
  //because the upgrade will happen in the constructor, it doesn't matter what we really return, so just a return a null
  public GameAction generateUpgradeAction(Action act) throws Exception{
    Territory src=generateTerr(act.getSourceName());
    if(src.getOwner().getID()!=act.getPlayerID()){
      throw new WrongActionStringException("The source territory "+act.getSourceName()+ " doesn't belong to the Player");
    }

    //when we new a upgrade action, the upgrade will be executed
    for(int i=0;i<act.getUnitIDList().size();i++){
      String levelStr=act.getLevelList().get(i);
      int level=Integer.parseInt(levelStr.substring(2));
      new UpgradeAction(src,act.getUnitIDList().get(i),level);
    }
    return null;
  }


  //This function will do the parsing for a move action message
  //generate a action from "M src target num"
  public GameAction generateMoveAction(Action act)throws Exception{

    Territory src=generateTerr(act.getSourceName());
    Territory target=generateTerr(act.getTargetName());
    
    if(src.getOwner().getID()!=act.getPlayerID()&&!allPlayer.get(act.getPlayerID()).isAlliance(allPlayer.get(src.getOwner().getID()))){
       throw new WrongActionStringException("The source territory "+act.getSourceName()+ " doesn't belong to the Player");
    }

    if(target.getOwner().getID()!=act.getPlayerID()&&!allPlayer.get(act.getPlayerID()).isAlliance(allPlayer.get(target.getOwner().getID()))){
       throw new WrongActionStringException("The target territory "+act.getTargetName()+ " doesn't belong to the Player");
    }
    

    return new MoveAction(src,target,act.getUnitIDList());
  }

  public GameAction generateAttackAction(Action act,ArrayList<AttackAction> allAttackAction)throws Exception{

    Territory src=generateTerr(act.getSourceName());
    Territory target=generateTerr(act.getTargetName());
    
    if(src.getOwner().getID()!=act.getPlayerID()){
       throw new WrongActionStringException("The source territory "+act.getSourceName()+ " doesn't belong to the Player");
    }
    
    if(target.getOwner().getID()==act.getPlayerID()){
       throw new WrongActionStringException("The target territory "+act.getTargetName()+ " belongs to the Player who want to attack");
    }

    for(AttackAction oneAttack:allAttackAction){
      if(oneAttack.testAlliance(src,target)){
        oneAttack.combineAlliance(src,act.getUnitIDList());
        return null;
      }
    }


    return new AttackAction(src,target,act.getUnitIDList());

  }
  
  private Territory generateTerr(String terrName)throws WrongActionStringException {
    if(!gameBoard.hasTerr(terrName)){
      throw new WrongActionStringException(terrName+ " is not a territory name");
    }

    return gameBoard.getTerritory(terrName);
  }
  
  
}
