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
<link rel="stylesheet" type="text/css" href="/css/chat/chat.css">
  <link rel="stylesheet" href="https://code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
  <link rel="stylesheet" href="https://jqueryui.com/resources/demos/style.css">
  <script src="https://code.jquery.com/jquery-1.12.4.js"></script>
  <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
<script type="text/javascript">
  function openChat() {
      var src = "/chat/servlet/LoginServlet";
      if ($( "#chatFrame" ).attr("src") == "") {
    	  //$( "#chatFrame" ).on("load", resizeChatFrame);
	      $( "#chatFrame" ).attr("src", src);
      }
  	  $( "#chatDialog" ).dialog( "open" );
  }
  
  $( function() {
	    $( "#chatDialog" ).dialog({
	      autoOpen: false,
	      width: 840,
	      show: {
	        effect: "blind",
	        duration: 1000
	      },
	      hide: {
	        effect: "blind",
	        duration: 1000
	      }
	    });
	 
	    $( "#chatOpener" ).on( "click", openChat);
	  } 
  );

</script>   
</head>
<body>

<H2>Welcome, <%= user.getName() %></H2>
<a href="Test_UserSelection_SoC.jsp">Change user</a>
<button id="chatOpener">Open Chat</button>
<div id="chatDialog" title="chat tool">
	<iframe 
		id="chatFrame" 
		src="" 
		style="width:50em; height:25em; position: relative;" 
		frameborder="0">
	</iframe>
</div>
</body>
</html>