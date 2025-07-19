import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;
import java.util.Random;;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.BorderFactory;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GamePanel extends JPanel implements ActionListener {
  public static final int PANELSIZE = 200;
  static final int[] DX = {0, 1, 0, -1};
  static final int[] DY = {1, 0, -1, 0};
  static final int D = 0;
  static final int R = 1;
  static final int U = 2;
  static final int L = 3;
  static final int BOXLEN = 10;
  static final int BOARDLEN = PANELSIZE/BOXLEN;
  static final Random random = new Random();

  int[][] heights;
  ArrayList<int[]> snake;
  TreeSet<int[]> blanks;
  int speed = 0;
  double startInt = 200;
  Timer timer;
  int dir = R;
  int stagedDir = dir;
  int[] snack;
  boolean chance = true;
  int score = 0;
  boolean gg = false;

  GamePanel() {
    heights = new int[BOARDLEN][BOARDLEN];
    snake = new ArrayList<int[]>();
    blanks = new TreeSet<int[]>(
      (a, b) -> {
        if (a[0] != b[0]) {
          return a[0] - b[0];
        } else if (a[1] != b[1]) {
          return a[1] - b[1];
        } else {
          return a[2] - b[2]; 
        }
      }
    );
    for (int i = 0; i < BOARDLEN; ++i) {
      for (int k = 0; k < BOARDLEN; ++k) {
        if (0 <= i && i < 15 && k == 10) {
          snake.add(new int[]{i, k});
          heights[i][k]++;
        } else {
          blanks.add(new int[]{i, k, 0});
          blanks.add(new int[]{i, k, 1});
        }
      }
    }

    snack = newSnack();
    blanks.remove(snack);
     
    this.setBackground(Color.BLUE);
    this.setBorder(BorderFactory.createLineBorder(Color.ORANGE));
    this.setMaximumSize(new Dimension(PANELSIZE, PANELSIZE));
    this.setFocusable(true);
    this.addKeyListener(new MyKeyAdapter());
    timer = new Timer((int) startInt, this);
    timer.start();
  }

  private int[] newSnack() {
    int[] find = new int[] {
      random.nextInt(BOARDLEN),
      random.nextInt(BOARDLEN),
      random.nextInt(2)
    };
    int[] snack = blanks.floor(find);
    if (snack == null) {
      snack = blanks.ceiling(find);
    }
    return snack;
  }

  private static int getNextInterval(double y, int x) {
    return (int) (
      40.0 + y*2.0 
      / (1.0 + Math.pow(Math.E, 0.2*(double)(x + 1)))
    );
  }

  @Override
  public void paintComponent(Graphics graphics) {
    super.paintComponent(graphics);
  
    graphics.setColor(Color.BLACK);
    graphics.fillRect(0, PANELSIZE, PANELSIZE, 4*BOXLEN);
    
    graphics.setColor(Color.WHITE);
    graphics.drawString("Score: " + score, BOXLEN*3, PANELSIZE + 2*BOXLEN); 

    for(int[] square : snake) {
      switch (heights[square[0]][square[1]]) {
        case 1:
          graphics.setColor(Color.ORANGE);
          break;
        case 2:
          graphics.setColor(Color.YELLOW);
          break;
        case 3:
          graphics.setColor(Color.WHITE);
          break;
      }    
      graphics.fillRect(  
        square[0]*(BOXLEN), 
        square[1]*(BOXLEN), 
        BOXLEN, 
        BOXLEN
      );;
    }
    
    if (snack[2] > 0) {
        graphics.setColor(Color.YELLOW);
    } else {
        graphics.setColor(Color.ORANGE);
    }

    graphics.fillOval(
      snack[0]*(BOXLEN),  
      snack[1]*(BOXLEN), 
      BOXLEN, 
      BOXLEN
    );

    if (gg) {
      graphics.setColor(Color.BLACK);
      graphics.fillRect(4*BOXLEN, PANELSIZE/2, 10*BOXLEN, 4*BOXLEN); 
      graphics.setColor(Color.WHITE);
      graphics.drawString("Game Over", 5*BOXLEN, PANELSIZE/2 + 2*BOXLEN); 
    }
  }

  @Override
  public void actionPerformed(ActionEvent arg0) {
    dir = stagedDir;

    int[] head = snake.get(snake.size() - 1);
    int x = head[0] + DX[dir];
    int y = head[1] + DY[dir];
    if (x < 0 || y < 0 || x >= BOARDLEN || y >= BOARDLEN) {
      timer.stop();
      if (chance) {
        timer = new Timer(200, this);
        timer.start();
        chance = false;
        return;
      } else {
        gg = true;
        repaint();
        return;
      } 
    }

    if (!chance) {
      int nextInt = getNextInterval(startInt, speed);

      timer.stop();
      timer = new Timer(nextInt, this);
      timer.start();
      chance = true;
    }

    snake.add(new int[]{x, y});
    heights[x][y]++;
    blanks.remove(new int[]{x, y, heights[x][y] - 1});

    if (x == snack[0] && y == snack[1] && snack[2] == heights[x][y] - 1) {
      score += snack[2] == 0 ? 10 : 15;;
      snack = newSnack();
      blanks.remove(snack);

      int nextInt = getNextInterval(startInt, speed);

      timer.stop();
      timer = new Timer(nextInt, this);
      timer.start();
      
      ++speed;
    } else {
      int[] remove = snake.remove(0);
      heights[remove[0]][remove[1]]--;
      blanks.add(
        new int[]{remove[0], remove[1], heights[remove[0]][remove[1]]}
      );
    }

    repaint();
  }

  public class MyKeyAdapter extends KeyAdapter {
    @Override
    public void keyPressed(KeyEvent e) {
      switch (e.getKeyCode()) {
        case KeyEvent.VK_LEFT:
          if (dir != R) stagedDir = L; 
          break;
        case KeyEvent.VK_RIGHT:
          if (dir != L) stagedDir = R;
          break;
        case KeyEvent.VK_UP:
          if (dir != D) stagedDir = U;
          break;
        case KeyEvent.VK_DOWN:
          if (dir != U) stagedDir = D; 
          break;
      }

      repaint();
    }
  }
}
