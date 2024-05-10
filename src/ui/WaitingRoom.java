package ui;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.util.Locale;

public class WaitingRoom {
  private JPanel WaitingRoomPanel;
  private JPanel Red;
  private JPanel Blue;
  private JLabel redTitle;
  private JLabel blueTitle;
  private JButton selectRoleButton2;
  private JButton selectRoleButton3;
  private JTable redDetectives;
  private JTable table1;
  private JButton selectRoleButton1;
  private JButton selectRoleButton;
  private JButton startGameButton;

  public JPanel getWaitingRoomPanel() {
    return WaitingRoomPanel;
  }

  {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
    $$$setupUI$$$();
  }

  /**
   * Method generated by IntelliJ IDEA GUI Designer
   * >>> IMPORTANT!! <<<
   * DO NOT edit this method OR call it in your code!
   *
   * @noinspection ALL
   */
  private void $$$setupUI$$$() {
    WaitingRoomPanel = new JPanel();
    WaitingRoomPanel.setLayout(new GridBagLayout());
    WaitingRoomPanel.setMinimumSize(new Dimension(800, 800));
    WaitingRoomPanel.setPreferredSize(new Dimension(800, 400));
    Blue = new JPanel();
    Blue.setLayout(new GridBagLayout());
    Blue.setBackground(new Color(-9994047));
    Blue.setMinimumSize(new Dimension(350, 400));
    Blue.setPreferredSize(new Dimension(400, 400));
    GridBagConstraints gbc;
    gbc = new GridBagConstraints();
    gbc.gridx = 2;
    gbc.gridy = 1;
    gbc.fill = GridBagConstraints.BOTH;
    WaitingRoomPanel.add(Blue, gbc);
    blueTitle = new JLabel();
    Font blueTitleFont = this.$$$getFont$$$(null, -1, 64, blueTitle.getFont());
    if (blueTitleFont != null) blueTitle.setFont(blueTitleFont);
    blueTitle.setText("Team Blue");
    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.gridwidth = 5;
    gbc.anchor = GridBagConstraints.NORTHWEST;
    Blue.add(blueTitle, gbc);
    final JLabel label1 = new JLabel();
    Font label1Font = this.$$$getFont$$$(null, -1, 24, label1.getFont());
    if (label1Font != null) label1.setFont(label1Font);
    label1.setText("Spymaster:");
    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 2;
    gbc.gridwidth = 4;
    gbc.anchor = GridBagConstraints.WEST;
    Blue.add(label1, gbc);
    final JLabel label2 = new JLabel();
    label2.setFocusable(true);
    Font label2Font = this.$$$getFont$$$(null, -1, 24, label2.getFont());
    if (label2Font != null) label2.setFont(label2Font);
    label2.setText("Detectives");
    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 4;
    gbc.gridwidth = 3;
    gbc.anchor = GridBagConstraints.WEST;
    Blue.add(label2, gbc);
    selectRoleButton3 = new JButton();
    selectRoleButton3.setText("Select Role");
    gbc = new GridBagConstraints();
    gbc.gridx = 3;
    gbc.gridy = 4;
    gbc.gridwidth = 2;
    gbc.anchor = GridBagConstraints.EAST;
    Blue.add(selectRoleButton3, gbc);
    table1 = new JTable();
    table1.setBackground(new Color(-9994047));
    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 5;
    gbc.gridwidth = 5;
    gbc.fill = GridBagConstraints.BOTH;
    Blue.add(table1, gbc);
    selectRoleButton = new JButton();
    selectRoleButton.setText("Select Role");
    gbc = new GridBagConstraints();
    gbc.gridx = 4;
    gbc.gridy = 2;
    gbc.anchor = GridBagConstraints.EAST;
    Blue.add(selectRoleButton, gbc);
    final JLabel label3 = new JLabel();
    label3.setText("");
    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 3;
    gbc.anchor = GridBagConstraints.WEST;
    Blue.add(label3, gbc);
    final JSeparator separator1 = new JSeparator();
    separator1.setForeground(new Color(-9994047));
    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 1;
    gbc.gridwidth = 4;
    gbc.fill = GridBagConstraints.BOTH;
    gbc.ipady = 50;
    Blue.add(separator1, gbc);
    final JSeparator separator2 = new JSeparator();
    gbc = new GridBagConstraints();
    gbc.gridx = 2;
    gbc.gridy = 3;
    gbc.gridwidth = 3;
    gbc.fill = GridBagConstraints.BOTH;
    gbc.ipady = 30;
    Blue.add(separator2, gbc);
    final JPanel spacer1 = new JPanel();
    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 1;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.ipadx = 20;
    WaitingRoomPanel.add(spacer1, gbc);
    Red = new JPanel();
    Red.setLayout(new GridBagLayout());
    Red.setBackground(new Color(-3119001));
    Red.setMinimumSize(new Dimension(350, 400));
    Red.setPreferredSize(new Dimension(400, 400));
    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.fill = GridBagConstraints.BOTH;
    WaitingRoomPanel.add(Red, gbc);
    final JLabel label4 = new JLabel();
    Font label4Font = this.$$$getFont$$$(null, -1, 24, label4.getFont());
    if (label4Font != null) label4.setFont(label4Font);
    label4.setText("Spymaster:");
    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 2;
    gbc.gridwidth = 3;
    gbc.anchor = GridBagConstraints.WEST;
    Red.add(label4, gbc);
    final JLabel label5 = new JLabel();
    Font label5Font = this.$$$getFont$$$(null, -1, 24, label5.getFont());
    if (label5Font != null) label5.setFont(label5Font);
    label5.setText("Detectives");
    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 5;
    gbc.gridwidth = 2;
    gbc.anchor = GridBagConstraints.WEST;
    Red.add(label5, gbc);
    selectRoleButton2 = new JButton();
    selectRoleButton2.setText("Select Role");
    gbc = new GridBagConstraints();
    gbc.gridx = 2;
    gbc.gridy = 5;
    gbc.gridwidth = 2;
    gbc.anchor = GridBagConstraints.EAST;
    Red.add(selectRoleButton2, gbc);
    redDetectives = new JTable();
    redDetectives.setAutoCreateColumnsFromModel(false);
    redDetectives.setBackground(new Color(-3119001));
    redDetectives.setGridColor(new Color(-15460304));
    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 6;
    gbc.gridwidth = 4;
    gbc.fill = GridBagConstraints.BOTH;
    Red.add(redDetectives, gbc);
    redTitle = new JLabel();
    redTitle.setAlignmentY(0.5f);
    Font redTitleFont = this.$$$getFont$$$(null, -1, 64, redTitle.getFont());
    if (redTitleFont != null) redTitle.setFont(redTitleFont);
    redTitle.setText("Team Red");
    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.gridwidth = 4;
    gbc.anchor = GridBagConstraints.NORTH;
    Red.add(redTitle, gbc);
    selectRoleButton1 = new JButton();
    selectRoleButton1.setText("Select Role");
    gbc = new GridBagConstraints();
    gbc.gridx = 3;
    gbc.gridy = 2;
    gbc.anchor = GridBagConstraints.EAST;
    Red.add(selectRoleButton1, gbc);
    final JLabel label6 = new JLabel();
    label6.setText("");
    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 4;
    gbc.anchor = GridBagConstraints.WEST;
    Red.add(label6, gbc);
    final JSeparator separator3 = new JSeparator();
    separator3.setForeground(new Color(-3119001));
    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 1;
    gbc.gridwidth = 3;
    gbc.fill = GridBagConstraints.BOTH;
    gbc.ipady = 50;
    Red.add(separator3, gbc);
    final JSeparator separator4 = new JSeparator();
    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 3;
    gbc.gridwidth = 3;
    gbc.fill = GridBagConstraints.BOTH;
    gbc.ipady = 30;
    Red.add(separator4, gbc);
    final JPanel panel1 = new JPanel();
    panel1.setLayout(new GridBagLayout());
    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 2;
    gbc.gridwidth = 3;
    gbc.fill = GridBagConstraints.BOTH;
    WaitingRoomPanel.add(panel1, gbc);
    startGameButton = new JButton();
    startGameButton.setText("Start Game");
    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.insets = new Insets(10, 0, 0, 0);
    panel1.add(startGameButton, gbc);
    final JPanel spacer2 = new JPanel();
    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 0;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    panel1.add(spacer2, gbc);
    final JPanel spacer3 = new JPanel();
    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.fill = GridBagConstraints.VERTICAL;
    panel1.add(spacer3, gbc);
    final JLabel label7 = new JLabel();
    Font label7Font = this.$$$getFont$$$(null, -1, 24, label7.getFont());
    if (label7Font != null) label7.setFont(label7Font);
    label7.setText("Invite your friends to this game with ID: ");
    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.gridwidth = 3;
    gbc.ipady = 10;
    WaitingRoomPanel.add(label7, gbc);
  }

  /**
   * @noinspection ALL
   */
  private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
    if (currentFont == null) return null;
    String resultName;
    if (fontName == null) {
      resultName = currentFont.getName();
    } else {
      Font testFont = new Font(fontName, Font.PLAIN, 10);
      if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
        resultName = fontName;
      } else {
        resultName = currentFont.getName();
      }
    }
    Font font = new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
    boolean isMac = System.getProperty("os.name", "").toLowerCase(Locale.ENGLISH).startsWith("mac");
    Font fontWithFallback = isMac ? new Font(font.getFamily(), font.getStyle(), font.getSize()) : new StyleContext().getFont(font.getFamily(), font.getStyle(), font.getSize());
    return fontWithFallback instanceof FontUIResource ? fontWithFallback : new FontUIResource(fontWithFallback);
  }

  /**
   * @noinspection ALL
   */
  public JComponent $$$getRootComponent$$$() {
    return WaitingRoomPanel;
  }

}
