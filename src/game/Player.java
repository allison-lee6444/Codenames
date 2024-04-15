package game;


import java.util.Objects;

public class Player {
  public enum Team {RED, BLUE}

  ;

  public enum Role {SPYMASTER, DETECTIVE}

  ;

  private String username;
  private int userId;
  private int gameId;
  private boolean assignedRole = false;
  private Role role;
  private Team team;

  public Player(String username, int userId, int gameId) {
    this.username = username;
    this.userId = userId;
    this.gameId = gameId;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  public int getGameId() {
    return gameId;
  }

  public void setGameId(int gameId) {
    this.gameId = gameId;
  }

  public void setRole(Team team, Role role) {
    this.team = team;
    this.role = role;
    assignedRole=true;
  }

  public Role getRole() {
    return role;
  }

  public Team getTeam() {
    return team;
  }

  public boolean isAssignedRole() {
    return assignedRole;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Player)) return false;
    Player player = (Player) o;
    return getUserId() == player.getUserId() && getGameId() == player.getGameId() && Objects.equals(getUsername(), player.getUsername()) && role == player.role && team == player.team;
  }

  @Override
  public int hashCode() {
    return Objects.hash(getUsername(), getUserId(), getGameId(), role, team);
  }
}
