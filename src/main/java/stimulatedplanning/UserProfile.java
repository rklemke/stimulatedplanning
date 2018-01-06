package stimulatedplanning;

public class UserProfile extends GenericUserObject {
	
	protected String email;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public UserProfile(String id, User user, String email) {
		super(id, user);
		
		this.email = email;
	}

}
