<%@ page isErrorPage="false" errorPage="error.jsp" import="java.text.DateFormat,chat.*, stimulatedplanning.*, stimulatedplanning.util.*, senseofcommunity.*, java.util.*" %>
<%
	System.out.println("displayMessages: 0.1");

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
  
	String roonName = null;
	String nickname = user.getName();
	String userId = user.getId();
	//String nickname = (String)session.getAttribute("nickname");
	ChatRoomList roomList = null;
	ChatRoom chatRoom = null;
	User chatter = null;
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
				chatter = chatRoom.getChatter(userId);
				if (chatRoom != null)
				{
					long enteredAt = 0; //chatter.getEnteredInRoomAt();
					if (enteredAt != -1)
					{
						messages = chatRoom.getMessages(enteredAt);					
					}
					else
					{
						//messages = chatRoom.getMessages(chatter.getLoginTime());
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
	
%>
<HTML>

<HEAD>
<!--<meta http-equiv="refresh" content="10">-->
<link rel="stylesheet" type="text/css" href="/css/chat/chat.css">
<link rel="stylesheet" type="text/css" href="/jquery/jquery-ui.css">
<script  type="text/javascript" src="/jquery/external/jquery/jquery.js"></script>
<script  type="text/javascript" src="/jquery/jquery-ui.js"></script>

<script type="text/javascript">
$(document).ready(function () {
	//alert("WTF");
	 $( "input" ).checkboxradio({
	      icon: false
	    });
	})
<%
	int refreshAfter = 5000; // 5 seconds
	String t = application.getInitParameter("refreshAfter"); // gets seconds
	if (t != null)
	{
		try
		{
			refreshAfter = Integer.parseInt(t);
			refreshAfter = refreshAfter * 1000; // convert to mili seconds
		}
		catch (NumberFormatException nfe)
		{							
		}
	}
%>

function reload()
{
	window.location.reload();
}


setInterval('reload()', <%=refreshAfter%>);

function winopen(path)
{
	chatterinfo = window.open(path,"chatterwin","scrollbars=no,resizable=yes, width=400, height=300, location=no, toolbar=no, status=no");
	chatterinfo.focus();
}

</script>

</HEAD>
<BODY onLoad="window.location.hash='#current'" bgcolor="#FFFFFF">

<div class = "container">

<div class="column" id="columnOne">

<div id="headerHolder">
<%@ include file="header.jsp" %>
</div><!-- headerholder -->

<div id="userName">
<img id  = "ClanLogo" src="${pageContext.request.contextPath}/img/clan/DefaultClan.png"/> 
<span>Welcome, <i><%=(String)session.getAttribute("nickname")%> </i></span>
</div><!-- userName -->

<div id="messageDisplayBox">
<%

	System.out.println("displayMessages: 1");
	
	if(messages != null && messages.length > 0)
	{
		System.out.println("displayMessages: 2");
		for (int i = 0; i < messages.length; i++)
		{
			Message message = (Message)messages[i];
			String chatterName = message.getChatterName();
			String strmsg = message.getMessage();
			long time = message.getTimeStamp();
			Date date = new Date(time);

			if (chatterName.equalsIgnoreCase((String)session.getAttribute("nickname")))
			{
				out.write("<font face=\"Arial\" size=\"2\" color=\"blue\"><b>" + chatterName + " ("+ DateFormat.getTimeInstance().format(date)+ ")&gt;</b></font> " + strmsg+"<br>\n");
			}
			else if (chatterName.equalsIgnoreCase("system"))
			{
				out.write("<span class=\"error\">" + strmsg+"</span><br>\n");
			}
			else
			{
				out.write("<font face=\"Arial\" size=\"2\"><b>"+chatterName + " ("+ DateFormat.getTimeInstance().format(date)+ ")&gt;</b></font> " + strmsg + "<br>\n");
			}			
		}
		out.write("<a name=\"current\"></a>");
	}
	else
	{
		System.out.println("displayMessages: 2");
		out.write("<font color=\"red\" face=\"Arial\" size=\"2\">There are currently no messages in this room</font>");
	}
	System.out.println("displayMessages: 3");
	out.write("<a name=\"current\"></a>");
	%>
</div><!-- messageDisplayBox -->
</div><!-- columnOne -->

<div class="column" id="columnTwo">
<div id="roomSelection">
 
 <fieldset>
    <legend>Select a Room: [Population]</legend>
    <label for="room-1">Need a challenge <span>[<%=chatRoom.getNoOfChatters()%>]</span> </label>
    <input type="radio" name="room" id="room-1">
    <label for="room-2">Just chat <span>[<%=chatRoom.getNoOfChatters()%>]</span></label>
    <input type="radio" name="room" id="room-2">
    <label for="room-3">Need a teacher <span>[<%=chatRoom.getNoOfChatters()%>]</span> </label>
    <input type="radio" name="room" id="room-3">
    <label for="room-4">Need help <span>[<%=chatRoom.getNoOfChatters()%>]</span> </label>
    <input type="radio" name="room" id="room-4">
 </fieldset>

</div><!-- roomSelection -->

<div id="chatters">
<fieldset>
    <legend>In the room</legend>
<%
	User[] chatters = chatRoom.getChattersArray();
	String currentUserId = "";
	for(int i = 0; i < chatters.length; i++)
	{
		System.out.println("displayMessages: 5 "+chatters[i].getName());
  		currentUserId = "user"+chatters[i].getId();
  		session.setAttribute(currentUserId, chatters[i]);
  		%><jsp:include page="/UserIconDisplay.jsp" >
			<jsp:param name="userId" value="<%= currentUserId %>" />
		 </jsp:include>
   	<% }

}
else
{
	System.out.println("displayMessages: 6");
	response.sendRedirect("login.jsp");
}
%>
</fieldset>
</div><!-- chatters -->
</div><!-- ColumnTwo -->

</div><!-- container -->

</BODY>
</HTML>