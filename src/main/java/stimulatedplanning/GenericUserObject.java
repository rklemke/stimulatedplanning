package stimulatedplanning;

import java.io.Serializable;

public class GenericUserObject extends GenericDescriptor {
	User user;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public GenericUserObject(String id, User user) {
		super(id, "", "", "");
		this.user = user;
		// TODO Auto-generated constructor stub
	}

}
