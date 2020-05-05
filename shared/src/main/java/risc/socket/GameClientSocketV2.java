package risc.socket;

import risc.interfaces.game.Game;
import risc.interfaces.player.Player;
import risc.interfaces.socket.response.Response;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class GameClientSocketV2 {

  private String hostName;
  private int portNumber;
  private Socket socket;
  private ObjectOutputStream out;
  private ObjectInputStream in;
  private Game game;

  private int playerID;

  public GameClientSocketV2() {
    game = new Game(3);
    playerID = 1;
  }

  public String getHostName() {
    return hostName;
  }

  public void setHostName(String hostName) {
    this.hostName = hostName;
  }

  public int getPortNumber() {
    return portNumber;
  }

  public void setPortNumber(int portNumber) {
    this.portNumber = portNumber;
  }

  public int getPlayerID() {
    return playerID;
  }

  public void setPlayerID(int playerID) {
    this.playerID = playerID;
  }


  public void connect() throws IOException {
    socket = new Socket(hostName, portNumber);
    out = new ObjectOutputStream(socket.getOutputStream());
    in = new ObjectInputStream(socket.getInputStream());
  }

  public void waitAllTheClientsReady() throws IOException, ClassNotFoundException {
    String line = (String) in.readObject();
    while (!line.equals("start")) {}
    System.out.println("[Player "+ playerID +"] received start signal.");
  }

  public void sendInput(String userInput) throws IOException {
    out.reset();
    out.writeObject(userInput);
  }

  public int requestPlayerID() throws IOException, ClassNotFoundException {
    System.out.println("request PlayerID...");
    playerID = Integer.parseInt((String) in.readObject());
    return playerID;
  }

  public Game receiveGame() throws IOException, ClassNotFoundException {
    game = (Game) in.readObject();
    return game;
  }

  public void sendReady() throws IOException {
    out.reset();
    out.writeObject("ready");
  }

  public Response receiveInputValidationResponse() throws IOException, ClassNotFoundException {
    Response res = (Response) in.readObject();
    return res;
  }

  public void sendInputObj(Action action) throws IOException {
    out.reset();
    out.writeObject(action);
  }

  public String receiveStart() throws IOException, ClassNotFoundException {
    String line = (String) in.readObject();
    return line;
  }

  public Response validateInput(Action action) throws IOException, ClassNotFoundException {
    sendInputObj(action);
    return receiveInputValidationResponse();
  }

  public void close() throws IOException {
    out.close();
    socket.close();
  }

  public Player getPlayer() {
    return game.getPlayer(playerID);
  }

  public Game getGame() {
    return game;
  }

  public void setGame(Game game) {
    this.game = game;
  }
}
