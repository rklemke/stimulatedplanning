package senseofcommunity;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import stimulatedplanning.PersistentStore;
import stimulatedplanning.StimulatedPlanningFactory;
import stimulatedplanning.User;
import stimulatedplanning.util.HashArrayList;

public class SelectionObject extends InformationObject {
	protected HashArrayList<SelectionOption> options;
	protected SelectionObjectType type;

	public SelectionObject() {
		// TODO Auto-generated constructor stub
		options = new HashArrayList<>();
	}

	public SelectionObject(String id, String title, String description, String url) {
		super(id, title, description, url);
		// TODO Auto-generated constructor stub
		options = new HashArrayList<>();
	}
	
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

	public SelectionObjectType getType() {
		return type;
	}

	public void setType(SelectionObjectType type) {
		this.type = type;
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
