package stimulatedplanning;

import java.util.ArrayList;
import java.util.ListIterator;

import stimulatedplanning.util.HashArrayList;

public class UserGoal extends GenericUserObject {
	
	GoalDescriptor goalDescriptor;
	HashArrayList<UserLesson> lessons;
	String completionGoal = "";

	public GoalDescriptor getGoalDescriptor() {
		return goalDescriptor;
	}

	public void setGoalDescriptor(GoalDescriptor goalDescriptor) {
		this.goalDescriptor = goalDescriptor;
	}

	public ListIterator<UserLesson> getLessons() {
		return lessons.listIterator();
	}
	public void addLesson(UserLesson lesson) {
		this.lessons.add(lesson);
	}
	
	
	public String getCompletionGoal() {
		return completionGoal;
	}

	public void setCompletionGoal(String completionGoal) {
		this.completionGoal = completionGoal;
	}

	public boolean trackLearningProgress(UserPlan userPlan, String contentUrl, String activityType) {
		LessonStatus minStatus = LessonStatus.COMPLETED;
		LessonStatus maxStatus = LessonStatus.INITIAL;
		boolean updated = false;
		
		if (lessons.size()>0) {
			for (UserLesson lesson : lessons) {
				if (lesson.trackLearningProgress(userPlan, contentUrl, activityType)) {
					updated = true;
				}
				
				if (userPlan.hasPlanItemForLesson(lesson.getLesson())) {
					PlanItem item = userPlan.getPlanItemForLesson(lesson.getLesson());
					if (item.trackLearningProgress(userPlan, lesson, contentUrl, activityType)) {
						updated = true;
					}
				}

				if (lesson.getStatus().compareTo(maxStatus) > 0) {
					maxStatus = lesson.getStatus();
				}
				if (lesson.getStatus().compareTo(minStatus) < 0) {
					minStatus = lesson.getStatus();
				}
			}
			
//			if (minStatus == LessonStatus.COMPLETED && this.status == LessonStatus.STARTED) { // all contents are completed: the lesson is completed
//				this.status = LessonStatus.COMPLETED;
//			} else if ((maxStatus.compareTo(LessonStatus.INITIAL) > 0)  && this.status == LessonStatus.INITIAL) { // at least one content is started or completed: the lesson is started
//				this.status = LessonStatus.STARTED;
//			}
		}
		
		return updated;
		
	}

	public UserGoal(String id, User user, GoalDescriptor goalDescriptor) {
		super(id, user);
		this.goalDescriptor = goalDescriptor;
		this.lessons = new HashArrayList<UserLesson>();
		// TODO Auto-generated constructor stub
	}

}
