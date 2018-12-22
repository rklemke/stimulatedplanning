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

	
	public InformationObject() {
		// TODO Auto-generated constructor stub
	}

	public InformationObject(String id, String title, String description, String url) {
		super(id, title, description, url);
		// TODO Auto-generated constructor stub
	}

}
