package game;

public class Hint extends Move{
  private String word;
  private int number;

  public Hint(Player player, String word, int number) {
    super(player, MoveType.HINT);
    this.word = word;
    this.number = number;
  }

  public String getWord() {
    return word;
  }

  public void setWord(String word) {
    this.word = word;
  }

  public int getNumber() {
    return number;
  }

  public void setNumber(int number) {
    this.number = number;
  }

  @Override
  public String toString() {
    return "Hint{" + super.toString()+
            "word='" + word + '\'' +
            ", number=" + number +
            '}';
  }
}
