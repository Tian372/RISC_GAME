package risc.socket;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ActionTypeTest {
  @Test
  void test() {
    ActionType actionType = ActionType.MOVE;
    actionType = ActionType.ATTACK;
    actionType = ActionType.DONE;
    actionType = ActionType.UPGRADE;
  }
}