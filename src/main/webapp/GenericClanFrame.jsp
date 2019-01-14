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
  InformationObject currentInformationObject = (InformationObject)session.getAttribute("currentInformationObject");
  int currentInformationObjectIdx = 0;
  Object idxS = (Object)session.getAttribute("currentInformationObjectIdx");
  if (idxS != null) {
	  currentInformationObjectIdx = ((Integer)idxS).intValue();
  }
  
  
%>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Test User Navigation</title>
    <link rel="stylesheet" href="css/GenericClanFrameStyling.css">
    <link rel="stylesheet" href="css/jquery-ui.css">
    <script src="https://code.jquery.com/jquery-1.12.4.js"></script>
    <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
    <script type="text/javascript">
    
    	function SoC_updateClan() {
			$("#AW_frameHolder").attr("src","ClanMembers.jsp?userid=<%= user.getId() %>&userName=<%= user.getName() %>");
    		//setTimeout(SoC_updateClan, 5000);    		
    	}
    	
	    $(document).ready(function () {
			//this is temp and the frameholder src should be replaced by the active link
			$("#Selection_frameHolder").attr("src","InformationObjectServlet_SoC?userid=<%= user.getId() %>&userName=<%= user.getName() %>");
			$("#AW_frameHolder").attr("src","ClanMembers.jsp?userid=<%= user.getId() %>&userName=<%= user.getName() %>");
    		<% if (informationObjectList != null) { %>
    		<%    if (currentInformationObject instanceof SelectionObject) { %>
				$("#buttonSubmit").on("click", function() {
					$("#Selection_frameHolder").contents().find('form').submit();
				});
    		<%    } %>
    		<% } %>
    	    $(document).tooltip();
    		//setTimeout(SoC_updateClan, 5000);
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
    <iframe id="Selection_frameHolder">
    <p>Your browser does not support iframes.</p> 
    </iframe>
    </div>
    <div id="buttonControl">
	<form id="infoNavForm" method="POST" action="GenericClanFrameServlet_SoC">
		<% if (informationObjectList != null) { %>
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
    	<iframe id="AW_frameHolder" style="position: relative; height: 99%; width: 99%;" frameBorder="0"></iframe>
    </div><!-- RowTwoColumnOne -->
    
	</div><!--container-->

</body>
</html>