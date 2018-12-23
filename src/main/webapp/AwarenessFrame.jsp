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
	  otherClanSize = otherClan.userCount();
	  if (otherClanSize == 0) {
		  otherClanSize = 1;
	  }
	  otherClanOnline = otherClan.getOnlineUsers().size();
	  otherClanRecent = otherClan.getRecentUsers().size();
	  otherClanOffline = otherClan.getOfflineUsers().size();
  }
  
  
%>


<!DOCTYPE html>
<html>
<% if (user.isTreatmentGroup()) { %>
<head>
    <title>Awareness Widget</title>
    <link rel="stylesheet" href="css/AwarenessWidgetStyling.css">
    <link rel="stylesheet" href="css/jquery-ui.css">
    <script src="https://code.jquery.com/jquery-1.12.4.js"></script>
    <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
    <script type="text/javascript">
        //when the document has finished loading. "$" sign calls the jquery
        $(document).ready(function () {
				//this is temp and the frameholder src should be replaced by the active link
			$("#AW_frameHolder").attr("src","ClanMembers.jsp");
		
    	    $("#AW_OtherClanOnline").progressbar({
                value: <%= 100*otherClanOnline/otherClanSize %>
            });
        	$("#AW_OtherClanOffline").progressbar({
                value: <%= 100*otherClanOffline/otherClanSize %>
            });
        	$(document).tooltip();
	
		})
    </script>
</head>
<body>

    <div class="container">
	
        <div class="column" id="GridOne">
			<div class="imgHolder"><img src = "<%= userClan.getClanLogo() %>"/></div>
			<div class="headerText"><%= userClan.getTitle() %> [<%= user.getName() %>]</div>
			<iframe id="AW_frameHolder"></iframe>
		</div><!--columnOne-->
		
        <div class="column" id="GridTwo">
			<div class="imgHolder"><img src = "<%= otherClan.getClanLogo() %>"/></div>
			<div class="headerText"><%= otherClan.getTitle() %></div>
			
            <div style="display: block; margin-top: 2%; padding: 1%"><Strong>Online members: </strong></div>
			<div id="AW_OtherClanOnline"></div>
            <div style="display: block; margin-top: 2%; padding: 1%"><Strong>Offline members: </strong></div>
			<div id="AW_OtherClanOffline"></div>
        </div><!--columnTwo-->
		
		<div class="column" id="GridThree">
		<div class="ledger">
		<svg> <rect style="fill: green;"></rect></svg>
		<strong style="color: green; margin: 0.25em;"> Online in your page</strong>
		</div>
		
		<div class = "ledger">
		<svg> <rect style="fill: blue;" /> </svg>
		<strong style="color: blue; margin: 0.25em;"> Online in other pages</strong>
		</div>
		
        <div class = "ledger">
		<svg><rect style="fill: orange;" /></svg>
		<strong style="color: orange;margin: 0.25em;"> Recently Online </strong>
		</div>
		
		<div class = "ledger">
		<svg><rect style="fill: grey;" /></svg>
		<strong style="color: grey; margin: 0.25em;"> Offline </strong>
		</div><!--ledger-->
		
		</div><!--GridThree-->
		
    </div><!--container-->



</body>
<% } %>

</html>
