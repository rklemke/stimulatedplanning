package stimulatedplanning;

import java.util.Collection;
import java.util.HashMap;

public class UserPlan {
	protected User user;
	protected GoalDescriptor goal;
	protected HashMap<String, PlanItem> planItems;

	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	public GoalDescriptor getGoal() {
		return goal;
	}
	public void setGoal(GoalDescriptor goal) {
		this.goal = goal;
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

	
	public UserPlan(User user) {
		super();
		this.user = user;
		this.planItems = new HashMap<String, PlanItem>();
	}
	
}
