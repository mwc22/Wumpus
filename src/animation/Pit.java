package animation;
/**
 * Class that gives animation when you fall into a pit
 * @author Bhavana Gorti
 * @author Patrick Martin
 * @author Michael Curtis
 */
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import javax.swing.JPanel;

public class Pit extends JPanel {

  //Constructor
  public Pit() {
    constructMedia();
  }

  private Image avatar;
  private int x, y;

  //pulls media from the directories
  private void constructMedia() {
    String path = System.getProperty("user.dir") + "/images/";
    avatar = Toolkit.getDefaultToolkit().getImage(path + "pit.gif");
  }
  
  //draws images
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2 = (Graphics2D) g;
    g2.drawImage(avatar, x, y, this);
  }
  
  //animates images
  public void animate() {
    x = 110;
    y = 100;
    repaint();
  }
}