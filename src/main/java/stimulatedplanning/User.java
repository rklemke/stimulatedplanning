package stimulatedplanning;

import java.io.Serializable;

import senseofcommunity.Clan;
import senseofcommunity.UserOnlineStatus;

public class User implements Serializable {
	protected String name;
	protected String id;
	protected boolean treatmentGroup;
	protected Clan clan;
	protected UserOnlineStatus onlineStatus;

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
	
	public UserOnlineStatus getOnlineStatus() {
		return onlineStatus;
	}
	public void setOnlineStatus(UserOnlineStatus onlineStatus) {
		this.onlineStatus = onlineStatus;
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
