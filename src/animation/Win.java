package animation;
/**
 * Class that is called when User wins the game
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
import java.io.File;
import java.net.URL;

import javax.swing.JPanel;
import javax.swing.Timer;

public class Win extends JPanel implements ActionListener {

	public static final int DELAY = 100;
    
	//Constructor
	public Win() {
		constructMedia();
	}

	private Image wumpus, hero;
	private int x, y, a, b, tic, n;
	private AudioClip phatBeat;
	private Timer timer;

	//pulls media from directory
	private void constructMedia() {
		String path = System.getProperty("user.dir") + "/audio/";
		try {
			wumpus = Toolkit.getDefaultToolkit().getImage(
					"images" + File.separator + "alot.png");
		} catch (Exception e) {
			System.out.println("Problem loading image\nMessage: "
					+ e.getMessage());
		}

		try {
			hero = Toolkit.getDefaultToolkit().getImage(
					"images" + File.separator + "victory.gif");
		} catch (Exception e) {
			System.out.println("Problem loading image\nMessage: "
					+ e.getMessage());
		}

		try {
			phatBeat = Applet.newAudioClip(new URL("file:" + path
					+ "/dance.wav/"));
		} catch (Exception e) {
			System.out.println("Problem loading clip\nMessage: "
					+ e.getMessage());
		}
	}

	//draws images
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.drawImage(wumpus, a, b, this);
		g2.drawImage(hero, x, y, this);
	}

	//starts timer and animates images
	public void animate() {
		x = 220;
		y = 70;
		a = 140;
		b = 240;
		timer = new Timer(DELAY, this);
		timer.start();
		tic = 1;
		n = 300;
		phatBeat.play();
		repaint();
	}

	//stops timer and animates images
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (tic <= n) {
			tic++;
		} else {
			timer.stop();
			phatBeat.stop();
		}
	}
}