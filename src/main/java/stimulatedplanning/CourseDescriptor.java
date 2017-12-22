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
	
	public static CourseDescriptor generateTestCourse() {
		CourseDescriptor course = new CourseDescriptor("c01", "Course 1", "Test Course 1 Description", "");
		for (int i=1; i<6; i++) {
			ModuleDescriptor module = new ModuleDescriptor("c01_m0"+i, "Module 1."+i, "Module 1."+i+" Description", "");
			course.addModule(module);
			for (int j=1; j<6; j++) {
				LessonDescriptor lesson = new LessonDescriptor("c01_m0"+i+".l0"+j,"Lesson 1."+i+"."+j,"Lesson 1."+i+"."+j+" Description","");
				module.addLesson(lesson);
			}
		}
	
		GoalDescriptor goal1 = new GoalDescriptor("c01_g01", "Browsing the Course", "Browsing the Course Description", "");
		goal1.addModule(course.modules.get(0));
		goal1.addModule(course.modules.get(1));
		goal1.addModule(course.modules.get(2));
		goal1.addModule(course.modules.get(3));
		goal1.addModule(course.modules.get(4));
		course.addGoal(goal1);
		GoalDescriptor goal2 = new GoalDescriptor("c01_g02", "Internet Security", "Internet Security Description", "");
		goal2.addModule(course.modules.get(0));
		goal2.addModule(course.modules.get(1));
		goal2.addModule(course.modules.get(2));
		course.addGoal(goal2);
		GoalDescriptor goal3 = new GoalDescriptor("c01_g03", "Computer Security", "Computer Security Description", "");
		goal3.addModule(course.modules.get(0));
		goal3.addModule(course.modules.get(3));
		goal3.addModule(course.modules.get(4));
		course.addGoal(goal3);
		GoalDescriptor goal4 = new GoalDescriptor("c01_g04", "Safe behaviour on the Internet", "Safe behaviour on the Internet Description", "");
		goal4.addModule(course.modules.get(0));
		goal4.addModule(course.modules.get(1));
		goal4.addModule(course.modules.get(3));
		course.addGoal(goal4);
		GoalDescriptor goal5 = new GoalDescriptor("c01_g05", "Complete the Course", "I intend to complete the course Description", "");
		goal5.addModule(course.modules.get(0));
		goal5.addModule(course.modules.get(1));
		goal5.addModule(course.modules.get(2));
		goal5.addModule(course.modules.get(3));
		goal5.addModule(course.modules.get(4));
		course.addGoal(goal5);
		
		return course;
	}

}
