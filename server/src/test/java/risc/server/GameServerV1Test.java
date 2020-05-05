package risc.server;

import org.junit.jupiter.api.Test;
import risc.interfaces.exceptions.InvalidUnitException;
import risc.interfaces.server.GameServer;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.TimeUnit;

class GameServerV1Test {
  @Test
  void mockGame() throws IOException, InterruptedException, InvalidUnitException, BrokenBarrierException {

    RecursiveAction recursiveAction = new RecursiveAction() {
      @Override
      protected void compute() {
        GameServer gameServer = null;
        try {
          gameServer = new GameServerV1(8080, 2);
          gameServer.run();

        } catch (IOException e) {
          e.printStackTrace();
        } catch (InvalidUnitException e) {
          e.printStackTrace();
        } catch (InterruptedException e) {
          e.printStackTrace();
        } catch (BrokenBarrierException e) {
          e.printStackTrace();
        }
      }
    };

    ForkJoinPool forkJoinPool = new ForkJoinPool();
    forkJoinPool.submit(recursiveAction);
    TimeUnit.SECONDS.sleep(1);
    forkJoinPool.shutdownNow();


  }

}