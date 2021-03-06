package stimulatedplanning;

import java.time.Duration;
import java.util.ArrayList;
import java.util.ListIterator;

import stimulatedplanning.util.HashArrayList;

public class ModuleDescriptor extends GenericDescriptor {
	
	protected HashArrayList<LessonDescriptor> lessons;

	public ModuleDescriptor() {
		super();
		lessons = new HashArrayList<LessonDescriptor>();
		// TODO Auto-generated constructor stub
	}

	public ModuleDescriptor(String id, String title, String description, String url) {
		super(id, title, description, url);
		lessons = new HashArrayList<LessonDescriptor>();
		// TODO Auto-generated constructor stub
	}
	
	public void addLesson(LessonDescriptor lesson) {
		this.lessons.add(lesson);
	}
	
	public ListIterator<LessonDescriptor> getLessons() {
		return lessons.listIterator();
	}
	
	public Duration getModuleDuration() {
		Duration duration = Duration.ZERO;
		for (LessonDescriptor lesson : lessons) {
			duration = duration.plus(lesson.getLessonDuration());
		}
		return duration;
	}
	
	

}
