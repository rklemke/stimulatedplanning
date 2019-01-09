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
						room.addMessage(new Message(user, msg, new java.util.Date().getTime()));
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
$(document).ready(function () {
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

		var chat_timeoutInterval = 5000;
		
		function chat_tickerRequest() {
			$.ajax({
				url: 'displayMessages.jsp',
				success: function(result) {
					$( '#messageDisplayBox' ).html(result);
				},
				complete: function() {
					// Schedule the next request when the current one's complete
					//setTimeout(messages_tickerRequest, messages_timeoutInterval);
				}
			});
			$.ajax({
				url: 'listrooms.jsp',
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
				url: 'listChatters.jsp',
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
			msgText = $( '#messagebox' ).val()
			$.ajax({
				url: 'displayMessages.jsp',
			    method: 'POST',
			    data: {
			    	messagebox: msgText
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
	    	alert("change room: "+$( e.target ).val());
			$.ajax({
				url: 'listrooms.jsp',
			    method: 'POST',
			    data: {
			    	room: $( e.target ).val()
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

<div class="row">
<div class="column" id="columnOne">
<div id="headerHolder">
<%@ include file="header.jsp" %>
</div><!-- headerholder -->

<div id="userName">
<img id  = "ClanLogo" src="${pageContext.request.contextPath}/img/clan/DefaultClan.png"/> 
<span>Welcome, <i><%=(String)session.getAttribute("nickname")%> </i></span>
</div><!-- userName -->

<div id="messageDisplayBox">
<jsp:include page="displayMessages.jsp" />
</div><!-- messageDisplayBox -->
</div><!-- column One -->
<div class="column" id="columnTwo">

<div id="roomSelection">
<jsp:include page="listrooms.jsp" />
</div><!-- roomSelection -->

<div id="chatters">
<jsp:include page="listChatters.jsp" />
</div><!-- chatters -->

</div><!-- columnTwo -->
</div><!-- row one -->

<div class="row">

<div class="column" id="sendText">
<INPUT type="text" name="messagebox" id="messagebox" maxlength="300"  />
<INPUT type="hidden" name="nickname" value="<%=session.getAttribute("nickname")%>"/>

</div><!-- sendText -->
<div class="column" id="submitButton">

<INPUT name="sendMessageBtn" id="sendMessageBtn" type="button" value="Send" />

</div><!-- submitbutton -->
</div><!-- row two -->

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
