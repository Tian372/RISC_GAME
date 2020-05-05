package risc.interfaces.unit;

import risc.interfaces.exceptions.MaxLevelException;
import risc.interfaces.exceptions.WrongLevelException;
import risc.interfaces.player.Player;

public interface CombatUnit extends Comparable<CombatUnit> {
    /**
     * this method returns the level of current unit
     *
     * @return an string that represent the level of current unit
     */
    String getUnitLv();

//    /**
//     * this method will have bonus attack damage added on top of 20-side dice roll
//     *
//     * @return an integer that represent the dmg of current unit
//     */
//    int getUnitDmg();

    /**
     * Get the type of this unit
     *
     * @return a String of the type of this unit
     */
    String getCombatUnitType();

//now this method seems irrelevant
    ////  /**
    ////   * Get the amount of current unit
    ////   * Return value will always be greater than or equal to zero
    ////   *
    // //  * @return an integer of the amount of this unit
    // //  */
    //  //int getCombatUnitAmount();

//  /**
//   * Add a certain amount of units to this unit
//   * Both input unit and target unit have to be the same unit type
//   *
//   * @exception InvalidUnitException thrown if two units are not the same type
//   * @param combatUnit2Add input unit contains the amount to add
//   */
//  void add(CombatUnit combatUnit2Add) throws InvalidUnitException, InsufficientUnitNumException;

//    /**
//     * Remove a certain amount of units from this
//     * Both input unit and target unit have to be the same unit type
//     *
//     * @param combatUnit2Remove input unit contains the amount to remove
//     * @throws InvalidUnitException         thrown if two units are not the same type
//     * @throws InsufficientUnitNumException thrown when input isUnit has more unit than this
//     */
//    void remove(CombatUnit combatUnit2Remove)
//            throws InsufficientUnitNumException, InvalidUnitNameException;

    /**
     * This method will return an array of attack bonuses as described in 5(a)
     * This method generates attack bonus by rolling a 20-side dice
     *
     * @return an integers represent attack bonus of each unit within this
     */
    int getAttackBonus();

//    /**
//     * this method is only used by CombatFactory
//     *
//     * @param num number to init
//     */
//    void initAdd(int num);

    int getUpgradeCost(int level) throws MaxLevelException, WrongLevelException;


    void upgrade(int targetLv) throws MaxLevelException, WrongLevelException;

    int getUnitID();

    Player getOwner();

}
