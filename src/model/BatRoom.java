package model;
/**
 * @author: Bhavana Gorti
 * @author: Patrick Martin
 * @author: Michael Curtis
 */

import java.util.Random;
/**
 * This class accounts for a Bat Room in the game.
 *
 */
public class BatRoom implements RoomType{
	
	//Empty Constructor
	public BatRoom()
	{}	
	
	//Method is called when hunter enters that roomType
	@Override
	public void enter(GameMap game) {
		Random generate= new Random();
		int chance= generate.nextInt(4);
		if (chance>0)
		{
			int newRoom= generate.nextInt(20) + 1;
			game.bats(true);
			game.batMove(newRoom);
		}
		else
			game.bats(false);
			
	}
	
	//Returns a string of what you would hear if you were in a room adjacent to this type of room
	@Override
	public String nextToText() {
		return "You hear flapping.\n";
	}
	
	//checks whether roomTypeName is equivalent to BatRoom
	@Override
	public boolean is(String roomTypeName) {
		return roomTypeName.equals("BatRoom");
	}
	
}
