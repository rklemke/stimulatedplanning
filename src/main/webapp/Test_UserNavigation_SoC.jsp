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


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Test User Navigation</title>
  <link rel="stylesheet" href="https://code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
  <link rel="stylesheet" href="https://jqueryui.com/resources/demos/style.css">
  <script src="https://code.jquery.com/jquery-1.12.4.js"></script>
  <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
    <script type="text/javascript">
    $( function() {
  	    var contentId = $( "#contentId" );
  	    var contentName = $( "#contentName" );
   
      $( "#contentSelect" ).selectmenu({
        change: function( event, data ) {
          contentId.val(data.item.value);
          contentName.val($( "#contentSelect option:selected" ).text());
          $("#contentFrame").attr("src", "GenericClanFrameServlet_SoC?contentId="+data.item.value+"&contentName="+$( "#contentSelect option:selected" ).text());
        }
       });
   
    } );
	</script>
</head>
<body>

<H2>Select a content.</H2>
<form id="contentSelectForm">
<label for="contentSelect">Select a content:</label>
<select name="contentSelect" id="contentSelect">
  <option value="unknown">Default</option>
  <% for (ModuleDescriptor module: course.getModuleList()) { %>
  <% 	for (LessonDescriptor lesson: module.getLessonList()) { %>
  <% 		for (ContentDescriptor content: lesson.getContentList()) { %>
    <option value="<%= content.getId() %>"><%= content.getTitle() %></option>
  <%		} %>
  <%	} %>
  <% } %>
</select>
<input type="hidden" value="unknown" id="contentId" name="contentId">
<input type="hidden" value="Guest" id="contentName" name="contentName">
<input type="submit" id="submitLogin" name="submitLogin" value="Login"></input>
<input type="submit" id="submitClan" name="submitClan" value="Clan"></input>  -->
</form>

<iframe id="contentFrame" style="width:860px; height:480px;"></iframe>

</body>
</html>