package stimulatedplanning;

import java.time.Duration;
import java.util.ArrayList;
import java.util.ListIterator;

public class GoalDescriptor extends GenericDescriptor {

	protected ArrayList<ModuleDescriptor> modules;
	protected String plannedTimePerWeek;
	
	public void addModule(ModuleDescriptor module) {
		this.modules.add(module);
	}
	
	public ListIterator<ModuleDescriptor> getModules() {
		return modules.listIterator();
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
		for (ModuleDescriptor module : modules) {
			duration = duration.plus(module.getModuleDuration());
		}
		return duration;
	}
	
	public GoalDescriptor() {
		super();
		modules = new ArrayList<ModuleDescriptor>();
		// TODO Auto-generated constructor stub
	}

	public GoalDescriptor(String id, String title, String description, String url) {
		super(id, title, description, url);
		modules = new ArrayList<ModuleDescriptor>();
		// TODO Auto-generated constructor stub
	}

}
