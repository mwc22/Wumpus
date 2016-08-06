package animation;
/**
 * Animation class for when killed by Wumpus
 * @author Patrick Martin
 * @author Bhavana Gorti
 * @author Michael Curtis
 */
import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.JPanel;
import javax.swing.Timer;

public class Wumpus extends JPanel implements ActionListener {

	public static final int DELAY = 100;
	
	//Constructor
	public Wumpus() {
		constructMedia();
	}

	private Image avatar;
	private int x, y, tic, n;
	private Timer timer;
	private AudioClip gameOver;

	//pulls media from directories
	private void constructMedia() {
		String path = System.getProperty("user.dir") + "/audio/";
		String imgPath = System.getProperty("user.dir") + "/images/";
		avatar = Toolkit.getDefaultToolkit().getImage(
				imgPath + "Wumpus.jpg");
		System.out.println(System.getProperty("user.dir"));

		// try {
		// mamamia = Applet.newAudioClip(new URL("file:" + path +
		// "/gameOver.mp3/"));
		// } catch (Exception e) {
		// System.out.println("Problem loading clip\nMessage: " +
		// e.getMessage());
		// }

		try {
			gameOver = Applet.newAudioClip(new URL("file:" + path
					+ "/gameOver.wav/"));
		} catch (Exception e) {
			System.out.println("Problem loading clip\nMessage: "
					+ e.getMessage());
		}
	}

	//draws images and calls animate()
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		if (timer.isRunning())
			g2.drawImage(avatar, x, y, 1000, 500, this);
	}

	//animates images
	public void animate() {
		x = -1000;
		y = 50;
		timer = new Timer(DELAY, this);
		timer.start();
		tic = 1;
		n = 2000;
		gameOver.play();
	}

	//animates images and stops timer
	public void actionPerformed(ActionEvent arg0) {
		if (tic <= n) {
			x = x + 20;
			// y = y + 5;
			repaint();
			tic++;
		} else {
			timer.stop();
			repaint();
			gameOver.stop();
		}
	}
}