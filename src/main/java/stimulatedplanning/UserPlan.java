package stimulatedplanning;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.ListIterator;

public class UserPlan extends GenericUserObject {
	protected CourseDescriptor course;
	protected ArrayList<UserGoal> goals;
	protected HashMap<String, PlanItem> planItems;

	
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
			planItems.put(planItem.getId(), planItem);
		}
	}
	
	public Collection<PlanItem> getPlanItems() {
		return planItems.values();
	}

	public boolean hasPlanItem(String id) {
		return planItems.containsKey(id);
	}

	
	public UserPlan(String id, User user) {
		super(id, user);
		this.planItems = new HashMap<String, PlanItem>();
		this.goals = new ArrayList<UserGoal>();
	}
	
}
