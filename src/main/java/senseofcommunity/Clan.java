package senseofcommunity;

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
	
}
