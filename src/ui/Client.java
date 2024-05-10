package ui;

import game.*;

import java.awt.*;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
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

  public Client() {
    super("Codenames ui.Client");
    initializeUI();

  }

  public void run() {
    try {
      while (true) {
        if (game.getState() == Game.State.WAITING) {
          PlayerList newPlayerList = (PlayerList) istream.readObject();
          if (!newPlayerList.isJoining()) {
            remove(waitingRoom.getWaitingRoomPanel());
            add(play.getPlayPanel(), BorderLayout.CENTER);
            game.start();
            revalidate();
            repaint();
          }
          game.setPlayerList(newPlayerList);
          waitingRoom.setPlayerList(newPlayerList);
          waitingRoom.getStartGameButton().setEnabled(newPlayerList.isReady());
        } else {
          ArrayList<Move> moves = (ArrayList<Move>) istream.readObject();
          game.setMoves(moves);
        }
        Thread.sleep(100);
      }
    } catch (IOException | InterruptedException | ClassNotFoundException ex) {
      ex.printStackTrace();
    }
  }

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

  private void applyMove(Move move) {
    Game.State state = game.getState();
    if (state == Game.State.WAITING) {
      return;
    }
  }

  public static void main(String[] args) {
    Client client = new Client();
    client.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    client.setVisible(true);
  }
}
