package model;

/**
 * This class accounts for a Pit Room in the game
 * @author: Bhavana Gorti
 * @author: Patrick Martin
 * @author: Michael Curtis 
 */
public class PitRoom implements RoomType{
	
	//Empty Constructor
	public PitRoom(){}
	@Override
	public void enter(GameMap game) {
		game.lose("PIT");
	}
	
	//returns what the hunter feels when he is in a room adjacent to this room Type
	@Override
	public String nextToText() {
		return "You feel a draft.\n";
	}
	
	//Checks whether roomTypeName is Pit Room
	@Override
	public boolean is(String roomTypeName) {
		return roomTypeName.equals("PitRoom");
	}

}
