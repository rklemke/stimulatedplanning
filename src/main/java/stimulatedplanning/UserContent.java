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

	public boolean trackLearningProgress(UserPlan userPlan, String contentUrl, String activityType) {
		boolean updated = false;
		if (contentUrl != null && contentUrl.equals(this.getContent().getUrl())) {
			if (StimulatedPlanningFactory.ACTIVITY_TYPE_ACCESS.equals(activityType) && status == LessonStatus.INITIAL) {
				status = LessonStatus.STARTED;
				updated = true;
			} else if (StimulatedPlanningFactory.ACTIVITY_TYPE_ACCESS.equals(activityType) && status == LessonStatus.STARTED) {
				status = LessonStatus.COMPLETED;
				updated = true;
			} else if (StimulatedPlanningFactory.ACTIVITY_TYPE_COMPLETE.equals(activityType) && status == LessonStatus.STARTED) {
				status = LessonStatus.COMPLETED;
				updated = true;
			}
		}
		return updated;
	}

	public UserContent(String id, User user, ContentDescriptor content) {
		super(id, user);
		this.content = content;
		this.status = LessonStatus.INITIAL;
		// TODO Auto-generated constructor stub
	}

}
