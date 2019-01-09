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
	
	public static final String emojiPre = "<img src=\"/img/chat/emojis/";
	public static final String emojiMiddle = ".png\" title=\"(";
	public static final String emojiPost = ")\" width=\"24\" height=\"24\">";
	public static final CharSequence[] emojis = {
			"like",
			"dislike",
			"smile",
			"smiling",
			"laughing",
			"cool",
			"grinning",
			"angry",
			"anguish",
			"confused",
			"crying",
			"curious",
			"dead",
			"devil",
			"ghost",
			"heart",
			"in-love",
			"muted",
			"nerd",
			"ninja",
			"sad",
			"scared",
			"secret",
			"shocked",
			"shocked-2",
			"skull",
			"sleeping",
			"tired",
			"tongue",
			"wink"	
	};

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
	
	
	public String getDisplayMessage() {
		String tempMsg = getMessage();
		if (tempMsg != null && tempMsg.length()>0) {
			for (CharSequence emoji : emojis) {
				tempMsg = tempMsg.replace("("+emoji+")", emojiPre+emoji+emojiMiddle+emoji+emojiPost);
			}
		}
		return tempMsg;
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