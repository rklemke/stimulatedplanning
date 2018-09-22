<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="stimulatedplanning.*, stimulatedplanning.util.*, java.util.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <%
  session = StimulatedPlanningFactory.initializeSession(request, response);

  User user = (User)session.getAttribute("user");
  CourseDescriptor course = (CourseDescriptor)session.getAttribute("course");
  UserPlan userPlan = (UserPlan)session.getAttribute("userPlan");

  
  %>
  <link rel="stylesheet" href="https://code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
  <link rel="stylesheet" href="https://jqueryui.com/resources/demos/style.css">
  <script src="https://code.jquery.com/jquery-1.12.4.js"></script>
  <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<div class="label-username"><%= user.getName() %></div>
<iframe width="1100" height="600" id="stimulatedPlanningFrame"></iframe>

<script type="text/javascript">
	var timeoutInterval = 30000;
	var userNameDefault = "Guest";
	var userIdDefault = "unknown";
	var pageUrlDefault = window.location.href;

	function getUserName() {
	  var tmpName = $('.label-username').html();
	  if (tmpName == null) {
	    tmpName = userNameDefault;
	  }
	  return tmpName;
	}

	function getUserId() {
	  return getUserName();
	}

	function getPageUrl() {
		var tmpId = $('#pageurl', window.parent.document).val();
		if (tmpId == null) {
		  tmpId = pageUrlDefault;
		}
		return tmpId;
	}
	
	function tickerRequest() {
		  var userName = getUserName();
		  var userId = getUserId(); 
		  var pageUrl = getPageUrl(); 
		  $.ajax({
		    dataType: 'jsonp',
		    url: '/DataTrackerServlet',
		    method: 'POST',
		    data: {
			userName: userName,
			userid: userId,
			page: pageUrl
		    }, 
		    success: function(data) {
		    	if ($('#stimulatedPlanningFrame').length) {
		    		$("#stimulatedPlanningFrame").attr("src", "/GoalSettingServlet");
					$( '#ajaxresult' ).html('Connected. '+userName);
		    	} else if ($('#feedbackFrame').length) {
		    		$("#feedbackFrame").attr("src", "/FeedbackFrame.jsp");
					$( '#ajaxresult' ).html('Connected. '+userName);
		    	} else {
					var datatxt = data.feedbackFrame;
					  $( '#ajaxresult' ).html(datatxt);
		    	}
		    },
		    complete: function() {
		      // Schedule the next request when the current one's complete
		      
		    	if (!$('#stimulatedPlanningFrame').length && !$('#feedbackFrame').length) {
				      timeoutInterval += timeoutInterval; // get slower, when user just stays on page.
				      setTimeout(tickerRequest, timeoutInterval);
		    	}
		    }
		  });
		}

		$('#trackerFrame').ready(function() {
		  // run the first time; all subsequent calls will take care of themselves
		  setTimeout(tickerRequest, 20);
		  
//		  if ( $( "#stimulatedPlanningFrame" ).length ) {
//			    $( "#stimulatedPlanningFrame" ).attr("src", "/GoalSettingServlet");
//			}	  
		});

</script>


<div id="ajaxresult">Loading content.</div>


<a href="/Test_UserSelection.jsp">Home</a>

</body>
</html>