package risc.interfaces.action;

import risc.interfaces.territory.Territory;

public interface GameAction {

    //boolean setStart(Territory startT);
    //boolean setTarget(Territory targetT);
    //boolean setNum(int num);

    /**
     * this method returns the priority of current action
     * 0 is the most prioritized, then 1 and so on
     * @return an integer which represent the priority
     */
    int getPriorityTag();

    /**
     * this method is used to apply an action
     *
     */
    void applyAction();
}
