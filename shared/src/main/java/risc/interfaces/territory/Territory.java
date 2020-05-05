package risc.interfaces.territory;


import risc.interfaces.exceptions.InsufficientUnitNumException;
import risc.interfaces.exceptions.InvalidUnitException;
import risc.interfaces.player.Player;
import risc.interfaces.unit.CombatUnit;

import java.util.ArrayList;
import java.util.HashSet;

/***
 * This is an interface for RISC territory which is the smallest
 * unit of a RISC map.
 */
public interface Territory {

    void addAdjacentTerr(Territory adjacentTerr);

    /**
     * Get the name of the owner of this
     * Any unclaimed territory will have an empty String as owner
     *
     * @return a String of name of the owner of this, return an empty String if this is unclaimed
     *
     */
    Player getOwner();

    /**
     * Set the owner of this.
     * Replace whoever owns the territory
     *
     */
    void setOwner(Player player); //later will replace by (Player player)

    /**
     * Check if testing Territory is connected to current territory
     * Returns a boolean, true if two territory are adjacent to each other;
     * Returns false otherwise.
     *
     * @param testingIsTerritory the territory which is check with this
     * @return if testing Territory is adjacent to this, return true; false otherwise.
     */
    boolean isAdjacent(Territory testingIsTerritory);

    /**
     * Get all every unit name in an array
     * Return an empty array if there is no unit on current territory
     *
     * @return ArrayList of String consist all unit names
     */
    ArrayList<Integer> getCombatUnitID();

    /**
     * Get the integer number of unitName remaining on current territory
     * The return will always be greater or equal to zero
     *
     * @param combatUnitID name of the unit
     * @return the number of soldiers on this
     */
    ArrayList<Integer> getCombatUnitAttack(ArrayList<Integer> combatUnitID) throws InvalidUnitException;

    /**
     * Add certain number of units to this territory.
     * If unitName is not currently on this, add unit and its number to this
     * InsufficientSoldierNum exception will be thrown if input integer is less than zero
     *
     * @exception InsufficientUnitNumException throw an exception when soldier2add is less than zero
     * @param combatUnit2Add input unit needs to be added to this
     *
     */
    void addCombatUnit(ArrayList<CombatUnit> combatUnit2Add) throws InsufficientUnitNumException;

    /**
     * Add certain number of units to this territory.
     * If unitName is not currently on this, add unit and its number to this
     * InsufficientSoldierNum exception will be thrown if input integer is less than zero
     *
     * @exception InsufficientUnitNumException throw an exception when soldier2add is less than zero
     * @param combatUnit2Add input unit needs to be added to this
     *
     */
    void addCombatUnit(CombatUnit combatUnit2Add) throws InsufficientUnitNumException, InvalidUnitException;


    /**
     * Add certain number of unit to this territory
     * Input integer must be greater or equal to zero.
     *
     * InsufficientUnitNum exception will be thrown if input has more units than this contains
     * InvalidUnitNameException will be thrown if input unitName is not in current territory
     *
     * @exception InsufficientUnitNumException throw an exception
     * @exception InvalidUnitException throw an exception if input unitName is not in current territory
     * @param combatUnit2Remove input unit that need to be removed from this
     */
    ArrayList<CombatUnit> removeCombatUnit(ArrayList<Integer> combatUnit2Remove) throws InvalidUnitException, InsufficientUnitNumException;

    /**
     * Add certain number of unit to this territory
     * Input integer must be greater or equal to zero.
     *
     * InsufficientUnitNum exception will be thrown if input has more units than this contains
     * InvalidUnitNameException will be thrown if input unitName is not in current territory
     *
     * @exception InsufficientUnitNumException throw an exception
     * @exception InvalidUnitException throw an exception if input unitName is not in current territory
     * @param combatUnit2Remove input unit that need to be removed from this
     */
    ArrayList<CombatUnit> removeCombatUnit(Integer combatUnit2Remove) throws InsufficientUnitNumException, InvalidUnitException;


    /**
     * Get the name of current territory
     *
     * @return a String of the name of current territory
     */
    String getTerritoryName();


    /**
     * this is method gets all the adjacent territories
     *
     * @return a list that contents all the adjacent territroies
     */
    ArrayList<Territory> getAdjacentTerr();

    //only used for attackAction
    /**
     * this method get the combat unit with given name
     *
     * @return the unit with the name
     */
    ArrayList<CombatUnit> getCombatUnit();

    /**
     * this method adds one to all the units in this territory
     *
     */
    void addOneUnit();

    int generateFood();

    int generateTech();

    int getPassFood();

    CombatUnit getOneUnit(int ID);

     int getID();

    int getCost(int t2);

     void setCostMap(int[] costMap);


}
