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
  
//String nickname = request.getParameter("chatterName");
	String nickname = user.getName();
%>

<HTML>
<HEAD>
<META http-equiv="pragma" content="no-cache">
<TITLE><%=nickname%>'s information</TITLE>
<LINK rel="stylesheet" href="/css/chat/chat.css" type="text/css">
</HEAD>

<BODY bgcolor="#FFFFFF">
<%
if (nickname != null)
{
	//ChatRoomList roomList = (ChatRoomList)application.getAttribute("chatroomlist");
	ChatRoomList roomList = StimulatedPlanningFactory.getChatRoomListForUser(user);
ChatRoom chatRoom = roomList.getRoomOfChatter(nickname);
if (chatRoom != null)
{
Chatter chatter = chatRoom.getChatter(nickname);
%>
<div align="center">
<center>
<table border="0" width="100%" cellpadding="0" cellspacing="0">
<tr>
	<TD valign="top"><h4>Nickname: </h4></td><TD valign="top"><%=nickname%></td>
</tr>
<tr>
<TD valign="top"><h4>Age: </h4></td><TD valign="top"><% int age = chatter.getAge();
		if(age == -1)
		out.write("Not specified");
		else
		out.write(String.valueOf(age));
		%></td>
</tr>
<tr>
	<TD valign="top"><h4>Email: </h4></td><TD valign="top"><% String email = chatter.getEmail();
			 if (email != null && email.length() >0)
			  	 out.write(email);
			 else
				 out.write("Not specified");
		%></td>
</tr>
<tr>
<TD valign="top"><H4>Comment: </h4></td><TD valign="top"><%
				String comment = chatter.getComment();
				if (comment != null && comment.length() >0)
					out.write(comment);
				else
					out.write("Not specified");
			%></h4>
</tr>
<tr><TD valign="top">&nbsp;</td>
<TD valign="top">
<form name="closing">
<input type="button" onClick="window.close()" value="Close">
</form>
</td>
<tr>
</table>
</center>
</div>

<%
}
else
{

%>
<div align="center">
<center>
<span class="error">User <%=nickname %> doesn't exist.<br>He / She may have logged out.</span>
<form name="closing">
<input type="button" onClick="window.close()" value="Close">
</form>
</center>
</div>
<%
}
}
else
{
%>
<div align="center">
<center>
<span class="error">No username provided.</span>
</center>
</div>
<%
}
%>
</BODY>
</HTML>