package senseofcommunity;

import java.util.Calendar;
import java.util.Date;
import java.util.ListIterator;

import stimulatedplanning.LessonDescriptor;
import stimulatedplanning.StimulatedPlanningFactory;
import stimulatedplanning.User;
import stimulatedplanning.util.HashArrayList;

public class Clan extends InformationObject {

	protected HashArrayList<UserOnlineStatus> userStati;

	public Clan() {
		// TODO Auto-generated constructor stub
		userStati = new HashArrayList<UserOnlineStatus>();
	}

	public Clan(String id, String title, String description, String url) {
		super(id, title, description, url);
		// TODO Auto-generated constructor stub
		userStati = new HashArrayList<UserOnlineStatus>();
	}

	public void addUser(User user) {
		UserOnlineStatus onlineStatus = StimulatedPlanningFactory.getUserOnlineStatus(user);
		userStati.add(onlineStatus);
	}

	public void addUserOnlineStatus(UserOnlineStatus status) {
		userStati.add(status);
	}

	public ListIterator<UserOnlineStatus> getUserOnlineStatus() {
		return userStati.listIterator();
	}
	
	public int userCount() {
		return userStati.size();
	}


	private HashArrayList<UserOnlineStatus> getUsersInTimeframe(int maxMinutesAgo, int minMinutesAgo) {
		Date now = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(now);
		cal.add(Calendar.MINUTE, -maxMinutesAgo);
		Date min = cal.getTime();
		cal.setTime(now);
		cal.add(Calendar.MINUTE, -minMinutesAgo);
		Date max = cal.getTime();
		HashArrayList<UserOnlineStatus> recentUsers = new HashArrayList<>();
		for (UserOnlineStatus status: userStati) {
			if ((minMinutesAgo == 0 || status.getLastAccess().after(min)) 
					&& (maxMinutesAgo == 0 || status.getLastAccess().before(max))) {
				recentUsers.add(status);
			}
		}
		return recentUsers;
	}
	
	public HashArrayList<UserOnlineStatus> getOnlineUsers() {
		return getUsersInTimeframe(2, 0);
	}

	public HashArrayList<UserOnlineStatus> getRecentUsers() {
		return getUsersInTimeframe(10, 2);
	}

	public HashArrayList<UserOnlineStatus> getOfflineUsers() {
		return getUsersInTimeframe(0, 10);
	}
}
