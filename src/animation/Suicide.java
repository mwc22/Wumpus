package animation;
/**
 * Animation when you hit yourself with an arrow
 * @author Bhavana Gorti
 * @author Patrick Martin
 * @author Michael Curtis
 */
import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.net.URL;

import javax.swing.JPanel;

public class Suicide extends JPanel {
	
	//Constructor
	public Suicide() {
		constructMedia();
	}

	private Image avatar;
	private int x, y;
	private AudioClip ouch;
	
	//pulls the media from directories
	private void constructMedia() {
		String path = System.getProperty("user.dir") + "/audio/";
		try {
			avatar = Toolkit.getDefaultToolkit().getImage("images" + File.separator
					+ "Suicide.gif");
		} catch (Exception e){
			System.out.println("Problem loading image\nMessage: " + e.getMessage());
		}
		
		
		try {
			ouch = Applet.newAudioClip(new URL("file:" + path + "/ouch.wav/"));
		} catch (Exception e) {
			System.out.println("Problem loading clip\nMessage: "
					+ e.getMessage());
		}
	}

	//Paint component to draw images and calls animate()
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.drawImage(avatar, x, y, this);
	}
	
	//animates the image
	public void animate() {
		x = 170;
		y = 100;
		ouch.play();
		repaint();
	}
}