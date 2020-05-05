package risc.interfaces.action;

import risc.interfaces.exceptions.InsufficientUnitNumException;
import risc.interfaces.exceptions.InvalidUnitException;
import risc.interfaces.exceptions.ResourceException;
import risc.interfaces.exceptions.UnconnectedException;
import risc.interfaces.player.Player;
import risc.interfaces.territory.Territory;
import risc.interfaces.unit.CombatUnit;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class AttackAction implements GameAction, Comparable<GameAction>, Serializable {
    //zero has the highest priority
    private int priorityTag = 2; //attack has to happen after move
    private final Territory startT; //the territory from where the attack initiated
    private final Territory targetT; //the territory to where the attack is going
    private final ArrayList<CombatUnit> attackGrp;  //the type of units that is attacking
    private final Player attackOwner; //the player who initiated the attack

    private Territory startT2; //the alliance's start territory
    private ArrayList<CombatUnit> attackGrp2;  //the alliance's attack unit group
    private Player attackOwner2; //the alliance


    /**
     * This is the constructor of attack action
     * The action should be able to be applied once the object is instantiated
     *
     * @param startT  the territory from where the attack initiated
     * @param targetT the territory to where the attack is going
     * @param UnitID  all the units' ID to attack with
     * @throws InvalidUnitException         when there is no such unit with the given name
     * @throws InsufficientUnitNumException when there is not enough units
     * @throws UnconnectedException         when startT and targetT are not adjacent
     */
    public AttackAction(Territory startT, Territory targetT, ArrayList<Integer> UnitID) throws InvalidUnitException, InsufficientUnitNumException, UnconnectedException, ResourceException {
        this.startT = startT;
        this.targetT = targetT;

        this.attackOwner = startT.getOwner();


        if (!startT.isAdjacent(targetT)) {
            throw new UnconnectedException("Two territories are not adjacent.");
        }

        this.attackGrp = this.startT.removeCombatUnit(UnitID);
        this.attackOwner.removeFoodResource(this.attackGrp.size());

        if(startT.getOwner().isAlliance(targetT.getOwner())){
            startT.getOwner().removeAlliance();
            targetT.getOwner().removeAlliance();
        }

        this.startT2=null;
        this.attackGrp2=new ArrayList<>();
        this.attackOwner2=null;
    }

    public boolean testAlliance(Territory startT2,Territory targetT2){
        return attackOwner.isAlliance(startT2.getOwner()) && this.targetT == targetT2;
    }

    //when the alliance wants to attack the same territory, combined into one source
    public void combineAlliance(Territory startT2, ArrayList<Integer> UnitID2)throws InvalidUnitException, InsufficientUnitNumException, UnconnectedException, ResourceException{
        this.startT2 = startT2;

        this.attackOwner2 = this.startT2.getOwner();

        if (!this.startT2.isAdjacent(targetT)) {
            throw new UnconnectedException("Two territories are not adjacent.");
        }

        this.attackGrp2 = this.startT2.removeCombatUnit(UnitID2);
        this.attackOwner2.removeFoodResource(this.attackGrp2.size());


    }

    //20-side dice
    private int[] dice20() {
        return new int[]{new Random().nextInt(20) + 1, new Random().nextInt(20) + 1};
    }

    @Override
    public int compareTo(GameAction gameAction) {
        return gameAction.getPriorityTag() - this.getPriorityTag();
    }

    /**
     * this method returns the priority of current action
     * 0 is the most prioritized, then 1 and so on
     *
     * @return an integer which represent the priority
     */
    @Override
    public int getPriorityTag() {
        return this.priorityTag;
    }

    /**
     * this method is used to apply an action
     */
    @Override
    public void applyAction() {

        ArrayList<CombatUnit> defender = this.targetT.getCombatUnit();
        ArrayList<Integer> defeatDefenders = new ArrayList<>(0);

        Collections.sort(this.attackGrp);
        Collections.sort(this.attackGrp2);

        boolean turn = true;

        int flagGroup=1;
        Random rand = new Random();
        while (!(this.attackGrp.isEmpty()&&this.attackGrp2.isEmpty()) && !defender.isEmpty()) {

            if(attackGrp.size()==0){
                flagGroup=2;
            }
            else if(attackGrp2.size()==0){
                flagGroup=1;
            }
            else {
                flagGroup=rand.nextInt()%2+1;
            }

            ArrayList<CombatUnit> currentGrp;
            if(flagGroup==1){
                currentGrp=this.attackGrp;
            }
            else{
                currentGrp=this.attackGrp2;
            }


            if (turn) {

                int[] dice = this.dice20();
                int[] addDice = {currentGrp.get(0).getAttackBonus() + dice[0], defender.get(defender.size() - 1).getAttackBonus() + dice[1]};

                if (addDice[0] > addDice[1]) {
                    defeatDefenders.add(defender.get(defender.size() - 1).getUnitID());
                    defender.remove(defender.size() - 1);
                } else {
                    currentGrp.remove(0);
                }

            } else {

                int[] dice = this.dice20();
                int[] addDice = {currentGrp.get(currentGrp.size() - 1).getAttackBonus() + dice[0], defender.get(0).getAttackBonus() + dice[1]};

                if (addDice[0] < addDice[1]) {
                    currentGrp.remove(currentGrp.size() - 1);

                } else {
                    defeatDefenders.add(defender.get(0).getUnitID());
                    defender.remove(0);
                }
            }
            turn = !turn;
        }

        if(!defeatDefenders.isEmpty()){
            try {
                this.targetT.removeCombatUnit(defeatDefenders);
            } catch (InvalidUnitException | InsufficientUnitNumException ignore) {
            }
        }

        //which means the owner of the territory wins the fight, so there is no need to change the territory info
        if(!defender.isEmpty()){
            return;
        }

        //The original owner lose, give the territory to the player that has more units left
        if(attackGrp.size()>attackGrp2.size()) {
            this.targetT.setOwner(attackOwner);
            try {
                if(startT2!=null) {
                    this.startT2.addCombatUnit(this.attackGrp2);
                }
                this.targetT.addCombatUnit(this.attackGrp);
            } catch (InsufficientUnitNumException ignore) {

            }
        }
        else {
            this.targetT.setOwner(attackOwner2);
            try {
                this.startT.addCombatUnit(this.attackGrp);
                this.targetT.addCombatUnit(this.attackGrp2);
            } catch (InsufficientUnitNumException ignore) {

            }
        }


    }

}
