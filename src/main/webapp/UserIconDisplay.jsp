<%@ page 
	language="java" 
	contentType="text/html; charset=UTF-8" 
	pageEncoding="UTF-8" 
	import="stimulatedplanning.*, stimulatedplanning.util.*, senseofcommunity.*, java.util.*" 
%><%
  session = request.getSession();
  String userId = request.getParameter("userId");
  User user = (User)session.getAttribute(userId);
  UserOnlineStatus status = user.getOnlineStatus();
  String onlineStatus = "offline";
  if (status.isOnline()) {
	  onlineStatus = "online";
  } else if (status.isRecent()) {
	  onlineStatus = "recently online";
  }
  String color = request.getParameter("color");
  if (color == null) {
	  if (status.isOnline()) color = "lawngreen";
	  else if (status.isRecent()) color = "blue";
	  else color = "grey";
  }
  
%><div class="profiles" style="border-color:<%=color%>; background-color:<%=color%>;">
	<a 
		href="#" 
		title= "<%= user.getName() %><br /><%= onlineStatus %><br /><img src='<%= user.getAvatarUrl() %>' width='75px' height='75px'>"
	>
		<img src="<%= user.getAvatarUrl() %>" width="25px" height="25px">
	</a>
</div>