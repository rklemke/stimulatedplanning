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
	
	public String getLessonDurationString() {
		long minutes = lessonDuration.toMinutes();
		long hours = 0;
		while (minutes > 60) {
			hours++;
			minutes -= 60;
		}
		String formatted = String.format("%02d", hours) + ":" + String.format("%02d", minutes);
		return formatted;
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
