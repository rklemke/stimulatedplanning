

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

import senseofcommunity.Clan;
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
		
		boolean isControl = !user.isTreatmentGroup();
		boolean isClanA = false;
		boolean isClanB = false;
		if (!isControl) {
			isClanA = user.getClan() != null && Clan.CLAN_1_ID.equals(user.getClan().getId());
			isClanB = user.getClan() != null && Clan.CLAN_2_ID.equals(user.getClan().getId());
		}
		
		String contentId = request.getParameter("contentId");
		String contentName = request.getParameter("contentName");
		String pageurl = request.getParameter("pageurl");
		log.info("contentId "+contentId+", contentName "+contentName+", pageurl "+pageurl);
		ContentDescriptor contentDescriptor = null;
		if (contentId != null) {
			contentDescriptor = (ContentDescriptor)StimulatedPlanningFactory.getObject(contentId);
		} 
		if (contentDescriptor == null && pageurl != null) {
			contentDescriptor = course.getContentByUrl(pageurl);
		} 
		if (contentDescriptor == null) {
			contentDescriptor = (ContentDescriptor)session.getAttribute("contentDescriptor");
		}
		
		List<InformationObject> informationObjectList = null;
		InformationObject currentInformationObject = null;
		int currentInformationObjectIdx = 0;
		
		String submitForward = (String)request.getAttribute("selectionSubmitForward");

		if (contentDescriptor != null) {
			informationObjectList = (List<InformationObject>)session.getAttribute("informationObjectList");
			if (informationObjectList == null) {
				//informationObjectList = contentDescriptor.getAllInformationObjectList();
				informationObjectList = contentDescriptor.getFilteredInformationObjectList(isControl, isClanA, isClanB);
			}
			Object cioi = request.getParameter("currentInformationObjectIdx");
			if (cioi != null) {
				currentInformationObjectIdx = Integer.valueOf((String)cioi);
			}

			if (request.getParameter("buttonPrev") != null) {
				//contentDescriptor = (ContentDescriptor)session.getAttribute("contentDescriptor");
				currentInformationObjectIdx--;
				currentInformationObject = informationObjectList.get(currentInformationObjectIdx);
				session.setAttribute("currentInformationObject", currentInformationObject);
				session.setAttribute("currentInformationObjectIdx", currentInformationObjectIdx);
				
			} else if (request.getParameter("buttonNext") != null) {
				//contentDescriptor = (ContentDescriptor)session.getAttribute("contentDescriptor");
				currentInformationObjectIdx++;
				currentInformationObject = informationObjectList.get(currentInformationObjectIdx);
				session.setAttribute("currentInformationObject", currentInformationObject);
				session.setAttribute("currentInformationObjectIdx", currentInformationObjectIdx);
				
			} else if (submitForward != null) {
				session.setAttribute("contentDescriptor", contentDescriptor);
				//informationObjectList = contentDescriptor.getAllInformationObjectList();
				informationObjectList = contentDescriptor.getFilteredInformationObjectList(isControl, isClanA, isClanB);
				if (informationObjectList != null) {
					session.setAttribute("informationObjectList", informationObjectList);
					if (informationObjectList.size()>0) {
						currentInformationObject = informationObjectList.get(currentInformationObjectIdx);
						session.setAttribute("currentInformationObject", currentInformationObject);
						session.setAttribute("currentInformationObjectIdx", currentInformationObjectIdx);
					}
				}
			} else {
				session.setAttribute("contentDescriptor", null);
				session.setAttribute("informationObjectList", null);
				session.setAttribute("currentInformationObject", null);
				session.setAttribute("currentInformationObjectIdx", 0);

				session.setAttribute("contentDescriptor", contentDescriptor);
				//informationObjectList = contentDescriptor.getAllInformationObjectList();
				informationObjectList = contentDescriptor.getFilteredInformationObjectList(isControl, isClanA, isClanB);
				if (informationObjectList != null) {
					session.setAttribute("informationObjectList", informationObjectList);
					if (informationObjectList.size()>0) {
						currentInformationObject = informationObjectList.get(0);
						session.setAttribute("currentInformationObject", currentInformationObject);
						session.setAttribute("currentInformationObjectIdx", 0);
					}
				}
			}
		}

		String nextServlet = "/GenericClanFrame.jsp";
		if (currentInformationObject == null) {
			nextServlet = "/AwarenessFrame.jsp";
//		} else if (request.getParameter("submitClan") != null) {
//			nextServlet = "/AwarenessFrame.jsp";
//		} else if (request.getParameter("submitCoping") != null) {
//			nextServlet = "/CopingPlan.jsp";
//		} else if (request.getParameter("submitLearning") != null) {
//			nextServlet = "/Test_LearningProgress.jsp";
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
