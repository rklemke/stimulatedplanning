package stimulatedplanning;

import java.util.ArrayList;
import java.util.ListIterator;

import stimulatedplanning.util.HashArrayList;

public class UserGoal extends GenericUserObject {
	
	GoalDescriptor goalDescriptor;
	HashArrayList<UserLesson> lessons;

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
	
	
	public UserGoal(String id, User user, GoalDescriptor goalDescriptor) {
		super(id, user);
		this.goalDescriptor = goalDescriptor;
		this.lessons = new HashArrayList<UserLesson>();
		// TODO Auto-generated constructor stub
	}

}
