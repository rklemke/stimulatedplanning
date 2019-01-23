package chat;

import java.util.Map;
import java.util.LinkedList;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Iterator;
import java.util.HashMap;
import java.util.Set;

import stimulatedplanning.User;
import stimulatedplanning.util.HashArrayList;
import stimulatedplanning.util.IObjectWithId;

/** This class represents a chat room in the Chat System
*/
public class ChatRoom implements IObjectWithId, Serializable
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5881629834245237107L;
	/*
	* used to store name of the room
	*/
	private String name = null;
	/*
	* used to store description of the room
	*/
	private String description = null;
	
	/*
	* Map to store Chatter objects
	*/
	private HashArrayList<User> users = new HashArrayList<>();
	/*
	* Linked list to store Message object
	*/
	private List messages = new LinkedList();
	
	/*
	* Used to set the maximum no of messages
	*/
	private int messages_size = 150;
	/**
	* This constructor takes a name and description
	* to create a new ChatRoom
	* @param name Name of the Room
	* @param descr Description of the Room
	*/
	public ChatRoom(String name, String descr)
	{
		this.name= name;
		this.description = descr;
	}
	
	/**
	* Returns name of the room
	* @return java.lang.String
	*/
	public String getName()
	{
		return name;
	}
	
	/**
	* Returns id of the room, which is the name. Used for compliance with IObjectWithId interface.
	* @return java.lang.String
	*/
	public String getId()
	{
		return getName();
	}
	
	/**
	* Returns description of the room
	* @return java.lang.String
	*/
	public String getDescription()
	{
		return description;
	}
	
	/**
	* adds a Chatter object to list of Chatters
	* @param user User object
	* @return void
	*/
	public synchronized void addChatter(User user)
	{
		users.add(user);
	}
	/**
	* removes a Chatter object from list of Chatters
	* @param userId name of the chatter.
	* @return void
	*/
	public synchronized void removeChatter(String userId)
	{
		users.remove(users.get(userId));
	}
	
	/**
	* returns a Chatter object from chatters list.
	* @param userId name of the chatter
	* @return chat.Chatter
	*/
	public User getChatter(String userId)
	{
		return users.get(userId);
	}
	
	/**
	* checks whether a chatter exists or not
	* @param userId name of the chatter to check
	* @return boolean
	*/
	
	public boolean chatterExists(String userId)
	{
		return users.containsKey(userId);
	}
	
	/**
	* returns total number of chatters in this room
	* @return int
	*/
	public int getNoOfChatters()
	{
		return users.size();
	}
	
	/** returns an array containing all Chatter objects
	* @return chat.Chatter[]
	*/
	public User[] getChattersArray()
	{
		User[] chattersArray = new User[users.size()];
		Iterator<User> chattersit = users.iterator();
		int i = 0;
		while(chattersit.hasNext())
		{
			User user = chattersit.next();
			chattersArray[i] = user;
			i++;
		}
		return chattersArray;
	}
	
	/** adds the message to the messages list
	* @param msg A Message Object
	* @return void
	*/
	public synchronized void addMessage(Message msg)
	{
		if(messages.size()==messages_size)
		{
			((LinkedList)messages).removeFirst();
		}
		messages.add(msg);
	}
	
	/**
	* returns a ListIterator object containing all the messages
	* @return java.util.ListIterator
	*/	
	public ListIterator getMessages()
	{
		return messages.listIterator();
	}

	/**
	* returns an array of messages sent after given time
	* @param afterTimeStamp Time in milliseconds.
	* @return array
	*/	
	public Message[] getMessages(long afterTimeStamp)
	{
		ListIterator li = messages.listIterator();
		List temp = new ArrayList();
		Message m;
		while (li.hasNext())
		{
			m = (Message)li.next();
			if (m.getTimeStamp() >= afterTimeStamp)
			{
				temp.add(m);
			}
		}
		Object o[] = temp.toArray();
		Message[] arr = new Message[o.length];
		for (int i = 0; i < arr.length; i++)
		{
			arr[i] = (Message)o[i];
		}
		return arr;
	}

	/**
	* returns total number of messages in the messages List
	* @return int
	*/
	public int getNoOfMessages()
	{
		return messages.size();
	}
	
	/**
	* sets maxmium number of messages to this number.
	* @param size the maximum no of messages to hold at a time.
	* @return void
	*/
	public void setMaximumNoOfMessages(int size)
	{
		messages_size = size;
	}
	
	/**
	* returns maxmium number of messages set.
	* @return int
	*/
	public int getMaxiumNoOfMessages()
	{
		return messages_size;
	}
}