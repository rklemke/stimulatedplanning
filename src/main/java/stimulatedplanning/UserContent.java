package stimulatedplanning;

import java.util.logging.Logger;

public class UserContent extends GenericUserObject {
	private static final Logger log = Logger.getLogger(UserContent.class.getName());   
	ContentDescriptor content;
	LessonStatus status;

	public ContentDescriptor getContent() {
		return content;
	}

	public void setContent(ContentDescriptor content) {
		this.content = content;
	}

	public LessonStatus getStatus() {
		return status;
	}

	public void setStatus(LessonStatus status) {
		this.status = status;
	}

	public boolean trackLearningProgress(UserPlan userPlan, String contentUrl, String contentId, String activityType) {
		boolean updated = false;
		boolean match = false;
		if (contentId != null && contentId.length()>0 && !"not_found".equals(contentId)) {
			//log.info("trackLearningProgress: checking contentId: "+contentId+", "+this.getContent().getId());
			if (contentId.equals(this.getContent().getId())) {
				//log.info("trackLearningProgress: match via contentId: "+contentId);
				match = true;
			}
		} else {
			int iend = contentUrl.indexOf("?"); 
			if (iend != -1) {
				contentUrl = contentUrl.substring(0 , iend);
			}
			String thiscontent = this.getContent().getUrl();
			if (thiscontent != null) {
				int iend2 = thiscontent.indexOf("?");
				if (iend2 != -1) {
					thiscontent = thiscontent.substring(0 , iend2);
				}
				if (thiscontent.endsWith("1") && iend2 > 0) {
					thiscontent = thiscontent.substring(0 , iend2-1);
				}
			}
			if (contentUrl != null && contentUrl.equals(thiscontent)) {
				//log.info("trackLearningProgress: match via contentUrl: "+contentUrl);
				match = true;
			}
		}
		if (match) {
			if (StimulatedPlanningFactory.ACTIVITY_TYPE_ACCESS.equals(activityType) && status == LessonStatus.INITIAL) {
				status = LessonStatus.STARTED;
				updated = true;
			} else if (StimulatedPlanningFactory.ACTIVITY_TYPE_ACCESS.equals(activityType) && status == LessonStatus.STARTED) {
				status = LessonStatus.COMPLETED;
				updated = true;
			} else if (StimulatedPlanningFactory.ACTIVITY_TYPE_COMPLETE.equals(activityType) && status == LessonStatus.STARTED) {
				status = LessonStatus.COMPLETED;
				updated = true;
			}
		}
		if (updated) {
			log.info("trackLearningProgress: "+user.getName()+", "+this.getContent().getTitle()+": TRACKED");
		}
		return updated;
	}

	public UserContent(String id, User user, ContentDescriptor content) {
		super(id, user);
		this.content = content;
		this.status = LessonStatus.INITIAL;
		// TODO Auto-generated constructor stub
	}

}
