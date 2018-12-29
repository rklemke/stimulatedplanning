package senseofcommunity;

import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;

import stimulatedplanning.GenericUserObject;
import stimulatedplanning.User;
import stimulatedplanning.util.HashArrayList;

public class UserOnlineStatus extends GenericUserObject {
	
	Date lastAccess;
	String lastUrl;
	int lastIndex;
	
	public static final int ONLINE_SECONDS = 120;
	public static final int RECENT_SECONDS = 600;
	

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
	
	private boolean isInTimeframe(int maxSecondsAgo, int minSecondsAgo) {
		Date now = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(now);
		cal.add(Calendar.SECOND, -maxSecondsAgo);
		Date max = cal.getTime();
		cal.setTime(now);
		cal.add(Calendar.SECOND, -minSecondsAgo);
		Date min = cal.getTime();
		if ((maxSecondsAgo == 0 || getLastAccess().after(max)) 
				&& (minSecondsAgo == 0 || getLastAccess().before(min))) {
			return true;
		}
		return false;
	}

	public boolean isOnline() {
		return isInTimeframe(ONLINE_SECONDS, 0);
	}

	public boolean isRecent() {
		return isInTimeframe(RECENT_SECONDS, ONLINE_SECONDS);
	}

	public boolean isOffline() {
		return isInTimeframe(0, RECENT_SECONDS);
	}


}
