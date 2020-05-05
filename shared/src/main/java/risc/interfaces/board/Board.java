package risc.interfaces.board;

import risc.interfaces.territory.Territory;

public interface Board {
    /**
     *  Check if two territory are next to each other
     *
     * @param terr1 one of the two territory to test
     * @param terr2 one of the two territory to test
     * @return true if they are adjacent, false otherwise
     */
    boolean isAdjacent(Territory terr1, Territory terr2);
    /**
     * Check if a territory has certain amount of units
     *
     * @param terrName the name of the territory to check
     * @param unitType the type of the unit to check
     * @param amount the amount of unitType to check
     * @return true if terrName has enough of unitType, false otherwise
     */

    boolean hasEnoughUnit(String terrName, String unitType, int amount);


    /**
     * Check if this has a territory names "terrName"
     *
     * @param terrName the name of the territory to check
     * @return true if board contains terrName, false otherwise
     */
    boolean hasTerr(String terrName);


    /**
     * get the territory with the name terrName
     *
     * @param terrName the name of the territory to check
     * @return an Territory object that has the name terrName
     */
    Territory getTerritory(String terrName);


    public Territory getTerritoryByID(int id);
//
//    /**
//     * get the territory with the name terrName
//     *
//     * @param terrID the name of the territory to check
//     * @return an Territory object that has the name terrName
//     */
//    Territory getTerritory(int terrID);

}



