<%@ page session="true" import="chat.ChatRoomList, chat.ChatRoom" errorPage="error.jsp" import="stimulatedplanning.*, stimulatedplanning.util.*, senseofcommunity.*, java.util.*" %>
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
  
	//String nickname = (String)session.getAttribute("nickname");
	String nickname = user.getName();
	if (nickname != null && nickname.length() > 0)
	{
		ChatRoomList roomList = StimulatedPlanningFactory.getChatRoomListForUser(user);
		ChatRoom room = roomList.getRoomOfChatter(nickname);
		String roomname = room.getName();
%>
	
<HTML>
<HEAD>
<TITLE>S2R Chat - <%=nickname%> (<%=roomname%>) </TITLE>
<link rel="stylesheet" type="text/css" href="/css/chat/chat.css">
</HEAD>
<div class="container">
<div>
<iframe SRC="displayMessages.jsp#current" name="MessageWin" style="width:100%; height:84%;"></iframe>
</div>

<div>
<iframe SRC="sendMessage.jsp" name="TypeWin" style="width:100%; height:16%;margin-top: 0.2em;"></iframe>
</div>

</div><!-- container -->

<NOFRAMES>
<H2>This chat rquires a browser with frames support</h2>
</NOFRAMES>
</HTML>
<%
}
else
{
	response.sendRedirect("login.jsp");
}
%>