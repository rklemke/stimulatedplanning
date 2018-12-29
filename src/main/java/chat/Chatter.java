package chat;
import stimulatedplanning.User;


/**
* This class represents a chatter in the chat room.
* For each Chatter object a name, age and login time is required.
*/
public class Chatter
{
	private String name = null;
	private String iconUrl = null;
	private String comment = null;
	private String email = null;
	private User user = null;
	private long loginTime = -1;
	private long enteredInRoomAt = -1;
	private int age = -1;
	
	/**
	* Used to create a new Chatter object.
	* It acccepts a name age and time of his login.
	* @param name name of the chatter
	* @param age age of the chatter
	* @param loginTime time when user logged in. Got using Date.getTime(). Works as a timestamp.
	*/
	
	public Chatter(String name, String iconUrl, long loginTime, User user)
	{
		this.name = name;
		this.iconUrl = iconUrl;
		this.loginTime = loginTime;
		this.user = user;
	}	
	
	/**
	* returns name of the Chatter
	* @return java.lang.String
	*/
	public String getName()
	{
		return name;
	}

	/** 
	 * returns iconUrl of the Chatter
	* @return java.lang.String
	*/
	public String getIconUrl()
	{
		return iconUrl;
	}
	
	/** returns the User object linked to this chatter
	* @return User
	*/	
	public User getUser()
	{
		return user;
	}
	
	/** sets comments provided by the chatter.
	* @param comment Comment made by the chatter
	* @return void
	*/
	public void setComment(String comment)
	{
		this.comment = comment;
	}
	
	/** returns comments provided by the chatter
	* @return java.lang.String
	*/	
	public String getComment()
	{
		return comment;
	}
	
	/** sets age of the chatter
	* @param age Age of the chatter
	* @return void
	*/
	public void setAge(int age)
	{
		this.age=age;
	}
	
	/** returns age of the chatter
	* @return int
	*/
	public int getAge()
	{
		return age;
	}
	
	/** sets email of the chatter
	* @param email email address of the person
	* @return void
	*/
	public void setEmail(String email)
	{
		this.email = email;
	}
	
	/**
	* returns email of the user
	* @return String
	*/
	public String getEmail()
	{
		return email;
	}
	/**
	* returns time of user login in milliseconds got using Date.getTime()
	*/
	public long getLoginTime()
	{
		return loginTime;
	}

	/**
	* sets time in milliseconds when a user has entered into a particular room.
	* It is used to display only messages sent after a person has entered into a room.
	* @param long milliseconds got using Date.getTime()
	*/

	public void setEnteredInRoomAt( long enteredAt)
	{
		this.enteredInRoomAt = enteredAt;
	}
	/**
	* returns time in milliseconds when a user has entered into a particular room.
	* It is used to display only messages sent after a person has entered into a room.
	* @return long milliseconds got using Date.getTime()
	*/
	public long getEnteredInRoomAt()
	{
		return enteredInRoomAt;
	}
}