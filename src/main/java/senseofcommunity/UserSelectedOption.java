package senseofcommunity;

import stimulatedplanning.GenericUserObject;
import stimulatedplanning.User;

public class UserSelectedOption extends GenericUserObject {
	
	protected SelectionObject selectionObject;
	protected SelectionOption selectedOption;

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
