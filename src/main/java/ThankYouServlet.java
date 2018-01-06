

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
 * Servlet implementation class ThankYouServlet
 */
@WebServlet("/ThankYouServlet")
public class ThankYouServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(ThankYouServlet.class.getName());   
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ThankYouServlet() {
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
		
		String submit = request.getParameter("submit");
		String nextJSP = "/Test_LearningProgress.jsp"; // TODO: needs to be adapted in production environment to meaningful address!
		if (submit != null && submit.equals("Next")) {
			nextJSP = "/Test_LearningProgress.jsp";
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
