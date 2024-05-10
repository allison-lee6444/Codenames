package game;

import java.util.ArrayList;

import game.Player.*;

public class Game {
  private ArrayList<Move> moves = new ArrayList<>();
  private int id;
  private PlayerList playerList = new PlayerList();
  private static int nextId = 1;

  public enum State {WAITING, PLAYING}

  ;
  private State state = State.WAITING;

  public Game() {
    this.id = nextId++;
  }

  public Game(int id){
    this.id = id;
  }

  public boolean pickRole(Player player, Team team, Role role) {
    if (player.getGameId() != id) {
      return false;
    }
    boolean succeeded = playerList.setRole(player, team, role);
    if (!succeeded) { // restore role
      if (player.isAssignedRole())
        playerList.setRole(player, player.getTeam(), player.getRole());
      else
        playerList.addUnassigned(player);
    }
    return succeeded;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id= id;
  }

  public PlayerList getPlayerList() {
    return playerList;
  }

  public ArrayList<Move> getMoves() {
    return moves;
  }

  public State getState() {
    return state;
  }

  public void setPlayerList(PlayerList pl){
    playerList = pl;
  }

  public void setMoves(ArrayList<Move> moves){
    this.moves = moves;
  }

  public void start(){
    state = State.PLAYING;
    playerList.endJoining();
  }

  public static void main(String[] args) {
    Player player1 = new Player("1", 1, 1);
    Player player2 = new Player("2", 2, 1);
    Game game = new Game();
    PlayerList playerList = game.getPlayerList();

    // test uniqueness of spymaster
    game.pickRole(player1, Team.RED, Role.SPYMASTER);
    game.pickRole(player2, Team.RED, Role.SPYMASTER);
    assert playerList.getRedSpymaster().equals(player1);

    // test integrity of role change: spymaster
    game.pickRole(player1, Team.BLUE, Role.SPYMASTER);
    game.pickRole(player2, Team.RED, Role.SPYMASTER);
    assert playerList.getBlueSpymaster().equals(player1);
    assert playerList.getRedSpymaster().equals(player2);

    // test integrity of role change: detective
    game.pickRole(player2, Team.BLUE, Role.DETECTIVE);
    game.pickRole(player1, Team.BLUE, Role.DETECTIVE);
    assert playerList.getBlueDetectives().contains(player1);
    assert playerList.getBlueDetectives().contains(player2);

    assert !playerList.isReady();
  }
}


