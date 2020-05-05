package risc.socket.response;

import org.junit.jupiter.api.Test;
import risc.socket.Action;
import risc.socket.ActionType;

import static org.junit.jupiter.api.Assertions.*;

class ActionInputResponseTest {
  @Test
  void test() {
    ActionInputResponse action = new ActionInputResponse(true, "test");
    action.getErrorMessage();
    action.isSuccess();
    action.toString();
  }

}