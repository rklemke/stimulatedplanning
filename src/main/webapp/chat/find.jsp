<%@ page contentType="text/html" language="java" errorPage="error.jsp" import="chat.*, stimulatedplanning.*, stimulatedplanning.util.*, senseofcommunity.*, java.util.*" %>
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
  
	String submitted = request.getParameter("submitted");
	boolean postBack = false;
	if (submitted != null && "true".equals(submitted))
	{
		postBack = true;
	}
%>
<HTML>
<HEAD>
<TITLE>Find your friend</TITLE>
<META http-equiv="pragma" content="no-cache">
<LINK rel="stylesheet" href="/css/chat/chat.css" type="text/css">
</HEAD>
<BODY>
<%
	if (postBack)
	{
		//String nickname = request.getParameter("nickname");
		String nickname = user.getName();
		String roomName = null;
		if (nickname != null && nickname.length() > 0)
		{
			try
			{
				ChatRoomList roomList = StimulatedPlanningFactory.getChatRoomListForUser(user);
				ChatRoom chatRoom = roomList.getRoomOfChatter(nickname);
				if (chatRoom != null)
				{
					roomName = chatRoom.getName();
				}
				if (roomName == null)
				{
					out.write("<h3><i>" + nickname + "</i> not found in any room</h3>");
				}
				else
				{
					out.write("<h3><i>" + nickname + "</i> is in room " + roomName + "</h2>");
				}

			}
			catch (Exception e)
			{
				out.write("<h1>Some problem with server</h1>");
			}
		}
		else
		{
			out.write("<h4 class=\"error\">Please enter nickname of the person you want to find below</h4>");
		}
	}
%>
<H2>Enter nickname of the person you want to find.</H2>
<FORM action="find.jsp" method="post" name="find" id="find">
<STRONG>Nickname </STRONG>
		<INPUT name="nickname" type="text" id="nickname">
		<INPUT type="hidden" name="submitted" value="true">
		<input type="submit" value="Find">
</FORM>
<div align="center">
<center>
<form name="closing">
	<input type="button" onClick="window.close()" value="Close">
</form>
</center>
</div>
</BODY>
</HTML>
