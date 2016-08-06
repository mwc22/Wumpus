package view;

/**
 * Main WumpusGUI
 * @author Bhavana Gorti
 * @author Patrick Martin
 * @author Michael Curtis
 */
import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import animation.Suicide;
import animation.Pit;
import animation.Arrows;
import animation.Wumpus;
import animation.Win;

import javax.swing.Timer;

import model.GameMap;

public class WumpusGUI extends JFrame implements Observer {

	//Creates the JFrame and creates the Introduction
	public static void main(String[] args) {
		JFrame aWindow = new WumpusGUI();
		aWindow.setVisible(true);
		JOptionPane
				.showMessageDialog(
						null,
						wrapText(
								45,
								"There's a Wumpus 'round this cave here who's been killing the local villagers! You have been tasked to explore the cave and kill the Wumpus. You have three arrows; if you run out of arrows, you die. If you happen upon a bottomless pit, you die. If you mistakenly enter the Wumpus's room, you die. Be careful of bats, they sometimes move you to other rooms. So keep a sharp eye and a ready bow, and godspeed."));
	}

	//Wraps text for the introduction
	private static String wrapText(int limit, String str) {
		String wrap = "";
		int lastEnding = 0;
		int prevSpace = 0;
		int count = 0;
		for (int i = 0; i < str.length(); i++) {
			count++;
			if (str.charAt(i) == ' ' || str.charAt(i) == '\n') {
				prevSpace = i;
			}
			if (str.charAt(i) == '\n')
				count = 0;
			if (count > limit) {
				wrap += str.substring(lastEnding, prevSpace) + "\n";
				count = 0;
				lastEnding = prevSpace + 1;
			}
		}
		wrap += str.substring(lastEnding);
		return wrap;
	}

	private GameMap game;
	private JPanel textPanel;
	private JPanel graphicPanel;
	private JPanel buttons = new JPanel();
	private JButton newGame = new JButton("New Game");
	private JButton instructions = new JButton("Instructions");
	private JRadioButton graphicView = new JRadioButton("Graphical View", true);
	private JRadioButton textView = new JRadioButton("Text View", false);
	private ButtonGroup buttonGroup = new ButtonGroup();
	private JPanel radioPanel = new JPanel(new GridLayout(0, 1));

	//Constructor of WumpusGUI
	public WumpusGUI() {
		registerListeners();
		setupWindow();
	}
	
	//sets up the Window for the Game
	private void setupWindow() {
		this.setTitle("Hunt the Wumpus");
		this.setSize(700, 600);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		game = new GameMap();
		buttonGroup.add(graphicView);
		buttonGroup.add(textView);
		radioPanel.add(graphicView);
		radioPanel.add(textView);
		buttons.add(radioPanel);
		buttons.add(newGame);
		buttons.add(instructions);
		buttons.setAlignmentX(Component.CENTER_ALIGNMENT);
		this.add(BorderLayout.NORTH, buttons);
		graphicPanel = new GraphicalView(game);
		graphicPanel.setSize(700, 600);
		graphicPanel.setLocation(0, 100);
		textPanel = new TextView(game);
		if (textView.isSelected()) {
			graphicPanel.setVisible(false);
			textPanel.setVisible(true);
		} else {
			textPanel.setVisible(false);
			graphicPanel.setVisible(true);
		}
		this.add(graphicPanel);
		this.add(textPanel);
		game.addObserver(this);
	} // end of class setupWindow

	private void registerListeners() {
		InstructionsListener instructionsListener = new InstructionsListener();
		instructions.addActionListener(instructionsListener);

		NewGameListener newGameListener = new NewGameListener();
		newGame.addActionListener(newGameListener);

		GraphicViewListener graphicViewListener = new GraphicViewListener();
		graphicView.addActionListener(graphicViewListener);

		TextViewListener textViewListener = new TextViewListener();
		textView.addActionListener(textViewListener);

	} // end of class registerListeners

	//Programs the New Game button
	public void newGame() {
		this.remove(graphicPanel);
		this.remove(textPanel);
		setupWindow();
		graphicPanel.updateUI();
		textPanel.updateUI();
		if (graphicPanel.isVisible())
			JOptionPane.showMessageDialog(null, wrapText(45, "There's a Wumpus 'round this cave here who's been killing the local villagers! You have been tasked to explore the cave and kill the Wumpus. You have three arrows; if you run out of arrows, you die. If you happen upon a bottomless pit, you die. If you mistakenly enter the Wumpus's room, you die. Be careful of bats, they sometimes move you to other rooms. So keep a sharp eye and a ready bow, and godspeed."));
	}

	private class InstructionsListener implements ActionListener {

		public void actionPerformed(ActionEvent click) {
			if (textPanel.isVisible())
				JOptionPane.showMessageDialog(null,
						((TextView) textPanel).getInstructions());
			else {
				JOptionPane.showMessageDialog(null,
						((GraphicalView) graphicPanel).getInstructions());
			}
		}
	} // end of class InstructionsListener

	private class NewGameListener implements ActionListener {

		public void actionPerformed(ActionEvent click) {
			newGame();
		}
	} // end of class NewGameListener

	private class GraphicViewListener implements ActionListener {

		public void actionPerformed(ActionEvent click) {
			textPanel.setVisible(false);
			graphicPanel.setVisible(true);
		}
	} // end of class GraphicViewListener

	private class TextViewListener implements ActionListener {

		public void actionPerformed(ActionEvent click) {
			graphicPanel.setVisible(false);
			textPanel.setVisible(true);
		}
	}

	//updates the observers
	@Override
	public void update(Observable obs, Object obj) {
		if (obs == game) {
			if (graphicPanel.isVisible()) {
				if (game.isOver()) {

					String reason = game.getGameOverReason();
					if (reason.equals("WUMPUS")) {

						JOptionPane.showMessageDialog(null,
								"You were eaten by the Wumpus.\nGAME OVER");
						WumpusAnimation();

					} else if (reason.equals("PIT")) {
						JOptionPane.showMessageDialog(null,
								"You fell into a bottomless pit.\nGAME OVER");
						Pit();

					} else if (reason.equals("SUICIDE")) {
						JOptionPane.showMessageDialog(null,
								"You shot yourself with an arrow.\nGAME OVER");
						Suicide();

					} else if (reason.equals("ARROWS")) {
						JOptionPane
								.showMessageDialog(null,
										"You ran out of arrows before killing the Wumpus.\nGAME OVER");
						Arrows();

					} else if (reason.equals("WIN")) {
						JOptionPane.showMessageDialog(null,
								"You Killed the Wumpus.\nCongratulations!");
						Win();
					}
				} else if (obj.equals("BATSY")) {
					JOptionPane.showMessageDialog(null,
							"The bats have moved you to a new room!");
				} else if (obj.equals("BATSN")) {
					JOptionPane.showMessageDialog(null,
							"The bats ignore your presence.");

				}
			}
		}
	}

	//method for Animation that calls the Wumpus class
	public void WumpusAnimation() {
		this.remove(textPanel);
		this.remove(graphicPanel);
		textPanel = new Wumpus();
		textPanel.setVisible(true);
		this.add(textPanel);
		textPanel.updateUI();
		((Wumpus) textPanel).animate();
	}
	
	//method for Animation that calls the Suicide class
	public void Suicide() {
		this.remove(graphicPanel);
		this.remove(textPanel);
		graphicPanel = new Suicide();
		graphicPanel.setVisible(true);
		this.add(graphicPanel);
		graphicPanel.updateUI();
		((Suicide) graphicPanel).animate();
	}

	//method for Animation that calls the Pit Class
	public void Pit() {
		this.remove(graphicPanel);
		this.remove(textPanel);
		graphicPanel = new Pit();
		graphicPanel.setVisible(true);
		this.add(graphicPanel);
		graphicPanel.updateUI();
		((Pit) graphicPanel).animate();
	}

	//method for Animation that calls the Arrows Class
	public void Arrows() {
		this.remove(graphicPanel);
		this.remove(textPanel);
		graphicPanel = new Arrows();
		graphicPanel.setVisible(true);
		this.add(graphicPanel);
		graphicPanel.updateUI();
		((Arrows) graphicPanel).animate();
	}

	//method for Animation that calls the Win class
	public void Win() {
		this.remove(graphicPanel);
		this.remove(textPanel);
		graphicPanel = new Win();
		graphicPanel.setVisible(true);
		this.add(graphicPanel);
		graphicPanel.updateUI();
		((Win) graphicPanel).animate();
	}
}
