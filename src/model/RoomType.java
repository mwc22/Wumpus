package model;
/**
 * This is an interface for RoomType that is implemented by all types of rooms
 * @author Bhavana Gorti
 * @author Patrick Martin
 * @author Michael Curtis
 *
 */
public interface RoomType {
	
	//All RoomTypes should say what will happen when the hunter enters that kind of room
	public void enter(GameMap game);

	//All RoomTypes should say what will happen if you are in an adjacent room to that kind of room
	public String nextToText();

	//All RoomTypes should check to see if roomTypeName is one of those Rooms
	public boolean is(String roomTypeName);

}
