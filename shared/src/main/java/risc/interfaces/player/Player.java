package risc.interfaces.player;
import risc.interfaces.exceptions.ResourceException;
import risc.interfaces.territory.Territory;

import java.util.HashSet;


public interface Player {
    /**
     * Each Player Object will be assigned an ID when it is created to identify different players
     * This method will return the ID of the current Player
     *
     * @return a integer that represents the Player's ID
     */
    int getID();

    boolean isFinished();

    boolean isWin();

    void finishPlaying(boolean isWin);

    /**
     * Each Player Object will be assigned a name
     * This method will return the name of the current Player
     *
     * @return a String that represents the Player's name
     */
    String getName();

    /**
     * This method will check whether the Player owns the territory
     * It will be called in class Game for command legability check
     *
     * @param t, which is the Territory we will check if the Player owns
     * @return a boolean. true if the Player owns this territory, false otherwise
     */
    boolean ownsTerritory(Territory t);

    /**
     * @return a LinkedList of all the territory that the current Player owns.
     */
    HashSet<Territory> getAllTerritory();

    /**
     * This method will add a territory to the current Player
     *
     * @param  t, which is the territory to add to this player
     * @return a boolean, true if added successfully; false otherwise
     */
    boolean addTerritory(Territory t);

    /**
     * This method will remove a Territory from the current Player
     *
     * @param  t, which is the territory we will remove from this player
     * @return a boolean, true if removed successfully; false otherwise
     */
    boolean removeTerritory(Territory t);


    int getFoodResource();

    int getTechResource();

    void addFoodResource(int foodR);

    void addTechResource(int techR);

    void removeFoodResource(int foodR) throws ResourceException;

    void removeTechResource(int techR) throws ResourceException;

    void generateNewResource();

    //whether the player has an ally now
    boolean hasAlly();

    //if the currentPlayer is allied with "anotherPlayer", return true
    boolean isAlliance(Player anotherPlayer);

    //make the current player be allied with "anotherPlayer"
    //return false if the add fails (i.e. the current player already has an ally)
    boolean addAlliance(Player anotherPlayer);

    //remove the alliance between the current player and "anotherPlayer"
    //return false if the remove fails (i.e. "anotherPlayer" is not ally of current player)
    boolean removeAlliance();

}
