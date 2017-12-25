package stimulatedplanning;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.ListIterator;

import stimulatedplanning.util.HashArrayList;

public class UserPlan extends GenericUserObject {
	protected CourseDescriptor course;
	protected HashArrayList<UserGoal> goals;
	protected HashArrayList<PlanItem> planItems;
	protected String plannedTimePerWeek;

	
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
	
	public void addPlanItem(PlanItem planItem) {
		if (planItem != null && planItem.getId() != null && planItem.getLesson() != null) {
			planItems.add(planItem);
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

	public boolean containsGoal(GoalDescriptor goal) {
		for (UserGoal userGoal : goals) {
			if (userGoal.getGoalDescriptor() != null && userGoal.getGoalDescriptor().getId().equals(goal.getId())) {
				return true;
			}
		}
		return false;
	}

	
	public UserPlan(String id, User user) {
		super(id, user);
		this.planItems = new HashArrayList<PlanItem>();
		this.goals = new HashArrayList<UserGoal>();
	}
	
}
