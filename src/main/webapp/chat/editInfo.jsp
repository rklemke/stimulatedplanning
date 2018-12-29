<%@ page isErrorPage="false" errorPage="error.jsp" import="chat.*, stimulatedplanning.*, stimulatedplanning.util.*, senseofcommunity.*, java.util.*" %>
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
if (nickname == null)
{
	out.write("<font color=\"red\" size=\"+1\">You have not logged in.</font>");
	out.close();
	return;
}
ChatRoomList roomList = StimulatedPlanningFactory.getChatRoomListForUser(user);
ChatRoom chatRoom = roomList.getRoomOfChatter(nickname);
if (chatRoom != null)
{
	Chatter chatter = chatRoom.getChatter(nickname);
%>
<HTML>
<HEAD>
<META http-equiv="pragma" content="no-cache">

<TITLE>Edit your (<%=chatter.getName()%>'s) Information</TITLE>
<LINK rel="stylesheet" href="/css/chat/chat.css" type="text/css">

</HEAD>

<BODY bgcolor="#FFFFFF">
<div align="center">
<center>
<FORM name="chatterinfo" method="post" action="<%=request.getContextPath()%>/servlet/saveInfo">
  <TABLE width="80%" border="0" cellspacing="0" cellpadding="2" align="center" bordercolor="#6633CC">
    <TR>
      <TD valign="top"><h4>Nickname:</h4></TD>
      <TD valign="top"><%=chatter.getName()%></TD>
		<input type="hidden" name="nickname" value="<%=chatter.getName()%>">
    </TR>
    <TR>
      <TD valign="top"><h4>Icon:</h4></TD>
      <TD valign="top"><%= chatter.getIconUrl() %></TD>
    </TR>
    <TR>
      <TD valign="top"><h4>Age:</h4></TD>
      <TD valign="top">
		<% String temp;
		int age = chatter.getAge();
		if(age == -1)
			temp = "";
		else
			temp = String.valueOf(age);
		%>
        <INPUT type="text" name="age" maxlength="2" size="2" value="<%=temp%>">
      </TD>
    </TR>
    <TR>
      <TD valign="top"><h4>Email:</h4></TD>
      <TD valign="top">
		<%
			temp = chatter.getEmail();
			if(temp == null)
				temp = "Not Specified";			
		%>
        <INPUT type="text" name="email" value="<%=temp%>">
      </TD>
    </TR>
    <TR>
      <TD valign="top"><h4>Comment:</h4></TD>
      <TD valign="top">
		<% 
			temp = chatter.getComment();
			if(temp==null)
				temp = "Not Specified";		
		%>
        <TEXTAREA cols="30" rows="5" wrap="VIRTUAL" name="comment"><%=temp%></TEXTAREA>
      </TD>
    </TR>
    <TR>
      <TD valign="top">&nbsp;</TD>
      <TD valign="top">
        <INPUT type="submit" name="Submit" value="Save">
      </TD>
    </TR>
  </TABLE>
</FORM>
</center>
</div>
</BODY>
</HTML>
<%
}
else
{
	out.write("<span class=\"error\">Problem getting room information of the chatter</span>");
}
%>