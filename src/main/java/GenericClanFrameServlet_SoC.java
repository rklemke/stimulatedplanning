

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
import stimulatedplanning.ContentDescriptor;
import stimulatedplanning.CourseDescriptor;
import stimulatedplanning.StimulatedPlanningFactory;
import stimulatedplanning.User;
import stimulatedplanning.UserPlan;

/**
 * Servlet implementation class Test_UserSelectionServlet
 */
@WebServlet("/GenericClanFrameServlet_SoC")
public class GenericClanFrameServlet_SoC extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(GenericClanFrameServlet_SoC.class.getName());   
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GenericClanFrameServlet_SoC() {
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
		log.info("contentId "+contentId+" contentName "+contentName);
		ContentDescriptor contentDescriptor = (ContentDescriptor)StimulatedPlanningFactory.getObject(contentId);
		if (contentDescriptor != null) {
			session.setAttribute("contentDescriptor", contentDescriptor);
			List<InformationObject> informationObjectList = contentDescriptor.getAllInformationObjectList();
			if (informationObjectList != null) {
				session.setAttribute("informationObjectList", informationObjectList);
				if (informationObjectList.size()>0) {
					InformationObject currentInformationObject = informationObjectList.get(0);
					session.setAttribute("currentInformationObject", currentInformationObject);
					session.setAttribute("currentInformationObjectIdx", 0);
				}
			}
		}


		String nextServlet = "/GenericClanFrame.jsp";
//		if (request.getParameter("submitLogin") != null) {
//			nextServlet = "/Test_UserNavigation_SoC.jsp";
//		} else if (request.getParameter("submitClan") != null) {
//			nextServlet = "/AwarenessFrame.jsp";
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
