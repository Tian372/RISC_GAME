package risc.socket;

import java.io.Serializable;
import java.util.ArrayList;

public class Action implements Serializable {
  public ActionType getActionType() {
    return actionType;
  }

  public void setActionType(ActionType actionType) {
    this.actionType = actionType;
  }

  public ArrayList<Integer> getUnitIDList() {
    return unitIDList;
  }

  public ArrayList<String> getLevelList() {
    return levelList;
  }

  public void setUnitIDList(ArrayList<Integer> unitIDList) {
    this.unitIDList = unitIDList;
  }

  public String getSourceName() {
    return sourceName;
  }

  public void setSourceName(String sourceName) {
    this.sourceName = sourceName;
  }

  public String getTargetName() {
    return targetName;
  }

  public void setTargetName(String targetName) {
    this.targetName = targetName;
  }

  public int getPlayerID() {
    return playerID;
  }

  public void setPlayerID(int playerID) {
    this.playerID = playerID;
  }

  public void setLevelList(ArrayList<String> levelList) {
    this.levelList = levelList;
  }

  public int getAllyID() {
    return allyID;
  }

  public void setAllyID(int allyID) {
    this.allyID = allyID;
  }

  int playerID;



  //the id of the player that the current player want to be allied with
  //must be set when the actionType=="ALLY"
  int allyID;

  ActionType actionType;
  ArrayList<Integer> unitIDList = new ArrayList<>();

  String sourceName;
  String targetName;

  //used for upgrade action
  ArrayList<String> levelList = new ArrayList<>();


}
