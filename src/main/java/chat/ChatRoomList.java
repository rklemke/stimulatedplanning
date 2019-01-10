package chat;

import java.util.Map;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import stimulatedplanning.PersistentStore;
import stimulatedplanning.util.HashArrayList;
import stimulatedplanning.util.IObjectWithId;

import java.util.Iterator;

/**
* This class is used to manipulate and store ChatRoom objects.
* It provides methods to store and retrieve ChatRoom objects
* in this <code>ChatRoomList</code>.
*/
public class ChatRoomList implements IObjectWithId, Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 849470430196780881L;

	/**
	* Stores all the ChatRoom objects
	*/
	private HashArrayList<ChatRoom> roomList;
	
	private String id = null;
	/**
	*/
	public ChatRoomList(String id)
	{
		this.id = id;
		roomList = new HashArrayList<>();
	}
	
	public String getId() {
		return id;
	}
	
	/**
	* adds new chat room object to a list of Rooms.
	* @param room ChatRoom object
	* @return void
	*/
	public synchronized void addRoom(ChatRoom room)
	{
		roomList.add(room);
		ArrayList<Message> arrayList = PersistentStore.readMessagesForChat(this, room);
		for (Message msg : arrayList) {
			room.addMessage(msg);
		}
	}
	
	/**
	* Used to remove a ChatRoom object from the
	* list of ChatRooms.
	* @param name is a String object is the name of the
	* room to be removed from the list of rooms.
	* @return void
	*/
	public synchronized void removeRoom(String name)
	{
		roomList.removeById(name);
	}
	
	/** Returns a ChatRoom object
	* @param name is the name of the ChatRoom object to be returned.
	* @return ChatRoom object.
	*/
	public ChatRoom getRoom(String name)
	{
		return (ChatRoom) roomList.get(name);
	}
	/** Finds the room of chatter having this name.
	* @param name is the name of the Chatter object.
	* @return ChatRoom object.
	*/
	public ChatRoom getRoomOfChatter(String name)
	{
		ChatRoom[] rooms = this.getRoomListArray();
		for (ChatRoom room : roomList)
		{
			boolean chatterexists = room.chatterExists(name);
			if (chatterexists)
			{
				return room;
			}
		}
		return null;
	}

	/** Returns a Set containing all the ChatRoom objects
	* @return Set
	*/
//	public Set getRoomList()
//	{
//		return roomList.entrySet();
//	}
	
	/** returns an array containing all ChatRoom objects
	* @return chat.ChatRoom[]
	*/
	public ChatRoom[] getRoomListArray()
	{
		ChatRoom[] roomListArray = new ChatRoom[roomList.size()];
		roomListArray = roomList.arrayList().toArray(roomListArray);
		return roomListArray;
	}
	
	/**
	* searches each ChatRoom for existance of a chatter.
	* @param userId Name of the chatter to find.
	* @return boolean
	*/
	public boolean chatterExists(String userId)
	{
		boolean chatterexists = false;
		ChatRoom[] rooms = this.getRoomListArray();
		for (int i = 0; i < rooms.length; i++)
		{
			chatterexists = rooms[i].chatterExists(userId);
			if (chatterexists)
			{
				break;
			}
		}
		return chatterexists;
	}
}