package stimulatedplanning;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import com.google.gson.GsonBuilder;

public class PlanItem extends GenericUserObject {
	LessonDescriptor lesson;
	String jsonPlanItem;
	LessonStatus status;
	PlanStatus planStatus;
	PlanCompletionStatus planCompletionStatus;

	LocalDate startDate;
	LocalDate endDate;

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

		GsonBuilder builder = new GsonBuilder();
		Map calendarItem = builder.create().fromJson(jsonPlanItem, Map.class);
		String startTime = (String)calendarItem.get("start");
		String endTime = (String)calendarItem.get("end");
		
		if (startTime != null && endTime != null) {
			DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
			LocalDate newStartDate = LocalDate.parse(startTime, formatter);
			LocalDate newEndDate = LocalDate.parse(endTime, formatter);
			
			if (startDate != null && endDate != null && newStartDate != null && newEndDate != null) {
				if (newStartDate.isAfter(startDate) || newEndDate.isAfter(endDate) || newStartDate.isBefore(startDate) || newEndDate.isBefore(endDate)) {
					this.startDate = newStartDate;
					this.endDate = newEndDate;
					if (this.planStatus == PlanStatus.PLANNED) {
						this.planStatus = PlanStatus.RE_PLANNED;
					}
					trackPlanStatus();
				}
			}
		}
	}

	public LessonStatus getStatus() {
		return status;
	}
	public void setStatus(LessonStatus status) {
		this.status = status;
	}
	
	public PlanStatus getPlanStatus() {
		return planStatus;
	}
	public void setPlanStatus(PlanStatus planStatus) {
		this.planStatus = planStatus;
	}
	public PlanCompletionStatus getPlanCompletionStatus() {
		return planCompletionStatus;
	}
	public void setPlanCompletionStatus(PlanCompletionStatus planCompletionStatus) {
		this.planCompletionStatus = planCompletionStatus;
	}
	
	protected void initDates() {
		GsonBuilder builder = new GsonBuilder();
		Map calendarItem = builder.create().fromJson(jsonPlanItem, Map.class);
		String startTime = (String)calendarItem.get("start");
		String endTime = (String)calendarItem.get("end");
		DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
		
		startDate = LocalDate.parse(startTime, formatter);
		endDate = LocalDate.parse(endTime, formatter);
	}
	
	public boolean trackLearningProgress(UserPlan userPlan, UserLesson userLesson, String contentUrl, String activityType) {
		if (userLesson.getStatus().compareTo(this.getStatus()) > 0) {
			this.setStatus(userLesson.getStatus());
			trackPlanStatus();
			return true;
		}
		return false;
	}
	
	protected boolean trackPlanStatus() {
		boolean updated = false;
		
		LocalDate date = LocalDate.now();

		switch (planCompletionStatus) {
			case OPEN : 
				if (date.isAfter(endDate)) {
					if (status != LessonStatus.COMPLETED) {
						planCompletionStatus = PlanCompletionStatus.DELAYED;
						updated = true;
					} else {
						planCompletionStatus = PlanCompletionStatus.COMPLETED_DELAYED;
						updated = true;
					}
				} else if (date.isAfter(startDate)) {
					if (status == LessonStatus.COMPLETED) {
						planCompletionStatus = PlanCompletionStatus.COMPLETED_ON_TIME;
						updated = true;
					}
				} else if (date.isBefore(startDate)) {
					if (status == LessonStatus.COMPLETED) {
						planCompletionStatus = PlanCompletionStatus.COMPLETED_EARLY;
						updated = true;
					}
				}
				break;
			case DELAYED :
				if (planStatus == PlanStatus.PLANNED) {
					if (status == LessonStatus.COMPLETED) {
						planCompletionStatus = PlanCompletionStatus.COMPLETED_DELAYED;
						updated = true;
					}
				} else if (planStatus == PlanStatus.RE_PLANNED) {
					if (date.isAfter(endDate)) {
						if (status == LessonStatus.COMPLETED) {
							planCompletionStatus = PlanCompletionStatus.COMPLETED_DELAYED;
							updated = true;
						}
					} else if (date.isAfter(startDate)) {
						if (status == LessonStatus.COMPLETED) {
							planCompletionStatus = PlanCompletionStatus.COMPLETED_ON_TIME;
							updated = true;
						} else {
							planCompletionStatus = PlanCompletionStatus.OPEN;
							updated = true;
						}
					} else if (date.isBefore(startDate)) {
						if (status == LessonStatus.COMPLETED) {
							planCompletionStatus = PlanCompletionStatus.COMPLETED_EARLY;
							updated = true;
						} else {
							planCompletionStatus = PlanCompletionStatus.OPEN;
							updated = true;
						}
					}
				}
				break;
			default :
				break;
		}
		
		return updated;
	}
	

	public PlanItem(String id, User user, LessonDescriptor lesson, String jsonPlanItem) {
		super(id, user);
		this.lesson = lesson;
		this.jsonPlanItem = jsonPlanItem;
		this.status = LessonStatus.INITIAL;
		this.planStatus = PlanStatus.PLANNED;
		this.planCompletionStatus = PlanCompletionStatus.OPEN;
		
		initDates();
	}

}
