package chat;

import java.io.Serializable;

import stimulatedplanning.StimulatedPlanningFactory;
import stimulatedplanning.User;
import stimulatedplanning.util.IObjectWithId;

/**
Represents a Message sent by a user.
*/
public class Message implements Serializable, IObjectWithId
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
	* String used to store the name of a chatter
	*/
	private ChatRoom room = null;
	public ChatRoom getRoom() {
		return room;
	}

	/**
	* String used to store the name of a chatter
	*/
	private ChatRoomList roomList = null;
	public ChatRoomList getRoomList() {
		return roomList;
	}

	/**
	* String containing message
	*/
	private String message = null;

	/**
	 * identifier for database purposes
	 */
	private String id = null;
	
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
	* This constructor accepts a name of the chatter and the message.
	* @param name Name of the chatter
	* @param message message of the chatter
	* @param timeStamp time of the message
	*/
	public Message(User user, String message, long timeStamp, ChatRoom room, ChatRoomList roomList, String id)
	{
		this.id = id;
		this.user = user;
		this.message= message;
		this.timeStamp = timeStamp;
		this.room = room;
		this.roomList = roomList;
	}
	
	public String getId() {
		return id;
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
		tempMsg = tempMsg.replace(":-)","(smile)");
		tempMsg = tempMsg.replace(":)","(smile)");
		tempMsg = tempMsg.replace(";-)","(wink)");
		tempMsg = tempMsg.replace(";)","(wink)");
		tempMsg = tempMsg.replace(":-D","(smiling)");
		tempMsg = tempMsg.replace(":D","(smiling)");
		tempMsg = tempMsg.replace(":P","(tongue)");
		tempMsg = tempMsg.replace(":-P","(tongue)");
		tempMsg = tempMsg.replace(":p","(tongue)");
		tempMsg = tempMsg.replace(":-p","(tongue)");
		tempMsg = tempMsg.replace(":-|","(scared)");
		tempMsg = tempMsg.replace(":|","(scared)");
		tempMsg = tempMsg.replace(":-(","(sad)");
		tempMsg = tempMsg.replace(":(","(sad)");
		tempMsg = tempMsg.replace("8-)","(nerd)");
		tempMsg = tempMsg.replace("8)","(nerd)");
		tempMsg = tempMsg.replace("XD","(dead)");
		tempMsg = tempMsg.replace("<3","(heart)");
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