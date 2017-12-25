package stimulatedplanning;

import java.util.ArrayList;
import java.util.ListIterator;

import stimulatedplanning.util.HashArrayList;

public class UserGoal extends GenericUserObject {
	
	GoalDescriptor goalDescriptor;
	HashArrayList<UserLesson> lessons;
	String completionGoal = "";

	public GoalDescriptor getGoalDescriptor() {
		return goalDescriptor;
	}

	public void setGoalDescriptor(GoalDescriptor goalDescriptor) {
		this.goalDescriptor = goalDescriptor;
	}

	public ListIterator<UserLesson> getLessons() {
		return lessons.listIterator();
	}
	public void addLesson(UserLesson lesson) {
		this.lessons.add(lesson);
	}
	
	
	public String getCompletionGoal() {
		return completionGoal;
	}

	public void setCompletionGoal(String completionGoal) {
		this.completionGoal = completionGoal;
	}

	public UserGoal(String id, User user, GoalDescriptor goalDescriptor) {
		super(id, user);
		this.goalDescriptor = goalDescriptor;
		this.lessons = new HashArrayList<UserLesson>();
		// TODO Auto-generated constructor stub
	}

}
