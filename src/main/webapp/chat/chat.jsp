<%@ page session="true" import="chat.*" errorPage="error.jsp" import="java.text.DateFormat,chat.*,stimulatedplanning.*, stimulatedplanning.util.*, senseofcommunity.*, java.util.*" %>
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
  
	//String nickname = (String)session.getAttribute("nickname");
	String roomName = null;
	String nickname = user.getName();
	String userId = user.getId();
	ChatRoomList roomList = null;
	ChatRoom room = null;
	User chatter = null;
	Message[] messages = null;
	
	if (nickname != null && nickname.length() > 0)
	{
		try
		{
			roomList = StimulatedPlanningFactory.getChatRoomListForUser(user);
			room = roomList.getRoomOfChatter(userId);
			roomName = room.getName();
			if (roomName != null && roomName != "")
			{
				room = roomList.getRoom(roomName);
				chatter = room.getChatter(userId);
				if ( room != null)
				{
					String msg = request.getParameter("messagebox");
					
					if ( msg != null && msg.length() > 0)
					{
						msg = msg.trim();
						room.addMessage(StimulatedPlanningFactory.createMessage(user, msg, new java.util.Date().getTime(), room, roomList));
						StimulatedPlanningFactory.trackAndLogEvent(request, response, "chat.message");
					}
					
					long enteredAt = 0; //chatter.getEnteredInRoomAt();
					if (enteredAt != -1)
					{
						messages = room.getMessages(enteredAt);					
						}
					else
					{
						//messages = chatRoom.getMessages(chatter.getLoginTime());
						}
				}
				else
				{
					out.write("<b>Room " + roomName + " not found</b>");
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
<!DOCTYPE html>
<HTML>
<HEAD>
<meta charset="UTF-8">
<TITLE>Cryptography Chat - <%=nickname%> (<%=roomName%>) </TITLE>
<link rel="stylesheet" href="/css/ClanMembersStyling.css">
<link rel="stylesheet" type="text/css" href="/css/chat/chat.css">
<link rel="stylesheet" type="text/css" href="/jquery/jquery-ui.css">
<script  type="text/javascript" src="/jquery/external/jquery/jquery.js"></script>
<script  type="text/javascript" src="/jquery/jquery-ui.js"></script>
<script type="text/javascript">
var userid = "<%= user.getId() %>";
var userName = "<%= user.getName() %>";


$(document).ready(function () {
	$(".emoji").on("click", function() {
		var emoText = $("#messagebox").val() +" "+ $(this).attr("alt") +" ";
		$("#messagebox").val(emoText);
		});
	
	$(document).tooltip({
		content: function() {
			return $(this).prop('title');
		}
	});
	
	$( "#roomSelection input" ).checkboxradio({
	    icon: false
	});
    $( "[name='room']").on( "change", chat_changeRoom );
	$( "#sendMessageBtn" ).click(function() {
		chat_sendMessage();
	});
	$( "#rules" ).on( "click", function() {
		$( "#dialog" ).dialog( "open" );
	});
	
	$('#messagebox').on('keypress', function (e) {
        if(e.which === 13){

           //Disable textbox to prevent multiple submit
           $(this).attr("disabled", "disabled");

           chat_sendMessage();

           //Enable the textbox again if needed.
           $(this).removeAttr("disabled");
        }
    });	
	setInterval(chat_tickerRequest, chat_timeoutInterval);
})
	$( function() {
	    $( "#dialog" ).dialog({
			autoOpen: false,
			draggable: false,
			height:300,
			width:600,
			modal: true,
			resizable: false
	    })
	    });
	    


		var chat_timeoutInterval = 5000;
		
		function chat_tickerRequest() {
			$.ajax({
				url: '<%= StimulatedPlanningFactory.applicationHome %>/chat/displayMessages.jsp?userid='+userid+'&userName='+userName,
				success: function(result) {
					$( '#messageDisplayBox' ).html(result);
				},
				complete: function() {
					// Schedule the next request when the current one's complete
					//setTimeout(messages_tickerRequest, messages_timeoutInterval);
				}
			});
			$.ajax({
				url: '<%= StimulatedPlanningFactory.applicationHome %>/chat/listrooms.jsp?userid='+userid+'&userName='+userName,
				success: function(result) {
					$( '#roomSelection' ).html(result);
					$( "#roomSelection input" ).checkboxradio({
					    icon: false
					});
				    $( "[name='room']").on( "change", chat_changeRoom );
				},
				complete: function() {
					// Schedule the next request when the current one's complete
					//setTimeout(messages_tickerRequest, messages_timeoutInterval);
				}
			});
			$.ajax({
				url: '<%= StimulatedPlanningFactory.applicationHome %>/chat/listChatters.jsp?userid='+userid+'&userName='+userName,
				success: function(result) {
					$( '#chatters' ).html(result);
				},
				complete: function() {
					// Schedule the next request when the current one's complete
					//setTimeout(messages_tickerRequest, messages_timeoutInterval);
				}
			});
		}

		function chat_sendMessage() {
			msgText = $( '#messagebox' ).val();
			$.ajax({
				url: '<%= StimulatedPlanningFactory.applicationHome %>/chat/displayMessages.jsp',
			    method: 'POST',
			    data: {
			    	messagebox: msgText,
			    	userid: userid,
			    	userName: userName
			    }, 
				success: function(result) {
					$( '#messageDisplayBox' ).html(result);
					$( '#messageDisplayBox').animate({ scrollTop: $('#messageDisplayBox').prop("scrollHeight")}, 1000);
					$( '#messagebox' ).val('');
					$( '#messagebox' ).focus('');
				},
				complete: function() {
					// Schedule the next request when the current one's complete
					//setTimeout(messages_tickerRequest, messages_timeoutInterval);
				}
			});
		}

	    function chat_changeRoom( e ) {
			$.ajax({
				url: '<%= StimulatedPlanningFactory.applicationHome %>/chat/listrooms.jsp',
			    method: 'POST',
			    data: {
			    	room: $( e.target ).val(),
			    	userid: userid,
			    	userName: userName
			    }, 
				success: function(result) {
					$( '#roomSelection' ).html(result);
					$( "#roomSelection input" ).checkboxradio({
					    icon: false
					});
				    $( "[name='room']").on( "change", chat_changeRoom );
				},
				complete: function() {
					// Schedule the next request when the current one's complete
					//setTimeout(messages_tickerRequest, messages_timeoutInterval);
				}
			});
		}

</script>
</HEAD>

<BODY>

<div class = "container">

<div  id="RowOneColumnOne">
<div id="headerHolder">
<%@ include file="header.jsp" %>
</div><!-- headerholder -->

<div id="userName">
<% if (user.isTreatmentGroup()) { %>
<img id  = "ClanLogo" style="vertical-align: middle;width: 2em;height: 2em;" src="<%=user.getClan().getClanLogo()%>"/> 
<% } %>	
<span>Welcome, <i><%=(String)session.getAttribute("nickname")%> </i></span>
</div><!-- userName -->

<div id="messageDisplayBox">
<jsp:include page="displayMessages.jsp" />
</div><!-- messageDisplayBox -->
</div><!-- RowOnecolumn One -->

<div id="RowOneColumnTwo">

<div id="roomSelection">
<jsp:include page="listrooms.jsp" />
</div><!-- roomSelection -->

<div id="chatters">
<jsp:include page="listChatters.jsp" />
</div><!-- chatters -->

</div><!-- RowTwoColumnTwo -->


<div id="RowTwoColumnOne">
		<input type="hidden" id="userid" name="userid" value="<%= user.getId() %>"></input>
		<input type="hidden" id="userName" name="userName" value="<%= user.getName() %>"></input>
<INPUT type="text" name="messagebox" id="messagebox" maxlength="300"  />
<INPUT type="hidden" name="nickname" value="<%=session.getAttribute("nickname")%>"/>
<INPUT name="sendMessageBtn" id="sendMessageBtn" type="button" value="Send" />
</div><!-- sendText -->

<div id="RowTwoColumnTwo">
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
  <li>No spaming or off-topic material can be shared</li>
  <li>On a more Safety level: We advise that you never reveal any personal information about yourself or anyone else (for example: telephone number, home address or email address)</li>
  
  </ol>
</div><!-- dialog-->
</div><!-- container -->

</BODY>
</HTML>
<%
}
else
{
	System.out.println("displayMessages: 6");
	response.sendRedirect("login.jsp");
	out.write("<h2 class=\"error\">Your room couldn't be found. You can't send message</h2>");
	response.sendRedirect("login.jsp");
}
%>
