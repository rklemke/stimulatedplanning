package stimulatedplanning;

import java.io.Serializable;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.logging.Logger;

import stimulatedplanning.util.HashArrayList;

public class UserPlan extends GenericUserObject {
	private static final Logger log = Logger.getLogger(UserPlan.class.getName());   

	protected CourseDescriptor course;
	protected HashArrayList<UserGoal> goals;
	protected HashArrayList<PlanItem> planItems;
	protected String plannedTimePerWeek;
	protected boolean allCourseIntention = false;
	HashMap<String, String> completionStatusMap;
	protected String obstacles;
	protected String copingPlan;
	protected boolean intentionCompleted = false;
	
	public CourseDescriptor getCourse() {
		return course;
	}
	public void setCourse(CourseDescriptor course) {
		this.course = course;
	}

	public ListIterator<UserGoal> getGoals() {
		return goals.listIterator();
	}
	public void addGoal(UserGoal goal) {
		this.goals.add(goal);
	}
	
	public boolean hasPlannableGoals() {
		boolean hasPlannableGoals = false;
		for (UserGoal goal : goals) {
			if (goal.lessons.size()>0) {
				hasPlannableGoals = true;
				break;
			}
		}
		return hasPlannableGoals;
	}
	
	public void addPlanItem(PlanItem planItem) {
		if (planItem != null && planItem.getId() != null && planItem.getLesson() != null) {
			planItems.add(planItem);
			planItems.sort(Comparator.comparing(PlanItem::getStartDate));
		}
	}
	
	public Collection<PlanItem> getPlanItemsCollection() {
		return planItems.values();
	}

	public ListIterator<PlanItem> getPlanItems() {
		return planItems.listIterator();
	}

	public boolean hasPlanItem(String id) {
		return planItems.containsKey(id);
	}
	
	public boolean hasPlanItemForLesson(LessonDescriptor lesson) {
		for (PlanItem item : planItems) {
			if (item.getLesson().getId().equals(lesson.getId())) {
				return true;
			}
		}
		return false;
	}
	
	public PlanItem getPlanItemForLesson(LessonDescriptor lesson) {
		for (PlanItem item : planItems) {
			if (item.getLesson().getId().equals(lesson.getId())) {
				return item;
			}
		}
		return null;
	}
	
	public String getPlannedTimePerWeek() {
		return plannedTimePerWeek;
	}
	public void setPlannedTimePerWeek(String plannedTimePerWeek) {
		this.plannedTimePerWeek = plannedTimePerWeek;
	}
	public int getPlannedTimePerWeekAsInt() {
		try {
			return Integer.valueOf(plannedTimePerWeek);
		} catch (Exception e) {
			
		}
		return 1;
	}
	
	public boolean isAllCourseIntention() {
		return allCourseIntention;
	}
	public void setAllCourseIntention(boolean allCourseIntention) {
		this.allCourseIntention = allCourseIntention;
	}

	public HashMap<String, String> getCompletionStatusMap() {
		return completionStatusMap;
	}
	public void setCompletionStatusMap(HashMap<String, String> completionStatusMap) {
		this.completionStatusMap = completionStatusMap;
	}
	public String getObstacles() {
		return obstacles;
	}
	public void setObstacles(String obstacles) {
		this.obstacles = obstacles;
	}
	public String getCopingPlan() {
		return copingPlan;
	}
	public void setCopingPlan(String copingPlan) {
		this.copingPlan = copingPlan;
	}
	public boolean isIntentionCompleted() {
		return intentionCompleted;
	}
	public void setIntentionCompleted(boolean intentionCompleted) {
		this.intentionCompleted = intentionCompleted;
	}
	public Duration getPlanDuration() {
		Duration duration = Duration.ZERO;
		for (UserGoal goal : goals) {
			duration = duration.plus(goal.getGoalDuration());
		}
		return duration;
	}

	public boolean trackLearningProgress(String contentUrl, String contentId, String activityType) {
		boolean updated = false;

		for (UserGoal goal: goals) {
			if (goal.trackLearningProgress(this, contentUrl, contentId, activityType)) {
				updated = true;
			}
		}
		
		for (PlanItem planItem : planItems) {
			if (planItem.trackPlanStatus()) { // it is sufficient here to just track the plan status, since planItems affected by this request are already updated via the corresponding userLesson.
				updated = true;
			}
		}
		
		if (updated) {
			calculateAchievementRates();
		}

		return updated;
	}
	
	
	public void calculateAchievementRates() {
		log.info("updating achievment rates.");
		int userGoalTotal = goals.size();
		int plannedItemsTotal = planItems.size();
		
		int courseGoals = course.goals.size();
		int courseLessons = 0;
		int courseContents = 0;

		for (GoalDescriptor goal : course.goals) {
			courseLessons += goal.lessons.size();
			for (LessonDescriptor lesson : goal.lessons) {
				courseContents += lesson.contents.size();
			}
		}
		

		int plannableItems = 0;
		int userContentItems = 0;
		int achievedGoals = 0;
		int achievedLessons = 0;
		int achievedContents = 0;
		
		for (UserGoal goal : goals) {
			plannableItems += goal.lessons.size();
			for (UserLesson lesson : goal.lessons) {
				userContentItems += lesson.contents.size();
				for (UserContent content : lesson.contents) {
					if (content.getStatus() == LessonStatus.COMPLETED) {
						achievedContents++;
					}
				}
				if (lesson.getStatus() == LessonStatus.COMPLETED) {
					achievedLessons++;
				}
			}
			if (goal.getStatus() == LessonStatus.COMPLETED) {
				achievedGoals++;
			}
		}
		
		int planAchievementInitial = 0;
		int planAchievementStarted = 0;
		int planAchievementCompleted = 0;
		int planAchievementCompEarly = 0;
		int planAchievementCompOnTime = 0;
		int planAchievementCompLate = 0;
		int planAchievementOpen = 0;
		int planAchievementDelayed = 0;
		int planAchievementPlanned = 0;
		int planAchievementRePlanned = 0;
		
		for (PlanItem planItem : planItems) {
			switch (planItem.status ) {
			case INITIAL : 
				planAchievementInitial++;
				break;
			case STARTED : 
				planAchievementStarted++;
				break;
			case COMPLETED : 
				planAchievementCompleted++;
				break;
			}
			switch (planItem.planStatus ) {
			case PLANNED : 
				planAchievementPlanned++;
				break;
			case RE_PLANNED : 
				planAchievementRePlanned++;
				break;
			default : 
				break;
			}
			switch (planItem.planCompletionStatus ) {
			case OPEN : 
				planAchievementOpen++;
				break;
			case DELAYED : 
				planAchievementDelayed++;
				break;
			case COMPLETED_EARLY : 
				planAchievementCompEarly++;
				break;
			case COMPLETED_ON_TIME : 
				planAchievementCompOnTime++;
				break;
			case COMPLETED_DELAYED : 
				planAchievementCompLate++;
				break;
			default : 
				break;
			}
		}

		int overallGoalAchievementRate = (courseGoals > 0 ? (100 * achievedGoals / courseGoals) : 0);
		int overallLessonAchievementRate = (courseLessons > 0 ? (100 * achievedLessons / courseLessons) : 0);
		int overallContentAchievementRate = (courseContents > 0 ? (100 * achievedContents / courseContents) : 0);
		
		int userGoalAchievementRate = (userGoalTotal > 0 ? (100 * achievedGoals / userGoalTotal) : 0);
		int userLessonAchievementRate = (plannableItems > 0 ? (100 * achievedLessons / plannableItems) : 0);
		int userContentAchievementRate = (userContentItems > 0 ? (100 * achievedContents / userContentItems) : 0);

		int userPlanAchievementRate = (plannedItemsTotal > 0 ? (100 * planAchievementCompleted / plannedItemsTotal) : 0);
		int userPlanOpenRate = (plannedItemsTotal > 0 ? (100 * planAchievementOpen / plannedItemsTotal) : 0);
		int userPlanDelayedRate = (plannedItemsTotal > 0 ? (100 * planAchievementDelayed / plannedItemsTotal) : 0);
		int userPlanAchEarlyRate = (plannedItemsTotal > 0 ? (100 * planAchievementCompEarly / plannedItemsTotal) : 0);
		int userPlanAchOnTimeRate = (plannedItemsTotal > 0 ? (100 * planAchievementCompOnTime / plannedItemsTotal) : 0);
		int userPlanAchLateRate = (plannedItemsTotal > 0 ? (100 * planAchievementCompLate / plannedItemsTotal) : 0);
		int userPlannedTotalRate = (plannableItems > 0 ? (100 * plannedItemsTotal / plannableItems) : 0);
		int userPlannedRate = (plannableItems > 0 ? (100 * planAchievementPlanned / plannableItems) : 0);
		int userRePlannedRate = (plannableItems > 0 ? (100 * planAchievementRePlanned / plannableItems) : 0);
		
		int userPlanNotPlanned = plannableItems - plannedItemsTotal;
		int userPlanNotPlannedRate = (plannableItems > 0 ? 100 * userPlanNotPlanned / plannableItems : 0);
		
		completionStatusMap.put("raw.courseGoals", String.valueOf(courseGoals));
		completionStatusMap.put("raw.courseLessons", String.valueOf(courseLessons));
		completionStatusMap.put("raw.courseContents", String.valueOf(courseContents));
		completionStatusMap.put("raw.userGoalTotal", String.valueOf(userGoalTotal));
		
		if (getUser().isTreatmentGroup()) {
			completionStatusMap.put("raw.plannedItemsTotal", String.valueOf(plannedItemsTotal));
			completionStatusMap.put("raw.plannableItems", String.valueOf(plannableItems));
		}
		completionStatusMap.put("raw.userContentItems", String.valueOf(userContentItems));
		completionStatusMap.put("raw.achievedGoals", String.valueOf(achievedGoals));
		completionStatusMap.put("raw.achievedLessons", String.valueOf(achievedLessons));
		completionStatusMap.put("raw.achievedContents", String.valueOf(achievedContents));
		
		if (getUser().isTreatmentGroup()) {
			completionStatusMap.put("raw.planAchievementInitial", String.valueOf(planAchievementInitial));
			completionStatusMap.put("raw.planAchievementStarted", String.valueOf(planAchievementStarted));
			completionStatusMap.put("raw.planAchievementCompleted", String.valueOf(planAchievementCompleted));
			completionStatusMap.put("raw.planAchievementCompEarly", String.valueOf(planAchievementCompEarly));
			completionStatusMap.put("raw.planAchievementCompOnTime", String.valueOf(planAchievementCompOnTime));
			completionStatusMap.put("raw.planAchievementCompLate", String.valueOf(planAchievementCompLate));
			completionStatusMap.put("raw.planAchievementOpen", String.valueOf(planAchievementOpen));
			completionStatusMap.put("raw.planAchievementDelayed", String.valueOf(planAchievementDelayed));
			completionStatusMap.put("raw.planAchievementPlanned", String.valueOf(planAchievementPlanned));
			completionStatusMap.put("raw.planAchievementRePlanned", String.valueOf(planAchievementRePlanned));
		}
		
		completionStatusMap.put("calc.overallGoalAchievementRate", String.valueOf(overallGoalAchievementRate));
		completionStatusMap.put("calc.overallLessonAchievementRate", String.valueOf(overallLessonAchievementRate));
		completionStatusMap.put("calc.overallContentAchievementRate", String.valueOf(overallContentAchievementRate));

		completionStatusMap.put("calc.userGoalAchievementRate", String.valueOf(userGoalAchievementRate));
		completionStatusMap.put("calc.userLessonAchievementRate", String.valueOf(userLessonAchievementRate));
		completionStatusMap.put("calc.userContentAchievementRate", String.valueOf(userContentAchievementRate));

		if (getUser().isTreatmentGroup()) {
			completionStatusMap.put("calc.userPlanAchievementRate", String.valueOf(userPlanAchievementRate));
			completionStatusMap.put("calc.userPlanOpenRate", String.valueOf(userPlanOpenRate));
			completionStatusMap.put("calc.userPlanDelayedRate", String.valueOf(userPlanDelayedRate));
		
			completionStatusMap.put("calc.userPlanAchEarlyRate", String.valueOf(userPlanAchEarlyRate));
			completionStatusMap.put("calc.userPlanAchOnTimeRate", String.valueOf(userPlanAchOnTimeRate));
			completionStatusMap.put("calc.userPlanAchLateRate", String.valueOf(userPlanAchLateRate));
			
			completionStatusMap.put("calc.userPlannedTotalRate", String.valueOf(userPlannedTotalRate));
			completionStatusMap.put("calc.userPlannedRate", String.valueOf(userPlannedRate));
			completionStatusMap.put("calc.userRePlannedRate", String.valueOf(userRePlannedRate));
			
			completionStatusMap.put("calc.userPlanNotPlanned", String.valueOf(userPlanNotPlanned));
			completionStatusMap.put("calc.userPlanNotPlannedRate", String.valueOf(userPlanNotPlannedRate));
		}


	}

	public boolean containsGoal(GoalDescriptor goal) {
		for (UserGoal userGoal : goals) {
			if (userGoal.getGoalDescriptor() != null && userGoal.getGoalDescriptor().getId().equals(goal.getId())) {
				return true;
			}
		}
		return false;
	}
	
	public void resetGoals() {
		allCourseIntention = false;
		for (UserGoal goal : goals) {
			PersistentStore.deleteGenericEntity(goal);
		}
		PersistentStore.deleteToManyRelation(this, "userGoals", UserGoal.class.getName());
		goals = new HashArrayList<UserGoal>();
	}

	
	public UserPlan(String id, User user) {
		super(id, user);
		this.planItems = new HashArrayList<PlanItem>();
		this.goals = new HashArrayList<UserGoal>();
		this.completionStatusMap = new HashMap<>();
		this.obstacles = "";
		this.copingPlan = "";
	}
	
}
