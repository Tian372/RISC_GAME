package risc.interfaces.board;

import risc.interfaces.player.Player;
import risc.interfaces.territory.Territory;
import risc.interfaces.territory.TerritoryV2;

import javax.json.*;
import java.io.*;
import java.util.*;


public class GameBoard implements Board, Serializable {

    //private boolean[][] terrMap;

    private HashMap<String, Territory> terrMap;
    int[][] adjacencyMatrix;

    public GameBoard(ArrayList<Player> playerList, int terrForPlayer) {
        String name_file = "../shared/src/main/resources/terrNames.txt";
        String map_file = "../shared/src/main/resources/map_info";

        //String costMapFile = "../shared/src/main/resources/costMap";

        this.terrMap = new HashMap<>(0);

        //a txt for names

        try {

            FileReader nameReader = new FileReader(name_file);

            BufferedReader bufferedReader = new BufferedReader(nameReader);
            List<String> lines = new ArrayList<>();
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                lines.add(line);
            }
            bufferedReader.close();

            String[] terrNames = lines.toArray(new String[0]);

            int totalTerr = terrForPlayer * playerList.size();
            this.adjacencyMatrix = new int[totalTerr][totalTerr];

            //this.terrMap = new boolean[totalTerr][totalTerr];

            ArrayList<Territory> terrList = new ArrayList<>();

            FileReader mapReader = new FileReader(map_file);
            //create JsonReader object
            JsonReader jsonReader = Json.createReader(mapReader);
            JsonArray myMap = jsonReader.readObject().getJsonArray(String.valueOf(playerList.size()));

            //TODO : may change Territory Ownership Initialization
            int playerNum = 0;
            Random random = new Random();
            for (int i = 1; i <= totalTerr; i++) {
                TerritoryV2 currentTerr = new TerritoryV2(playerList.get(playerNum),i - 1, terrNames[i], 10, 15, 20 + random.nextInt(5), 10);
                terrList.add(currentTerr);
                this.terrMap.put(currentTerr.getTerritoryName(), currentTerr);

                if (i % terrForPlayer == 0) {
                    playerNum++;
                }
            }

            for (int i = 0; i < myMap.size(); i++) {
                JsonObject currentObject = myMap.getJsonObject(i);

                int t1 = currentObject.getInt("t1");
                int t2 = currentObject.getInt("t2");

                terrList.get(t1).addAdjacentTerr(terrList.get(t2));
                this.adjacencyMatrix[t1][t2] = terrList.get(t2).getPassFood();
                this.adjacencyMatrix[t2][t1] = terrList.get(t1).getPassFood();

            }

            int[][] shortestPath = this.getShortestPath();

            for (Territory t : terrList) {
               // System.out.println(Arrays.toString(shortestPath[t.getID()]));
                t.setCostMap(shortestPath[t.getID()]);
            }


//            JsonArrayBuilder costMap = Json.createArrayBuilder();
//
//
//            for (int i = 0; i < totalTerr; i++) {
//                JsonArrayBuilder currentMap = Json.createArrayBuilder();
//                for (int j = 0; j < totalTerr; j++) {
//                    currentMap.add(shortestPath[i][j]);
//                }
//                costMap.add(currentMap);
//            }
//
//            JsonArray jsonArray = costMap.build();
//
//            JsonWriter jsonWriter = Json.createWriter(new FileWriter(costMapFile));
//            jsonWriter.writeArray(jsonArray);
//            jsonWriter.close();

        } catch (IOException e) {
            // Print out the exception that occurred
            System.out.println("Unable to read files: " + e.getMessage());
        }


    }

    private int[][] getShortestPath() {
        int[][] shortestPath = new int[adjacencyMatrix[0].length][adjacencyMatrix[0].length];

        int n = this.adjacencyMatrix[0].length;
        for (int k = 0; k < n; k++) {

            int[] shortest = new int[n];
            boolean[] added = new boolean[n];

            for (int v = 0; v < n; v++) {
                shortest[v] = Integer.MAX_VALUE;
                //added[v] = false;
            }


            shortest[k] = 0;

            //int[] parents = new int[n];
            //parents[0] = -1;

            for (int i = 1; i < n; i++) {
                int temp = -1; //store temp data
                int min = Integer.MAX_VALUE;

                for (int v = 0; v < n; v++) {
                    if (!added[v] && shortest[v] < min) {
                        temp = v;
                        min = shortest[v];
                    }
                }
                added[k] = true;
                if (temp != -1) {
                    added[temp] = true;
                } else {
                    continue;
                }
                for (int v = 0; v < n; v++) {
                    int dist = adjacencyMatrix[temp][v];
                    if (dist > 0 && ((min + dist) < shortest[v])) {
                        //parents[v] = v1;
                        shortest[v] = min + dist;
                    }
                }
            }
            shortestPath[k] = shortest;

        }
        return shortestPath;
    }

    /**
     * Check if two territory are next to each other
     *
     * @param terr1 one of the two territory to test
     * @param terr2 one of the two territory to test
     * @return true if they are adjacent, false otherwise
     */
    @Override
    public boolean isAdjacent(Territory terr1, Territory terr2) {
        return terr1.isAdjacent(terr2);
    }

    /**
     * Check if a territory has certain amount of units
     *
     * @param terrName the name of the territory to check
     * @param unitType the type of the unit to check
     * @param amount   the amount of unitType to check
     * @return true if terrName has enough of unitType, false otherwise
     */
    @Override
    public boolean hasEnoughUnit(String terrName, String unitType, int amount) {
//        Territory temp = this.terrMap.get(terrName);
//        try{
//            int unitAmount = temp.getCombatUnitAmount(unitType);
//            return unitAmount >= amount;
//        }
//
//        catch (InvalidUnitException e){
//            return false;
//        }
        return false;
    }

    /**
     * Check if this has a territory names "terrName"
     *
     * @param terrName the name of the territory to check
     * @return true if board contains terrName, false otherwise
     */
    @Override
    public boolean hasTerr(String terrName) {
        return this.terrMap.containsKey(terrName);
    }

    /**
     * get the territory with the name terrName
     *
     * @param terrName the name of the territory to check
     * @return an Territory object that has the name terrName
     */
    @Override
    public Territory getTerritory(String terrName) {
        return this.terrMap.get(terrName);

    }

    @Override
    public Territory getTerritoryByID(int id) {
        for (String key : terrMap.keySet()) {
            Territory terr = terrMap.get(key);
            if (terr.getID() == id) {
                return terr;
            }
        }
        return null;
    }


}
