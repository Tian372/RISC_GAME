package risc.interfaces.game;

import risc.interfaces.action.AttackAction;
import risc.interfaces.action.GameAction;
import risc.interfaces.board.GameBoard;
import risc.interfaces.factory.ActionFactory;
import risc.interfaces.player.HumanPlayer;
import risc.interfaces.player.Player;
import risc.interfaces.socket.response.Response;
import risc.interfaces.territory.Territory;
import risc.socket.Action;
import risc.socket.ActionType;
import risc.socket.response.ActionInputResponse;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;

public class Game implements Serializable {
    //will assign the possible name to each players
    //because we can at most have 5 players, so there are only 5 possible names
    private static String [] possibleName= {"Green","Red","Blue","Orange","Purple"};


    //will assign an ID to each player, this field is used to track what next ID we will assign
    int nextID;

    //the map of the game
    GameBoard gameBoard;

    //a list of all players
    //the index of a player in this array is the same as the player ID
    ArrayList<Player> allPlayer;

    //How many players are still in the game(palyers who haven't lost)
    int numRemainingPlayer;

    int []allyRequest;


    //this factory will transfer the input message from player to an Action Object.
    ActionFactory actionFactory;


    //this arraylist will store all the actions that need to be performed in one turn
    //temporary comment action related code

    ArrayList<AttackAction> allAttackAction;


    /**
     * This constructor creates the game based on how many players we have
     * It will setup the socket for each player
     * It will initialize the board of the game and setup each player's info
     */
    public Game(int numPlayer) {
        numRemainingPlayer=numPlayer;

        allyRequest=new int[numPlayer];

        nextID=0;
        allPlayer=new ArrayList<Player>();
        for(int index=0;index<numPlayer;index++) {

            //this line may need to be changed based on later adjustment of GameSocket

            //temporary comment socket initialization related code
    /*
    allServerSocket.set(index, new ServerSocket());
    */
            allPlayer.add(new HumanPlayer(nextID,possibleName[index]));
            nextID++;
        }
        if(numPlayer==2){
            gameBoard=new GameBoard(allPlayer,9);
        }
        else if(numPlayer==3){
            gameBoard=new GameBoard(allPlayer,6);
        }
        //TODO: in fact we cannot support 4 and 5 players now
        else if (numPlayer==4||numPlayer==5){
            gameBoard=new GameBoard(allPlayer,3);
        }
        else{
            System.out.println("cannot handle this many players");
        }

        actionFactory=new ActionFactory(gameBoard,allPlayer);
        //now need to setup board and setup each player's info

        allAttackAction = new ArrayList<AttackAction> ();
    }


    //temporary comment action related code
  /*
 public void oneTurn() {
  for(GameAction act:allAction) {
   act.applyAction();
  }
 }
  */

    /**
     * @return the ArrayList of all the Player Object in this game
     */
    public ArrayList<Player> getAllPlayer(){
        return allPlayer;
    }

    public Player getPlayerByID(int playerID) {
        return allPlayer.get(playerID);
    }
    /**
     * @param id which is the id of the Player we want to get
     * @return The Player Object that has the specified id
     */
    public Player getPlayer(int id) {
        return allPlayer.get(id);
    }

    /*
     * @return the board of this game
     */
    public GameBoard getBoard() {
        return gameBoard;
    }

    //the initial part of a game
    public void startOneTurn(){
        allAttackAction = new ArrayList<AttackAction> ();
        for(int i=0;i<allPlayer.size();i++){
            allyRequest[i]=-1;
        }

    }

    //will construct and apply part of the actions
    public Response validate(Action act){

        if(act.getActionType()==ActionType.ALLY){
            Player onePlayer=getPlayer(act.getPlayerID());
            if(onePlayer.hasAlly()) {
                return new ActionInputResponse(false, "You already have an ally, you cannot be allied with more than one players");
            }
            else if(allPlayer.get(act.getAllyID()).isFinished()){
                return new ActionInputResponse(false, "You cannot be allied with a failed player");
            }
            else if(act.getPlayerID()==act.getAllyID()){
                return new ActionInputResponse(false, "You cannot be allied with yourself");
            }
            else if(allyRequest[act.getPlayerID()]!=-1&&allyRequest[act.getPlayerID()]!=act.getAllyID()) {
                return new ActionInputResponse(false, "You already commit an ally request, you cannot be allied with more than one players");
            }

            allyRequest[act.getPlayerID()]=act.getAllyID();
            return new ActionInputResponse(true,"order is valid, your ally request has submitted, please wait till next round to know whether it succeeds");
        }

        try{
            GameAction currAction=actionFactory.generateAction(act,allAttackAction);

            //if currAction is null, it means this attack action has been combined
            if(act.getActionType()==ActionType.ATTACK&&currAction!=null){
                allAttackAction.add((AttackAction)currAction);
            }
            return new ActionInputResponse(true,"order is valid");
        }
        catch(Exception e){
            e.printStackTrace();
            return new ActionInputResponse (false, e.getMessage());
        }

    }


    // in fact there is no need for the argument Queue<InputMessage> messageQueue, but haven't changed the interfaces
    public void applyAllOrders(){

        //given the implementation details of Move Action and Attack Action, we only need to apply attack actions finally
        for (GameAction atk:allAttackAction){
            atk.applyAction();
        }

        //after finishing attacking, determine what players are losing and what players are still in the game
        for(Player onePlayer:allPlayer){
            if(onePlayer.isFinished()){
                continue;
            }
            HashSet<Territory> allTerritory=onePlayer.getAllTerritory();
            System.out.println(onePlayer.getName()+": "+allTerritory.size());
            if(allTerritory.size()==0){
                numRemainingPlayer-=1;
                System.out.println("set Player false "+onePlayer.getName());
                onePlayer.finishPlaying(false);
            }


        }

        //Finally there will be only one player, and he will be winner
        if(numRemainingPlayer==1){
            for(Player onePlayer:allPlayer){
                if(onePlayer.isFinished()){
                    continue;
                }
                System.out.println("Set player True "+onePlayer.getName());
                onePlayer.finishPlaying(true);
            }
        }
        //add one unit to each territory at the end of one turn
        else{
            for(Player p : allPlayer){
                p.generateNewResource();
                for(Territory t: p.getAllTerritory()){
                    t.addOneUnit();
                }
            }
        }

        Player player1;
        Player player2;
        for(int id1=0;id1<allPlayer.size();id1++){
            int id2=allyRequest[id1];
            //make sure both players want to be allied with each other
            if(id2!=-1&&allyRequest[id2]==id1){
                player1=getPlayer(id1);
                player2=getPlayer(id2);
                player1.addAlliance(player2);
                //player2.addAlliance(player1) will be executed later in the iteration when id1 becomes the current id2
            }
        }

    }


    public boolean isFinished(){
        if(numRemainingPlayer==1){
            return true;
        }

        return false;
    }
}

