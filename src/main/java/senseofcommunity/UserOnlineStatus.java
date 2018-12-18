package senseofcommunity;

import java.util.Comparator;
import java.util.Date;

import stimulatedplanning.GenericUserObject;
import stimulatedplanning.User;

public class UserOnlineStatus extends GenericUserObject {
	
	Date lastAccess;
	String lastUrl;
	int lastIndex;
	

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
	
	
	public void updateOnlineStatus(String url, int index) {
		lastAccess = new Date();
		lastUrl = url;
		lastIndex = index;
	}
	
	
	public Comparator<UserOnlineStatus> relativeComparator() {
		UserOnlineStatus referenceStatus = this;
		return new Comparator<UserOnlineStatus>() {
			public int compare(UserOnlineStatus s1, UserOnlineStatus s2) {
				return Math.abs(Math.abs(referenceStatus.lastIndex-s1.lastIndex)-Math.abs(referenceStatus.lastIndex-s2.lastIndex));
			}
		};
	}


}
