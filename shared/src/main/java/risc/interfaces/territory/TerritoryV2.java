package risc.interfaces.territory;

import risc.interfaces.exceptions.InsufficientUnitNumException;
import risc.interfaces.exceptions.InvalidUnitException;
import risc.interfaces.player.Player;
import risc.interfaces.unit.CombatUnit;
import risc.interfaces.unit.SingleUnit;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

public class TerritoryV2 implements Territory, Serializable {


    int foodGen;
    int techGen;

    int foodPass;

    String terrName;
    Player owner;

    int territoryID;

    ArrayList<Territory> adjacentTerrs;

    HashMap<Integer, CombatUnit> combatUnit;

    int[] costMap;

    @Override
    public int getID() {
        return territoryID;
    }

    @Override
    public int getCost(int t2) {
        return this.costMap[t2];
    }

    public TerritoryV2(Player player, int territoryID, String terrName, int foodGen, int techGen, int foodPass, int numOfSoldier) {

        this.foodGen = (foodGen);
        this.techGen = (techGen);

        this.foodPass = (foodPass);

        this.terrName = terrName;

        this.owner = player;
        player.addTerritory(this);

        this.territoryID = territoryID;

        this.costMap = null;

        this.adjacentTerrs = new ArrayList<>(0);
        this.combatUnit = new HashMap<>(0);

        for (int i = 0; i < numOfSoldier; i++) {

            CombatUnit currentUnit = new SingleUnit(player);
            this.combatUnit.put(currentUnit.getUnitID(), currentUnit);

        }

    }


    @Override
    public void addAdjacentTerr(Territory someTerr) {

        if (!this.adjacentTerrs.contains(someTerr)) {
            this.adjacentTerrs.add(someTerr);
            someTerr.addAdjacentTerr(this);
        }

    }

    /**
     * Get the name of the owner of this
     * Any unclaimed territory will have an empty String as owner
     *
     * @return a String of name of the owner of this, return an empty String if this is unclaimed
     */
    @Override
    public Player getOwner() {
        return this.owner;
    }

    /**
     * Set the owner of this.
     * Replace whoever owns the territory
     *
     * @param player
     */
    @Override
    public void setOwner(Player player) {

        if (this.owner == null) {
            player.addTerritory(this);
            this.owner = player;
        } else if (this.owner.getID() != player.getID()) {
            this.owner.removeTerritory(this);
            player.addTerritory(this);
            this.owner = player;
        }

    }

    /**
     * Check if testing Territory is connected to current territory
     * Returns a boolean, true if two territory are adjacent to each other;
     * Returns false otherwise.
     *
     * @param someTerritory the territory which is check with this
     * @return if testing Territory is adjacent to this, return true; false otherwise.
     */
    @Override
    public boolean isAdjacent(Territory someTerritory) {
        return this.adjacentTerrs.contains(someTerritory);
    }

    /**
     * Get all every unit name in an array
     * Return an empty array if there is no unit on current territory
     *
     * @return ArrayList of String consist all unit names
     */
    @Override
    public ArrayList<Integer> getCombatUnitID() {
        ArrayList<Integer> out = new ArrayList<>(0);
        out.addAll(this.combatUnit.keySet());
        return out;
    }

    /**
     * Get the integer number of unitName remaining on current territory
     * The return will always be greater or equal to zero
     *
     * @param combatUnitID name of the unit
     * @return the number of soldiers on this
     */
    @Override
    public ArrayList<Integer> getCombatUnitAttack(ArrayList<Integer> combatUnitID) throws InvalidUnitException {

        ArrayList<Integer> attackDamage = new ArrayList<>(0);

        for (Integer integer : combatUnitID) {
            if (this.combatUnit.containsKey(integer)) {
                attackDamage.add(this.combatUnit.get(integer).getAttackBonus());
            } else {
                throw new InvalidUnitException("No Unit ID: " + integer);
            }
        }

        return attackDamage;
    }

    /**
     * Add certain number of units to this territory.
     * If unitName is not currently on this, add unit and its number to this
     * InsufficientSoldierNum exception will be thrown if input integer is less than zero
     *
     * @param combatUnit2Add input unit needs to be added to this
     * @throws InsufficientUnitNumException throw an exception when soldier2add is less than zero
     */
    @Override
    public void addCombatUnit(ArrayList<CombatUnit> combatUnit2Add) throws InsufficientUnitNumException {

        //check if there is anything to add;
        if (combatUnit2Add.isEmpty()) {
            throw new InsufficientUnitNumException("Amount has to be greater than zero.");
        }
        //check if this has the unit;

        for (CombatUnit unit : combatUnit2Add) {
            this.combatUnit.put(unit.getUnitID(), unit);
        }

    }

    /**
     * Add certain number of units to this territory.
     * If unitName is not currently on this, add unit and its number to this
     * InsufficientSoldierNum exception will be thrown if input integer is less than zero
     *
     * @param combatUnit2Add input unit needs to be added to this
     * @throws InsufficientUnitNumException throw an exception when soldier2add is less than zero
     */
    @Override
    public void addCombatUnit(CombatUnit combatUnit2Add) throws InsufficientUnitNumException, InvalidUnitException {
        //check if this has the unit;
        this.combatUnit.put(combatUnit2Add.getUnitID(), combatUnit2Add);
    }

    /**
     * Add certain number of unit to this territory
     * Input integer must be greater or equal to zero.
     * <p>
     * InsufficientUnitNum exception will be thrown if input has more units than this contains
     * InvalidUnitNameException will be thrown if input unitName is not in current territory
     *
     * @param combatUnit2Remove input unit that need to be removed from this
     * @throws InsufficientUnitNumException throw an exception
     * @throws InvalidUnitException         throw an exception if input unitName is not in current territory
     */
    @Override
    public ArrayList<CombatUnit> removeCombatUnit(ArrayList<Integer> combatUnit2Remove) throws InsufficientUnitNumException, InvalidUnitException {
        if (combatUnit2Remove.isEmpty()) {
            throw new InsufficientUnitNumException("Need to be more than zero unit to remove");
        }

        ArrayList<CombatUnit> out = new ArrayList<>(0);
        try {
            for (Integer integer : combatUnit2Remove) {
                out.add(this.combatUnit.remove(integer));
            }
        } catch (IndexOutOfBoundsException e) {
            if (out.isEmpty()) {
                throw new InvalidUnitException("There is a non-existing unit ID");
            } else {
                for (CombatUnit unit : out) {
                    this.combatUnit.put(unit.hashCode(), unit);
                    throw new InvalidUnitException("There is a non-existing unit ID");
                }
            }
        }

        return out;

    }

    /**
     * Add certain number of unit to this territory
     * Input integer must be greater or equal to zero.
     * <p>
     * InsufficientUnitNum exception will be thrown if input has more units than this contains
     * InvalidUnitNameException will be thrown if input unitName is not in current territory
     *
     * @param combatUnit2Remove input unit that need to be removed from this
     * @throws InvalidUnitException throw an exception if input unitName is not in current territory
     */
    @Override
    public ArrayList<CombatUnit> removeCombatUnit(Integer combatUnit2Remove) throws InvalidUnitException {

        ArrayList<CombatUnit> out = new ArrayList<>(0);
        try {
            out.add(this.combatUnit.remove(combatUnit2Remove));

        } catch (IndexOutOfBoundsException e) {
            if (out.isEmpty()) {
                throw new InvalidUnitException("There is a non-existing unit ID");
            } else {
                for (CombatUnit unit : out) {
                    this.combatUnit.put(unit.hashCode(), unit);
                    throw new InvalidUnitException("There is a non-existing unit ID");
                }
            }
        }

        return out;

    }

    /**
     * Get the name of current territory
     *
     * @return a String of the name of current territory
     */
    @Override
    public String getTerritoryName() {
        return this.terrName;
    }

    /**
     * this is method gets all the adjacent territories
     *
     * @return a list that contents all the adjacent territroies
     */
    @Override
    public ArrayList<Territory> getAdjacentTerr() {
        return this.adjacentTerrs;
    }

    /**
     * this method get the combat unit with given name
     *
     * @return the unit with the name
     */
    @Override
    public ArrayList<CombatUnit> getCombatUnit() {
        ArrayList<CombatUnit> out = new ArrayList<>(0);
        out.addAll(this.combatUnit.values());
        Collections.sort(out);
        return out;
    }

    /**
     * this method add one to all the units in this territory
     */
    @Override
    public void addOneUnit() {
        SingleUnit adding = new SingleUnit(this.owner);
        this.combatUnit.put(adding.getUnitID(), adding);
    }

    @Override
    public int generateFood() {
        return this.foodGen;
    }

    @Override
    public int generateTech() {
        return this.techGen;
    }

    @Override
    public int getPassFood() {
        return this.foodPass;
    }

    @Override
    public CombatUnit getOneUnit(int ID) {
        return this.combatUnit.get(ID);
    }

    @Override
    public void setCostMap(int[] costMap) {
        this.costMap = costMap;
    }


}
