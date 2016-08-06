package view;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import model.GameMap;

public class GraphicalView extends JPanel implements Observer {

	private BufferedImage background;
	private JTextArea nextRooms;
	private JTextField arrows;
	private JTextField neighborLeft;
	private JTextField neighborRight;
	private JTextField neighborCenter;
	private JTextField currentRoom;
	private JButton map;
	private JButton shoot;
	private GameMap currentCave;
	private JPanel roomLeft;
	private JPanel roomRight;
	private JPanel roomCenter;
	private int[] neighbors = new int[3];

	public GraphicalView(GameMap game) {
		loadImages();

		currentCave = game;
		currentCave.addObserver(this);

		// sets an array of neighbors from the getNeighbors method of Game Map
		neighbors = currentCave.getNeighbors();

		// sets an array of neighbors from the getNeighbors method of Game Map
		for (int i = 0; i < currentCave.getNeighbors().length; i++) {
			neighbors[i] = currentCave.getNeighbors()[i];
		}

		// Sets text for all the description to show in the GUI
		String[] messages = currentCave.nextToText().split("\n");
		Arrays.sort(messages);
		String returnMessage= "";
		for(String message : messages) {
			if(message.length() > 0) returnMessage += message + "\n";
		}
		returnMessage = returnMessage.trim();
		nextRooms= new JTextArea(returnMessage);
		nextRooms.setEditable(false);
		JPanel descriptions = new JPanel();
		descriptions.add(nextRooms);
		descriptions.setOpaque(false);
		// sets number of arrows so that it shows at the top of the screen
		arrows = new JTextField("Arrows: " + currentCave.getArrows());
		arrows.setEditable(false);
		currentRoom = new JTextField("Current Room:"
				+ currentCave.getCurrentRoomIndex());
		currentRoom.setEditable(false);
		// sets a map button that will pull up a picture of the map

		map = new JButton("Map");
		// sets a shoot button that you can shoot an arrow by clicking and
		// entering in up to 5 cave ids
		shoot = new JButton("Shoot");

		this.setLayout(new GridLayout(3, 1));
		JPanel info = new JPanel();
		info.add(map);
		info.add(arrows);
		info.add(currentRoom);
		info.add(shoot);
		info.setOpaque(false);
		this.add(info);

		// makes each of the three caves clickable
		JPanel clickCave = new JPanel();
		clickCave.setLayout(new GridLayout(1, 3));
		roomLeft = new JPanel();
		roomLeft.setOpaque(false);
		neighborLeft = new JTextField(neighbors[0] + "");
		neighborLeft.setEditable(false);
		roomLeft.add(neighborLeft);

		roomCenter = new JPanel();
		roomCenter.setOpaque(false);
		neighborCenter = new JTextField(neighbors[1] + "");
		neighborCenter.setEditable(false);
		roomCenter.add(neighborCenter);

		roomRight = new JPanel();
		roomRight.setOpaque(false);
		neighborRight = new JTextField(neighbors[2] + "");
		neighborRight.setEditable(false);
		roomRight.add(neighborRight);

		clickCave.add(roomLeft);
		clickCave.add(roomCenter);
		clickCave.add(roomRight);
		roomLeft.addMouseListener(new ClickListenerLeft());
		roomRight.addMouseListener(new ClickListenerRight());
		roomCenter.addMouseListener(new ClickListenerCenter());
		clickCave.setOpaque(false);
		this.add(clickCave);
		this.add(descriptions);

		// allows buttons to be able to be clicked
		map.addActionListener(new ButtonListener());
		ShootListener shootListener = new ShootListener();
		shoot.addActionListener(shootListener);
		
		

	}

	// paint component used to draw the background
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D gr = (Graphics2D) g;

		if (currentCave == null) {
			return;
		}

		gr.drawImage(background, 0, 0, null);
	}

	// loads the image of the background
	public void loadImages() {
		try {
			background = ImageIO.read(new File("images" + File.separator
					+ "Cave.jpg"));

		} catch (IOException e) {
			System.out.println("Cannot find 'Cave.jpg'");
		}
	}

	// Gives the instructions to the user
	public String getInstructions() {
		return "Click the room you want to navigate to.";
	}

	// listeners for Map button
	public class ButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			try {
				ImageIcon picture = new ImageIcon(ImageIO.read(new File(
						"images" + File.separator + "Map.png")));
				JOptionPane.showMessageDialog(null, picture);
			} catch (IOException e) {
				System.out.println("Can't find Map.png");
			}
		}

	}

	// Listener for shoot button
	public class ShootListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {

			String rooms = JOptionPane
					.showInputDialog("Enter up to 5 cave numbers separated by a comma and a space.");
			if (rooms != null) {
				String[] roomsString = rooms.split(", ");
				int[] roomsInt = new int[roomsString.length];
				try {
					for (int i = 0; i < roomsString.length; i++) {
						roomsInt[i] = Integer.parseInt(roomsString[i]);
					}
					currentCave.fire(roomsInt);
					
					if (!currentCave.isOver()) {
						JOptionPane.showMessageDialog(null,
								"Your arrow missed.");
					}

				} catch (Exception e) {
					JOptionPane.showMessageDialog(null,
							"You have entered wrong input.");
				}
			}

			arrows.setText("Arrows: " + currentCave.getArrows());
		}
	}

	// Click listeners for each of the adjacent rooms
	public class ClickListenerLeft implements MouseListener {
		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			// Nothing
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// Nothing
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// Nothing
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			int destination = neighbors[0];
			currentCave.move(destination);
			neighbors = currentCave.getNeighbors();
			String[] messages = currentCave.nextToText().split("\n");
			Arrays.sort(messages);
			String returnMessage= "";
			for(String message : messages) {
				if(message.length() > 0) returnMessage += message + "\n";
			}
			returnMessage = returnMessage.trim();
			nextRooms.setText(returnMessage);
			neighborLeft.setText(neighbors[0] + "");
			neighborCenter.setText(neighbors[1] + "");
			neighborRight.setText(neighbors[2] + "");
			currentRoom.setText("Current Room:"
					+ currentCave.getCurrentRoomIndex());
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// Nothing
		}

	}

	public class ClickListenerCenter implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent e) {
			// Nothing
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// Nothing
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// Nothing
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			int destination = neighbors[1];
			currentCave.move(destination);
			neighbors = currentCave.getNeighbors();
			String[] messages = currentCave.nextToText().split("\n");
			Arrays.sort(messages);
			String returnMessage= "";
			for(String message : messages) {
				if(message.length() > 0) returnMessage += message + "\n";
			}
			returnMessage = returnMessage.trim();
			nextRooms.setText(returnMessage);
			neighborLeft.setText(neighbors[0] + "");
			neighborCenter.setText(neighbors[1] + "");
			neighborRight.setText(neighbors[2] + "");
			currentRoom.setText("Current Room:"
					+ currentCave.getCurrentRoomIndex());
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// Nothing
		}

	}

	public class ClickListenerRight implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent e) {
			// Nothing
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// Nothing
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// Nothing
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			int destination = neighbors[2];
			currentCave.move(destination);
			neighbors = currentCave.getNeighbors();
			String[] messages = currentCave.nextToText().split("\n");
			Arrays.sort(messages);
			String returnMessage= "";
			for(String message : messages) {
				if(message.length() > 0) returnMessage += message + "\n";
			}
			returnMessage = returnMessage.trim();
			nextRooms.setText(returnMessage);
			neighborLeft.setText(neighbors[0] + "");
			neighborCenter.setText(neighbors[1] + "");
			neighborRight.setText(neighbors[2] + "");
			currentRoom.setText("Current Room:"
					+ currentCave.getCurrentRoomIndex());
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// Nothing
		}
	}

	@Override
	public void update(Observable o, Object arg) { //FIX "MOVE" WITH UPDATED CODE
		// TODO Auto-generated method stub
		if(arg!= null) {
		if(arg.equals("ARROWS"))
			arrows.setText("Arrows: " + currentCave.getArrows());
		if(arg.equals("MOVE")) {
			neighbors = currentCave.getNeighbors();
			String[] messages = currentCave.nextToText().split("\n");
			Arrays.sort(messages);
			String returnMessage= "";
			for(String message : messages) {
				if(message.length() > 0) returnMessage += message + "\n";
			}
			returnMessage = returnMessage.trim();
			nextRooms.setText(returnMessage);
			neighborLeft.setText(neighbors[0] + "");
			neighborCenter.setText(neighbors[1] + "");
			neighborRight.setText(neighbors[2] + "");
			currentRoom.setText("Current Room:"
					+ currentCave.getCurrentRoomIndex());
		}
		}
			
		
	}


}
