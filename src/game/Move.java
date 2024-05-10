package game;

import java.io.Serializable;

public class Move implements Serializable {
  private final Player player;
  public enum MoveType {HINT, GUESS}
  private final MoveType moveType;

  public Move(Player player, MoveType moveType) {
    this.player = player;
    this.moveType = moveType;
  }

  public Player getPlayer() {
    return player;
  }

  public MoveType getMoveType() {
    return moveType;
  }
}
