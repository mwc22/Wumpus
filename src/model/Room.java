package model;
/**
 * This class implements code for a Room and grabs the strategy from each Room to implement based on which one the hunter is associating with
 * @author Bhavana Gorti
 * @author Patrick Martin
 * @author Michael Curtis
 *
 */
public class Room {
	
	private RoomType roomType;
	private int left;
	private int right;
	private int center;
	
	//Constructor (sets all rooms to Empty and sets the neighbors)
	public Room(int left, int right, int center){
		this.left = left;
		this.right = right;
		this.center = center;
		roomType= new EmptyRoom();
	}
	
	//Enter method calls the enter method from RoomType
	public void enter(GameMap game){
		roomType.enter(game);
	}
	
	//Calls nextToText method in RoomType
	public String nextToText() {
		return roomType.nextToText();
	}
	
	//Returns an int array of the neighboring caves of the hunter
	public int[] getNeighbors() {
		int[] neighbors = {left, center, right};
		return neighbors;
	}
	
	//Returns whether or not that roomType is a Wumpus Room
	public boolean hasWumpus() {
		return roomType.is("WumpusRoom");
	}
	//allows you to set a Room as a certain roomType
	public void setRoomType(RoomType roomType)
	{
		this.roomType= roomType;
	}
	public RoomType getRoomType()
	{
		return roomType;
	}
}


