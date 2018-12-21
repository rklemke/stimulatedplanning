package stimulatedplanning;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
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
	
	public List<ModuleDescriptor> getModuleList() {
		return modules.unmodifiableList();
	}
	
	public void addGoal(GoalDescriptor goal) {
		this.goals.add(goal);
	}
	
	public ListIterator<GoalDescriptor> getGoals() {
		return goals.listIterator();
	}
	
	public List<GoalDescriptor> getGoalList() {
		return goals.unmodifiableList();
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

	public int indexInCourse(String url) {
		int counter=0;
		boolean found=false;
		if (url == null) {
			return 100;
		}
		int iend = url.indexOf("?"); 
		if (iend != -1) {
			url = url.substring(0 , iend);
		}

		foundit: // label to break the three loops
		for (ModuleDescriptor module: modules) {
			for (LessonDescriptor lesson : module.lessons) {
				for (ContentDescriptor content: lesson.contents) {
					String curl = content.url;
					if (curl != null) {
						int iend3 = curl.indexOf("?");
						if (iend3 != -1) {
							curl = curl.substring(0 , iend3);
						}
						if (curl.endsWith("1") && iend3 > 0) {
							curl = curl.substring(0 , iend3-1);
						}
					}
					
					if (!found) counter++;
					if (url.equals(content.url)) {
						found = true;
						break foundit; // breaks out of all three nested loops
					}
				}
			}
		}
		return counter;
	}
	
	public int distance(String url1, String url2) {
		int counter1=0, counter2=0;
		boolean found1=false, found2=false;
		if (url1 == null || url2 == null) {
			return 100;
		}
		int iend1 = url1.indexOf("?"); 
		if (iend1 != -1) {
			url1 = url1.substring(0 , iend1);
		}
		int iend2 = url2.indexOf("?"); 
		if (iend2 != -1) {
			url2 = url2.substring(0 , iend2);
		}
		if (url1.equals(url2)) {
			return 0;
		}
		for (ModuleDescriptor module: modules) {
			for (LessonDescriptor lesson : module.lessons) {
				for (ContentDescriptor content: lesson.contents) {
					String curl = content.url;
					if (curl != null) {
						int iend3 = curl.indexOf("?");
						if (iend3 != -1) {
							curl = curl.substring(0 , iend3);
						}
						if (curl.endsWith("1") && iend3 > 0) {
							curl = curl.substring(0 , iend3-1);
						}
					}
					
					if (!found1) counter1++;
					if (!found2) counter2++;
					if (url1.equals(content.url)) {
						found1 = true;
					}
					if (url2.equals(content.url)) {
						found2 = true;
					}
				}
			}
		}
		return Math.abs(counter1-counter2);
	}

}
