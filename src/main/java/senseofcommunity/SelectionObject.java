package senseofcommunity;

import java.util.ListIterator;

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
	
	public int getOptionCount() {
		return options.size();
	}

	public SelectionObjectType getType() {
		return type;
	}

	public void setType(SelectionObjectType type) {
		this.type = type;
	}
	
	

}
