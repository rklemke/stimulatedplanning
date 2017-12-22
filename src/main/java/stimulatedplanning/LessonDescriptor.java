package stimulatedplanning;

import java.time.Duration;

public class LessonDescriptor extends GenericDescriptor {
	Duration lessonDuration;

	public Duration getLessonDuration() {
		return lessonDuration;
	}

	public void setLessonDuration(Duration lessonDuration) {
		this.lessonDuration = lessonDuration;
	}

	public LessonDescriptor() {
		super();
		lessonDuration = Duration.ofMinutes(20); // default lesson duration is assumed to be twenty minutes
	}

	public LessonDescriptor(String id, String title, String description, String url) {
		super(id, title, description, url);
		lessonDuration = Duration.ofMinutes(20); // default lesson duration is assumed to be twenty minutes
	}

}
