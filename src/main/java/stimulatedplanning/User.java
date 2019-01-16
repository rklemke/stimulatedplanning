package stimulatedplanning;

import java.io.Serializable;

import senseofcommunity.Clan;
import senseofcommunity.UserOnlineStatus;
import stimulatedplanning.util.IObjectWithId;

public class User implements Serializable, IObjectWithId {

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
	
	//protected Clan clan;
	protected String clanId;
	public Clan getClan() {
		//return clan;
		return StimulatedPlanningFactory.getClan(clanId);
	}
	public void setClan(Clan clan) {
		this.clanId = clan.getId();
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
		return "/img/profile/user.png";
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
