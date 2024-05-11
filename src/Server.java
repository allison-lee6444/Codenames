import java.awt.BorderLayout;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import game.Game;
import game.Move;
import game.Player;
import game.PlayerList;

import javax.swing.*;


public class Server extends JFrame {
  private JTextArea log;
  private ServerSocket serverSocket;
  private static int nextUserId = 1;
  private static int nextGameId = 1;
  private final HashMap<Integer, Game> gameTable = new HashMap<>();

  private class ClientReader implements Runnable { // producer
    private final int id;
    private final int gameId;
    private final Game game;
    private final ObjectInputStream inputFromClient;

    public ClientReader(ObjectInputStream inputFromClient, int id, int gameId) {
      this.inputFromClient = inputFromClient;
      this.id = id;
      this.gameId = gameId;
      this.game = gameTable.get(gameId);
    }

    public void run() {
      try {
        while (true) {
          Object input = inputFromClient.readObject();
          if (game.getState() == Game.State.WAITING) {
            PlayerList newStatus = (PlayerList) input;
            synchronized (game) {
              game.setPlayerList(newStatus);
              if (!newStatus.isJoining()) {
                game.start();
              }
              game.notifyAll();
            }
          } else {
            ArrayList<Move> newStatus = (ArrayList<Move>) input;
            synchronized (game) {
              game.setMoves(newStatus);
              game.notifyAll();
            }
          }
          Thread.sleep(50);
        }
      } catch (IOException | InterruptedException | ClassNotFoundException e) {
        e.printStackTrace();
      }

    }
  }

  private class ClientWriter implements Runnable { // consumer
    private final int id;
    private final int gameId;
    private final Game game;
    private final ObjectOutputStream outputToClient;

    public ClientWriter(ObjectOutputStream outputToClient, int id, int gameId) {
      this.outputToClient = outputToClient;
      this.id = id;
      this.gameId = gameId;
      this.game = gameTable.get(gameId);
    }

    public void run() {
      try {
        while (true) {
          synchronized (game) {
            game.wait();
            if (game.getMoves().isEmpty()) {
              outputToClient.writeObject(game.getPlayerList());
              outputToClient.flush();
              if (!game.getPlayerList().isJoining()) { // just started the game
                outputToClient.writeObject(game.getBoard());
              }
            } else
              outputToClient.writeObject(game.getMoves());
            outputToClient.flush();
          }
        }
      } catch (InterruptedException | IOException e) {
        e.printStackTrace();
      }
    }
  }

  public Server() {

    super("Codenames Server");
    initializeUI();

    Thread thread = new Thread(() -> {
      try {
        serverSocket = new ServerSocket(9898);
        log.append(String.format("Codenames server started at %s\n", new Date()));
        while (true) {
          Socket socket = serverSocket.accept();
          ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
          ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());

          String clientArgs = inputStream.readUTF();
          String clientUsername = clientArgs.split("\n")[0];
          String clientGameIdString = clientArgs.split("\n")[1];
          int clientGameId;
          log.append(String.format("Starting thread for client %d at %s\n", nextUserId, new Date()));
          log.append(String.format("Client %d's host name is %s\n Client %d's IP Address is %s\n",
                  nextUserId, socket.getInetAddress().getHostName(), nextUserId, socket.getInetAddress()));
          if (clientGameIdString.equals("\0")) {
            log.append(String.format("New game: %d\n", nextGameId));
            clientGameId = nextGameId;
            gameTable.put(nextGameId, new Game());
            outputStream.writeBoolean(true); // game id validity
            outputStream.flush();
          } else {
            clientGameId = Integer.parseInt(clientGameIdString);
            boolean isValid = gameTable.containsKey(clientGameId) &&
                    gameTable.get(clientGameId).getPlayerList().isJoining();
            outputStream.writeBoolean(isValid);
            outputStream.flush();
            if (!isValid) {
              log.append(String.format("Invalid game ID: %d\n", clientGameId));
              socket.close();
              continue;
            }

            log.append(String.format("Joining Game: %d\n", clientGameId));
          }
          Game currentGame = gameTable.get(clientGameId);
          Player currentPlayer = new Player(clientUsername, nextUserId, clientGameId);
          synchronized (currentGame) {
            currentGame.getPlayerList().addUnassigned(currentPlayer);
            outputStream.writeInt(clientGameId);
            outputStream.flush();
            outputStream.writeInt(nextUserId);
            outputStream.flush();
            outputStream.writeObject(currentGame.getPlayerList());
            outputStream.flush();
            currentGame.notifyAll();
          }
          ClientReader clientReader = new ClientReader(inputStream, nextUserId, clientGameId);
          ClientWriter clientWriter = new ClientWriter(outputStream, nextUserId, clientGameId);
          ++nextUserId;
          ++nextGameId;
          new Thread(clientReader).start();
          new Thread(clientWriter).start();
        }
      } catch (IOException ex) {
        ex.printStackTrace();
      }
    });
    thread.start();
  }

  private void initializeUI() {
    setSize(400, 400);

    JMenuBar menuBar = new JMenuBar();
    JMenu network = new JMenu("Network");
    JMenuItem exit = new JMenuItem("Exit");

    exit.addActionListener((e) -> System.exit(0));

    network.add(exit);
    menuBar.add(network);
    setJMenuBar(menuBar);

    log = new JTextArea();
    log.setEditable(false);
    add(log, BorderLayout.CENTER);
  }


  public static void main(String[] args) {
    Server server = new Server();
    server.setVisible(true);
    server.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }


}


