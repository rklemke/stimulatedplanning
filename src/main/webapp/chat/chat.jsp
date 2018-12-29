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
</HEAD>
<!-- TODO: replace with iframes? -->
<FRAMESET rows="80%,20%">
<FRAME SRC="displayMessages.jsp#current" name="MessageWin">
<FRAME SRC="sendMessage.jsp" name="TypeWin">
</FRAMESET>
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