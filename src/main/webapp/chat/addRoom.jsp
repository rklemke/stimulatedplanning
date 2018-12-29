<%@ page errorPage="error.jsp" import="stimulatedplanning.*, stimulatedplanning.util.*, senseofcommunity.*, java.util.*" %>
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
<html>
<head>
	<title>Add new Room</title>
	<link rel="stylesheet" type="text/css" href="/css/chat/chat.css">
</head>

<body>
<%@ include file="header.jsp" %>

<div align="center">
<center>
<form action="<%=request.getContextPath()%>/servlet/manageChat" method="post">
<table width="80%" cellpadding="0" cellspacing="0" border="0">
	<%
	Object e = request.getAttribute("error");
	String error = null;
	if ( e != null)
	{
		error = (String)e;
	%>
	<tr>
		<td colspan="2"><h3 class="error"><%=error%></h3></td>
	</tr>
	<%
	}
	%>
	<tr>
		<td colspan="2"><h2>Add new Room</h2></td>
	</tr>
	<tr>
		<td><b>Room Name (no spaces)</b></td><td><input type="text" name="rn"></td>
	</tr>
	<tr>
		<td><b>Description</b></td><td><textarea rows="5" cols="30" name="rd"></textarea></td>
	</tr>
	<tr>
		<td>&nbsp;</td><td><input type="submit" value="Submit"></td>
	</tr>
</table>
</form>
</center>
</div>
<%@ include file="footer.jsp"%>

</body>
</html>