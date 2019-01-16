package senseofcommunity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.ListIterator;

import stimulatedplanning.GenericDescriptor;
import stimulatedplanning.LessonDescriptor;
import stimulatedplanning.PersistentStore;
import stimulatedplanning.StimulatedPlanningFactory;
import stimulatedplanning.User;
import stimulatedplanning.util.HashArrayList;

public class Clan extends InformationObject {

	public static final String CLAN_1_ID = "clan-1";
	public static final String CLAN_2_ID = "clan-2";

	public Clan() {
		// TODO Auto-generated constructor stub
	}

	public Clan(String id, String title, String description, String url) {
		super(id, title, description, url, true, true, true);
		// TODO Auto-generated constructor stub
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
	
	protected HashArrayList<UserOnlineStatus> userStati = null;
	private void ensureUserStati() {
		if (userStati == null) {
			userStati = new HashArrayList<UserOnlineStatus>();
			ArrayList<GenericDescriptor> relationList;
			try {
				relationList = PersistentStore.readToManyRelation(this, "userStati", UserOnlineStatus.class.getName(), true, new HashMap<String, Object>());
				for (GenericDescriptor generic : relationList) {
					userStati.add((UserOnlineStatus)generic);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	public void addUser(User user) {
		ensureUserStati();
//		UserOnlineStatus onlineStatus = StimulatedPlanningFactory.getUserOnlineStatus(user);
		UserOnlineStatus onlineStatus = user.getOnlineStatus();
		userStati.add(onlineStatus);
	}

	public void addUserOnlineStatus(UserOnlineStatus status) {
		ensureUserStati();
		userStati.add(status);
	}

	public void updateUserOnlineStatus(UserOnlineStatus status) {
		ensureUserStati();
		userStati.addOrReplace(status);
	}

	public ListIterator<UserOnlineStatus> getUserOnlineStatus() {
		ensureUserStati();
		return userStati.listIterator();
	}
	
	public int userCount() {
		ensureUserStati();
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
		ensureUserStati();
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
		return getUsersInTimeframe(UserOnlineStatus.ONLINE_SECONDS, 0);
	}

	public HashArrayList<UserOnlineStatus> getRecentUsers() {
		return getUsersInTimeframe(UserOnlineStatus.RECENT_SECONDS, UserOnlineStatus.ONLINE_SECONDS);
	}

	public HashArrayList<UserOnlineStatus> getOfflineUsers() {
		return getUsersInTimeframe(0, UserOnlineStatus.RECENT_SECONDS);
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
