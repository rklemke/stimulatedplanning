<%@ page errorPage="error.jsp" import="chat.*, stimulatedplanning.*, stimulatedplanning.util.*, senseofcommunity.*, java.util.*" %>
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
  
  System.out.println("start.jsp: 1");
  
	String roomname = request.getParameter("rn");
	//String nickname = (String)session.getAttribute("nickname");
	String nickname = user.getName();
	//ChatRoomList roomlist = (ChatRoomList) application.getAttribute("chatroomlist");
	ChatRoomList roomlist = StimulatedPlanningFactory.getChatRoomListForUser(user);
	if (nickname == null)
	{
		System.out.println("start.jsp: 2");
		response.sendRedirect("login.jsp");
	}
	else if (roomname == null)
	{
		System.out.println("start.jsp: 3");
		response.sendRedirect("listrooms.jsp");
	}
	else
	{
		System.out.println("start.jsp: 4 "+roomname);
		ChatRoom chatRoom = roomlist.getRoom(roomname);
		if (chatRoom == null)
		{
			out.write("<font color=\"red\" size=\"+1\">Room " + roomname + " not found</font>");
			out.close();
			return;
		}
		System.out.println("start.jsp: 5 "+ nickname);
		ChatRoom chatRoomOld = roomlist.getRoomOfChatter(nickname);
		if (chatRoomOld != null && chatRoom != null)
		{
			System.out.println("start.jsp: 6");
			Chatter chatter = chatRoomOld.getChatter(nickname);
			
			if (!chatRoomOld.getName().equals(chatRoom.getName()))
			{
				chatRoomOld.removeChatter(nickname);
				chatRoom.addChatter(chatter);
				if (!chatRoomOld.getName().equalsIgnoreCase("StartUp"))
				{
					chatRoomOld.addMessage(new Message("system", nickname + " has left and joined " + 	chatRoom.getName() + ".", new java.util.Date().getTime()));
				}
				chatRoom.addMessage(new Message("system", nickname + " has joined.", new java.util.Date().getTime()));
				chatter.setEnteredInRoomAt(new java.util.Date().getTime());

			}

			if (session.getAttribute("nickname") == null)
			{
				session.setAttribute("nickname", nickname);
			}
			response.sendRedirect("chat.jsp");
		}
		else
		{
			System.out.println("start.jsp: 7");
			out.write("<span class=\"error\">Some error occured");
		}
	}	
%>