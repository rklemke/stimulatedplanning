package stimulatedplanning;

import java.util.ArrayList;
import java.util.ListIterator;
import java.util.logging.Logger;

import stimulatedplanning.util.HashArrayList;

public class UserLesson extends GenericUserObject {
	private static final Logger log = Logger.getLogger(UserLesson.class.getName());   
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

	public boolean trackLearningProgress(UserPlan userPlan, String contentUrl, String contentId, String activityType) {
		LessonStatus minStatus = LessonStatus.COMPLETED;
		LessonStatus maxStatus = LessonStatus.INITIAL;
		boolean updated = false;
		
		for (UserContent content : contents) {
			if (content.trackLearningProgress(userPlan, contentUrl, contentId, activityType)) {
				updated = true;
			}

			if (content.getStatus().compareTo(maxStatus) > 0) {
				maxStatus = content.getStatus();
			}
			if (content.getStatus().compareTo(minStatus) < 0) {
				minStatus = content.getStatus();
			}
		}
		
		if (minStatus == LessonStatus.COMPLETED && this.status == LessonStatus.STARTED) { // all contents are completed: the lesson is completed
			this.status = LessonStatus.COMPLETED;
			updated = true;
		} else if ((maxStatus.compareTo(LessonStatus.INITIAL) > 0)  && this.status == LessonStatus.INITIAL) { // at least one content is started or completed: the lesson is started
			this.status = LessonStatus.STARTED;
			updated = true;
		}

		if (userPlan.hasPlanItemForLesson(getLesson())) {
			PlanItem planItem = userPlan.getPlanItemForLesson(getLesson());
			if (planItem.trackLearningProgress(userPlan, this, contentUrl, contentId, activityType)) {
				updated = true;
			}
		}
		
		return updated;
		
	}

	public UserLesson(String id, User user, LessonDescriptor lesson) {
		super(id, user);
		this.lesson = lesson;
		this.status = LessonStatus.INITIAL;
		this.contents = new HashArrayList<UserContent>();
	}

}
