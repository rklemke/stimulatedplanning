<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="stimulatedplanning.*, stimulatedplanning.util.*, senseofcommunity.*, java.util.*, java.util.logging.Logger" %>
<%! static Logger log = Logger.getLogger("InformationWidget_jsp"); %>
<%
  session = StimulatedPlanningFactory.initializeSession(request, response);

  User user = (User)session.getAttribute("user");
  UserOnlineStatus userStatus = user.getOnlineStatus();
  CourseDescriptor course = (CourseDescriptor)session.getAttribute("course");
  UserPlan userPlan = (UserPlan)session.getAttribute("userPlan");

  Clan userClan = null;
  Clan otherClan = null;
  int otherClanSize = 1;
  int otherClanOnline = 0;
  int otherClanRecent = 0;
  int otherClanOffline = 0;
  
  HashArrayList<UserOnlineStatus> onlineUsers = new HashArrayList<>();
  HashArrayList<UserOnlineStatus> recentUsers = new HashArrayList<>();
  HashArrayList<UserOnlineStatus> offlineUsers = new HashArrayList<>();
  if (user.isTreatmentGroup()) {
	  userClan = user.getClan();
	  otherClan = StimulatedPlanningFactory.getOtherClan(userClan);
	  onlineUsers = userClan.getOnlineUsersSorted(userStatus);
	  recentUsers = userClan.getRecentUsersSorted(userStatus);
	  offlineUsers = userClan.getOfflineUsersSorted(userStatus);
	  otherClanSize = otherClan.userCount()+1;
	  otherClanOnline = otherClan.getOnlineUsers().size()+1;
	  otherClanRecent = otherClan.getRecentUsers().size()+1;
	  otherClanOffline = otherClan.getOfflineUsers().size()+1;
  }
  
	ContentDescriptor contentDescriptor = null;
	String contentId = request.getParameter("contentId");
	String contentName = request.getParameter("contentName");
	String pageurl = request.getParameter("pageurl");
	if (contentId != null) {
		contentDescriptor = (ContentDescriptor)StimulatedPlanningFactory.getObject(contentId);
	} 
	if (contentDescriptor == null && pageurl != null) {
		contentDescriptor = course.getContentByUrl(pageurl);
	} 
	if (contentDescriptor == null) {
		contentDescriptor = (ContentDescriptor)session.getAttribute("contentDescriptor");
	}
	
	List<InformationObject> informationObjectList = (List<InformationObject>)session.getAttribute("informationObjectList");
	if (informationObjectList == null && contentDescriptor != null) {
		informationObjectList = contentDescriptor.getAllInformationObjectList();
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

	  String logString = "contentDescriptor: "+(contentDescriptor==null?"null":contentDescriptor.getId());
	  logString += ", informationObjectList: "+(informationObjectList==null?"null":informationObjectList.size());
	  logString += ", contentId: "+contentId;
	  logString += ", currentInformationObjectIdx: "+currentInformationObjectIdx;
	  log.info(logString);
	  
  
%>
	<div class="container">
<% if (currentInformationObject != null) { %>	
	<div style="display: inline-block; text-align: center; width: 98%; height: 10%">
	<strong style=" font-size: 24px"><%= currentInformationObject.getTitle() %></strong>
	</div>
	
	<div class="column">
		<%= currentInformationObject.getDescription() %>
		<%= currentInformationObject.getContent() %>
	</div><!--column-->	
<% } %> 	
	</div><!--container-->
