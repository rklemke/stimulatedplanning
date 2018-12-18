

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Test_UserSelectionServlet
 */
@WebServlet("/Test_UserSelectionServlet_SoC")
public class Test_UserSelectionServlet_SoC extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(Test_UserSelectionServlet_SoC.class.getName());   
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Test_UserSelectionServlet_SoC() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub

		String nextServlet = "/AwarenessFrame.jsp";
//		if (request.getParameter("submitIntention") != null) {
//			nextServlet = "/Test_Intention.jsp";
//		} else if (request.getParameter("submitPlanning") != null) {
//			nextServlet = "/StimulatedPlanningServlet";
//		} else if (request.getParameter("submitCoping") != null) {
//			nextServlet = "/CopingPlan.jsp";
//		} else if (request.getParameter("submitLearning") != null) {
//			nextServlet = "/Test_LearningProgress.jsp";
//		}
		
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextServlet);
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
