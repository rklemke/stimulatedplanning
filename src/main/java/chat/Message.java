package chat;

import java.io.Serializable;

import stimulatedplanning.User;

/**
Represents a Message sent by a user.
*/
public class Message implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2348925488118290085L;
	/**
	* String used to store the name of a chatter
	*/
	private User user = null;
	/**
	* String containing message
	*/
	private String message = null;

	/**
	* long containing the time when message was delivered
	*/
	private long timeStamp;
	/**
	* This constructor accepts a name of the chatterand the message.
	* @param name Name of the chatter
	* @param message message of the chatter
	* @param timeStamp time of the message
	*/
	public Message(User user, String message, long timeStamp)
	{
		this.user = user;
		this.message= message;
		this.timeStamp = timeStamp;
	}
	
	/**
	* Returns name of the Chatter
	* @return String
	*/
	public User getUser()
	{
		return user;
	}
	
	/**
	* Returns name of the Chatter
	* @return String
	*/
	public String getChatterName()
	{
		if (user == null) {
			return "system";
		}
		return user.getName();
	}
	
	/**
	* Returns message of the chatter
	* @return String
	*/
	public String getMessage()
	{
		return message;
	}
	/**
	* Returns time of the message
	* @return long
	*/
	public long getTimeStamp()
	{
		return timeStamp;
	}
}