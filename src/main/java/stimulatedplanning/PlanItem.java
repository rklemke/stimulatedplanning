package stimulatedplanning;

public class PlanItem {
	LessonDescriptor lesson;
	String jsonPlanItem;
	String id;
	LessonStatus status;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
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
	
	public PlanItem(String id, LessonDescriptor lesson, String jsonPlanItem) {
		super();
		this.id = id;
		this.lesson = lesson;
		this.jsonPlanItem = jsonPlanItem;
		this.status = LessonStatus.INITIAL;
	}

}
