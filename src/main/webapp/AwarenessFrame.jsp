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
		});
        
    	function clan_tickerRequest() {
  		  $.ajax({
  		    dataType: 'jsonp',
  		    url: '/ClanStatusServlet',
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

    </script>
</head>
<body>

    <div class="container">
    
    <div class="row" id="RowOne">
    <div class="column" id="RowOneColumnOne">
    
    <img src = "<%= userClan.getClanLogo() %>" class="imgHolder" />
    <div class="headerText"><%= userClan.getTitle() %> [<%= user.getName() %>]</div>
    
    </div><!-- column  -->
    <div class="column" id="RowOneColumnTwo">
    
    <img src = "<%= otherClan.getClanLogo() %>" class="imgHolder"/>
    <div class="headerText"><%= otherClan.getTitle() %></div>
    
    </div><!-- column -->
    </div><!-- row One -->
    
    <div class="row" id="RowTwo">
    <div class="column" id="RowTwoColumnOne">
    
    <fieldset>
    <legend>Clan Members</legend>
    <iframe id="AW_frameHolder" frameBorder="0" style="position: relative; height: 98%; width: 98%;"></iframe>
    </fieldset>
			
	</div><!--columnOne-->
	
	<div class="column" id="RowTwoColumnTwo">
	
	<Strong>Online members: </strong>
	<div id="AW_OtherClanOnline"></div>
	
    <Strong>Offline members: </strong>
	<div id="AW_OtherClanOffline"></div>
	
	<fieldset>
    <legend>Legend</legend>
	<strong style="color: green; margin: 0.25em;"> 
	<svg> <rect width="15" height="15" style="fill:green;" /></svg> 
	Online in your page</strong>
	
	<strong style="color: blue; margin: 0.25em;">
	<svg> <rect width="15" height="15" style="fill:blue;" /> </svg> 
	Online in other pages</strong>
	
	<strong style="color: orange;margin: 0.25em;"> 
	<svg><rect width="15" height="15" style="fill:orange;"/></svg> 
	Recently Online </strong>
	
	<strong style="color: grey; margin: 0.25em;"> 
	<svg><rect width="15" height="15" style="fill:grey;"/></svg> 
	Offline </strong>
	</fieldset>
			
	</div><!-- columnTwo -->
    </div><!-- rowTwo -->
    
    <div class="clear"></div>
    </div><!--container-->

</body>

<% } %>

</html>
