package risc.interfaces.player;

import risc.interfaces.exceptions.InsufficientUnitNumException;
import risc.interfaces.exceptions.InvalidUnitException;
import risc.interfaces.exceptions.ResourceException;
import risc.interfaces.territory.Territory;
import risc.interfaces.unit.CombatUnit;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;


public class HumanPlayer implements Player, Serializable {

    Player alliance;
    HashSet<Territory> myTerritory;
    int id;
    String name;

    int food;
    int tech;

    //null if there is no ally currently
    private Player ally;

    private boolean flagFinish;
    private boolean flagWin;

    public HumanPlayer(){
        flagFinish=false;
    }

    public boolean isFinished() {
        return flagFinish;
    }

    public boolean isWin() {
        return flagWin;
    }

    public void finishPlaying(boolean flagWin) {
        flagFinish = true;
        this.flagWin = flagWin;
        //when lose, also need to remove the alliance
        if(hasAlly()){
            ally.removeAlliance();
            this.removeAlliance();
        }
    }

    public HumanPlayer(int id, String name) {
        this.ally=null;

        this.food = 100;
        this.tech = 100;

        this.id = id;
        this.name = name;
        this.flagFinish = false;
        myTerritory = new HashSet<Territory>();
    }


    public int getID() {
        return id;
    }


    public String getName() {
        return name;
    }


    public boolean ownsTerritory(Territory t) {
        return myTerritory.contains(t);
    }

    public HashSet<Territory> getAllTerritory() {
        return myTerritory;
    }


    public boolean addTerritory(Territory t) {
        return myTerritory.add(t);
    }

    public boolean removeTerritory(Territory t) {
        return myTerritory.remove(t);
    }

    @Override
    public int getFoodResource() {
        return this.food;
    }

    @Override
    public int getTechResource() {
        return this.tech;
    }

    @Override
    public void addFoodResource(int foodR) {
        this.food += (foodR);
    }

    @Override
    public void addTechResource(int techR) {
        this.tech += (techR);
    }

    @Override
    public void removeFoodResource(int foodR) throws ResourceException {
        if (this.food < foodR) {
            throw new ResourceException("Does not have enough food.");
        }
        this.food -= (foodR);

    }

    @Override
    public void removeTechResource(int techR) throws ResourceException {
        if (this.tech < techR) {
            throw new ResourceException("Does not have enough tech.");
        }
        this.tech -= (techR);
    }

    @Override
    public void generateNewResource() {

        for (Territory t : this.myTerritory) {
            this.food += t.generateFood();
            this.tech += t.generateTech();
        }
    }

    @Override
    public boolean hasAlly() {
        return ally != null;
    }


    @Override
    public boolean isAlliance(Player anotherPlayer) {
        return ally == anotherPlayer;
    }

    @Override
    public boolean addAlliance(Player anotherPlayer) {
        //the current player already has an ally, so cannot add another ally
        if(hasAlly()){
            return false;
        }

        ally=anotherPlayer;
        return true;
    }

    public Territory closestAllianceTerr(Territory currentTerr) {
        // Mark all the vertices as not visited(By default
        // set as false)
        HashSet<Territory> visited = new HashSet<>();

        // Create a queue for BFS
        LinkedList<Territory> queue = new LinkedList<Territory>();

        // Mark the current node as visited and enqueue it

        visited.add(currentTerr);
        queue.add(currentTerr);
        Territory s;
        while (queue.size() != 0) {
            s = queue.poll();

            for (Territory terr : s.getAdjacentTerr()) {
                if (terr.getOwner() == this.alliance) {
                    return terr;
                }
                if (!visited.contains(terr)) {
                    visited.add(terr);
                    queue.add(terr);
                }
            }

        }

        return null;
    }

    @Override
    public boolean removeAlliance() {
        if(ally == null){
            return false;
        }else{
            try {
                //move all units
                for (Territory terr : this.myTerritory) {
                    ArrayList<Integer> allianceUnitIDs = new ArrayList<>();
                    for (CombatUnit unit : terr.getCombatUnit()) {
                        if (unit.getOwner() == this.alliance) {
                            allianceUnitIDs.add(unit.getUnitID());
                        }
                    }
                    ArrayList<CombatUnit> moveUnit = terr.removeCombatUnit(allianceUnitIDs);

                    Territory terr2Move = this.closestAllianceTerr(terr);
                    terr2Move.addCombatUnit(moveUnit);

                }
            } catch (InvalidUnitException | InsufficientUnitNumException e) {
                e.printStackTrace();

            }
            ally=null;
            return true;
        }


    }


}
