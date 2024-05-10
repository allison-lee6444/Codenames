package game;

import java.util.ArrayList;

public class PlayerList {
  private Player redSpymaster;
  private Player blueSpymaster;
  private final ArrayList<Player> redDetectives = new ArrayList<>();
  private final ArrayList<Player> blueDetectives = new ArrayList<>();

  public boolean isReady() {
    return redSpymaster != null && blueSpymaster != null &&
            !redDetectives.isEmpty() && !blueDetectives.isEmpty();
  }

  public boolean setRole(Player player, Player.Team team, Player.Role role) {
    // unassign player first
    if (player.isAssignedRole()) {
      if (player.getTeam() == Player.Team.RED) {
        if (player.getRole() == Player.Role.SPYMASTER) {
          redSpymaster = null;
        } else {
          redDetectives.remove(player);
        }
      } else {
        if (player.getRole() == Player.Role.SPYMASTER) {
          blueSpymaster = null;
        } else {
          blueDetectives.remove(player);
        }
      }
    }
    // assign role
    if (team == Player.Team.RED) {
      if (role == Player.Role.SPYMASTER && redSpymaster == null) {
        redSpymaster = player;
      } else if (role == Player.Role.DETECTIVE && !redDetectives.contains(player)) {
        redDetectives.add(player);
      } else {
        return false;
      }
    } else {
      if (role == Player.Role.SPYMASTER && blueSpymaster == null) {
        blueSpymaster = player;
      } else if (role == Player.Role.DETECTIVE && !blueDetectives.contains(player)) {
        blueDetectives.add(player);
      } else {
        return false;
      }
    }
    player.setRole(team, role);
    return true;
  }

  public Player getRedSpymaster() {
    return redSpymaster;
  }

  public Player getBlueSpymaster() {
    return blueSpymaster;
  }

  public ArrayList<Player> getRedDetectives() {
    return redDetectives;
  }

  public ArrayList<Player> getBlueDetectives() {
    return blueDetectives;
  }
}
