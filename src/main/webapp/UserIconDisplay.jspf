<%@ page 
	language="java" 
	contentType="text/html; charset=UTF-8" 
	pageEncoding="UTF-8" 
	import="stimulatedplanning.*, stimulatedplanning.util.*, senseofcommunity.*, java.util.*" 
%><%
  session = request.getSession();
  String userId = request.getParameter("userId");
  User user = (User)session.getAttribute(userId);
  String color = request.getParameter("color");
  if (color == null) {
	  UserOnlineStatus status = user.getOnlineStatus();
	  if (status.isOnline()) color = "lawngreen";
	  else if (status.isRecent()) color = "blue";
	  else color = "grey";
  }
  
%><div class="profiles" style="border-color:<%=color%>; background-color:<%=color%>;">
	<a href="#" title= "<%= user.getName() %>">
		<img src="<%= user.getAvatarUrl() %>" width="25px" height="25px">
	</a>
</div>