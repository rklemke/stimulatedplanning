package stimulatedplanning;

import java.time.Duration;
import java.util.ArrayList;
import java.util.ListIterator;

public class CourseDescriptor extends GenericDescriptor {

	protected ArrayList<ModuleDescriptor> modules;
	protected ArrayList<GoalDescriptor> goals;

	public CourseDescriptor() {
		super();
		modules = new ArrayList<ModuleDescriptor>();
		goals = new ArrayList<GoalDescriptor>();
		// TODO Auto-generated constructor stub
	}

	public CourseDescriptor(String id, String title, String description, String url) {
		super(id, title, description, url);
		modules = new ArrayList<ModuleDescriptor>();
		goals = new ArrayList<GoalDescriptor>();
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
