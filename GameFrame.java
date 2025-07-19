import javax.swing.JFrame;
import javax.swing.JLabel;

public class GameFrame extends JFrame {
  GameFrame() {
    GamePanel panel = new GamePanel();
    this.setSize(GamePanel.PANELSIZE + 2, GamePanel.PANELSIZE + 65);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.add(panel);
    this.setVisible(true);
  }
}
