package ui;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;
import javax.swing.*;


public class Client extends JFrame implements Runnable {
  private Socket socket;
  private JTextArea chat;
  private JTextField input;


  public Client() {
    super("Codenames ui.Client");
    initializeUI();

  }

  public void run() {
    try {
      while (true) {
        DataInputStream inputFromServer = new DataInputStream(
                socket.getInputStream());
        String message = inputFromServer.readUTF();
        String[] messageParts = message.split(" ");
        int sender_id = Integer.parseInt(messageParts[0]);
        message = String.join(" ", Arrays.copyOfRange(messageParts, 1, messageParts.length));
        chat.append(String.format("%d: %s\n", sender_id, message));

        Thread.sleep(100);
      }
    } catch (IOException ex) {
      ex.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
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

    chat = new JTextArea();
    chat.setEditable(false);
    input = new JTextField();
    input.addActionListener((e) -> sendMessage());

  }

  private void sendMessage() {
    try {
      DataOutputStream outputToServer = new DataOutputStream(socket.getOutputStream());
      outputToServer.writeUTF(input.getText());
      chat.append(input.getText() + "\n");
      outputToServer.flush();
      input.setText("");
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

  public void connectServer(String ip, int port) {
    try {
      socket = new Socket(ip, port);
      Thread thread = new Thread(this);
      thread.start();
    } catch (IOException e1) {
      e1.printStackTrace();
    }
  }


  public static void main(String[] args) {
    Client client = new Client();
    client.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    client.setVisible(true);
  }
}