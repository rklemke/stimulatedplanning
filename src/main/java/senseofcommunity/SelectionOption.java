package senseofcommunity;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import stimulatedplanning.PersistentStore;
import stimulatedplanning.User;

public class SelectionOption extends InformationObject {
	
	protected boolean isCorrect;

	public boolean isCorrect() {
		return isCorrect;
	}

	public void setCorrect(boolean isCorrect) {
		this.isCorrect = isCorrect;
	}

	public SelectionOption() {
		// TODO Auto-generated constructor stub
	}

	public SelectionOption(String id, String title, String description, String url) {
		super(id, title, description, url, true, true, true);
		// TODO Auto-generated constructor stub
	}
	
	private HashMap<String, UserSelectedOption> userSelectedOptions = null;
	private Date nextUpdate = null;
	private void initUserSelectedOptions(SelectionObject currentSelectionObject, boolean force) {
		if (force || userSelectedOptions == null || nextUpdate.before(new Date())) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			cal.add(cal.SECOND, 10);
			nextUpdate = cal.getTime();
			userSelectedOptions = PersistentStore.readUserSelectionOptions(currentSelectionObject, this);
		}
	}
	
	public UserSelectedOption getUserSelectedOption(User user, SelectionObject currentSelectionObject) {
		initUserSelectedOptions(currentSelectionObject, false);
		UserSelectedOption userOption = userSelectedOptions.get(user.getId());
		
		return userOption;

	}
	
	public void addUserSelectedOption(UserSelectedOption option) {
		initUserSelectedOptions(option.getSelectionObject(), true);
		userSelectedOptions.put(option.getUser().getId(), option);
	}
	
	public void removeUserSelectedOption(UserSelectedOption option) {
		initUserSelectedOptions(option.getSelectionObject(), true);
		userSelectedOptions.remove(option.getUser().getId());
	}
	
	
}
