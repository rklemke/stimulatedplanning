<%@ page errorPage="error.jsp" import="chat.*, stimulatedplanning.*, stimulatedplanning.util.*, senseofcommunity.*, java.util.*" %>
<%
  session = StimulatedPlanningFactory.initializeSession(request, response);
  User user = (User)session.getAttribute("user");
  UserOnlineStatus userStatus = user.getOnlineStatus();
  CourseDescriptor course = (CourseDescriptor)session.getAttribute("course");
  UserPlan userPlan = (UserPlan)session.getAttribute("userPlan");
  Clan userClan = null;

  HashArrayList<UserOnlineStatus> onlineUsers = new HashArrayList<>();
  HashArrayList<UserOnlineStatus> recentUsers = new HashArrayList<>();
  HashArrayList<UserOnlineStatus> offlineUsers = new HashArrayList<>();
  if (user.isTreatmentGroup()) {
	  userClan = user.getClan();
	  onlineUsers = userClan.getOnlineUsersSorted(userStatus);
	  recentUsers = userClan.getRecentUsersSorted(userStatus);
	  offlineUsers = userClan.getOfflineUsersSorted(userStatus);
  }
  
%>
<HTML>
<HEAD>
<TITLE>You have logged out</TITLE>
<link rel="stylesheet" type="text/css" href="/css/chat/chat.css">
<META http-equiv="pragma" content="no-cache">
</HEAD>

<BODY>
<%@ include file="header.jsp" %>
<div align="center">
<center>

<%
	//String nickname = (String)session.getAttribute("nickname");
	String nickname = user.getName();
	if (nickname != null && nickname.length() > 0)
	{
		//ChatRoomList roomlist = (ChatRoomList) application.getAttribute("chatroomlist");
		ChatRoomList roomlist = StimulatedPlanningFactory.getChatRoomListForUser(user);
		ChatRoom chatRoom = roomlist.getRoomOfChatter(nickname);
		chatRoom.addMessage(new Message("system", nickname + " has logged out.", new java.util.Date().getTime()));
		if ( chatRoom != null)
		{
			chatRoom.removeChatter(nickname);
			session.invalidate();
			out.write("<font color=\"blue\">You successfully logged out</font><br>");
			out.write("<a href=\"login.jsp\">Login again</a>");
		}
		else
		{
			//out.write("<h3><font color=\"red\">Unable to logout</font></h3>");
			response.sendRedirect("login.jsp");
		}
	}
	else
	{
		response.sendRedirect("login.jsp");
	}
	%>
</center>
</div>
</BODY>
</HTML>