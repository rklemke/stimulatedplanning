package senseofcommunity;

import stimulatedplanning.GenericDescriptor;

public class InformationObject extends GenericDescriptor {
	
	private String content;
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}

	
	private int sequence=-1;
	public int getSequence() {
		return sequence;
	}
	public void setSequence(int sequence) {
		this.sequence = sequence;
	}
	
	private boolean isControlGroupVisible = true;
	private boolean isClanAVisible = true;
	private boolean isClanBVisible = true;
	public boolean isControlGroupVisible() {
		return isControlGroupVisible;
	}
	public boolean isTreatmentGroupVisible() {
		return isClanAVisible || isClanBVisible;
	}
	public boolean isClanAVisible() {
		return isClanAVisible;
	}
	public boolean isClanBVisible() {
		return isClanBVisible;
	}

	public InformationObject() {
		// TODO Auto-generated constructor stub
	}

	public InformationObject(String id, String title, String description, String url, boolean isControlGroupVisible, boolean isClanAVisible, boolean isClanBVisible) {
		super(id, title, description, url);
		// TODO Auto-generated constructor stub
		this.isControlGroupVisible = isControlGroupVisible;
		this.isClanAVisible = isClanAVisible;
		this.isClanBVisible = isClanBVisible;
	}

}
