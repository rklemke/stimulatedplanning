package stimulatedplanning;

import java.io.Serializable;

import senseofcommunity.Clan;
import senseofcommunity.UserOnlineStatus;

public class User implements Serializable {

	protected String name;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	protected String id;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	protected boolean treatmentGroup;
	public boolean isTreatmentGroup() {
		return treatmentGroup;
	}
	public void setTreatmentGroup(boolean treatmentGroup) {
		this.treatmentGroup = treatmentGroup;
	}
	
	protected Clan clan;
	public Clan getClan() {
		return clan;
	}
	public void setClan(Clan clan) {
		this.clan = clan;
	}
	
	protected UserOnlineStatus onlineStatus;
	public UserOnlineStatus getOnlineStatus() {
		return onlineStatus;
	}
	public void setOnlineStatus(UserOnlineStatus onlineStatus) {
		this.onlineStatus = onlineStatus;
	}
	
	protected String avatarUrl;
	public String getAvatarUrl() {
		if (avatarUrl != null && avatarUrl.length()>0) {
			return avatarUrl;
		}
		return "/img/profile/avatar.png";
	}
	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
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
