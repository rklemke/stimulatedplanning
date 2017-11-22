

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
		HttpSession session = request.getSession();
		CourseDescriptor course = (CourseDescriptor)session.getAttribute("course");
		if (course == null) {
			course = CourseDescriptor.generateTestCourse();
			session.setAttribute("course", course);
		}
		  
		String selectedGoalId = request.getParameter("goalSelectRadio");
		GoalDescriptor selectedGoal = null;
		
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
			session.setAttribute("selectedSchedule", selectedSchedule);
			if (selectedGoal != null) {
				selectedGoal.setPlannedTimePerWeek(selectedSchedule);
			}
		}
		
		String submit = request.getParameter("submit");
		String nextJSP = "/GoalSetting.jsp";
		
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
