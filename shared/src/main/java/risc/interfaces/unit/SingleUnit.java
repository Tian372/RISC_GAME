package risc.interfaces.unit;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import risc.interfaces.exceptions.MaxLevelException;
import risc.interfaces.exceptions.WrongLevelException;
import risc.interfaces.player.Player;

import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;


public class SingleUnit implements CombatUnit, Comparable<CombatUnit>, Serializable {

    //private String name;
    private int level;
    private final int UnitID;
    private JSONArray lv_info;
    private final Player owner;

    public SingleUnit(Player player) {
        String level_file = "../shared/src/main/resources/lv_info";
        this.level = 0;
        this.owner = player;
        try {

            FileReader levelReader = new FileReader(level_file);
            //create JsonReader object
            JSONParser parser = new JSONParser();
            lv_info = (JSONArray) ((JSONObject) parser.parse(levelReader)).get("level");


        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        this.UnitID = this.hashCode();
    }

    /**
     * this method returns the level of current unit
     *
     * @return an integer that represent the level of current unit
     */
    @Override
    public String getUnitLv() {
        return (String) ((JSONObject) this.lv_info.get(this.level)).get("str");
    }


    /**
     * Get the type of this unit
     *
     * @return a String of the type of this unit
     */
    @Override
    public String getCombatUnitType() {
        return "Default Unit";
    }


    /**
     * This method will return an array of attack bonuses as described in 5(a)
     * This method generates attack bonus by rolling a 20-side dice
     *
     * @return an integers represent attack bonus of each unit within this
     */
    @Override
    public int getAttackBonus() {
        long result = (long) (((JSONObject) this.lv_info.get(this.level)).get("Attack"));
        return (int) result;
    }


    @Override
    public int getUpgradeCost(int level) throws MaxLevelException, WrongLevelException {
        if (level < this.level) {
            throw new WrongLevelException("Upgrade Level needs to be higher than current level.");
        } else if (level > 6) {
            throw new WrongLevelException("Upgrade Level needs to be lower than 6.");
        } else if (this.level == 6) {
            throw new MaxLevelException("This is alright at its max level.");
        } else {

            long currentCostLong = (Long) ((JSONObject) this.lv_info.get(this.level)).get("upgrade");
            long targetCostLong = (Long) ((JSONObject) this.lv_info.get(level)).get("upgrade");

            int currentCost = (int) currentCostLong;
            int targetCost = (int) targetCostLong;

            return (targetCost - currentCost);
        }

    }


    @Override
    public void upgrade(int targetLv) throws MaxLevelException, WrongLevelException {
        if (targetLv < this.level) {
            throw new WrongLevelException("Upgrade Level needs to be higher than current level.");
        } else if (targetLv > 6) {
            throw new WrongLevelException("Upgrade Level needs to be lower than 6.");
        } else if (this.level == 6) {
            throw new MaxLevelException("This is alright at its max level.");
        } else {
            this.level = targetLv;
        }
    }

    @Override
    public int getUnitID() {
        return this.UnitID;
    }

    @Override
    public Player getOwner() {
        return this.owner;
    }

    @Override
    public int compareTo(CombatUnit combatUnit) {
        return this.getAttackBonus() - combatUnit.getAttackBonus();
    }


}

