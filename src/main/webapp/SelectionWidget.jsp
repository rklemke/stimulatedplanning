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
  
  ContentDescriptor contentDescriptor = (ContentDescriptor)session.getAttribute("contentDescriptor");
  List<InformationObject> informationObjectList = (List<InformationObject>)session.getAttribute("informationObjectList");
  SelectionObject currentSelectionObject = (SelectionObject)session.getAttribute("currentInformationObject");
  int currentInformationObjectIdx = 0;
  Object idxS = (Object)session.getAttribute("currentInformationObjectIdx");
  if (idxS != null) {
	  currentInformationObjectIdx = ((Integer)idxS).intValue();
  }
  
%>


<!DOCTYPE html>
<html>
<head>
    <title>Selection Widget</title>
    <link rel="stylesheet" href="css/SelectionWidgetStyling.css">
    <link rel="stylesheet" href="https://code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
    <!-- link rel="stylesheet" href="css/jquery-ui.css"  -->
    <script src="https://code.jquery.com/jquery-1.12.4.js"></script>
    <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
    <script type="text/javascript">
	$(document).ready(function () {
		  $( "#selectable" ).selectable();
	});

	</script>

</head>

	<body>
	
	<div class="container">
	
	<div style="display: inline-block; text-align: center; width: 98%; height: 10%">
	<strong style=" font-size: 24px">Help your clan to find your identity!</strong>
	</div>
	
	<div class="column">
	<form id="selectionForm" method="POST" action="InformationObjectServlet_SoC">
	<input type="hidden" id="submitIndicator" name="submitIndicator" value="true">
	<ol id="selectable">
	<% for (SelectionOption option : currentSelectionObject.getOptionList()) { 
		ArrayList<UserSelectedOption> selectedOptions = new ArrayList<>();
		UserSelectedOption userSelectedOption = PersistentStore.readUserSelectionOption(user, currentSelectionObject, option);
		int optionCount = 0;
		int clanSize = 1;
		boolean isSelected = (userSelectedOption!=null);
		if (user.isTreatmentGroup()) {
			selectedOptions = StimulatedPlanningFactory.readClanSelectionOption(user.getClan(), currentSelectionObject, option);
			optionCount = selectedOptions.size();
			clanSize = Math.max(clanSize, user.getClan().userCount());
		}
	%>
	  <li class="ui-widget-content" id="li-<%= option.getId() %>">
    	<input class="selectionOption" type="checkbox" name="selectionRadio" id="so-<%= option.getId() %>" value="<%= option.getId() %>" <% if (isSelected) { %>checked <% } %> >
	  	<%= option.getTitle() %><%
		if (user.isTreatmentGroup()) {
			for (UserSelectedOption selectedOption: selectedOptions) { 
			%><!-- div class="profiles" style="" -->
					<a href="#" title= "<%= selectedOption.getUser().getName() %>">
						<img src="<%= selectedOption.getUser().getAvatarUrl() %>" width="25px" height="25px">
					</a>
	      		<!-- /div --><%
	        } 
		 } 
		 %></li>
	<% } %>
	</ol>
	</form>
	
	</div><!--column-->
	
	</div><!--container-->
	
	</body>

</html>
