package game;

import java.io.IOException;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.ArrayList;

import game.Player.*;

public class Game implements Serializable {
  private final ArrayList<Move> moves = new ArrayList<>();
  private final int id;
  private final PlayerList playerList = new PlayerList();

  public enum State {START, WAITING, PLAYING};
  private State state = State.START;

  public Game(int id) {
    this.id = id;
  }

  public boolean pickRole(Player player, Team team, Role role) {
    if (player.getGameId() != id) {
      return false;
    }
    return playerList.setRole(player, team, role);
  }

  public int getId() {
    return id;
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

  public void setState(State state) {
    this.state = state;
  }

  public static void main(String[] args) {
    Player player1 = new Player("1", 1, 1);
    Player player2 = new Player("2", 2, 1);
    Game game = new Game(1);
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


