<%@ page isErrorPage="false" errorPage="error.jsp" import="java.text.DateFormat,chat.*, stimulatedplanning.*, stimulatedplanning.util.*, senseofcommunity.*, java.util.*" %>
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
  
	String roonName = null;
	String nickname = user.getName();
	//String nickname = (String)session.getAttribute("nickname");
	ChatRoomList roomList = null;
	ChatRoom chatRoom = null;
	Chatter chatter = null;
	Message[] messages = null;

	if (nickname != null)
	{
		try
		{
			roomList = StimulatedPlanningFactory.getChatRoomListForUser(user);
			roonName = roomList.getRoomOfChatter(nickname).getName();
			if (roonName != null && roonName != "")
			{
				chatRoom = roomList.getRoom(roonName);
				chatter = chatRoom.getChatter(nickname);
				if (chatRoom != null)
				{
					long enteredAt = chatter.getEnteredInRoomAt();
					if (enteredAt != -1)
					{
						messages = chatRoom.getMessages(enteredAt);					
					}
					else
					{
						messages = chatRoom.getMessages(chatter.getLoginTime());
					}

				}
				else
				{
					out.write("<b>Room " + roonName + " not found</b>");
					out.close();
				}
			}
		}
		catch(Exception e)
		{
			System.out.println("Exception: "+ e.getMessage());
			throw new ServletException("Unable to get handle to ServletContext");
		}	
	
%>
<HTML>
<HEAD>
<!--<meta http-equiv="refresh" content="10">-->
<link rel="stylesheet" type="text/css" href="/css/chat/chat.css">
<%
	int refreshAfter = 5000; // 5 seconds
	String t = application.getInitParameter("refreshAfter"); // gets seconds
	if (t != null)
	{
		try
		{
			refreshAfter = Integer.parseInt(t);
			refreshAfter = refreshAfter * 1000; // convert to mili seconds
		}
		catch (NumberFormatException nfe)
		{							
		}
	}
%>
<script language="JavaScript" type="text/javascript">
<!--
function reload()
{
	window.location.reload();
}

setInterval('reload()', <%=refreshAfter%>);

function winopen(path)
{
	chatterinfo = window.open(path,"chatterwin","scrollbars=no,resizable=yes, width=400, height=300, location=no, toolbar=no, status=no");
	chatterinfo.focus();
}
//-->
</script>
</HEAD>
<BODY onLoad="window.location.hash='#current'" bgcolor="#FFFFFF">

<table width="100%" border="0">
<tr>
<td width="70%" valign="top">
<%@ include file="header.jsp" %>
<table>
<tr>
<td>
<h3><i><%=(String)session.getAttribute("nickname")%></i> you are in room <b><%=roonName%></b></h3>
<%
	
	if(messages != null && messages.length > 0)
	{
		for (int i = 0; i < messages.length; i++)
		{
			Message message = (Message)messages[i];
			String chatterName = message.getChatterName();
			String strmsg = message.getMessage();
			long time = message.getTimeStamp();
			Date date = new Date(time);

			if (chatterName.equalsIgnoreCase((String)session.getAttribute("nickname")))
			{
				out.write("<font face=\"Arial\" size=\"2\" color=\"blue\"><b>" + chatterName + " ("+ DateFormat.getTimeInstance().format(date)+ ")&gt;</b></font> " + strmsg+"<br>\n");
			}
			else if (chatterName.equalsIgnoreCase("system"))
			{
				out.write("<span class=\"error\">" + strmsg+"</span><br>\n");
			}
			else
			{
				out.write("<font face=\"Arial\" size=\"2\"><b>"+chatterName + " ("+ DateFormat.getTimeInstance().format(date)+ ")&gt;</b></font> " + strmsg + "<br>\n");
			}			
		}
		out.write("<a name=\"current\"></a>");
	}
	else
	{
		out.write("<font color=\"red\" face=\"Arial\" size=\"2\">There are currently no messages in this room</font>");
	}
	out.write("<a name=\"current\"></a>");
	%>
</td>
</tr>
</table>
	</td>
	<td width="30%" valign="top">
	<table width="100%" border="1" cellpadding="2" cellspacing="0">
		<tr>
			<td>
			<table width="100%" border="0" cellpadding="0" cellspacing="0" bgcolor="#2C259C">
		<tr>
			<td>
	<span class="white"><%=chatRoom.getNoOfChatters()%> people in this room</span><br>
	</td>
	</tr>
	</table>
	<%
	Chatter[] chatters = chatRoom.getChattersArray();
	String currentUserId = "";
	for(int i = 0; i < chatters.length; i++)
	{
  		currentUserId = "user"+chatters[i].getUser().getId();
  		session.setAttribute(currentUserId, chatters[i].getUser());
  		%><jsp:include page="/UserIconDisplay.jspf" >
			<jsp:param name="userId" value="<%= currentUserId %>" />
		 </jsp:include>
   	<% }

}
else
{
	response.sendRedirect("login.jsp");
}
%>
		</td>
	</tr>
</table>
</td>
</tr>
</table>
</BODY>
</HTML>