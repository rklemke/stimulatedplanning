package stimulatedplanning;

import java.io.Serializable;

public class User implements Serializable {
	protected String name;
	protected String id;
	protected boolean treatmentGroup;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public boolean isTreatmentGroup() {
		return treatmentGroup;
	}
	public void setTreatmentGroup(boolean treatmentGroup) {
		this.treatmentGroup = treatmentGroup;
	}
	
	public User(String name, String id) {
		super();
		this.name = name;
		this.id = id;
	}
	
	public User() {
		super();
	}
	
	
}
