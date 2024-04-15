package game;

public class Guess extends Move{
  private int wordId;

  public Guess(Player player, int wordId) {
    super(player, false);
    this.wordId = wordId;
  }

  public int getWordId() {
    return wordId;
  }

  public void setWordId(int wordId) {
    this.wordId = wordId;
  }

  @Override
  public String toString() {
    return "Guess{" + super.toString()+
            "wordId=" + wordId +
            '}';
  }
}
