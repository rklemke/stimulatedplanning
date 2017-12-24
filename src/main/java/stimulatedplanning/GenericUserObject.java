package stimulatedplanning;

import java.io.Serializable;

public class GenericUserObject implements Serializable {
	String id;
	User user;

	public String getId() {
		return id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public GenericUserObject(String id, User user) {
		this.id = id;
		this.user = user;
		// TODO Auto-generated constructor stub
	}

}
