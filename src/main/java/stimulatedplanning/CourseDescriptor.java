package stimulatedplanning;

import java.time.Duration;
import java.util.ArrayList;
import java.util.ListIterator;

import stimulatedplanning.util.HashArrayList;

public class CourseDescriptor extends GenericDescriptor {

	protected HashArrayList<ModuleDescriptor> modules;
	protected HashArrayList<GoalDescriptor> goals;

	public CourseDescriptor() {
		super();
		modules = new HashArrayList<ModuleDescriptor>();
		goals = new HashArrayList<GoalDescriptor>();
		// TODO Auto-generated constructor stub
	}

	public CourseDescriptor(String id, String title, String description, String url) {
		super(id, title, description, url);
		modules = new HashArrayList<ModuleDescriptor>();
		goals = new HashArrayList<GoalDescriptor>();
		// TODO Auto-generated constructor stub
	}
	
	public void addModule(ModuleDescriptor module) {
		this.modules.add(module);
	}
	
	public ListIterator<ModuleDescriptor> getModules() {
		return modules.listIterator();
	}
	
	public void addGoal(GoalDescriptor goal) {
		this.goals.add(goal);
	}
	
	public ListIterator<GoalDescriptor> getGoals() {
		return goals.listIterator();
	}
	
	public GoalDescriptor getGoal(String id) {
		return goals.get(id);
	}
	
	public LessonDescriptor retrieveLessonById(String id) {
		for (ModuleDescriptor module : modules) {
			for (LessonDescriptor lesson : module.lessons) {
				if (lesson.getId().equals(id)) {
					return lesson;
				}
			}
		}
		return null;
	}
	
	public Duration getCourseDuration() {
		Duration duration = Duration.ZERO;
		for (ModuleDescriptor module : modules) {
			duration = duration.plus(module.getModuleDuration());
		}
		return duration;
	}

}
