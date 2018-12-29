package senseofcommunity;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import stimulatedplanning.PersistentStore;
import stimulatedplanning.StimulatedPlanningFactory;
import stimulatedplanning.User;
import stimulatedplanning.util.HashArrayList;

public class SelectionObject extends InformationObject {

	public SelectionObject() {
		// TODO Auto-generated constructor stub
		options = new HashArrayList<>();
	}

	public SelectionObject(String id, String title, String description, String url) {
		super(id, title, description, url);
		// TODO Auto-generated constructor stub
		options = new HashArrayList<>();
	}
	
	protected HashArrayList<SelectionOption> options;
	public void addOption(SelectionOption option) {
		this.options.add(option);
	}
	public ListIterator<SelectionOption> getOptions() {
		return options.listIterator();
	}
	public List<SelectionOption> getOptionList() {
		return options.unmodifiableList();
	}
	public int getOptionCount() {
		return options.size();
	}

	protected SelectionObjectType type;
	public SelectionObjectType getType() {
		return type;
	}
	public void setType(SelectionObjectType type) {
		this.type = type;
	}
	public boolean isSingleUser() {
		return (type == SelectionObjectType.SINGLE_USER_MULTI_TEST || 
				type == SelectionObjectType.SINGLE_USER_SINGLE_TEST || 
				type == SelectionObjectType.SINGLE_USER_SELECTION);
	}
	public boolean isClan() {
		return (type == SelectionObjectType.CLAN_MULTI_TEST || 
				type == SelectionObjectType.CLAN_SINGLE_TEST || 
				type == SelectionObjectType.CLAN_SELECTION);
	}
	public boolean isVoting() {
		return (type == SelectionObjectType.SINGLE_USER_SELECTION|| 
				type == SelectionObjectType.CLAN_SELECTION);
	}
	public boolean isSingleVoting() {
		return (type == SelectionObjectType.SINGLE_USER_SELECTION);
	}
	public boolean isMultiVoting() {
		return (type == SelectionObjectType.CLAN_SELECTION);
	}
	public boolean isTest() {
		return (type == SelectionObjectType.SINGLE_USER_MULTI_TEST || 
				type == SelectionObjectType.SINGLE_USER_SINGLE_TEST || 
				type == SelectionObjectType.CLAN_MULTI_TEST || 
				type == SelectionObjectType.CLAN_SINGLE_TEST);
	}
	public boolean isMultipleChoiceTest() {
		return (type == SelectionObjectType.SINGLE_USER_MULTI_TEST || 
				type == SelectionObjectType.CLAN_MULTI_TEST);
	}
	public boolean isSingleChoiceTest() {
		return (type == SelectionObjectType.SINGLE_USER_SINGLE_TEST || 
				type == SelectionObjectType.CLAN_SINGLE_TEST);
	}

	
	protected SelectionObjectPurpose purpose;
	public SelectionObjectPurpose getPurpose() {
		return purpose;
	}
	public void setPurpose(SelectionObjectPurpose purpose) {
		this.purpose = purpose;
	}
	public boolean isTestPurpose() {
		return purpose == SelectionObjectPurpose.TEST;
	}
	public boolean isAvatarPurpose() {
		return isClanAvatarPurpose() || isUserAvatarPurpose();
	}
	public boolean isUserAvatarPurpose() {
		return purpose == SelectionObjectPurpose.USER_AVATAR;
	}
	public boolean isClanAvatarPurpose() {
		return purpose == SelectionObjectPurpose.CLAN_AVATAR;
	}
	public boolean isClanIdentityPurpose() {
		return purpose == SelectionObjectPurpose.CLAN_IDENTITY;
	}


	public void clearSelectionForUser(User user) {
		UserSelectedOption selectedOption;
		for (SelectionOption option : options) {
			selectedOption = PersistentStore.readUserSelectionOption(user, this, option);
			PersistentStore.deleteGenericEntity(selectedOption);
		}
		
	}


	public void setSelectionForUser(User user, ArrayList<SelectionOption> selection) {
		clearSelectionForUser(user);
		for (SelectionOption option : selection) {
			StimulatedPlanningFactory.createUserSelectedOption(user, this, option);
		}
	}



}
