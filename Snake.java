import javax.swing.SwingUtilities;

public class Snake {
  public static void main(String... args) {
    SwingUtilities.invokeLater(() -> {
      new GameFrame();
    });
  }
}
