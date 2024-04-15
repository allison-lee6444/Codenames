package game;

public abstract class Move {
  private final Player player;
  private final boolean isHint; // true if hint, false if guess

  public Move(Player player, boolean isHint) {
    this.player = player;
    this.isHint = isHint;
  }

  public Player getPlayer() {
    return player;
  }

  public boolean isHint() {
    return isHint;
  }

  @Override
  public String toString() {
    return "Move{" +
            "player=" + player +
            ", isHint=" + isHint +
            '}';
  }
}
