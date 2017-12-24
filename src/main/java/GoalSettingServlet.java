

import java.io.IOException;
import java.util.ListIterator;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import stimulatedplanning.*;

/**
 * Servlet implementation class GoalSettingServlet
 */
@WebServlet("/GoalSettingServlet")
public class GoalSettingServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
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

		String intentionStep = (String)session.getAttribute("intentionStep");

		String selectedGoalId = request.getParameter("goalSelectRadio");

		GoalDescriptor selectedGoal = (GoalDescriptor)session.getAttribute("userGoal");
		
		if (selectedGoalId != null && !"".equals(selectedGoalId)) {
			ListIterator<GoalDescriptor> iterator = course.getGoals();
			while(selectedGoal == null && iterator.hasNext()) {
				GoalDescriptor goal = iterator.next();
				if (goal.getId().equals(selectedGoalId)) {
					selectedGoal = goal;
				}
			}
			if (selectedGoal != null) {
				session.setAttribute("userGoal", selectedGoal);
			}
		}

		String selectedSchedule = request.getParameter("scheduleSelectRadio");
		
		if (selectedSchedule != null) {
			System.out.println("selectedSchedule: "+selectedSchedule);
			session.setAttribute("selectedSchedule", selectedSchedule);
			if (selectedGoal != null) {
				selectedGoal.setPlannedTimePerWeek(selectedSchedule);
			}
		}
		
		String submit = request.getParameter("submit");
		String nextJSP = "/GoalSetting.jsp";
		
		if (submit != null && submit.equals("Next")) {
			if (PlanningSteps.intentionSteps[PlanningSteps.intentionSteps.length-1].equals(intentionStep)) {
				nextJSP = "/StimulatedPlanning.jsp";
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
		
		if (submit != null && submit.equals("Continue")) {
			nextJSP = "/StimulatedPlanning.jsp";
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
