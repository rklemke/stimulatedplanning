<%@ page language="java" 
	contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"
	import="stimulatedplanning.*, stimulatedplanning.util.*, senseofcommunity.*, java.util.*" 
%><%
  session = request.getSession();

  User user = (User)session.getAttribute("user");
  UserOnlineStatus userStatus = user.getOnlineStatus();
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
  
  boolean excludeSelf = true;
  String excludeSelfS = request.getParameter("color");
  if (excludeSelfS != null) {
	  excludeSelf = Boolean.valueOf(excludeSelfS);
  }

  
  String currentUserId ="";
  
  if (user.isTreatmentGroup()) {
	for (UserOnlineStatus status: onlineUsers) {
		if (!excludeSelf || !(user.getId().equals(status.getUser().getId()))) {
			currentUserId = "user"+status.getUser().getId();
			session.setAttribute(currentUserId, status.getUser());
			%><jsp:include page="UserIconDisplay.jsp" >
			<jsp:param name="userId" value="<%= currentUserId %>" />
			</jsp:include><% 
		}
	} 
	for (UserOnlineStatus status: recentUsers) { 
		if (!excludeSelf || !(user.getId().equals(status.getUser().getId()))) {
			currentUserId = "user"+status.getUser().getId();
			session.setAttribute(currentUserId, status.getUser());
			%><jsp:include page="UserIconDisplay.jsp" >
			<jsp:param name="userId" value="<%= currentUserId %>" />
			</jsp:include><% 
		}
	} 
	for (UserOnlineStatus status: offlineUsers)  { 
		if (!excludeSelf || !(user.getId().equals(status.getUser().getId()))) {
			currentUserId = "user"+status.getUser().getId();
			session.setAttribute(currentUserId, status.getUser());
			%><jsp:include page="UserIconDisplay.jsp" >
			<jsp:param name="userId" value="<%= currentUserId %>" />
			</jsp:include><% 
		}
	}
  }
%>