<%@ page session="true" errorPage="error.jsp" import="stimulatedplanning.*, stimulatedplanning.util.*, senseofcommunity.*, java.util.*,chat.*"%>
<%

	session = request.getSession();
	User user = (User)session.getAttribute("user");

	String userId = user.getId();
	ChatRoomList roomList = null;
	ChatRoom room = null;

	try
	{
		roomList = StimulatedPlanningFactory.getChatRoomListForUser(user);
		room = roomList.getRoomOfChatter(userId);
	}
	catch(Exception e)
	{
		System.out.println("Exception: "+ e.getMessage());
		throw new ServletException("Unable to get handle to ServletContext");
	}		
 
%>
 <fieldset>
    <legend>In the room</legend>
<%
	User[] chatters = room.getChattersArray();
	String currentUserId = "";
	for(User chatter: room.getChattersArray())
	{
  		currentUserId = "user"+chatter.getId();
  		session.setAttribute(currentUserId, chatter);
  		%> 
  		<jsp:include page="/UserIconDisplay.jsp" >
  		<jsp:param name="userId" value="<%= currentUserId %>" />
  		</jsp:include>
  		
   	<% } %>
</fieldset>
