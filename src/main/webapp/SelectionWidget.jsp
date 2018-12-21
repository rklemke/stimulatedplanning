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
  
  
%>


<!DOCTYPE html>
<html>
<head>
    <title>Selection Widget</title>
    <link rel="stylesheet" href="css/SelectionWidgetStyling.css">
    <link rel="stylesheet" href="css/jquery-ui.css">
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
	<ol id="selectable">
	<li class="ui-widget-content">Robin hack</li>
	<li class="ui-widget-content">Hacker MaNN</li>
	<li class="ui-widget-content">Promi Stalkers</li>
	<li class="ui-widget-content">Creditcard Chiefs</li>
	<li class="ui-widget-content">Curious Kids</li>
	</ol>
	
	</div><!--column-->
	
	<div id="buttonControl">
	<button class="ui-button ui-widget ui-corner-all">Submit</button>
	</div><!--button Control-->
	
	</div><!--container-->
	
	</body>

</html>
