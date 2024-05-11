package game;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Board implements Serializable {
  private final String[] words;

  public enum WordType {RED, BLUE, ASSASSIN, BYSTANDER}

  private final WordType[] wordTypes;

  public Board() {
    words = new String[25];
    wordTypes = new WordType[25];

    ArrayList<String> selectedWords = readFile("wordlist.txt");
    assert selectedWords != null;
    Collections.shuffle(selectedWords);
    for (int i = 0; i < 25; i++) {
      words[i] = selectedWords.get(i);
    }

    for (int i = 0; i < 25; i++) {
      if (i < 9) {
        wordTypes[i] = WordType.RED;
      } else if (i < 17) {
        wordTypes[i] = WordType.BLUE;
      } else if (i < 24) {
        wordTypes[i] = WordType.BYSTANDER;
      } else {
        wordTypes[i] = WordType.ASSASSIN;
      }
    }
    Collections.shuffle(Arrays.asList(wordTypes));
  }

  public String[] getWords() {
    return words;
  }

  public WordType[] getWordTypes() {
    return wordTypes;
  }


  // copied from assignment 6
  private ArrayList<String> readFile(String fileName) {
    try (FileReader f = new FileReader(fileName);
         BufferedReader in = new BufferedReader(f)
    ) {
      ArrayList<String> result = new ArrayList<>();
      String ln = in.readLine();
      while (ln != null) {
        result.add(ln);
        ln = in.readLine();
      }
      return result;
    } catch (IOException e) {
      return null;
    }
  }

  private void writeObject(java.io.ObjectOutputStream out) throws IOException {
    out.defaultWriteObject();
    for (int i = 0; i < 25; i++) {
      out.writeObject(words[i]);
      out.writeObject(wordTypes[i]);
    }
  }

  private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
    in.defaultReadObject();
    for (int i = 0; i < 25; i++) {
      words[i] = (String) in.readObject();
      wordTypes[i] = (WordType) in.readObject();
    }
  }

}
