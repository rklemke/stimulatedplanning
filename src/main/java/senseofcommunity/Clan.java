package senseofcommunity;

import java.util.Calendar;
import java.util.Date;
import java.util.ListIterator;

import stimulatedplanning.LessonDescriptor;
import stimulatedplanning.StimulatedPlanningFactory;
import stimulatedplanning.User;
import stimulatedplanning.util.HashArrayList;

public class Clan extends InformationObject {


	public Clan() {
		// TODO Auto-generated constructor stub
		userStati = new HashArrayList<UserOnlineStatus>();
	}

	public Clan(String id, String title, String description, String url) {
		super(id, title, description, url);
		// TODO Auto-generated constructor stub
		userStati = new HashArrayList<UserOnlineStatus>();
	}

	protected String clanLogo;
	public String getClanLogo() {
		if (clanLogo != null && clanLogo.length()>0) {
			return clanLogo;
		}
		return "/img/clan/"+title+"Default.png";
	}
	public void setClanLogo(String clanLogo) {
		this.clanLogo = clanLogo;
	}
	
	protected HashArrayList<UserOnlineStatus> userStati;
	public void addUser(User user) {
//		UserOnlineStatus onlineStatus = StimulatedPlanningFactory.getUserOnlineStatus(user);
		UserOnlineStatus onlineStatus = user.getOnlineStatus();
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


	/**
	 * Retrieves all users of the clan, that have been active in the given time interval.
	 * if maxSecondsAgo == 0, every activity earlier than minSeconds ago is counted.
	 * if minSecondsAgo == 0, every activity later than maxSeconds ago is counted.
	 * @param maxSecondsAgo positive amount of seconds or 0 to ignore border
	 * @param minSecondsAgo positive amount of seconds or 0 to ignore border
	 * @return
	 */
	private HashArrayList<UserOnlineStatus> getUsersInTimeframe(int maxSecondsAgo, int minSecondsAgo) {
		Date now = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(now);
		cal.add(Calendar.SECOND, -maxSecondsAgo);
		Date max = cal.getTime();
		cal.setTime(now);
		cal.add(Calendar.SECOND, -minSecondsAgo);
		Date min = cal.getTime();
		HashArrayList<UserOnlineStatus> recentUsers = new HashArrayList<>();
		for (UserOnlineStatus status: userStati) {
			if ((maxSecondsAgo == 0 || status.getLastAccess().after(max)) 
					&& (minSecondsAgo == 0 || status.getLastAccess().before(min))) {
				recentUsers.add(status);
			}
		}
		return recentUsers;
	}
	
	public HashArrayList<UserOnlineStatus> getOnlineUsers() {
		return getUsersInTimeframe(120, 0);
	}

	public HashArrayList<UserOnlineStatus> getRecentUsers() {
		return getUsersInTimeframe(600, 120);
	}

	public HashArrayList<UserOnlineStatus> getOfflineUsers() {
		return getUsersInTimeframe(0, 600);
	}

	public HashArrayList<UserOnlineStatus> getOnlineUsersSorted(UserOnlineStatus userStatus) {
		HashArrayList<UserOnlineStatus> sorted = getOnlineUsers();
		if (userStatus != null && sorted != null) {
			sorted.sort(userStatus.relativeComparator());
		}
		return sorted;
	}

	public HashArrayList<UserOnlineStatus> getRecentUsersSorted(UserOnlineStatus userStatus) {
		HashArrayList<UserOnlineStatus> sorted = getRecentUsers();
		if (userStatus != null && sorted != null) {
			sorted.sort(userStatus.relativeComparator());
		}
		return sorted;
	}

	public HashArrayList<UserOnlineStatus> getOfflineUsersSorted(UserOnlineStatus userStatus) {
		HashArrayList<UserOnlineStatus> sorted = getOfflineUsers();
		if (userStatus != null && sorted != null) {
			sorted.sort(userStatus.relativeComparator());
		}
		return sorted;
	}

}
