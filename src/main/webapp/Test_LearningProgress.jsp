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
 
      $( '#contentSelect' ).selectmenu({
    	        change: function( event, data ) {
    	        	pageurl.val(data.item.value);
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
<input type=hidden id="pagetitle" name="pagetitle" value="">
<select name="contentSelect" id="contentSelect">
	<option value="not set">Select the content to learn</option>
<% 
	for (ListIterator<GoalDescriptor> goalIter = course.getGoals(); goalIter.hasNext(); ) {
		GoalDescriptor goal = goalIter.next();
		for (ListIterator<LessonDescriptor> lessonIter = goal.getLessons(); lessonIter.hasNext(); ) {
			LessonDescriptor lesson = lessonIter.next();
			for (ListIterator<ContentDescriptor> contentIter = lesson.getContents(); contentIter.hasNext(); ) {
				ContentDescriptor content = contentIter.next();
%>
	<option value="<%= content.getUrl() %>"><%= lesson.getTitle()+": "+content.getTitle() %></option>
<%				
			}
			
		}
	}
%>
</select>


</form>
<p id="learningContent">Here goes the learning content!</p>

<!-- <iframe width="800" height="550" id="trackerFrame" src="./mooc-integration/appengine_additional_html.html"></iframe> -->
<iframe width="800" height="600" id="trackerFrame" src="./mooc-integration/openedx_tracker.html"></iframe>
</body>
</html>