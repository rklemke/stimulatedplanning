package stimulatedplanning;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.Set;

public class GoalDescriptor extends GenericDescriptor {

	protected ArrayList<LessonDescriptor> lessons;
	protected HashMap<String, String> completionGoals;
	protected String plannedTimePerWeek;
	
	public void addLesson(LessonDescriptor lesson) {
		this.lessons.add(lesson);
	}
	
	public ListIterator<LessonDescriptor> getLessons() {
		return lessons.listIterator();
	}
	
	public void addCompletionGoal(String percentage, String description) {
		completionGoals.put(percentage, description);
		completionGoals.keySet();
	}
	
	public Set<String> getCompletionGoalKeys() {
		return completionGoals.keySet();
	}
	
	public String getCompletionGoal(String key) {
		return completionGoals.get(key);
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


	public Duration getGoalDuration() {
		Duration duration = Duration.ZERO;
		for (LessonDescriptor lesson : lessons) {
			duration = duration.plus(lesson.getLessonDuration());
		}
		return duration;
	}
	
	public GoalDescriptor() {
		super();
		lessons = new ArrayList<LessonDescriptor>();
		completionGoals = new HashMap<String, String>();
		// TODO Auto-generated constructor stub
	}

	public GoalDescriptor(String id, String title, String description, String url) {
		super(id, title, description, url);
		lessons = new ArrayList<LessonDescriptor>();
		completionGoals = new HashMap<String, String>();
		// TODO Auto-generated constructor stub
	}

}
