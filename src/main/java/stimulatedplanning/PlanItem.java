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
	
	public PlanItem(String id, User user, LessonDescriptor lesson, String jsonPlanItem) {
		super(id, user);
		this.lesson = lesson;
		this.jsonPlanItem = jsonPlanItem;
		this.status = LessonStatus.INITIAL;
	}

}
