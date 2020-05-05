RISC_GAME
This game is a turn-based strategy game by which 2 to 5 players can play.

Version 1:

## 1.
There are three basic commands: Attack, Move and Done.
The format of Move command: M sourceTerritory targetTerritory numberOfUnit
The format of Attack command: M sourceTerritory targetTerritory numberOfUnit
The format of Done: D

## 2.
The result for each turn will be calculated when all players have committed all the commands with the final command "D".

## 3.
The server of this game is default run on vcm-13258.vm.duke.edu. If you let it run on a different IP address, you should change the code in client/src/main/java/risc/client/Main.java
 
## Run

1. gradle run-server
    - input N for new game, E for End
    - input number of player
2. gradle run-client
