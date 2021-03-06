

import java.io.IOException;
import java.util.ListIterator;
import java.util.logging.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import stimulatedplanning.*;
import stimulatedplanning.util.HashArrayList;

/**
 * Servlet implementation class GoalSettingServlet
 */
@WebServlet("/GoalSettingServlet")
public class GoalSettingServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(GoalSettingServlet.class.getName());   
	/**
     * @see HttpServlet#HttpServlet()
     */
    public GoalSettingServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = StimulatedPlanningFactory.initializeSession(request, response);

		User user = (User)session.getAttribute("user");
		CourseDescriptor course = (CourseDescriptor)session.getAttribute("course");
		UserPlan userPlan = (UserPlan)session.getAttribute("userPlan");
		boolean userPlanDirty = false;

		String intentionStep = (String)session.getAttribute("intentionStep");
		if (intentionStep == null || "".equals(intentionStep)) {
			intentionStep = request.getParameter("intentionStep");
			if (intentionStep == null || "".equals(intentionStep)) {
				session.setAttribute("intentionStep", intentionStep);
			}
		}

		String completionSelectRB = request.getParameter("completionSelectRB");
		if (completionSelectRB != null) {
			session.setAttribute("completionSelectRB", completionSelectRB);
		}
		
		String[] selectedGoalIds = request.getParameterValues("goalSelectRadio");

		//GoalDescriptor selectedGoal = (GoalDescriptor)session.getAttribute("userGoal");
		HashArrayList<GoalDescriptor> selectedGoals = (HashArrayList<GoalDescriptor>)session.getAttribute("selectedGoals");
		HashArrayList<LessonDescriptor> selectedLessons = (HashArrayList<LessonDescriptor>)session.getAttribute("selectedLessons");

		boolean updateGoals = false;
		if (selectedGoalIds != null && selectedGoalIds.length > 0) {
			if (selectedGoalIds.length != selectedGoals.size()) {
				updateGoals = true;
			} else {
				for (String goalId : selectedGoalIds) {
					if ("0".equals(goalId)) {
						if (!userPlan.isAllCourseIntention()) {
							updateGoals = true;
						}
					} else if (!selectedGoals.containsKey(goalId)) {
						updateGoals = true;
					} else {
						String[] selectedLessonIds = request.getParameterValues("goal" + goalId);
						if (selectedLessonIds != null && selectedLessonIds.length > 0) {
							for (String lessonId : selectedLessonIds) {
								if (!selectedLessons.containsKey(lessonId)) {
									updateGoals = true;
								}
							}
						}
						GoalDescriptor goal = selectedGoals.get(goalId);
						if (goal.getCompletionGoalKeys().size() > 0 && goal.getCompletionGoal(completionSelectRB) != null) {
							updateGoals = true;
						}
					}
				}
			}
		}
		
		if (updateGoals) {
			userPlanDirty = true;
			ListIterator<GoalDescriptor> iterator = course.getGoals();
			userPlan.resetGoals();
			selectedGoals = new HashArrayList<GoalDescriptor>();
			selectedLessons = new HashArrayList<LessonDescriptor>();
			for (String goalId : selectedGoalIds) {
				if ("0".equals(goalId)) {
					userPlan.setAllCourseIntention(true);
				} else {
					GoalDescriptor goal = course.getGoal(goalId);
					if (goal != null) {
						selectedGoals.add(goal);
						UserGoal userGoal = StimulatedPlanningFactory.createUserGoal(userPlan, goal);
						userPlan.addGoal(userGoal);
						String[] selectedLessonIds = request.getParameterValues("goal" + goal.getId());
						if (selectedLessonIds != null) {
							for (String lessonId : selectedLessonIds) {
								LessonDescriptor lesson = (LessonDescriptor)StimulatedPlanningFactory.getObject(lessonId);
								if (lesson != null) {
									selectedLessons.add(lesson);
									UserLesson userLesson = StimulatedPlanningFactory.createUserLesson(userGoal, lesson);
									userGoal.addLesson(userLesson);
								}
							}
						}
						if (goal.getCompletionGoalKeys().size() > 0 && goal.getCompletionGoal(completionSelectRB) != null) {
							userGoal.setCompletionGoal(completionSelectRB);
						}
					}
				}
			}
			session.setAttribute("selectedGoals", selectedGoals);
			session.setAttribute("selectedLessons", selectedLessons);
		}

		String selectedSchedule = request.getParameter("scheduleSelectRadio");
		
		if (selectedSchedule != null) {
			userPlanDirty = true;
			//log.info("selectedSchedule: "+selectedSchedule);
			//session.setAttribute("selectedSchedule", selectedSchedule);
			userPlan.setPlannedTimePerWeek(selectedSchedule);
		}
		
		String submit = request.getParameter("submit");
		String nextJSP = "/GoalSetting.jsp";
		
		log.info("GoalSettingServlet: intentStep: "+intentionStep+", submit: "+submit);
		if (submit != null && submit.equals("Next")) {
			if (PlanningSteps.intentionSteps[PlanningSteps.intentionSteps.length-1].equals(intentionStep)) {
				userPlan.setIntentionCompleted(true);
				userPlanDirty = true;
				if (user.isTreatmentGroup()) {
					nextJSP = "/StimulatedPlanning.jsp";
				} else {
					nextJSP = "/ThankYou.jsp";
				}
			} else {
				for (int i=0; i<PlanningSteps.intentionSteps.length-1; i++) {
					if ((intentionStep == null || PlanningSteps.intentionSteps[i].equals(intentionStep)) && i < PlanningSteps.intentionSteps.length-1) {
						intentionStep = PlanningSteps.intentionSteps[i+1];
						session.setAttribute("intentionStep", intentionStep);
						break;
					}
				}
			}
		}
		
		if (submit != null && submit.equals("Previous")) {
			if (PlanningSteps.intentionSteps[0].equals(intentionStep) || intentionStep == null) {
				intentionStep = PlanningSteps.intentionSteps[0];
				session.setAttribute("intentionStep", intentionStep);
			} else {
				for (int i=1; i<PlanningSteps.intentionSteps.length; i++) {
					if ((PlanningSteps.intentionSteps[i].equals(intentionStep)) && i > 0) {
						intentionStep = PlanningSteps.intentionSteps[i-1];
						session.setAttribute("intentionStep", intentionStep);
						break;
					}
				}
			}
		}
		
		log.info("GoalSettingServlet: intentStep next: "+intentionStep+", submit: "+submit);
		
		if (submit != null && submit.equals("Continue")) {
			userPlan.setIntentionCompleted(true);
			userPlanDirty = true;
			if (user.isTreatmentGroup()) {
				nextJSP = "/StimulatedPlanning.jsp";
			} else {
				nextJSP = "/ThankYou.jsp";
			}
		}
		
		if (submit == null && userPlan.isIntentionCompleted()) {
			if (user.isTreatmentGroup()) {
				nextJSP = "/StimulatedPlanning.jsp";
			} else {
				nextJSP = "/GoalSetting.jsp";
			}
		}
		
		if (userPlanDirty) {
			log.info("writing user plan for " + user.getName() + ", " + course.getId() + ", " + userPlan.getId());
			try {
				userPlan.calculateAchievementRates();
				PersistentStore.writeDescriptor(userPlan);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		try {
			StimulatedPlanningFactory.trackAndLogEvent(request, response, "intention");
			//PersistentStore.writeLog(request.getParameterMap());
		} catch (Exception e) {
			e.printStackTrace();
		}

		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextJSP);
		dispatcher.forward(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
