package stimulatedplanning;

import java.io.Serializable;

public class GenericDescriptor implements Serializable {
	
	protected String id;
	protected String title;
	protected String description;
	protected String url;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	public GenericDescriptor() {
		super();
	}

	public GenericDescriptor(String id, String title, String description, String url) {
		super();
		this.id = id;
		this.title = title;
		this.description = description;
		this.url = url;
		
		if (!(this instanceof GenericUserObject)) {
			StimulatedPlanningFactory.addObject(this);
		}
	}
	
	

}
