import java.awt.BorderLayout;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;

import javafx.util.Pair;

import javax.swing.*;


public class Server extends JFrame {
  private JTextArea log;
  private ServerSocket serverSocket;
  private static int nextId = 1;
  private final ArrayList<Pair<Integer, String>> messages = new ArrayList<>();

  private class ClientReader implements Runnable { // producer
    private final Socket socket;
    private final int id;

    public ClientReader(Socket socket, int id) {
      this.socket = socket;
      this.id = id;
    }

    public void run() {
      try {
        while (true) {
          DataInputStream inputFromClient = new DataInputStream(
                  socket.getInputStream());
          String message = inputFromClient.readUTF();
          synchronized (messages) {
            messages.add(new Pair<>(id, message));
            messages.notifyAll();
          }
          Thread.sleep(50);
        }
      } catch (IOException | InterruptedException e) {
        e.printStackTrace();
      }

    }
  }

  private class ClientWriter implements Runnable { // consumer
    private final Socket socket;
    private final int id;
    private int nextMessageId;

    public ClientWriter(Socket socket, int id) {
      this.socket = socket;
      this.id = id;
      try {
        synchronized (messages) { // load all previous messages prior to connection
          DataOutputStream outputToClient = new DataOutputStream(socket.getOutputStream());
          for (Pair<Integer, String> message : messages) {
            String pkt = String.format("%d %s", message.getKey(), message.getValue());
            outputToClient.writeUTF(pkt);
          }
          nextMessageId = messages.size();
        }
      } catch (IOException e) {
        e.printStackTrace();
        log.append("connection failure with client " + id + "\n");
      }
    }

    public void run() {
      try {
        DataOutputStream outputToClient = new DataOutputStream(socket.getOutputStream());
        while (true) {
          synchronized (messages) {
            messages.wait();
            for (int i = nextMessageId; i < messages.size(); i++) {
              Pair<Integer, String> message = messages.get(i);
              if (message.getKey() == id) {
                continue;
              }
              outputToClient.writeUTF(
                      String.format("%d %s", message.getKey(), message.getValue()));
            }
            nextMessageId = messages.size();
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

          ClientReader clientReader = new ClientReader(socket, nextId);
          ClientWriter clientWriter = new ClientWriter(socket, nextId);
          log.append(String.format("Starting thread for client %d at %s\n", nextId, new Date()));
          log.append(String.format("ui.Client %d's host name is %s\n ui.Client %d's IP Address is %s\n",
                  nextId, socket.getInetAddress().getHostName(), nextId, socket.getInetAddress()));
          ++nextId;
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


