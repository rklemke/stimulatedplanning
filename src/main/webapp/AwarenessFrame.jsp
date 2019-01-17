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
<head>
    <title>Awareness Widget</title>
    <link rel="stylesheet" href="css/AwarenessWidgetStyling.css">
    <link rel="stylesheet" href="css/ClanMembersStyling.css">
    <link rel="stylesheet" href="css/jquery-ui.css">
    <script src="https://code.jquery.com/jquery-1.12.4.js"></script>
    <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
    <script type="text/javascript">
        //when the document has finished loading. "$" sign calls the jquery
        $(document).ready(function () {
				//this is temp and the frameholder src should be replaced by the active link
<% if (user.isTreatmentGroup()) { %>
    	    $("#AW_OtherClanOnline").progressbar({
                value: <%= 100*otherClanOnline/otherClanSize %>
            });
        	$("#AW_OtherClanOffline").progressbar({
                value: <%= 100*otherClanOffline/otherClanSize %>
            });
<% } %>
        	$(document).tooltip();
		});
        
<% if (user.isTreatmentGroup()) { %>
    	function clan_tickerRequest() {
  		  $.ajax({
  		    dataType: 'jsonp',
  		    url: '<%= StimulatedPlanningFactory.applicationHome %>/ClanStatusServlet?userid=<%= user.getId() %>&userName=<%= user.getName() %>',
  		    success: function(clan_data) {
  	    	    $("#AW_OtherClanOnline").progressbar({
  	                value: (100 * clan_data.otherClanOnline + 100 * clan_data.otherClanRecent)/clan_data.otherClanSize
	            });
  	        	$("#AW_OtherClanOffline").progressbar({
  	                value: 100 * clan_data.otherClanOffline/clan_data.otherClanSize
  	            });
		    },
		    complete: function() {
 		    }
  		  });
  		}
  		
  		setInterval(clan_tickerRequest, 30000);
<% } %>

    </script>
</head>
<body>
<div class="container">

<% if (user.isTreatmentGroup()) { %>
    <div id="RowYourClan">
    <img src = "<%= userClan.getClanLogo() %>" class="imgHolder" />
    <div class="headerText"><%= userClan.getTitle() %> [<%= user.getName() %>]</div>
    </div><!-- rowYourClan -->
<% } %>

    <div id="RowClanMembers">
    <fieldset>
<% if (user.isTreatmentGroup()) { %>
    <legend>Clan Members</legend>
<% } else { %>
    <legend>Other Users</legend>
<% } %>
   		<jsp:include page="ClanMembers.jsp">
			<jsp:param name="userid" value="<%= user.getId() %>" />
			<jsp:param name="userName" value="<%= user.getName() %>" />
   		</jsp:include>
    </fieldset>	
	</div><!--rowClanMembers-->
	
	<div id="RowLegend">
	<fieldset>
    <legend>Legend</legend>
	
	<svg> <rect width="15" height="15" style="fill:green;" /></svg> 
	<strong style="color: green;"> Online</strong>
	
	<svg><rect width="15" height="15" style="fill:orange;"/></svg> 
	<strong style="color: orange;"> Recently Online </strong>
	
	<svg><rect width="15" height="15" style="fill:grey;"/></svg> 
	<strong style="color: grey;"> Offline </strong>
	</fieldset>
			
	</div><!-- rowLegend -->
	
	<div id="RowOtherClan">
    <img src = "<%= otherClan.getClanLogo() %>" class="imgHolder"/>
    <div class="headerText"><%= otherClan.getTitle() %></div>
    </div><!-- RowThree -->
    
	<div id="RowOtherClanMembers">
	
<% if (user.isTreatmentGroup()) { %>
	
	<Strong>Online members: </strong>
	<div id="AW_OtherClanOnline"></div>
	
    <Strong>Offline members: </strong>
	<div id="AW_OtherClanOffline"></div>
<% } %>
	</div><!-- RowThree -->

    <div class="clear"></div>
    </div><!--container-->

</body>

</html>
