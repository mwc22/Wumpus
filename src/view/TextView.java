package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import model.GameMap;
/**
 * 
 * @author Patrick Martin, Michael Curtis, Bhavana Gorti
 *
 */
public class TextView extends JPanel implements Observer {

	private GameMap game;
	private JTextField input;
	private JTextArea display;
	private ActionListener inputListener;

	/**
	 * Set up the Text View
	 * @param gamemap
	 * Game that has the view will run
	 */
	public TextView(GameMap gamemap) {
		game = gamemap;
		game.addObserver(this);
		setUpView();
		setUpListeners();
	}

	/**
	 * Initialize the stuff on the view: the input JTextField, the output JTextArea, the containing JScrollPane
	 */
	private void setUpView() {
		input = new JTextField(60);
		input.setEditable(true);
		display = new JTextArea(10, 50);
		display.setEditable(false);
		display.setText("There's a Wumpus 'round this cave here who's been killing the local villagers! You have been tasked to explore the cave and kill the Wumpus. You have three arrows; if you run out of arrows, you die. If you happen upon a bottomless pit, you die. If you mistakenly enter the Wumpus's room, you die. Be careful of bats, they sometimes move you to other rooms. So keep a sharp eye and a ready bow, and godspeed.\n");
		display.append(roomEnterMessage());
		display.setLineWrap(true);
		display.setWrapStyleWord(true);
		JScrollPane scroll = new JScrollPane(display);
		// scroll.setPreferredSize(new Dimension(700,400));

		this.add(scroll, BorderLayout.NORTH);
		this.add(input, BorderLayout.SOUTH);

	}

	/**
	 * Attach a listener to the input field
	 */
	private void setUpListeners() {
		inputListener = new InputListener();
		input.addActionListener(inputListener);
	}

	/**
	 * Takes the input text and converts it into a move or fire command
	 */
	private void parseInput() {
		String message = input.getText();
		if (message.indexOf("move") == 0) {
			if (move(message)) {
				input.setText("");
			} else if (!game.isOver())
				display.append("Invalid command.\n");
		} else if (message.indexOf("fire") == 0) {
			if (fire(message)) {
				input.setText("");
			} else if (!game.isOver())
				display.append("Invalid command.\n");
		} else if (!game.isOver())
			display.append("Invalid command.\n");

		display.selectAll();

	}

	/**
	 * Gets the text to show upon entering a room
	 * @return
	 */
	private String roomEnterMessage() {
		String[] messages = game.nextToText().split("\n");
		Arrays.sort(messages);
		String returnMessage = "\nYou enter room " + game.getCurrentRoomIndex()
				+ ".\nYou have " + game.getArrows() + " arrows.\n";
		for (String message : messages)
			if (message.length() > 0)
				returnMessage += message + "\n";
		returnMessage += "\n";

		String roomMessage = "Ahead you see passages leading to rooms ";
		int[] rooms = game.getNeighbors();
		roomMessage += rooms[0] + ", " + rooms[1] + ", " + rooms[2] + ".\n";
		returnMessage += roomMessage + "\n";

		return returnMessage;

	}

	/**
	 * Attempts to move to room specified in the message
	 * @param message
	 * Move command, must be of form "move X" where X is an integer 1-20
	 * @return
	 * true for a successful movement, false otherwise
	 */
	private boolean move(String message) {
		String[] messages = message.split(" ");
		try {
			int destination = Integer.parseInt(messages[1]);
			return game.move(destination);
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Attempts to fire an arrow through the rooms specified in the message
	 * @param message
	 * Fire command, must be of form "fire V W X Y Z" where V-Z are integers 1-20
	 * @return
	 * true for valid input, false otherwise
	 */
	private boolean fire(String message) {
		String[] messages = message.split(" ");
		try {
			int[] arrowPath = new int[Math.min(messages.length - 1, 5)];
			for (int i = 0; i < arrowPath.length; i++) {
				arrowPath[i] = Integer.parseInt(messages[i + 1]);
			}
			return game.fire(arrowPath);
		} catch (Exception e) {
			System.out.println(e);
			return false;
		}
	}

	/**
	 * 
	 * @return
	 * The instructions on how to play the game in the text view
	 */
	public String getInstructions() {
		return "Type \"move X\" to move to room X or \"fire V (W X Y Z)\" to shoot an arrow from the current room through rooms V through Z, a maximum of five rooms";
	}

	/**
	 * Shows the text at the game's end, depending on the reason for the game's finish
	 */
	public void end() {
		String condition = game.getGameOverReason();
		String outputText = "\n\n";
		if (condition.equals("WUMPUS")) {
			outputText = "Stumbling through the darkness, nearly choking from the stench, you enter the next room. The stench overwhelms your consciousness, and as your eyes close, you see the Wumpus preparing for its next meal. \n \n GAME OVER.";
		} else if (condition.equals("PIT")) {
			outputText = "Upon entering the next room, you see an enormous, bottomless, pit. The ground is quite slippery, and it is all you can do not to lose your footing. The wind abruptly changes direction, causing you to lose your balance, falling into the abyss. \n \nGAME OVER.";
		} else if (condition.equals("SUICIDE")) {
			outputText = "You let the arrow loose, listening for any signs of a kill. The whistling fades... then gets louder. Before you can react, the arrow returns from another corridor and strikes you. You have killed the beast within yourself, but also your village's last hope of salvation. \n \nGAME OVER.";
		} else if (condition.equals("ARROWS")) {
			outputText = "You let your last arrow loose, listening carefully for any signs of a kill. The whistling fades... and you hear a pathetic *clink* of the arrow harmlessly striking stone. The Wumpus lives on. \n \nGAME OVER.";
		} else if (condition.equals("WIN")) {
			outputText = "You let the arrow loose, listening for any signs of a kill. The whistling fades... and you hear a satisfying *thunk* and a bloodcurding shriek that lasts for several seconds. You trace the arrow's path to find the Wumpus, dead. \n\nYOU WIN!";
		}
		display.append(outputText);
		display.selectAll();
		input.setEditable(false);

	}

	/**
	 * Displays text about the bats in the room and whether they moved you to a new room
	 * @param moved
	 * true if the bats move the player, false otherwise
	 */
	private void bats(boolean moved) {
		if (moved) {
			display.append("The bats are disturbed by your presence, and in their anger move you to a new room!\n");
		} else {
			display.append("You tiptoe into the room, careful to avoid disturbing the bats. The bats aren't fooled; rather, they are afraid of ballerinas, and your attempts to be sneaky are scaring them.\n");
		}

		display.selectAll();
	}

	/**
	 * 
	 * @author Patrick Martin, Bhavana Gorti, Michael Curtis
	 *
	 */
	private class InputListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			if (!game.isOver()) {
				parseInput();
			}

		}

	}

	@Override
	/**
	 * arg0 will be the game. arg1 will be 
	 * null -> game is over
	 * ARROWS -> arrow fired
	 * MOVE -> player moved
	 * BATSY -> entered bat room and was moved
	 * BATSN -> entered bat room and was not moved
	 */
	public void update(Observable arg0, Object arg1) {
		if (game.isOver())
			end();
		else if (arg1.equals("ARROWS"))
			display.append("You let the arrow loose, listening carefully for any signs of a kill. The whistling fades... and you hear a pathetic *clink* of the arrow harmlessly striking stone. The Wumpus lives on.\nYou have "
					+ game.getArrows() + " arrows left.\n");
		else if (arg1.equals("MOVE"))
			display.append(roomEnterMessage());
		else if (arg1.equals("BATSY"))
			bats(true);
		else if (arg1.equals("BATSN"))
			bats(false);
	}


}
