

import java.io.IOException;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import senseofcommunity.InformationObject;
import senseofcommunity.SelectionObject;
import stimulatedplanning.ContentDescriptor;
import stimulatedplanning.CourseDescriptor;
import stimulatedplanning.StimulatedPlanningFactory;
import stimulatedplanning.User;
import stimulatedplanning.UserPlan;

/**
 * Servlet implementation class Test_UserSelectionServlet
 */
@WebServlet("/InformationObjectServlet_SoC")
public class InformationObjectServlet_SoC extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(InformationObjectServlet_SoC.class.getName());   
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public InformationObjectServlet_SoC() {
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
		
		String contentId = request.getParameter("contentId");
		String contentName = request.getParameter("contentName");
		
		ContentDescriptor contentDescriptor = (ContentDescriptor)session.getAttribute("contentDescriptor");
		List<InformationObject> informationObjectList = (List<InformationObject>)session.getAttribute("informationObjectList");
		InformationObject currentInformationObject = (InformationObject)session.getAttribute("currentInformationObject");
		int currentInformationObjectIdx = (int)session.getAttribute("currentInformationObjectIdx");

		String nextServlet = "/InformationWidget.jsp";
		if (currentInformationObject != null && currentInformationObject instanceof SelectionObject) {
			nextServlet = "/SelectionWidget.jsp";
		}
		
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
