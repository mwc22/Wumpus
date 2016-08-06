package model;
/**
 * @author: Bhavana Gorti
 * @author: Patrick Martin
 * @author: Michael Curtis
 */
import java.util.Random;
/**
 * This class accounts for an empty room in the game
 */
public class EmptyRoom implements RoomType {
	
	//Empty constructor
	public EmptyRoom()
	{}
	
	//Method that is called when hunter enters this type of room
	@Override
	public void enter(GameMap game) {
		Random generate= new Random();
		int chance= generate.nextInt(4);
		if (chance>0)
		{
			int newRoom= generate.nextInt(20) + 1;
			game.move(newRoom);
		}
			
	}

	//Method that is called when in a room adjacent to this room Type
	@Override
	public String nextToText() {
		return "";
	}
	//Checks whether the roomTypeName is EmptyRoom
	@Override
	public boolean is(String roomTypeName) {
		return roomTypeName.equals("EmptyRoom");
	}
	
}
