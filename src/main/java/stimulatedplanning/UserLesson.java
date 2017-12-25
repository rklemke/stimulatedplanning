package stimulatedplanning;

import java.util.ArrayList;
import java.util.ListIterator;

import stimulatedplanning.util.HashArrayList;

public class UserLesson extends GenericUserObject {
	LessonDescriptor lesson;
	LessonStatus status;
	HashArrayList<UserContent> contents;

	public LessonDescriptor getLesson() {
		return lesson;
	}

	public void setLesson(LessonDescriptor lesson) {
		this.lesson = lesson;
	}

	public LessonStatus getStatus() {
		return status;
	}

	public void setStatus(LessonStatus status) {
		this.status = status;
	}

	public ListIterator<UserContent> getContents() {
		return contents.listIterator();
	}
	public void addContent(UserContent content) {
		this.contents.add(content);
	}
	public UserLesson(String id, User user, LessonDescriptor lesson) {
		super(id, user);
		this.lesson = lesson;
		this.status = LessonStatus.INITIAL;
		this.contents = new HashArrayList<UserContent>();
	}

}
