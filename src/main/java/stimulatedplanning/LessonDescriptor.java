package stimulatedplanning;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import stimulatedplanning.util.HashArrayList;

public class LessonDescriptor extends GenericDescriptor {
	Duration lessonDuration;
	protected HashArrayList<ContentDescriptor> contents;

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
	
	public String getLessonTooltip() {
		return "'"+getTitle()+"' contains "+contents.size()+" activit"+(contents.size()==1?"y":"ies")+" and takes "+lessonDuration.toMinutes()+" minutes to be completed.";
	}

	public ContentDescriptor addContent(ContentDescriptor content) {
		this.contents.add(content);
		return content;
	}
	
	public ListIterator<ContentDescriptor> getContents() {
		return contents.listIterator();
	}
	
	public List<ContentDescriptor> getContentList() {
		return contents.unmodifiableList();
	}
	
	public LessonDescriptor() {
		super();
		lessonDuration = Duration.ofMinutes(20); // default lesson duration is assumed to be twenty minutes
		contents = new HashArrayList<ContentDescriptor>();
	}

	public LessonDescriptor(String id, String title, String description, String url) {
		super(id, title, description, url);
		lessonDuration = Duration.ofMinutes(20); // default lesson duration is assumed to be twenty minutes
		contents = new HashArrayList<ContentDescriptor>();
	}

}
