

import java.io.IOException;
import java.util.HashMap;
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
import senseofcommunity.SelectionObject;
import senseofcommunity.SelectionOption;
import senseofcommunity.UserOnlineStatus;
import senseofcommunity.UserSelectedOption;
import stimulatedplanning.ContentDescriptor;
import stimulatedplanning.CourseDescriptor;
import stimulatedplanning.PersistentStore;
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
		
		ContentDescriptor contentDescriptor = null;
		String contentId = request.getParameter("contentId");
		String contentName = request.getParameter("contentName");
		String pageurl = request.getParameter("pageurl");
		if (contentId != null) {
			contentDescriptor = (ContentDescriptor)StimulatedPlanningFactory.getObject(ContentDescriptor.class.getName(), contentId);
			if (contentDescriptor == null) {
				try {
					contentDescriptor = (ContentDescriptor)PersistentStore.readDescriptor(ContentDescriptor.class.getName(), contentId, new HashMap<String, Object>(), null);
				} catch (Exception exc) {
					exc.printStackTrace();
				}
			}
		} 
		if (contentDescriptor == null && pageurl != null) {
			contentDescriptor = course.getContentByUrl(pageurl);
		} 
		if (contentDescriptor == null) {
			contentDescriptor = (ContentDescriptor)session.getAttribute("contentDescriptor");
		}
		
		boolean isControl = !user.isTreatmentGroup();
		boolean isClanA = false;
		boolean isClanB = false;
		if (!isControl) {
			isClanA = user.getClan() != null && Clan.CLAN_1_ID.equals(user.getClan().getId());
			isClanB = user.getClan() != null && Clan.CLAN_2_ID.equals(user.getClan().getId());
		}
		
	  List<InformationObject> informationObjectList = (List<InformationObject>)session.getAttribute("informationObjectList");
		if (informationObjectList == null && contentDescriptor != null) {
			//informationObjectList = contentDescriptor.getAllInformationObjectList();
			informationObjectList = contentDescriptor.getFilteredInformationObjectList(isControl, isClanA, isClanB);
		}

		int currentInformationObjectIdx = 0;
	  Object idxS = session.getAttribute("currentInformationObjectIdx");
	  if (idxS == null) {
		  idxS = request.getParameter("currentInformationObjectIdx");
	  }
	  if (idxS != null) {
		  currentInformationObjectIdx = Integer.valueOf(idxS.toString());
	  }
	  
	  InformationObject currentInformationObject = (InformationObject)session.getAttribute("currentInformationObject");
	  if (currentInformationObject == null && informationObjectList != null && informationObjectList.size() > currentInformationObjectIdx) {
		  currentInformationObject = informationObjectList.get(currentInformationObjectIdx);
	  }

	  String submitIndicator = request.getParameter("submitIndicator");

	  String logString = "contentDescriptor: "+(contentDescriptor==null?"null":contentDescriptor.getId());
	  logString += ", informationObjectList: "+(informationObjectList==null?"null":informationObjectList.size());
	  logString += ", contentId: "+contentId;
	  logString += ", currentInformationObjectIdx: "+currentInformationObjectIdx;
	  logString += ", submitIndicator: "+submitIndicator;
	  log.info(logString);
	  
		if (submitIndicator != null && currentInformationObject != null && currentInformationObject instanceof SelectionObject) { // we handle a form submission
			SelectionObject currentSelectionObject = (SelectionObject)currentInformationObject;
			String[] selectedOptionIds = request.getParameterValues("selectionRadio");
			for (SelectionOption option : currentSelectionObject.getOptionList()) {
				//UserSelectedOption userOption = PersistentStore.readUserSelectionOption(user, currentSelectionObject, option);
				UserSelectedOption userOption = option.getUserSelectedOption(user, currentSelectionObject);  //PersistentStore.readUserSelectionOption(user, currentSelectionObject, option);
				boolean foundId = false;
				if (selectedOptionIds != null) {
					for (String optionId: selectedOptionIds) {
						log.info("option: "+option.getId()+", optionId: "+optionId);
						if (option.getId().equals(optionId)) {
							foundId = true;
							break;
						}
					}
				}
				
				// checking and storing the selection
				if (!foundId && userOption != null) {
					PersistentStore.deleteGenericEntity(userOption);
				} else if (foundId && userOption == null) {
					userOption = StimulatedPlanningFactory.createUserSelectedOption(user, currentSelectionObject, option);
					try {
						PersistentStore.writeDescriptor(userOption);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
				// checking and storing effects for special purpose cases
				if (foundId && userOption != null && currentSelectionObject.isUserAvatarPurpose()) {
					user.setAvatarUrl(userOption.getSelectedOption().getUrl());
					log.info("user "+user.getName()+" gets avatar "+userOption.getSelectedOption().getUrl());
					try {
						PersistentStore.writeUser(user);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else if (foundId && userOption != null && currentSelectionObject.isUserIdentityPurpose()) {
					UserOnlineStatus status = user.getOnlineStatus();
					status.setTitle(userOption.getSelectedOption().getTitle());
					status.setDescription(userOption.getSelectedOption().getDescription());
					log.info("user "+user.getName()+" gets identity "+userOption.getSelectedOption().getTitle());
					try {
						PersistentStore.writeDescriptor(status);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else if (currentSelectionObject.isClanAvatarPurpose()) {
					if (user.isTreatmentGroup()) {
						Clan clan = user.getClan();
						SelectionOption preferredOption = currentSelectionObject.getClanPreferredOption(clan);
						if (preferredOption != null) {
							clan.setClanLogo(preferredOption.getUrl());
							log.info("clan "+clan.getTitle()+" gets icon "+preferredOption.getUrl());
							try {
								PersistentStore.writeDescriptor(clan);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				} else if (currentSelectionObject.isClanIdentityPurpose()) {
					if (user.isTreatmentGroup()) {
						Clan clan = user.getClan();
						SelectionOption preferredOption = currentSelectionObject.getClanPreferredOption(clan);
						if (preferredOption != null) {
							log.info("clan "+clan.getTitle()+" will be updated to "+preferredOption.getTitle());
							clan.setTitle(preferredOption.getTitle());
							clan.setDescription(preferredOption.getDescription());
							try {
								PersistentStore.writeDescriptor(clan);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
		}
	

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
