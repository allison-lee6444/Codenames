package game;

import java.util.ArrayList;
import java.util.function.BiFunction;

import game.Player.*;

public class Game {
  private final ArrayList<Move> moves = new ArrayList<>();
  private Player redSpymaster;
  private Player blueSpymaster;
  private final ArrayList<Player> redDetectives = new ArrayList<>();
  private final ArrayList<Player> blueDetectives = new ArrayList<>();
  private final int id;

  public Game(int id) {
    this.id = id;
  }

  public boolean pickRole(Player player, Team team, Role role) {
    if (player.getGameId() != id){
      return false;
    }

    BiFunction<Team, Role, Object> teamRoleToObject = (team1, role1) -> {
      if (role1 == Role.SPYMASTER) {
        if (team1 == Team.RED) {
          return redSpymaster.getvariable();
        } else {
          return blueSpymaster;
        }
      } else {
        if (team1 == Team.RED) {
          return redDetectives;
        } else {
          return blueDetectives;
        }
      }
    };


    if (player.isAssignedRole()){
      Object correspondingObject = teamRoleToObject.apply(player.getTeam(), player.getRole());
      if (correspondingObject instanceof Player) {
        correspondingObject = null;
      } else {
        ((ArrayList<?>) correspondingObject).remove(player);
      }
    }

    if (role == Role.SPYMASTER) {
      if (team == Team.RED && redSpymaster == null) {
        redSpymaster = player;
        player.setRole(Team.RED, Role.SPYMASTER);
        return true;
      } else if (team == Team.BLUE && blueSpymaster == null) {
        blueSpymaster = player;
        player.setRole(Team.BLUE, Role.SPYMASTER);
        return true;
      }
      return false;
    } else {
      if (team == Team.RED) {
        redDetectives.add(player);
        player.setRole(Team.RED, Role.DETECTIVE);
        return true;
      } else {
        blueDetectives.add(player);
        player.setRole(Team.BLUE, Role.DETECTIVE);
        return true;
      }
    }
  }

  public int getId() {
    return id;
  }

  public ArrayList<Player> getBlueDetectives() {
    return blueDetectives;
  }

  public ArrayList<Player> getRedDetectives() {
    return redDetectives;
  }

  public Player getBlueSpymaster() {
    return blueSpymaster;
  }

  public Player getRedSpymaster() {
    return redSpymaster;
  }

  public ArrayList<Move> getMoves() {
    return moves;
  }

  public static void main(String[] args){
    Player player1 = new Player("1",1,1);
    Player player2 = new Player("2",2,1);
    Game game = new Game(1);

    // test uniqueness of spymaster
    game.pickRole(player1, Team.RED, Role.SPYMASTER);
    game.pickRole(player2, Team.RED, Role.SPYMASTER);
    assert game.getRedSpymaster().equals(player1);

    // test integrity of role change: spymaster
    game.pickRole(player1, Team.BLUE, Role.SPYMASTER);
    game.pickRole(player2, Team.RED, Role.SPYMASTER);
    assert game.getBlueSpymaster().equals(player1);
    assert game.getRedSpymaster().equals(player2);

    // test integrity of role change: detective
  }
}


