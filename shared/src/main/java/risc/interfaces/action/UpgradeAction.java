package risc.interfaces.action;

import risc.interfaces.exceptions.InvalidUnitException;
import risc.interfaces.exceptions.MaxLevelException;
import risc.interfaces.exceptions.ResourceException;
import risc.interfaces.exceptions.WrongLevelException;
import risc.interfaces.player.HumanPlayer;
import risc.interfaces.territory.Territory;
import risc.interfaces.territory.TerritoryV2;
import risc.interfaces.unit.CombatUnit;

import java.io.Serializable;
import java.util.HashSet;

public class UpgradeAction implements GameAction, Comparable<GameAction>, Serializable {
    int priorityTag = 0;

    public UpgradeAction(Territory targetT, int UnitID, int lv /* can only be 1,2,3,4,5,6 */) throws MaxLevelException, ResourceException, WrongLevelException {


        int playerFood = targetT.getOwner().getTechResource();

        CombatUnit unit = targetT.getOneUnit(UnitID);

        int upgradeCost = unit.getUpgradeCost(lv);


        if (playerFood < upgradeCost) {

            throw new ResourceException("Do not have enough tech resource to upgrade.");

        } else {

            targetT.getOwner().removeTechResource(upgradeCost);
            unit.upgrade(lv);

        }
    }

    /**
     * this method returns the priority of current action
     * 0 is the most prioritized, then 1 and so on
     *
     * @return an integer which represent the priority
     */
    @Override
    public int getPriorityTag() {
        return 0;
    }

    /**
     * this method is used to apply an action
     */
    @Override
    public void applyAction() {

    }

    @Override
    public int compareTo(GameAction gameAction) {
        return gameAction.getPriorityTag() - this.getPriorityTag();
    }


}
