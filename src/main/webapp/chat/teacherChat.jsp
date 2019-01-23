<%@ page session="true" import="chat.*" errorPage="error.jsp" import="java.text.DateFormat,chat.*,stimulatedplanning.*, stimulatedplanning.util.*, senseofcommunity.*, java.util.*" %>
<%

  session = StimulatedPlanningFactory.initializeSession(request, response);
  User user = (User)session.getAttribute("user");
  
  if ((!"Sandra_Xai".equals(user.getId()) && !"rklemke".equals(user.getId())) || !"xCtA3f".equals(request.getParameter("pw"))) {
	  throw new Exception("This is strictly illegal. Don't do it.");
  }

  HashArrayList<UserOnlineStatus> clan1OnlineUsers = new HashArrayList<>();
  HashArrayList<UserOnlineStatus> clan1RecentUsers = new HashArrayList<>();
  HashArrayList<UserOnlineStatus> clan1OfflineUsers = new HashArrayList<>();
  HashArrayList<UserOnlineStatus> clan2OnlineUsers = new HashArrayList<>();
  HashArrayList<UserOnlineStatus> clan2RecentUsers = new HashArrayList<>();
  HashArrayList<UserOnlineStatus> clan2OfflineUsers = new HashArrayList<>();
  HashArrayList<UserOnlineStatus> controlOnlineUsers = new HashArrayList<>();
  HashArrayList<UserOnlineStatus> controlRecentUsers = new HashArrayList<>();
  HashArrayList<UserOnlineStatus> controlOfflineUsers = new HashArrayList<>();
  
  HashArrayList<Clan> clans = StimulatedPlanningFactory.getClans("");
  Clan clan1 = clans.get(0);
  Clan clan2 = clans.get(1);
  
	System.out.println("teacherChat 3 "+clan2.getId());
  clan1OnlineUsers = clan1.getOnlineUsers();
  clan1RecentUsers = clan1.getRecentUsers();
  clan1OfflineUsers = clan1.getOfflineUsers();
  clan2OnlineUsers = clan2.getOnlineUsers();
  clan2RecentUsers = clan2.getRecentUsers();
  clan2OfflineUsers = clan2.getOfflineUsers();
  controlOnlineUsers = StimulatedPlanningFactory.getOnlineControlUsers();
  controlRecentUsers = StimulatedPlanningFactory.getRecentControlUsers();
  controlOfflineUsers = StimulatedPlanningFactory.getOfflineControlUsers();

	//String nickname = (String)session.getAttribute("nickname");
	String roomListName = null;
	String roomName = null;
	String nickname = user.getName();
	String userId = user.getId();
	HashMap<String, ChatRoomList> roomListMap = null;
	ChatRoomList roomList = null;
	ChatRoom room = null;
	User chatter = null;
	ArrayList<Message> messages = PersistentStore.readMessagesForTeacherChat();
	
	System.out.println("teacherChat 4 "+nickname);
	if (nickname != null && nickname.length() > 0)
	{
		try
		{
			roomListMap = StimulatedPlanningFactory.getChatRoomLists();
			roomListName = request.getParameter("roomList");
			if (roomListName != null) {
				roomList = roomListMap.get(roomListName);
				roomName = request.getParameter("room");
				if (roomName != null) {
					room = roomList.getRoom(roomName);
					chatter = room.getChatter(userId);
					if ( room != null) {
						String msg = request.getParameter("messageinput");
						if ( msg != null && msg.length() > 0) {
							msg = msg.trim();
							Message message = StimulatedPlanningFactory.createMessage(user, msg, new java.util.Date().getTime(), room, roomList);
							room.addMessage(message);
							messages.add(message);
							StimulatedPlanningFactory.trackAndLogEvent(request, response, "teacherchat.message");
						}
						long enteredAt = 0; //chatter.getEnteredInRoomAt();
					} else {
						out.write("<b>Room " + roomName + " not found</b>");
						out.close();
					}
				}
			}
		}
		catch(Exception e)
		{
			System.out.println("Exception: "+ e.getMessage());
			throw new ServletException("Unable to get handle to ServletContext");
		}		
		System.out.println("teacherChat 5 init done: "+messages.size());

%>
<!DOCTYPE html>
<HTML>
<HEAD>
<meta charset="UTF-8">
<TITLE>Cryptography Chat - Teacher Access </TITLE>
<link rel="stylesheet" href="/css/ClanMembersStyling.css">
<link rel="stylesheet" type="text/css" href="/css/chat/chat.css">
<link rel="stylesheet" type="text/css" href="/jquery/jquery-ui.css">
<link rel="stylesheet" type="text/css" href="/tablesorter/themes/blue/style.css">

<script  type="text/javascript" src="/jquery/external/jquery/jquery.js"></script>
<script  type="text/javascript" src="/jquery/jquery-ui.js"></script>
<script  type="text/javascript" src="/tablesorter/jquery.tablesorter.js"></script>
<script type="text/javascript">
var userid = "<%= user.getId() %>";
var userName = "<%= user.getName() %>";

$(document).ready(function () {
	$(".emoji").on("click", function() {
		var emoText = $("#messageinput").val() +" "+ $(this).attr("alt") +" ";
		$("#messageinput").val(emoText);
	});
	
	$(document).tooltip({
		content: function() {
			return $(this).prop('title');
		}
	});
	
	$( "#rules" ).on( "click", function() {
		$( "#dialog" ).dialog( "open" );
	});
    $( "#dialog" ).dialog({
		autoOpen: false,
		draggable: false,
		height:300,
		width:600,
		modal: true,
		resizable: false
    });

    $("#messageTable").tablesorter({ sortList: [[5,1]] });
	
	$( "#roomSelection input" ).checkboxradio({
	    icon: false
	});

    $( "#chatSelect" ).selectmenu({
        change: function( event, data ) {
          var val = data.item.value;
          var valRoomList = val.substr(0, val.indexOf("|"));
          var valRoom = val.substr(val.indexOf("|")+1);
          $("#roomList").val(valRoomList);
          $("#room").val(valRoom);
        }
       });

});
	    
</script>
</HEAD>
<BODY>

<div  id="RowOneColumnOneBig">
<div id="headerHolder">Teach Chat</div><!-- headerholder -->

<div id="userName">
<span>Welcome, <i><%=user.getId()%> </i></span>
</div><!-- userName -->
<% 
System.out.println("teacherChat 5b table");
%>
<div id="messageDisplayBox">

<table id="messageTable" class="tablesorter">
  <thead>
    <tr>
      <th>User Group</th>
      <th>User</th>
      <th>Chat Room</th>
      <th>Message</th>
      <th>Date</th>
      <th>Timestamp</th>
    </tr>
  </thead>
  <tbody>
  <%
  	String msgRoomList;
    String msgUserId;
    String msgRoom;
  	for (Message message: messages) { 
  		if (message != null) {
  			msgRoomList = (message.getRoomList() != null?message.getRoomList().getId():"");
  			msgUserId = (message.getUser() != null?message.getUser().getId():"System");
  			msgRoom = (message.getRoom() != null?message.getRoom().getId():"");
  %>
    <tr>
      <td><%= msgRoomList %></td>
      <td><%= msgUserId %></td>
      <td><%= message.getRoom().getId() %></td>
      <td><%= message.getDisplayMessage() %></td>
      <td><%= new Date(message.getTimeStamp()).toString() %></td>
      <td><%= message.getTimeStamp() %></td>
    </tr>
  <% 	}
  	} %>
  </tbody>
</table>

</div><!-- messageDisplayBox -->
</div><!-- RowOnecolumn One -->

<div id="RowTwoColumnOneBig">
<form method="POST" action="teacherChat.jsp">
		<input type="hidden" id="userid" name="userid" value="<%= user.getId() %>"></input>
		<input type="hidden" id="userName" name="userName" value="<%= user.getName() %>"></input>
		<input type="hidden" id="pw" name="pw" value="<%= request.getParameter("pw") %>"></input>
User Group and Chat Room:   
<select name="chatSelect" id="chatSelect">
  <option value="unknown|unknown">Default</option>
<% for (ChatRoomList chatRoomList: roomListMap.values()) { %>
  <% 	for (ChatRoom chatRoom: chatRoomList.getRoomListArray()) {
			if (!"StartUp".equalsIgnoreCase(chatRoom.getId())) {
		  		String value = chatRoomList.getId()+"|"+chatRoom.getId();
		  		String selected = "";
		  		if (value.equals(roomListName+"|"+roomName)) {
		  			selected = "selected";
		  		}
  %>
    <option value="<%= value %>" <%= selected %>><%= chatRoomList.getId() %>: <%= chatRoom.getId() %></option>
  <%		}
	  	}
  	} %>
</select>

<INPUT type="hidden" name="roomList" id="roomList" value="<%= roomListName %>" />
<INPUT type="hidden" name="room" id="room" value="<%= roomName %>"  />
Message: <INPUT type="text" name="messageinput" id="messageinput" maxlength="300"  />
<INPUT type="submit" value="Send" />
</form>
</div><!-- RowTwoColumnOneBig -->

<div id="RowTwoColumnOneBig">
<img class= "emoji" title ="smile" alt= "(smile)" src="${pageContext.request.contextPath}/img/chat/emojis/smile.png"/> 
<img class= "emoji" title ="wink" alt ="(wink)" src="${pageContext.request.contextPath}/img/chat/emojis/wink.png"/> 
<img class= "emoji" title ="grin" alt ="(grinning)" src="${pageContext.request.contextPath}/img/chat/emojis/grinning.png"/> 
<img class= "emoji" title ="secret" alt ="(secret)" src="${pageContext.request.contextPath}/img/chat/emojis/secret.png"/> 
<img class= "emoji" title ="scared" alt ="(scared)" src="${pageContext.request.contextPath}/img/chat/emojis/scared.png"/> 
<img class= "emoji" title ="sad" alt ="(sad)" src="${pageContext.request.contextPath}/img/chat/emojis/sad.png"/> 
<img class= "emoji" title ="cool" alt ="(cool)" src="${pageContext.request.contextPath}/img/chat/emojis/cool.png"/> 
<img class= "emoji" title ="dead" alt ="(dead)" src="${pageContext.request.contextPath}/img/chat/emojis/dead.png"/> 
<img class= "emoji" title ="shocked" alt ="(shocked)" src="${pageContext.request.contextPath}/img/chat/emojis/shocked.png"/> 
<img class= "emoji" title ="like" alt ="(like)" src="${pageContext.request.contextPath}/img/chat/emojis/like.png"/> 
<img class= "emoji" title ="dislike" alt ="(dislike)" src="${pageContext.request.contextPath}/img/chat/emojis/dislike.png"/> 
<img class= "emoji" title ="devil" alt ="(devil)" src="${pageContext.request.contextPath}/img/chat/emojis/devil.png"/> 
<img class= "emoji" title ="curious" alt ="(curious)" src="${pageContext.request.contextPath}/img/chat/emojis/curious.png"/> 
<img class= "emoji" title ="crying" alt ="(crying)" src="${pageContext.request.contextPath}/img/chat/emojis/crying.png"/> 
<img class= "emoji" title ="nerd" alt ="(nerd)" src="${pageContext.request.contextPath}/img/chat/emojis/nerd.png"/> 
<img class= "emoji" title ="confused" alt ="(confused)" src="${pageContext.request.contextPath}/img/chat/emojis/confused.png"/> 
<img class= "emoji" title ="skull" alt ="(skull)" src="${pageContext.request.contextPath}/img/chat/emojis/skull.png"/> 
<img class= "emoji" title ="tired" alt ="(tired)" src="${pageContext.request.contextPath}/img/chat/emojis/tired.png"/> 
<img class= "emoji" title ="sleeping" alt ="(sleeping)" src="${pageContext.request.contextPath}/img/chat/emojis/sleeping.png"/> 
<img class= "emoji" title ="ghost" alt ="(ghost)" src="${pageContext.request.contextPath}/img/chat/emojis/ghost.png"/> 
<img class= "emoji" title ="ninja" alt ="(ninja)" src="${pageContext.request.contextPath}/img/chat/emojis/ninja.png"/> 
<img class= "emoji" title ="laughing" alt ="(laughing)" src="${pageContext.request.contextPath}/img/chat/emojis/laughing.png"/> 
<img class= "emoji" title ="muted" alt ="(muted)" src="${pageContext.request.contextPath}/img/chat/emojis/muted.png"/>
<img class= "emoji" title ="tongue" alt ="(tongue)" src="${pageContext.request.contextPath}/img/chat/emojis/tongue.png"/> 
<img class= "emoji" title ="in-love" alt ="(in-love)" src="${pageContext.request.contextPath}/img/chat/emojis/in-love.png"/> 
<img class= "emoji" title ="anguish" alt ="(anguish)" src="${pageContext.request.contextPath}/img/chat/emojis/anguish.png"/>    
</div><!-- row two column one-->

<div style="display: block;width: 100%; float:left;"><strong>Please follow the Chat <a id="rules">rules</a>.</strong></div>
<div id="dialog" title="General Chat Rules">
  <ol>
  <li>Do not use the chat (or other tools provided in the course) to harm or hurt others fellow students</li>
  <li>Respect others opinion</li>
  <li>Contributions within the chat must be civil and tasteful</li>
  <li>No disruptive, offensive or abusive behaviour: contributions must be constructive and polite, not mean-spirited or contributed with the intention of causing trouble</li>
  <li>No spamming or off-topic material can be shared</li>
  <li>On a more Safety level: We advise that you never reveal any personal information about yourself or anyone else (for example: telephone number, home address or email address)</li>
  
  </ol>
</div><!-- dialog-->


<div id="RowTwoColumnOneBigger">

<div id="chattersBig">
Clan 1
<%
		String currentUserId;
		for(UserOnlineStatus listUserStatus: clan1OnlineUsers) { 
			currentUserId = "user"+listUserStatus.getUser().getId();
			session.setAttribute(currentUserId, listUserStatus.getUser());
			%><jsp:include page="/UserIconDisplay.jsp" >
			<jsp:param name="userId" value="<%= currentUserId %>" />
			</jsp:include><% } %>
<%
		for(UserOnlineStatus listUserStatus: clan1RecentUsers) { 
			currentUserId = "user"+listUserStatus.getUser().getId();
			session.setAttribute(currentUserId, listUserStatus.getUser());
			%><jsp:include page="/UserIconDisplay.jsp" >
			<jsp:param name="userId" value="<%= currentUserId %>" />
			</jsp:include><% } %>
<% 		
		for(UserOnlineStatus listUserStatus: clan1OfflineUsers) { 
			currentUserId = "user"+listUserStatus.getUser().getId();
			session.setAttribute(currentUserId, listUserStatus.getUser());
			%><jsp:include page="/UserIconDisplay.jsp" >
			<jsp:param name="userId" value="<%= currentUserId %>" />
			</jsp:include><% } %>
<BR>Clan 2
<%
		for(UserOnlineStatus listUserStatus: clan2OnlineUsers) { 
			currentUserId = "user"+listUserStatus.getUser().getId();
			session.setAttribute(currentUserId, listUserStatus.getUser());
			%><jsp:include page="/UserIconDisplay.jsp" >
			<jsp:param name="userId" value="<%= currentUserId %>" />
			</jsp:include><% } %>
<%
 		for(UserOnlineStatus listUserStatus: clan2RecentUsers) { 
			currentUserId = "user"+listUserStatus.getUser().getId();
			session.setAttribute(currentUserId, listUserStatus.getUser());
			%><jsp:include page="/UserIconDisplay.jsp" >
			<jsp:param name="userId" value="<%= currentUserId %>" />
			</jsp:include><% } %>
<%
 		for(UserOnlineStatus listUserStatus: clan2OfflineUsers) { 
			currentUserId = "user"+listUserStatus.getUser().getId();
			session.setAttribute(currentUserId, listUserStatus.getUser());
			%><jsp:include page="/UserIconDisplay.jsp" >
			<jsp:param name="userId" value="<%= currentUserId %>" />
			</jsp:include><% } %>
<BR>Control
<%
 		for(UserOnlineStatus listUserStatus: controlOnlineUsers) { 
			currentUserId = "user"+listUserStatus.getUser().getId();
			session.setAttribute(currentUserId, listUserStatus.getUser());
			%><jsp:include page="/UserIconDisplay.jsp" >
			<jsp:param name="userId" value="<%= currentUserId %>" />
			</jsp:include><% } %>
<%
 		for(UserOnlineStatus listUserStatus: controlRecentUsers) { 
			currentUserId = "user"+listUserStatus.getUser().getId();
			session.setAttribute(currentUserId, listUserStatus.getUser());
			%><jsp:include page="/UserIconDisplay.jsp" >
			<jsp:param name="userId" value="<%= currentUserId %>" />
			</jsp:include><% } %>
<%
 		for(UserOnlineStatus listUserStatus: controlOfflineUsers) { 
			currentUserId = "user"+listUserStatus.getUser().getId();
			session.setAttribute(currentUserId, listUserStatus.getUser());
			%><jsp:include page="/UserIconDisplay.jsp" >
			<jsp:param name="userId" value="<%= currentUserId %>" />
			</jsp:include><% } %>
</div><!-- chatters -->

</div><!-- RowTwoColumnTwo -->




</BODY>
</HTML>
<% 
} else {
	response.sendRedirect("login.jsp");
	out.write("<h2 class=\"error\">Your room couldn't be found. You can't send message</h2>");
	response.sendRedirect("login.jsp");
}
%>
