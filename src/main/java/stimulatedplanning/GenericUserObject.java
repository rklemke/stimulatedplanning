package stimulatedplanning;

import java.io.Serializable;
import java.util.logging.Logger;

public class GenericUserObject extends GenericDescriptor {
	private static final Logger log = Logger.getLogger(GenericUserObject.class.getName());   

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
