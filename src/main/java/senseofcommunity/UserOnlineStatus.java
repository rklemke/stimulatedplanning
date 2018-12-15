package senseofcommunity;

import java.util.Date;

import stimulatedplanning.GenericUserObject;
import stimulatedplanning.User;

public class UserOnlineStatus extends GenericUserObject {
	
	Date lastAccess;
	String lastUrl;
	

	public Date getLastAccess() {
		return lastAccess;
	}


	public void setLastAccess(Date lastAccess) {
		this.lastAccess = lastAccess;
	}


	public String getLastUrl() {
		return lastUrl;
	}


	public void setLastUrl(String lastUrl) {
		this.lastUrl = lastUrl;
	}


	public UserOnlineStatus(String id, User user) {
		super(id, user);
		// TODO Auto-generated constructor stub
		lastAccess = new Date();
		lastUrl = "";
	}
	
	
	public void updateOnlineStatus(String url) {
		lastAccess = new Date();
		lastUrl = url;
	}


}
