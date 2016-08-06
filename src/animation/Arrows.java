package animation;
/**
 * Class that has animations for when you lose because you run out of arrows
 * @author Bhavana Gorti
 * @author Patrick Martin
 * @author Michael Curtis
 */
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import javax.swing.JPanel;

public class Arrows extends JPanel {
  
  //Constructor
  public Arrows() {
    constructMedia();
  }

  private Image avatar;
  private int x, y;
  
  //pulls media from the directory
  private void constructMedia() {
    String path = System.getProperty("user.dir") + "/images/";
    avatar = Toolkit.getDefaultToolkit().getImage(path + "arrows.jpg");
  }
  
  //draws images
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2 = (Graphics2D) g;
    g2.drawImage(avatar, x, y, this);
  }

  //sets location of image
  public void animate() {
    x = 110;
    y = 100;
    repaint();
  }
}