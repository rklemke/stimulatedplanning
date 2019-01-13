<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="stimulatedplanning.*, stimulatedplanning.util.*, senseofcommunity.*, java.util.*" %>
<%
  session = StimulatedPlanningFactory.initializeSession(request, response);

  User user = (User)session.getAttribute("user");
  UserOnlineStatus userStatus = user.getOnlineStatus();
  CourseDescriptor course = (CourseDescriptor)session.getAttribute("course");
  UserPlan userPlan = (UserPlan)session.getAttribute("userPlan");
  Clan userClan = null;
  Clan otherClan = null;
  int otherClanSize = 1;
  int otherClanOnline = 0;
  int otherClanRecent = 0;
  int otherClanOffline = 0;
  
  HashArrayList<UserOnlineStatus> onlineUsers = new HashArrayList<>();
  HashArrayList<UserOnlineStatus> recentUsers = new HashArrayList<>();
  HashArrayList<UserOnlineStatus> offlineUsers = new HashArrayList<>();
  if (user.isTreatmentGroup()) {
	  userClan = user.getClan();
	  otherClan = StimulatedPlanningFactory.getOtherClan(userClan);
	  onlineUsers = userClan.getOnlineUsersSorted(userStatus);
	  recentUsers = userClan.getRecentUsersSorted(userStatus);
	  offlineUsers = userClan.getOfflineUsersSorted(userStatus);
	  otherClanSize = otherClan.userCount()+1;
	  otherClanOnline = otherClan.getOnlineUsers().size()+1;
	  otherClanRecent = otherClan.getRecentUsers().size()+1;
	  otherClanOffline = otherClan.getOfflineUsers().size()+1;
  }
  
  
%>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Test User Navigation</title>
  <link rel="stylesheet" href="https://code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
  <link rel="stylesheet" href="https://jqueryui.com/resources/demos/style.css">
  <script src="https://code.jquery.com/jquery-1.12.4.js"></script>
  <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
    <script type="text/javascript">
    function openChat() {
        if ($( "#chatFrame" ).attr("src") == "") {
            var src = "/chat/servlet/LoginServlet";
  	        $( "#chatFrame" ).attr("src", src);
        }
        if ($( "#chatDialog" ).dialog( "isOpen" )) {
        	$( "#chatDialog" ).dialog( "close" );
        } else {
        	$( "#chatDialog" ).dialog( "open" );
        }
    }
    
    $( function() {
  	    var contentId = $( "#contentId" );
  	    var contentName = $( "#contentName" );
  	    var pageurl = $( "#pageurl" );
   
      $( "#contentSelect" ).selectmenu({
        change: function( event, data ) {
          var val = data.item.value;
          var valId = val.substr(0, val.indexOf("|"));
          var valUrl = val.substr(val.indexOf("|")+1);
          contentId.val(valId);
          pageurl.val(valUrl);
          contentName.val($( "#contentSelect option:selected" ).text());
          $("#contentFrame").attr("src", "GenericClanFrameServlet_SoC?contentId="+valId+"&contentName="+$( "#contentSelect option:selected" ).text()+"&pageurl="+valUrl);
          //$("#contentFrame").attr("src", "GenericClanFrameServlet_SoC?pageurl="+valUrl);
        }
       });

	    $( "#chatDialog" ).dialog({
	  	      autoOpen: false,
	  	      width: 840,
	  	      show: {
	  	        effect: "blind",
	  	        duration: 500
	  	      },
	  	      hide: {
	  	        effect: "blind",
	  	        duration: 500
	  	      }
	  	 });
	  	 
	  	 $( "#chatOpener" ).on( "click", openChat);
    } );
	</script>
</head>
<body>

<H2>Welcome, <%= user.getName() %></H2><span class="label-username-long"><%= user.getName() %></span><span class="label-username"><%= user.getId() %></span>
<a href="Test_UserSelection_SoC.jsp">Change user</a>
<form id="contentSelectForm">
<label for="contentSelect">Select a content:</label>
<select name="contentSelect" id="contentSelect">
  <option value="unknown">Default</option>
  <% for (ModuleDescriptor module: course.getModuleList()) { %>
  <% 	for (LessonDescriptor lesson: module.getLessonList()) { %>
  <% 		for (ContentDescriptor content: lesson.getContentList()) { %>
    <option value="<%= content.getId() %>|<%= content.getUrl() %>"><%= content.getTitle() %></option>
  <%		} %>
  <%	} %>
  <% } %>
</select>
<input type="hidden" value="unknown" id="contentId" name="contentId">
<input type="hidden" value="Guest" id="contentName" name="contentName">
<input type="hidden" value="" id="pageurl" name="pageurl">
</form>

<iframe id="contentFrame" style="width:860px; height:480px;"></iframe>

<button id="chatOpener">Chat</button>
<div id="chatDialog" title="chat tool">
	<iframe 
		id="chatFrame" 
		src="" 
		style="width:50em; height:25em; position: relative;" 
		frameborder="0">
	</iframe>
</div>

<div id="SP_ajaxresult">Loading content.</div>
<script type="text/javascript">
	var SP_timeoutInterval = 10000;
	var SP_userNameDefault = "Guest";
	var SP_userIdDefault = "unknown";
	var SP_pageUrlDefault = window.location.href;
	function SP_getUserName() {
	  var tmpName = $('.label-username-long').html();
	  if (tmpName == null) {
	    tmpName = $('.username').html();;
	  }
	  if (tmpName == null) {
	    tmpName = SP_userNameDefault;
	  }
	  return tmpName;
	}
	function SP_getUserId() {
		  var tmpName = $('.label-username').html();
		  if (tmpName == null) {
		    tmpName = $('.username').html();;
		  }
		  if (tmpName == null) {
		    tmpName = SP_userNameDefault;
		  }
		  return tmpName;
	}
	function SP_getPageUrl() {
		var tmpId = $('#pageurl', window.parent.document).val();
		if (tmpId == null) {
		  tmpId = SP_pageUrlDefault;
		}
		return tmpId;
	}
	
	function SP_tickerRequest() {
		  var userName = SP_getUserName();
		  var userId = SP_getUserId(); 
		  var pageUrl = SP_getPageUrl();
		  if (userName != "Guest") {
			  $.ajax({
				    dataType: 'jsonp',
				    url: '/DataTrackerServlet',
				    method: 'POST',
				    data: {
					userName: userName,
					userid: userId,
					page: pageUrl,
					logType: "trackClan"
				    }, 
				    success: function(SP_data) {
				    	if ($('#SP_stimulatedPlanningFrame').length) {
				    		$("#SP_stimulatedPlanningFrame").attr("src", "https://stimulatedplanning.appspot.com/GoalSettingServlet?userName="+userName+"&userid="+userId);
							$( '#SP_ajaxresult' ).html('Connected. '+userName);
				    	} else if ($('#SP_feedbackFrame').length) {
				    		$("#SP_feedbackFrame").attr("src", "https://stimulatedplanning.appspot.com/FeedbackFrame.jsp");
							$( '#SP_ajaxresult' ).html('Connected. '+userName);
				    	} else if ($('#contentFrame').length) {
							$( '#SP_ajaxresult' ).html('Connected. '+userName+'. ');
				    	} else {
							var SP_datatxt = SP_data.feedbackFrame;
		 //                   alert(SP_datatxt);
							  $( '#SP_ajaxresult' ).html(SP_datatxt);
				    	}
				    },
				    complete: function() {
				      // Schedule the next request when the current one's complete
				      
				    	if (!$('#SP_stimulatedPlanningFrame').length && !$('#SP_feedbackFrame').length) {
						      //SP_timeoutInterval += SP_timeoutInterval; // get slower, when user just stays on page.
						      setTimeout(SP_tickerRequest, SP_timeoutInterval);
				    	}
				    }
			  });
			}
		}
		
		setTimeout(SP_tickerRequest, 2000);
</script>
</body>
</html>