package model;

/**
 * Controls most of the controls of the game by linking the GUI and Room
 * @author Bhavana Gorti
 * @author Patrick Martin
 * @author Michael Curtis
 */
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Random;

import javax.swing.JLabel;

public class GameMap extends Observable {

	private Room hunter;
	private int currentRoom;
	private int arrows;
	private Room[] map;
	private String gameOver;
	private boolean win;

	// Constructor
	// sets an array list of Rooms each corresponding to their neighbors and
	// then setsMap
	public GameMap() {
		map = new Room[21];
		gameOver = null;
		win = false;
		arrows = 3;
		map[1] = new Room(20, 5, 2);
		map[2] = new Room(10, 3, 1);
		map[3] = new Room(2, 4, 12);
		map[4] = new Room(3, 14, 5);
		map[5] = new Room(1, 6, 4);
		map[6] = new Room(5, 7, 15);
		map[7] = new Room(20, 8, 6);
		map[8] = new Room(16, 7, 19);
		map[9] = new Room(20, 10, 19);
		map[10] = new Room(9, 11, 2);
		map[11] = new Room(10, 12, 18);
		map[12] = new Room(11, 13, 3);
		map[13] = new Room(12, 17, 14);
		map[14] = new Room(4, 15, 13);
		map[15] = new Room(14, 16, 6);
		map[16] = new Room(17, 8, 15);
		map[17] = new Room(18, 16, 13);
		map[18] = new Room(11, 17, 19);
		map[19] = new Room(9, 8, 18);
		map[20] = new Room(9, 7, 1);

		setMap();
	}

	// sets up the types of Rooms in the Map
	public void setMap() {
		Random generate = new Random();
		int hunterIndex = generate.nextInt(20) + 1;
		hunter = map[hunterIndex];
		currentRoom = hunterIndex;

		List<Integer> previousRooms = new LinkedList<Integer>();
		int room = generate.nextInt(20) + 1;
		previousRooms.add(room);
		map[room].setRoomType(new WumpusRoom());

		for (int i = 0; i < 2; i++) {
			while (previousRooms.contains(room)) {
				room = generate.nextInt(20) + 1;
			}
			previousRooms.add(room);
			map[room].setRoomType(new BatRoom());
		}

		for (int i = 0; i < 2; i++) {
			while (previousRooms.contains(room)) {
				room = generate.nextInt(20) + 1;
			}
			previousRooms.add(room);
			map[room].setRoomType(new PitRoom());
		}

	}

	// What happens when hunter moves
	public boolean move(int room) {
		int[] neighbors = hunter.getNeighbors();
		if (neighbors[0] == room || neighbors[1] == room
				|| neighbors[2] == room) {
			hunter = map[room];
			currentRoom = room;
			if (!hunter.getRoomType().is("BatRoom")) {
				hunter.enter(this);
				if (!isOver()) {
					setChanged();
					notifyObservers("MOVE");
				}
			} else {
				setChanged();
				notifyObservers("MOVE");
				hunter.enter(this);
			}
			return true;
		}
		return false;
	}

	// What happens when hunter shoots an arrow
	public boolean fire(int[] path) {
		if (arrows > 0) {
			arrows--;
			int current = currentRoom;
			int[] neighbors = hunter.getNeighbors();
			for (int room : path) { // go through each room in the path
				if (neighbors[0] == room || neighbors[1] == room
						|| neighbors[2] == room) { // make sure it it possible
													// for the arrow to progress
													// to this room
					if (map[room].hasWumpus()) {// win if it hits the wumpus
						win();
						break;
					} else if (room == currentRoom) { // lose if you hit
														// yourself
						lose("SUICIDE");
						break;
					} else { // update the arrow's current room
						current = room;
						neighbors = map[current].getNeighbors();
					}
				} else { // the arrow hits a wall
					break;
				}
			}

			if (!isOver()) {
				if (arrows <= 0) {
					lose("ARROWS"); // lose if you run out of arrows
				} else {
					setChanged();
					notifyObservers("ARROWS");
				}
			}
			return true;
		} else
			return false;
	}

	// What happens when the hunter wins
	public void win() {
		win = true;
		gameOver = "WIN";
		setChanged();
		notifyObservers();
	}

	// What happens when the hunter loses
	public void lose(String reason) {
		win = false;
		gameOver = reason;
		setChanged();
		notifyObservers();
	}

	// updates when bats move you
	public void bats(boolean move) {
		setChanged();
		if (move)
			notifyObservers("BATSY");
		else
			notifyObservers("BATSN");
	}

	public void batMove(int room) {
		currentRoom = room;
		hunter = map[room];
		setChanged();
		notifyObservers("MOVE");
		hunter.enter(this);
	}

	// Returns the reason for gameOver
	public String getGameOverReason() {
		return gameOver;
	}

	// Returns a String for all the reactions to the neighboring rooms
	public String nextToText() {
		String display = "";
		int[] neighbors = hunter.getNeighbors();
		for (int neighbor : neighbors)
			display += map[neighbor].nextToText() + "\n";
		return display;
	}

	// returns an int array of neighbors from room
	public int[] getNeighbors() {
		return hunter.getNeighbors();
	}

	// Returns room at that index
	public Room getRoom(int index) {
		return map[index];
	}

	// Gives you the currentRooms index
	public int getCurrentRoomIndex() {
		return currentRoom;
	}

	// Gives you the number of arrows
	public int getArrows() {
		return arrows;
	}

	// Tells you whether game is over or not
	public boolean isOver() {
		// TODO Auto-generated method stub
		return gameOver != null;
	}
}
