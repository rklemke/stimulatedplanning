<%@ page 
	isErrorPage="false" 
	errorPage="error.jsp" 
	import="java.text.DateFormat,chat.*, stimulatedplanning.*, stimulatedplanning.util.*, senseofcommunity.*, java.util.*" 
%>
<%
	session = request.getSession();
	User user = (User)session.getAttribute("user");
  
	String roonName = null;
	String nickname = user.getName();
	String userId = user.getId();
	//String nickname = (String)session.getAttribute("nickname");
	ChatRoomList roomList = null;
	ChatRoom chatRoom = null;
	Message[] messages = null;

	if (nickname != null)
	{
		try
		{
			roomList = StimulatedPlanningFactory.getChatRoomListForUser(user);
			roonName = roomList.getRoomOfChatter(userId).getName();
			if (roonName != null && roonName != "")
			{
				chatRoom = roomList.getRoom(roonName);
				if (chatRoom != null)
				{
					long enteredAt = 0; //chatter.getEnteredInRoomAt();
					if (enteredAt != -1)
					{
						messages = chatRoom.getMessages(enteredAt);					
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
	

	if(messages != null && messages.length > 0)
	{
		for (int i = 0; i < messages.length; i++)
		{
			Message message = (Message)messages[i];
			session.setAttribute("currentMessage", message);
			%><jsp:include page="SingleMessage.jsp" /><%
		}
		out.write("<a name=\"current\"></a>");
	}
	else
	{
		out.write("<font color=\"red\" face=\"Arial\" size=\"2\">There are currently no messages in this room</font>");
	}
	out.write("<a name=\"current\"></a>");
}
%>
	