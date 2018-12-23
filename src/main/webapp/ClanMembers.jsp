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
<% if (user.isTreatmentGroup()) { %>
<head>
    <link rel="stylesheet" href="css/ClanMembersStyling.css">
    <link rel="stylesheet" href="css/jquery-ui.css">
    <script src="https://code.jquery.com/jquery-1.12.4.js"></script>
    <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
</head>
<body>
   <div class="container" id="AW_myClanFrame">
      	<% for (UserOnlineStatus status: onlineUsers) { %>
      		<div class="profiles" style="border-color:lawngreen; background-color:lawngreen;">
				<a href="#" title= "<%= status.getUser().getName() %>">
					<img src="<%= status.getUser().getAvatarUrl() %>" width="25px" height="25px">
				</a>
      		</div>
       	<% } %>
       	<% for (UserOnlineStatus status: recentUsers) { %>
      		<div class="profiles" style="border-color:blue; background-color:blue;">
				<a href="#" title= "<%= status.getUser().getName() %>">
					<img src="<%= status.getUser().getAvatarUrl() %>" width="25px" height="25px">
				</a>
      		</div>
       	<% } %>
       	<% for (UserOnlineStatus status: offlineUsers) { %>
      		<div class="profiles" style="border-color:grey; background-color:grey;">
				<a href="#" title= "<%= status.getUser().getName() %>">
					<img src="<%= status.getUser().getAvatarUrl() %>" width="25px" height="25px">
				</a>
      		</div>
       	<% } %>

        </div>

</body>
 <% } %>
</html>
