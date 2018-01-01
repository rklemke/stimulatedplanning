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
	
	Map calendarItem;
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
		calendarItem = builder.create().fromJson(jsonPlanItem, Map.class);
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
		calendarItem = builder.create().fromJson(jsonPlanItem, Map.class);
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
	
	public boolean trackPlanStatus() {
		boolean updated = false;
		
		LocalDate date = LocalDate.now();

		switch (planCompletionStatus) {
			case OPEN : 
				if (date.isAfter(endDate)) {
					if (!LessonStatus.COMPLETED.equals(status)) {
						planCompletionStatus = PlanCompletionStatus.DELAYED;
						updated = true;
					} else {
						planCompletionStatus = PlanCompletionStatus.COMPLETED_DELAYED;
						updated = true;
					}
				} else if (date.isAfter(startDate)) {
					if (LessonStatus.COMPLETED.equals(status)) {
						planCompletionStatus = PlanCompletionStatus.COMPLETED_ON_TIME;
						updated = true;
					}
				} else if (date.isBefore(startDate)) {
					if (LessonStatus.COMPLETED.equals(status)) {
						planCompletionStatus = PlanCompletionStatus.COMPLETED_EARLY;
						updated = true;
					}
				}
				break;
			case DELAYED :
				if (PlanStatus.PLANNED.equals(planStatus)) {
					if (LessonStatus.COMPLETED.equals(status)) {
						planCompletionStatus = PlanCompletionStatus.COMPLETED_DELAYED;
						updated = true;
					}
				} else if (PlanStatus.RE_PLANNED.equals(planStatus)) {
					if (date.isAfter(endDate)) {
						if (LessonStatus.COMPLETED.equals(status)) {
							planCompletionStatus = PlanCompletionStatus.COMPLETED_DELAYED;
							updated = true;
						}
					} else if (date.isAfter(startDate)) {
						if (LessonStatus.COMPLETED.equals(status)) {
							planCompletionStatus = PlanCompletionStatus.COMPLETED_ON_TIME;
							updated = true;
						} else {
							planCompletionStatus = PlanCompletionStatus.OPEN;
							updated = true;
						}
					} else if (date.isBefore(startDate)) {
						if (LessonStatus.COMPLETED.equals(status)) {
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
		
		if (updated) {
			updateMapAndJson();
		}
		
		return updated;
	}
	
	public void updateMapAndJson() {
		if (status.equals(LessonStatus.COMPLETED)) {
			calendarItem.put("class", new String[] {"plan-a", "plan-done"});
			calendarItem.put("color", "#afafaf");
		} else if (planCompletionStatus.equals(PlanCompletionStatus.DELAYED)) {
			calendarItem.put("class", new String[] {"plan-a", "plan-late"});
			calendarItem.put("color", "#cd0000");
		} else {
			calendarItem.put("class", new String[] {"plan-a", "plan-ok"});
			calendarItem.put("color", "#3a87ad");
		}
		GsonBuilder builder = new GsonBuilder();
		jsonPlanItem = builder.create().toJson(calendarItem);
		System.out.println("Title: "+calendarItem.get("title")+", status: "+status+", planCompletionStatus: "+planCompletionStatus+", jsonPlanItem: "+jsonPlanItem);
	}
	

	public PlanItem(String id, User user, LessonDescriptor lesson, String jsonPlanItem) {
		super(id, user);
		this.lesson = lesson;
		this.jsonPlanItem = jsonPlanItem;
		this.status = LessonStatus.INITIAL;
		this.planStatus = PlanStatus.PLANNED;
		this.planCompletionStatus = PlanCompletionStatus.OPEN;
		
		initDates();
//		trackPlanStatus();
	}

}
