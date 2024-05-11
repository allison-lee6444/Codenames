package ui;

import game.*;

import java.awt.*;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.swing.*;


public class Client extends JFrame implements Runnable {
  private Socket socket;
  private final Game game = new Game();
  private Player me;
  private ObjectInputStream istream;
  private ObjectOutputStream ostream;

  // ui
  private final Start start = new Start();
  private final WaitingRoom waitingRoom = new WaitingRoom();
  private final Play play = new Play();

  // manage ui interactions in the waiting room
  private class WaitingRoomInteractions {
    private final JButton redSpymasterButton = waitingRoom.getSelectRedSpymaster();
    private final JButton blueSpymasterButton = waitingRoom.getSelectBlueSpymaster();
    private final JButton redDetectiveButton = waitingRoom.getSelectRedDetective();
    private final JButton blueDetectiveButton = waitingRoom.getSelectBlueDetective();
    private final JButton startGameButton = waitingRoom.getStartGameButton();

    public WaitingRoomInteractions() {
      redSpymasterButton.addActionListener((e) -> {
        game.pickRole(me, Player.Team.RED, Player.Role.SPYMASTER);
        writeToServer();
      });
      blueSpymasterButton.addActionListener((e) -> {
        game.pickRole(me, Player.Team.BLUE, Player.Role.SPYMASTER);
        writeToServer();
      });
      redDetectiveButton.addActionListener((e) -> {
        game.pickRole(me, Player.Team.RED, Player.Role.DETECTIVE);
        writeToServer();
      });
      blueDetectiveButton.addActionListener((e) -> {
        game.pickRole(me, Player.Team.BLUE, Player.Role.DETECTIVE);
        writeToServer();
      });
      startGameButton.addActionListener((e) -> {
        game.start();
        writeToServer();
      });
    }

    public void writeToServer() {
      try {
        synchronized (game) {
          ostream.writeObject(game.getPlayerList());
          ostream.flush();
        }
      } catch (IOException e) {
        e.printStackTrace();
      }

    }
  }

  // manage interactions when playing
  private class PlayInteractions {
    private JButton[] buttons = play.getButtons();
    private JButton submitButton = play.getSubmitButton();
    private JTextField hintField = play.getHintField();
    private JTextField numField = play.getNumField();

    public PlayInteractions() {
      for (int i = 0; i < buttons.length; i++) {
        int finalI = i;
        buttons[i].addActionListener((e) -> {
          if (me.getRole() == Player.Role.SPYMASTER) {
            return;
          }
          if (me.getTeam() == Player.Team.RED && game.getPhase() != Game.Phase.RedGuess) {
            return;
          }
          if (me.getTeam() == Player.Team.BLUE && game.getPhase() != Game.Phase.BlueGuess) {
            return;
          }
          game.addMove(new Guess(me, finalI));
          writeToServer();
        });
      }
      submitButton.addActionListener((e) -> {
        if (me.getRole() != Player.Role.SPYMASTER) {
          return;
        }
        if (me.getTeam() == Player.Team.RED && game.getPhase() != Game.Phase.RedHint) {
          return;
        }
        if (me.getTeam() == Player.Team.BLUE && game.getPhase() != Game.Phase.BlueHint) {
          return;
        }
        if (hintField.getText().contains(" ") || hintField.getText().isEmpty()) {
          JOptionPane.showMessageDialog(null, "Hint needs to be in one word");
          return;
        }
        try {
          int num = Integer.parseInt(numField.getText());
          if (num < 1) {
            JOptionPane.showMessageDialog(null, "Please make sure your number is larger than 1.");
            return;
          }
        } catch (NumberFormatException ex) {
          JOptionPane.showMessageDialog(null, "Please make sure your number is an integer.");
          return;
        }
        game.addMove(new Hint(me, hintField.getText(), Integer.parseInt(numField.getText())));
        writeToServer();
        hintField.setText("");
        numField.setText("");
      });

    }

    public void writeToServer() {
      try {
        synchronized (game) {
          ostream.writeObject(game.getMoves());
          ostream.flush();
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public Client() {
    super("Codenames Client");
    initializeUI();
  }

  // repeatedly read from server and update the ui
  public void run() {
    try {
      while (true) {
        if (game.getState() == Game.State.WAITING) {
          PlayerList newPlayerList = (PlayerList) istream.readObject();
          if (!newPlayerList.isJoining()) { // switching to playing screen
            Board board = (Board) istream.readObject();
            remove(waitingRoom.getWaitingRoomPanel());
            add(play.getPlayPanel(), BorderLayout.CENTER);
            game.start();
            game.setBoard(board);
            play.setBoard(me, board);
            new PlayInteractions();
            revalidate();
            repaint();
          }
          game.setPlayerList(newPlayerList);
          waitingRoom.setPlayerList(newPlayerList);
          waitingRoom.getStartGameButton().setEnabled(newPlayerList.isReady());
        } else {
          ArrayList<Move> moves = (ArrayList<Move>) istream.readObject();
          game.setMoves(moves);
          applyMoves(moves);
        }
        Thread.sleep(100);
      }
    } catch (IOException | InterruptedException | ClassNotFoundException ex) {
      ex.printStackTrace();
    }
  }

  // initialize ui and manages the start screen interactions
  private void initializeUI() {
    setSize(800, 550);

    JMenuBar menuBar = new JMenuBar();
    JMenu gameMenu = new JMenu("Game");
    JMenuItem exit = new JMenuItem("Exit");


    exit.addActionListener((e) -> {
      try {
        if (socket != null) {
          socket.close();
        }
        System.exit(0);
      } catch (IOException ex) {
        System.out.println("Error on exit: ");
        ex.printStackTrace();
      }
    });

    gameMenu.add(exit);
    menuBar.add(gameMenu);
    setJMenuBar(menuBar);
    add(start.getPanel(), BorderLayout.CENTER);

    JButton joinExistingGameButton = start.getJoinExistingGameButton();
    JButton newGameButton = start.getNewGameButton();
    JTextField gameid = start.getGameid();
    JTextField username = start.getUsername();

    joinExistingGameButton.addActionListener((e) -> {
      if (username.getText().equals("") || gameid.getText().equals("")) {
        JOptionPane.showMessageDialog(null, "You must fill out the username and game ID to join a game.");
        return;
      }
      if (username.getText().length() > 10) {
        JOptionPane.showMessageDialog(null, "Please make sure your username is shorter than 10 characters.");
        return;
      }
      try {
        Integer.parseInt(gameid.getText());
      } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(null, "Please make sure your game ID is an integer.");
        return;
      }

      connectServer(username.getText(), gameid.getText());
    });

    newGameButton.addActionListener((e) -> {
      if (username.getText().equals("")) {
        JOptionPane.showMessageDialog(null, "You must fill out the username to start a new game.");
      }
      if (username.getText().length() > 20) {
        JOptionPane.showMessageDialog(null, "Please make sure your username is shorter than 20 characters.");
        return;
      }
      connectServer(username.getText(), null);
    });
  }


  // connect to server
  public void connectServer(String username, String gameid) {
    try {
      socket = new Socket("localhost", 9898);

      ostream = new ObjectOutputStream(socket.getOutputStream());

      ostream.writeUTF(username + "\n" + ((gameid == null) ? "\0" : gameid));
      ostream.flush();

      istream = new ObjectInputStream(socket.getInputStream());
      boolean isValid = istream.readBoolean();

      if (!isValid) {
        JOptionPane.showMessageDialog(null, "Invalid game ID!");
        socket.close();
        return;
      }

      int gameId = istream.readInt();
      int userId = istream.readInt();
      PlayerList currentList = (PlayerList) istream.readObject();
      game.setPlayerList(currentList);
      game.setId(gameId);
      me = new Player(username, userId, gameId);
      waitingRoom.setGameId(gameId);
      waitingRoom.setPlayerList(game.getPlayerList());
      if (game.getPlayerList().size() != 1) { // only first person gets to start
        waitingRoom.getStartGameButton().setVisible(false);
      }
      waitingRoom.getStartGameButton().setEnabled(false); // new member always prevent start until assigned role

      remove(start.getPanel());
      add(waitingRoom.getWaitingRoomPanel(), BorderLayout.CENTER);
      new WaitingRoomInteractions();

      revalidate();
      repaint();

      Thread thread = new Thread(this);
      thread.start();
    } catch (IOException | ClassNotFoundException e1) {
      e1.printStackTrace();
      JOptionPane.showMessageDialog(null, "We could not connect to the server. Please try again.");
    }
  }

  // show current state to ui by going thru moves
  private void applyMoves(ArrayList<Move> moves) {
    String lastHint = "Waiting...";
    JLabel hint = play.getHint();
    JLabel redRemain = play.getRedRemain();
    JLabel blueRemain = play.getBlueRemain();
    JLabel turnLabel = play.getTurnLabel();
    JButton[] buttons = play.getButtons();
    HashMap<Board.WordType, Color> colorMap = play.getColorMap();
    int redRemainCount = 9;
    int blueRemainCount = 8;

    // record end state
    for (Move move : moves) {
      if (move.getMoveType() == Move.MoveType.HINT) {
        lastHint = String.format("%s/%d", ((Hint) move).getWord(), ((Hint) move).getNumber());
      } else {
        int wordGuessed = ((Guess) move).getWordId();
        Board.WordType wordGuessedType = game.getBoard().getWordTypes()[wordGuessed];
        buttons[wordGuessed].setBackground(colorMap.get(wordGuessedType));
        buttons[wordGuessed].setForeground(new Color(255, 255, 255));

        if (wordGuessedType == Board.WordType.RED) {
          redRemainCount--;
        } else if (wordGuessedType == Board.WordType.BLUE) {
          blueRemainCount--;
        }
      }
    }

    // set labels text according to end state
    if (moves.get(moves.size() - 1).getMoveType() == Move.MoveType.GUESS &&
            (game.getPhase() == Game.Phase.RedHint || game.getPhase() == Game.Phase.BlueHint)) {
      lastHint = "Waiting...";
    }

    hint.setText("Hint: " + lastHint);
    redRemain.setText("Red: " + redRemainCount);
    blueRemain.setText("Blue: " + blueRemainCount);
    Game.Phase currentPhase = game.getPhase();
    if (redRemainCount == 0) {
      currentPhase = Game.Phase.RedWin;
    } else if (blueRemainCount == 0) {
      currentPhase = Game.Phase.BlueWin;
    }
    switch (currentPhase) {
      case RedHint:
        turnLabel.setText("Red Spymaster is giving hints");
        turnLabel.setForeground(colorMap.get(Board.WordType.RED));
        break;
      case BlueHint:
        turnLabel.setText("Blue Spymaster is giving hints");
        turnLabel.setForeground(colorMap.get(Board.WordType.BLUE));
        break;
      case RedGuess:
        turnLabel.setText("Red Detectives are guessing");
        turnLabel.setForeground(colorMap.get(Board.WordType.RED));
        break;
      case BlueGuess:
        turnLabel.setText("Blue Detectives are guessing");
        turnLabel.setForeground(colorMap.get(Board.WordType.BLUE));
        break;
      case RedWin:
        turnLabel.setText("Red Win");
        turnLabel.setForeground(colorMap.get(Board.WordType.RED));
        JOptionPane.showMessageDialog(null, "Red Win");
        break;
      case BlueWin:
        turnLabel.setText("Blue Win");
        turnLabel.setForeground(colorMap.get(Board.WordType.BLUE));
        JOptionPane.showMessageDialog(null, "Blue Win");
        break;
    }


  }

  public static void main(String[] args) {
    Client client = new Client();
    client.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    client.setVisible(true);
  }
}
