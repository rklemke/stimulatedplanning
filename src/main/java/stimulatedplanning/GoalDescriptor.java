package stimulatedplanning;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.Set;

import stimulatedplanning.util.HashArrayList;

public class GoalDescriptor extends GenericDescriptor {

	protected HashArrayList<LessonDescriptor> lessons;
	protected HashMap<String, String> completionGoals;
	protected ArrayList<String> completionGoalKeys;
	
	public void addLesson(LessonDescriptor lesson) {
		this.lessons.add(lesson);
	}
	
	public ListIterator<LessonDescriptor> getLessons() {
		return lessons.listIterator();
	}
	
	public void addCompletionGoal(String percentage, String description) {
		completionGoals.put(percentage, description);
		completionGoalKeys.add(percentage);		
	}
	
	public ArrayList<String> getCompletionGoalKeys() {
		return completionGoalKeys;
	}
	
	public String getCompletionGoal(String key) {
		return completionGoals.get(key);
	}
	


	public Duration getGoalDuration() {
		Duration duration = Duration.ZERO;
		for (LessonDescriptor lesson : lessons) {
			duration = duration.plus(lesson.getLessonDuration());
		}
		return duration;
	}
	
	public GoalDescriptor() {
		super();
		lessons = new HashArrayList<LessonDescriptor>();
		completionGoals = new HashMap<String, String>();
		completionGoalKeys = new ArrayList<>();
		// TODO Auto-generated constructor stub
	}

	public GoalDescriptor(String id, String title, String description, String url) {
		super(id, title, description, url);
		lessons = new HashArrayList<LessonDescriptor>();
		completionGoals = new HashMap<String, String>();
		completionGoalKeys = new ArrayList<>();
		// TODO Auto-generated constructor stub
	}

}
