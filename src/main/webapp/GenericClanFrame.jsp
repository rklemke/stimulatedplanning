<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="stimulatedplanning.*, stimulatedplanning.util.*, senseofcommunity.*, java.util.*" %>
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
	if (informationObjectList == null) {
		informationObjectList = contentDescriptor.getAllInformationObjectList();
	}
  InformationObject currentInformationObject = (InformationObject)session.getAttribute("currentInformationObject");

	int currentInformationObjectIdx = 0;
	Object idxS = session.getAttribute("currentInformationObjectIdx");
	if (idxS == null) {
		idxS = request.getParameter("currentInformationObjectIdx");
	}
	if (idxS != null) {
		currentInformationObjectIdx = Integer.valueOf(idxS.toString());
	}

  
  
%>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Test User Navigation</title>
    <link rel="stylesheet" href="css/GenericClanFrameStyling.css">
    <link rel="stylesheet" href="css/WidgetStyling.css">
    <link rel="stylesheet" href="css/SelectionWidgetStyling.css">
    <link rel="stylesheet" href="css/ClanMembersStyling.css">
    <link rel="stylesheet" href="css/jquery-ui.css">
    <script src="https://code.jquery.com/jquery-1.12.4.js"></script>
    <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
    <script type="text/javascript">
    
	    $(document).ready(function () {
    		<% if (informationObjectList != null) { %>
    		<%    if (currentInformationObject instanceof SelectionObject) { %>
				$("#buttonSubmit").on("click", function() {
					$("#Selection_frameHolder").contents().find('form').submit();
				});
    		<%    } %>
    		<% } %>
    	    $(document).tooltip();
		});
	</script>
</head>
<body>
	<div class="container">
	
    <div id="RowOneColumnOne">
    <% if (user.isTreatmentGroup()) { %>	
	<img id="ClanLogo" src="<%= userClan.getClanLogo() %>"/>
	
	<img id="UserLogo" src="<%= user.getAvatarUrl() %>">
	<% } %>	
    </div><!-- RowOneColumnOne -->
    
    <div id="RowOneColumnTwo">
    <div id="frameHolder">
    <div id="Selection_frameHolder">
	<%    if (currentInformationObject instanceof SelectionObject) { %>
   		<jsp:include page="SelectionWidget.jsp">
			<jsp:param name="userid" value="<%= user.getId() %>" />
			<jsp:param name="userName" value="<%= user.getName() %>" />
			<jsp:param name="contentId" value="<%= contentId %>" />
			<jsp:param name="currentInformationObjectIdx" value="<%= currentInformationObjectIdx %>" />
   		</jsp:include>
	<%    } else { %>
   		<jsp:include page="InformationWidget.jsp">
			<jsp:param name="userid" value="<%= user.getId() %>" />
			<jsp:param name="userName" value="<%= user.getName() %>" />
			<jsp:param name="contentId" value="<%= contentId %>" />
			<jsp:param name="currentInformationObjectIdx" value="<%= currentInformationObjectIdx %>" />
   		</jsp:include>
	<%    } %>
    </div>
    </div>
    <div id="buttonControl">
	<form id="infoNavForm" method="POST" action="/GenericClanFrameServlet_SoC">
		<input type="hidden" id="userid" name="userid" value="<%= user.getId() %>"></input>
		<input type="hidden" id="userName" name="userName" value="<%= user.getName() %>"></input>
		<input type="hidden" id="contentId" name="contentId" value="<%= contentId %>"></input>
		<% if (informationObjectList != null) { %>
			<input type="hidden" id="currentInformationObjectIdx" name="currentInformationObjectIdx" value="<%= currentInformationObjectIdx %>"></input>
		<%    if (currentInformationObjectIdx > 0) { %>
			<input type="submit" id="buttonPrev" name="buttonPrev" class="ui-button ui-widget ui-corner-all" value="Prev"></input>
		<%    } %>
		<%    if (/*!(currentInformationObject instanceof SelectionObject) && */currentInformationObjectIdx < informationObjectList.size()-1) { %>
			<input type="submit" id="buttonNext" name="buttonNext" class="ui-button ui-widget ui-corner-all" value="Next"></input>
		<%    } %>
		<% } %>
	</form>
	<%    if (currentInformationObject instanceof SelectionObject) { %>
		<button id="buttonSubmit" class="ui-button ui-widget ui-corner-all">Submit</button>
	<%    } %>
	
	</div><!--button Control-->
    </div><!-- RowOneColumnTwo -->
    
    <div id="RowTwoColumnOne">
    	<div id="AW_frameHolder">
    		<jsp:include page="ClanMembers.jsp">
				<jsp:param name="userid" value="<%= user.getId() %>" />
				<jsp:param name="userName" value="<%= user.getName() %>" />
    		</jsp:include>
    	</div>
    </div><!-- RowTwoColumnOne -->
    
	</div><!--container-->

</body>
</html>