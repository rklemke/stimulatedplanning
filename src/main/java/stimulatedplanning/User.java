package stimulatedplanning;

import java.io.Serializable;

import senseofcommunity.Clan;

public class User implements Serializable {
	protected String name;
	protected String id;
	protected boolean treatmentGroup;
	protected Clan clan;

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
	
	public Clan getClan() {
		return clan;
	}
	public void setClan(Clan clan) {
		this.clan = clan;
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
