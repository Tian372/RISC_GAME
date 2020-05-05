package risc.interfaces.server;

import risc.interfaces.exceptions.InvalidUnitException;

import java.io.IOException;
import java.util.concurrent.BrokenBarrierException;

public interface GameServer {
  /**
   * start game server
   */
  void run() throws IOException, InterruptedException, BrokenBarrierException, InvalidUnitException;


}
