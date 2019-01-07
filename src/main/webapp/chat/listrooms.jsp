<%@ page session="true" errorPage="error.jsp" import="stimulatedplanning.*, stimulatedplanning.util.*, senseofcommunity.*, java.util.*,chat.*"%>
<%

	session = request.getSession();
	User user = (User)session.getAttribute("user");

	String roomName = null;
	String userId = user.getId();
	String nickname = user.getName();
	ChatRoomList roomList = null;
	ChatRoom room = null;

	try
	{
		roomList = StimulatedPlanningFactory.getChatRoomListForUser(user);
		room = roomList.getRoomOfChatter(userId);
		roomName = room.getName();
		
		String newRoom = request.getParameter("room");
		if ( newRoom != null && newRoom.length() > 0)
		{
			ChatRoom newChatRoom = roomList.getRoom(newRoom);
			
			if (room != null && newChatRoom != null)
			{
				if (!room.getName().equals(newChatRoom.getName()))
				{
					room.removeChatter(userId);
					newChatRoom.addChatter(user);
					if (!room.getName().equalsIgnoreCase("StartUp"))
					{
						room.addMessage(new Message(null, nickname + " has left and joined " + 	newChatRoom.getName() + ".", new java.util.Date().getTime()));
					}
					newChatRoom.addMessage(new Message(null, nickname + " has joined.", new java.util.Date().getTime()));
					//chatter.setEnteredInRoomAt(new java.util.Date().getTime());

				}

				if (session.getAttribute("nickname") == null)
				{
					session.setAttribute("nickname", nickname);
					//session.setAttribute("userId", userId);
				}
			}
			
		}

		
	}
	catch(Exception e)
	{
		System.out.println("Exception: "+ e.getMessage());
		throw new ServletException("Unable to get handle to ServletContext");
	}		
 
%>
 <fieldset>
    <legend>Select a Room: [Population]</legend>
    <% for (ChatRoom otherRoom: roomList.getRoomListArray()) { 
    		if (!"startup".equalsIgnoreCase(otherRoom.getName())) { 
    			boolean isSelected = room.getName().equals(otherRoom.getName()); 
    %>
    <label 
    	for="<%= otherRoom.getName() %>"
    	title="<%= otherRoom.getDescription() %>"
    ><%= otherRoom.getName() %> <span>[<%=otherRoom.getNoOfChatters()%>]</span> </label>
    <input 
    	type="radio" 
    	name="room" 
    	id="<%= otherRoom.getName() %>"
    	value="<%= otherRoom.getName() %>"
    	<% if (isSelected) { %>checked <% } %> 
    >
	<% 		}
    	} %>
 </fieldset>
