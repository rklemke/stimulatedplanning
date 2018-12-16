package senseofcommunity;

import java.util.Date;

import stimulatedplanning.GenericUserObject;
import stimulatedplanning.User;

public class UserSelectedOption extends GenericUserObject {
	
	protected Date lastAccess;
	protected SelectionObject selectionObject;
	protected SelectionOption selectedOption;

	public Date getLastAccess() {
		return lastAccess;
	}


	public void setLastAccess(Date lastAccess) {
		this.lastAccess = lastAccess;
	}


	public SelectionObject getSelectionObject() {
		return selectionObject;
	}

	public void setSelectionObject(SelectionObject selectionObject) {
		this.selectionObject = selectionObject;
	}

	public SelectionOption getSelectedOption() {
		return selectedOption;
	}

	public void setSelectedOption(SelectionOption selectedOption) {
		this.selectedOption = selectedOption;
	}

	public UserSelectedOption(String id, User user) {
		super(id, user);
		// TODO Auto-generated constructor stub
	}

}
