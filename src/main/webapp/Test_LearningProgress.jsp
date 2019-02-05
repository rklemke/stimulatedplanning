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
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Learning Test</title>
  <link rel="stylesheet" href="https://code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
  <link rel="stylesheet" href="https://jqueryui.com/resources/demos/style.css">
  <style>
    fieldset {
      border: 0;
    }
    label {
      display: block;
      margin: 30px 0 0 0;
    }
    .overflow {
      height: 200px;
    }
  </style>
  <script src="https://code.jquery.com/jquery-1.12.4.js"></script>
  <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
  <script>
//  alert("script!");
  $( function() {
	    var pageurl = $( '#pageurl' );
	    var pagetitle = $( '#pagetitle' );
  	    var contentId = $( "#contentId" );
   
 
      $( '#contentSelect' ).selectmenu({
    	        change: function( event, data ) {
    	            var val = data.item.value;
    	            var valId = val.substr(0, val.indexOf("|"));
    	            var valUrl = val.substr(val.indexOf("|")+1);
    	            contentId.val(valId);
    	            pageurl.val(valUrl);
    	            //contentName.val($( "#contentSelect option:selected" ).text());
    	        	//pageurl.val(data.item.value);
    	        	pagetitle.val($( "#contentSelect option:selected" ).text());
    	        	$( '#trackerFrame' ).attr("src", $('#trackerFrame').attr("src"));
    	        	//alert("change! "+data.item.value);
    	        }
    	      });
  } );
  
  </script>
</head>
<body>
<div class="label-username"><%= user.getName() %></div>
<form>
<input type=hidden id="userName" name="userName" value="<%= user.getName() %>">
<input type=hidden id="userid" name="userid" value="<%= user.getId() %>">
<input type=hidden id="pageurl" name="pageurl" value="">
<input type=hidden id="contentId" name="contentId" value="">
<input type=hidden id="pagetitle" name="pagetitle" value="">
<select name="contentSelect" id="contentSelect">
	<option value="not|set">Select the content to learn</option>
<% 
	for (ListIterator<GoalDescriptor> goalIter = course.getGoals(); goalIter.hasNext(); ) {
		GoalDescriptor goal = goalIter.next();
		for (ListIterator<LessonDescriptor> lessonIter = goal.getLessons(); lessonIter.hasNext(); ) {
			LessonDescriptor lesson = lessonIter.next();
			for (ListIterator<ContentDescriptor> contentIter = lesson.getContents(); contentIter.hasNext(); ) {
				ContentDescriptor content = contentIter.next();
%>
	<option value="<%= content.getId() %>|<%= content.getUrl() %>"><%= lesson.getTitle()+": "+content.getTitle() %></option>
<%				
			}
			
		}
	}
%>
</select>


</form>
<p id="learningContent">Here goes the learning content!</p>

<!-- <iframe width="800" height="550" id="trackerFrame" src="./mooc-integration/appengine_additional_html.html"></iframe> -->
<!-- <iframe width="800" height="600" id="trackerFrame" src="./mooc-integration/openedx_tracker.html"></iframe> -->
<!-- <iframe width="1100" height="600" id="feedbackFrame" src=""></iframe> -->

<script type="text/javascript">
	var timeoutInterval = 5000;
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
	function SP_getContentId() {
		var contentId = $('#contentId').val();
		if (!contentId) {
			contentId = "not_found";
		}
		//alert("contentId: "+contentId);
		return contentId;
	}
	
	
	function tickerRequest() {
		  var userName = getUserName();
		  var userId = getUserId(); 
		  var pageUrl = getPageUrl();
		  var contentId = SP_getContentId(); 
		  $.ajax({
		    dataType: 'jsonp',
		    url: '/DataTrackerServlet',
		    method: 'POST',
		    data: {
			userName: userName,
			userid: userId,
			contentId: contentId,
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
				      //timeoutInterval += timeoutInterval; // get slower, when user just stays on page.
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

</body>
</html>