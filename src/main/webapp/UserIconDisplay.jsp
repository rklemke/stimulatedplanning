<%@ page 
	language="java" 
	contentType="text/html; charset=UTF-8" 
	pageEncoding="UTF-8" 
	import="stimulatedplanning.*, stimulatedplanning.util.*, senseofcommunity.*, java.util.*, java.util.logging.Logger" 
%><%! static Logger log = Logger.getLogger("UserIconDisplay_jsp"); %><%
  session = request.getSession();
  String userId = request.getParameter("userId");
  //log.info("userId: "+userId);
  User user = (User)session.getAttribute(userId);
  if (user==null) {
	  userId = userId.substring(4);
	  //user = (User)StimulatedPlanningFactory.getObject(User.class.getName(), userId);
	  user = PersistentStore.getUser(userId, new HashMap<String,Object>());
  }
  //log.info("userId: "+userId+", user: "+user);
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
	  else if (status != null && status.isRecent()) color = "orange";
	  else color = "grey";
  }

  String size = request.getParameter("size");
  if (size == null) {
	  size = "25px";
  }
  
  boolean allowChat = true;
  String allowChatS = request.getParameter("allowChat");
  if (allowChatS != null) {
	  allowChat = Boolean.valueOf(allowChatS);
  }
  
  if (allowChat) {
%><script>
$( function() {
    $( "#userIcon_<%= user.getId() %>" ).on( "click", openChat );
  } 
);

</script><%	  
  }
%><div class="profiles" style="border-color:<%=color%>; background-color:<%=color%>;">
	<a 
		href="#" 
		id="userIcon_<%= user.getId() %>"
		title= "<%= user.getName() %><br /><%= onlineStatus %><br /><img src='<%= user.getAvatarUrl() %>' width='75px' height='75px'><br /><%= user.getOnlineStatus().getTitle() %>"
	>
		<img src="<%= user.getAvatarUrl() %>" width="<%= size %>" height="<%= size %>">
	</a>
</div>