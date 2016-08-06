package model;
/**
 * 
 * @author: Bhavana Gorti
 * @author: Patrick Martin
 * @author: Michael Curtis
 *
 */
public class WumpusRoom implements RoomType{
	
	//Empty Constructor
	public WumpusRoom()
	{}
	
	//What happens when hunters this type of Room
	@Override
	public void enter(GameMap game) {
		game.lose("WUMPUS");
	}

	//What happens when hunter is adjacent to this type of room
	@Override
	public String nextToText() {
		return "You smell something foul.\n";
	}
	
	/**
	 * This is a WumpusRoom.
	 * Checks to see if roomTypeName is WumpusRoom.
	 */
	@Override
	public boolean is(String roomTypeName) {
		return roomTypeName.equals("WumpusRoom");
	}

}
