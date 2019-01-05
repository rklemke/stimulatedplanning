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
  if (status != null && status.isOnline()) {
	  onlineStatus = "online";
  } else if (status != null && status.isRecent()) {
	  onlineStatus = "recently online";
  }

  String color = request.getParameter("color");
  if (color == null) {
	  if (status != null && status.isOnline()) color = "lawngreen";
	  else if (status != null && status.isRecent()) color = "blue";
	  else color = "grey";
  }

  String size = request.getParameter("size");
  if (size == null) {
	  size = "25px";
  }
  
%><div class="profiles" style="border-color:<%=color%>; background-color:<%=color%>;">
	<a 
		href="#" 
		title= "<%= user.getName() %><br /><%= onlineStatus %><br /><img src='<%= user.getAvatarUrl() %>' width='75px' height='75px'>"
	>
		<img src="<%= user.getAvatarUrl() %>" width="<%= size %>" height="<%= size %>">
	</a>
</div>