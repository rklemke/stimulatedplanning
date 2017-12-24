package stimulatedplanning;

public class UserContent extends GenericUserObject {
	ContentDescriptor content;
	LessonStatus status;

	public ContentDescriptor getContent() {
		return content;
	}

	public void setContent(ContentDescriptor content) {
		this.content = content;
	}

	public LessonStatus getStatus() {
		return status;
	}

	public void setStatus(LessonStatus status) {
		this.status = status;
	}

	public UserContent(String id, User user, ContentDescriptor content) {
		super(id, user);
		this.content = content;
		this.status = LessonStatus.INITIAL;
		// TODO Auto-generated constructor stub
	}

}
