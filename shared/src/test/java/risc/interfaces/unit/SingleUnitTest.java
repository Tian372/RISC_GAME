package risc.interfaces.unit;

import org.junit.jupiter.api.Test;
import risc.interfaces.exceptions.MaxLevelException;
import risc.interfaces.exceptions.WrongLevelException;
import risc.interfaces.player.HumanPlayer;

import static org.junit.jupiter.api.Assertions.*;

class SingleUnitTest {

    @Test
    void getUnitLv() throws WrongLevelException, MaxLevelException {
        HumanPlayer player = new HumanPlayer(1,"T");
        SingleUnit unit = new SingleUnit(player);

        assertEquals(unit.getUnitLv(), "Level 0");
        assertEquals(unit.getCombatUnitType(), "Default Unit");
        assertEquals(unit.getAttackBonus(), 0);

        assertEquals(unit.getUpgradeCost(3), 30);

        unit.upgrade(3);

        assertEquals(unit.compareTo(unit), 0);
    }


}