package game;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

public class PlayerList implements Serializable {
  private Player redSpymaster;
  private Player blueSpymaster;
  private final ArrayList<Player> redDetectives = new ArrayList<>();
  private final ArrayList<Player> blueDetectives = new ArrayList<>();
  private final ArrayList<Player> unassigned = new ArrayList<>();
  private boolean joining = true;

  public boolean isReady() {
    return redSpymaster != null && blueSpymaster != null && unassigned.isEmpty() &&
            !redDetectives.isEmpty() && !blueDetectives.isEmpty();
  }


  public boolean isJoining() {
    return joining;
  }

  // allow or refuse player set role request
  public synchronized boolean setRole(Player player, Player.Team team, Player.Role role) {
    if (!joining) {
      return false;
    }
    // unassign player first. if this fails, the wrapper at Game will restore their role and team.
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
    } else {
      unassigned.remove(player);
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

  public ArrayList<Player> getUnassigned() {
    return unassigned;
  }

  public void addUnassigned(Player player) {
    unassigned.add(player);
  }

  public int size() {
    return redDetectives.size() + blueDetectives.size() + unassigned.size()
            + (redSpymaster != null ? 1 : 0) + (blueSpymaster != null ? 1 : 0);
  }

  public void endJoining() {
    joining = false;
  }

  // allow it to pass over the network
  private void writeObject(java.io.ObjectOutputStream out) throws IOException {
    out.defaultWriteObject();
    out.writeInt(unassigned.size());
    for (Player player : unassigned) {
      out.writeObject(player);
    }
    out.writeInt(redDetectives.size());
    for (Player player : redDetectives) {
      out.writeObject(player);
    }
    out.writeInt(blueDetectives.size());
    for (Player player : blueDetectives) {
      out.writeObject(player);
    }
  }

  private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
    in.defaultReadObject();
    unassigned.clear();
    int count = in.readInt();
    for (int i = 0; i < count; i++) {
      unassigned.add((Player) in.readObject());
    }

    redDetectives.clear();
    count = in.readInt();
    for (int i = 0; i < count; i++) {
      redDetectives.add((Player) in.readObject());
    }
    blueDetectives.clear();
    count = in.readInt();
    for (int i = 0; i < count; i++) {
      blueDetectives.add((Player) in.readObject());
    }
  }
}
