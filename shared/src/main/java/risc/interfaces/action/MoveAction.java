package risc.interfaces.action;


import risc.interfaces.exceptions.InsufficientUnitNumException;
import risc.interfaces.exceptions.InvalidUnitException;
import risc.interfaces.exceptions.ResourceException;
import risc.interfaces.exceptions.UnconnectedException;
import risc.interfaces.player.Player;
import risc.interfaces.territory.Territory;
import risc.interfaces.unit.CombatUnit;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;


public class MoveAction implements GameAction, Comparable<GameAction>, Serializable {


    private int priorityTag = 1; // the priority of current action


    public MoveAction(Territory startT, Territory targetT, ArrayList<Integer> UnitID) throws InvalidUnitException, InsufficientUnitNumException, UnconnectedException, ResourceException {
        //the territory from where the move was initiated
        //the territory to where the move is going
        Player movePlayer = startT.getOwner();

        //String map_file = "../shared/src/main/resources/costMap";
        //FileReader mapReader = new FileReader(map_file);
        //create JsonReader object
        //JsonReader jsonReader = Json.createReader(mapReader);
        //JsonArray myCostMap = jsonReader.readArray();


        if (!isConnect(startT, targetT)) {
            throw new UnconnectedException("There is no valid path between" + startT.getTerritoryName() + " and " + targetT.getTerritoryName());
        }

        int costToMove = startT.getCost(targetT.getID());

        movePlayer.removeFoodResource(costToMove);
        ArrayList<CombatUnit> moveUnit = startT.removeCombatUnit(UnitID); //the object that carries a single unit with certain amount
        targetT.addCombatUnit(moveUnit);

    }


    /**
     * A helper function for isConnected
     *
     * @param currentT
     * @param targetT
     * @param visited
     * @return
     */
    private boolean isConnectHelper(Territory currentT, Territory targetT, HashSet<Territory> visited) {

        Player owner = currentT.getOwner();

        if (visited.contains(currentT)) {
            return false;
        } else {
            visited.add(currentT);
        }


        if (currentT == targetT) {
            return true;
        } else if (currentT.getAdjacentTerr().isEmpty()) {
            return false;
        } else {
            boolean currentState = false;

            for (Territory t : currentT.getAdjacentTerr()) {

                if (t.getOwner().getID() == owner.getID()|| t.getOwner().isAlliance(owner)) {
                    if (t == targetT) {
                        return true;
                    } else {
                        if (isConnectHelper(t, targetT, visited)) {
                            currentState = true;
                            break;
                        }
                    }
                }

            }
            return currentState;
        }

    }

    /**
     * This method is used for check if two territory are connected
     *
     * @param startT
     * @param targetT
     * @return
     */
    public boolean isConnect(Territory startT, Territory targetT) {
        HashSet<Territory> visited = new HashSet<>();
        return isConnectHelper(startT, targetT, visited);
    }


//
//    /**
//     * This method is used for check if two territory are connected
//     *
//     * @param startT
//     * @param targetT
//     * @return
//     */
//    public int isConnect(Territory startT, Territory targetT) {
//        HashSet<Territory> visited = new HashSet<>();
//        if (startT.getAdjacentTerr().isEmpty()) {
//            return ;
//        } else if (startT == targetT) {
//            return 0;
//        } else {
//            this.getLowestPath(startT, targetT, visited);
//        }
//    }

    @Override
    public int getPriorityTag() {
        return this.priorityTag;
    }

    @Override
    public void applyAction() { }

    @Override
    public int compareTo(GameAction gameAction) {
        return gameAction.getPriorityTag() - this.getPriorityTag();
    }

}