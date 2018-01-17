package stimulatedplanning;

public class UserProfile extends GenericUserObject {
	
	protected String email;
	protected String fullName;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public UserProfile(String id, User user, String email) {
		super(id, user);
		
		this.email = email;
	}

}
