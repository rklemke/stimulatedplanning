

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import stimulatedplanning.CourseDescriptor;
import stimulatedplanning.PersistentStore;
import stimulatedplanning.StimulatedPlanningFactory;
import stimulatedplanning.User;
import stimulatedplanning.UserPlan;

/**
 * Servlet implementation class CopingPlanServlet
 */
@WebServlet("/CopingPlanServlet")
public class CopingPlanServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(CopingPlanServlet.class.getName());   
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CopingPlanServlet() {
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
		
		String obstacles = request.getParameter("obstacles");
		String copingPlan = request.getParameter("copingPlan");
		
		if (obstacles != null && !obstacles.equals(userPlan.getObstacles())) {
			userPlan.setObstacles(obstacles);
			userPlanDirty = true;
		}
		if (copingPlan != null && !copingPlan.equals(userPlan.getCopingPlan())) {
			userPlan.setCopingPlan(copingPlan);
			userPlanDirty = true;
		}

		if (userPlanDirty) {
			log.info("writing user plan for " + user.getName() + ", " + course.getId() + ", " + userPlan.getId());
			try {
				PersistentStore.writeDescriptor(userPlan);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		try {
			StimulatedPlanningFactory.trackAndLogEvent(request, response, "copingPlan");
			//PersistentStore.writeLog(request.getParameterMap());
		} catch (Exception e) {
			e.printStackTrace();
		}


		String submit = request.getParameter("submit");
		String nextJSP = "/CopingPlan.jsp";
		if (submit != null && submit.equals("Next")) {
			nextJSP = "/ThankYou.jsp";
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
