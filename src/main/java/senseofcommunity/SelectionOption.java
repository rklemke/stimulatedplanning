package senseofcommunity;

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
		super(id, title, description, url);
		// TODO Auto-generated constructor stub
	}

}
