package stimulatedplanning;

public class PlanItem extends GenericUserObject {
	LessonDescriptor lesson;
	String jsonPlanItem;
	LessonStatus status;

	public LessonDescriptor getLesson() {
		return lesson;
	}
	public void setLesson(LessonDescriptor lesson) {
		this.lesson = lesson;
	}

	public String getJsonPlanItem() {
		return jsonPlanItem;
	}
	public void setJsonPlanItem(String jsonPlanItem) {
		this.jsonPlanItem = jsonPlanItem;
	}

	public LessonStatus getStatus() {
		return status;
	}
	public void setStatus(LessonStatus status) {
		this.status = status;
	}
	
	public boolean trackLearningProgress(UserPlan userPlan, UserLesson userLesson, String contentUrl, String activityType) {
		if (userLesson.getStatus().compareTo(this.getStatus()) > 0) {
			this.setStatus(userLesson.getStatus());
			// TODO: what else to keep track of: timing with respect to planned time? 
			return true;
		}
		return false;
	}

	public PlanItem(String id, User user, LessonDescriptor lesson, String jsonPlanItem) {
		super(id, user);
		this.lesson = lesson;
		this.jsonPlanItem = jsonPlanItem;
		this.status = LessonStatus.INITIAL;
	}

}
