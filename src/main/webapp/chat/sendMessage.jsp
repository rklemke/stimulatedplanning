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
	String userId = user.getId();
	
	if (nickname != null && nickname.length() > 0)
	{
		//ChatRoomList roomList = (ChatRoomList)application.getAttribute("chatroomlist");
		ChatRoomList roomList = StimulatedPlanningFactory.getChatRoomListForUser(user);
		ChatRoom chatRoom = roomList.getRoomOfChatter(userId);
		if ( chatRoom != null)
		{
			String msg = request.getParameter("messagebox");
			
			if ( msg != null && msg.length() > 0)
			{
				msg = msg.trim();
				chatRoom.addMessage(new Message(user, msg, new java.util.Date().getTime()));
			}
	
%>
<HTML>
<HEAD>
<LINK rel="stylesheet" type="text/css" href="/css/chat/chat.css">
<META http-equiv="pragma" content="no-cache">
<SCRIPT language="JavaScript" type="text/javascript">
<!--

function winopen(path)
{
	chatterinfo = window.open(path,"chatterwin","scrollbars=no,resizable=yes, width=400, height=300, location=no, toolbar=no, status=no");
	chatterinfo.focus();

}

//-->
</SCRIPT>
</HEAD>
<BODY onLoad="document.msg.messagebox.focus();" bgcolor="#FFFFFF">

<div class="container">

<div class = "column" id= "columnOne_sendMessage">
<FORM name="msg" action="sendMessage.jsp" method="post">
<INPUT type="text" name="messagebox" maxlength="300"  />
<INPUT type="hidden" name="nickname" value="<%=session.getAttribute("nickname")%>"/>
<INPUT name="submit" type="submit" value="Send"/>
</FORM>
</div> <!-- columnOnesendMessage-->

<div class="column" id="columnTwo_sendMessage">
</div><!--columnTwosendMessage-->

</div><!-- container -->

</BODY>
</HTML>
<%
		}
		else
		{
			out.write("<h2 class=\"error\">Your room couldn't be found. You can't send message</h2>");
		}
	}
	else
	{
		response.sendRedirect("login.jsp");
	}
%>
