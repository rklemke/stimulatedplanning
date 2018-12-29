<%@ page session="true" errorPage="error.jsp" import="stimulatedplanning.*, stimulatedplanning.util.*, senseofcommunity.*, java.util.*,chat.*"%>
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
<TITLE>SS Chat - Room List</TITLE>
<LINK rel="stylesheet" type="text/css" href="/css/chat/chat.css">
<SCRIPT language="JavaScript">
//	 <!--
//	 if(window.top != window.self)
//	 {
//		 window.top.location = window.location;
//	 }
//	 //-->
</SCRIPT>
</HEAD>

<BODY bgcolor="#FFFFFF">
<%


//String nickname = (String)session.getAttribute("nickname");
String nickname = user.getName();
if (nickname == null || nickname == "")
{
	response.sendRedirect("/chat/login.jsp");
	//System.out.println("Redirecting");
}
else
{
	String roomname = request.getParameter("rn");	
	String descr = request.getParameter("sd");
	boolean see = false;
	if (descr != null && descr.equals("y"))
	{
		see = true;
	}
%>
<%@ include file="header.jsp" %>
<TABLE width="80%" align="center">	
	<tr>
		<td class="normal">Welcome <span class="chattername"><%=nickname%></span></td>
	</tr>
	<TR>
		<TD width="100%">Select the room you want to enter or click view description to view description
			 about the room.
		</TD>
	</TR>
</TABLE>
<BR>
	<%
				
		
		try
		{
			ChatRoomList roomlist = StimulatedPlanningFactory.getChatRoomListForUser(user);
			ChatRoom[] chatrooms = roomlist.getRoomListArray();
			if(roomname == null)
			{
				ChatRoom room = roomlist.getRoomOfChatter(nickname);
				if (room != null) {
					roomname = room.getName();	
				} else {
					roomname = "StartUp";
				}
			}
			roomname = roomname.trim();

	%>
<DIV align="center">
<CENTER>
	<FORM name="chatrooms" action="/chat/start.jsp" method="post">
	<TABLE width="80%" border="1" cellspacing="1" cellpadding="1" align="center">
	<TR>
	<TD colspan="2" class="pagetitle">Room List</TD>
	</TR>
	<%
			for (int i = 0; i < chatrooms.length; i++)
			{
				if (chatrooms[i].getName().equalsIgnoreCase("StartUp"))
					continue;
	%>
		<TR>
		<TD width="50%">
		<INPUT type=radio name="rn" value="<%=chatrooms[i].getName()%>"
		<%if (chatrooms[i].getName().equals(roomname))
			out.write("checked");%>><%=chatrooms[i].getName()%></A>
		</TD>
	<%
				if (see == true && chatrooms[i].getName().equals(roomname))
				{
	%>	
			<TD width="50%"><%=chatrooms[i].getDescription()%></TD>
	<%
				}
				else
				{
	%>
			<TD width="50%"><A href="/chat/listrooms.jsp?rn=<%=chatrooms[i].getName()%>&sd=y">View Description</A></TD>
	<%
				}
	%>
		</TR>
	<%
			}
		}
		catch (Exception e)
		{
			System.out.println("Unable to get handle to Servlet Context: " + e.getMessage());
			e.printStackTrace();
		}
%>
<TR>
	<TD>&nbsp;<A href="/chat/addRoom.jsp" title="Add new Room">Add new Room</A></TD>
	<TD><INPUT type="Submit" value="Start"></TD>
</TR>
</TABLE>
</FORM>
</CENTER>
</DIV>
<%
	}
%>
<%@ include file="footer.jsp"%>
</BODY>
</HTML>