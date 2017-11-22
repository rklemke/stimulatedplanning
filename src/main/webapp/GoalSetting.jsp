<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="stimulatedplanning.*, java.util.ListIterator" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title>Stimulated Planning - Goal selection and setting</title>
  <link rel="stylesheet" href="https://code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
  <link rel="stylesheet" href="https://jqueryui.com/resources/demos/style.css">
  <script src="https://code.jquery.com/jquery-1.12.4.js"></script>
  <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
  <script>
  $( function() {

    $( "#tabs" ).tabs().addClass( "ui-tabs-vertical ui-helper-clearfix" );
    $( "#tabs li" ).removeClass( "ui-corner-top" ).addClass( "ui-corner-left" );
    $( "#tabs2" ).tabs().addClass( "ui-tabs-vertical ui-helper-clearfix" );
    $( "#tabs2 li" ).removeClass( "ui-corner-top" ).addClass( "ui-corner-left" );
    $( "input" ).checkboxradio({
      icon: false,
      classes: {
      	"ui-checkboxradio-checked": "ui-checkboxradio-checked-orange"
      }
    });

  } );
  
  var goals = {
  	cbModule1: "",
  	cbModule2: "",
  	cbModule3: "",
  	cbModule4: ""
  }
  
    
  </script>
  <style>
  .ui-tabs-vertical { width: 65em; }
  .ui-tabs-vertical .ui-tabs-nav { padding: .2em .1em .2em .2em; float: left; width: 25em; }
  .ui-tabs-vertical .ui-tabs-nav li { clear: left; width: 100%; border-bottom-width: 1px !important; border-right-width: 0 !important; margin: 0 -1px .2em 0; }
  .ui-tabs-vertical .ui-tabs-nav li a { display:block; }
  .ui-tabs-vertical .ui-tabs-nav li.ui-tabs-active { padding-bottom: 0; padding-right: .1em; border-right-width: 1px; }
  .ui-tabs-vertical .ui-tabs-panel { padding: 1em; float: right; width: 36em;}
  
  .ui-checkboxradio-checked-orange .ui-state-active { background: #ff9800; }
  .ui-checkboxradio-checked-orange { background: #ff9800; }
  .ui-state-active { background: #ff9800; }
  .goalselect { float: right; }

	#confirm {
		float: right;
	}

.ui-button:active, .ui-button.ui-state-active:hover {
    background: #ff9800;
}
  </style>
</head>
<body>

  <%
  CourseDescriptor course = (CourseDescriptor)session.getAttribute("course");
  if (course == null) {
	  course = CourseDescriptor.generateTestCourse();
	  session.setAttribute("course", course);
  }
  
  GoalDescriptor userGoal = (GoalDescriptor)session.getAttribute("userGoal");
  String userScheduleIntention = (String)session.getAttribute("selectedSchedule");
  
  ListIterator<GoalDescriptor> iterator = course.getGoals();
  int m=0;
  
  %>

 
<form id="goalSelectForm" method="POST" action="GoalSettingServlet">
<h2>Your intentions with this course</h2>
<p>Please indicate your intentions with respect to this course's content offer (first selection) and the estimated time you intent to spend on this course's activities (second selection).</p>
<div id="tabs">
  <ul>
  <%
  while(iterator.hasNext()) {
	  GoalDescriptor goal = iterator.next();
  %>
    <li>
	    <label class="goalselect" for="<%= goal.getId() %>">Set Goal</label>
    	<input class="goalselectToggle" type="radio" name="goalSelectRadio" id="<%= goal.getId() %>" value="<%= goal.getId() %>" <% if (userGoal != null && userGoal.getId().equals(goal.getId())) { %>checked <% } %> >
    	<a href="#tabs-<%= goal.getId() %>"><%= goal.getTitle() %></a>
    </li>
  <%
  	m++;
  }
  %>
  </ul>
  <%
  iterator = course.getGoals();
  m = 0;
  while(iterator.hasNext()) {
	  GoalDescriptor goal = iterator.next();
  %>
  <div id="tabs-<%= goal.getId() %>">
    <h2><%= goal.getTitle() %></h2>
    <p><%= goal.getDescription() %></p>
    <p>Recommended Modules: (total estimated learning time: <%= goal.getGoalDuration().toHours() %> hours)
    </p>
    <ul>
  <%
  ListIterator<ModuleDescriptor> iterator2 = goal.getModules();
  while(iterator2.hasNext()) {
	  ModuleDescriptor module = iterator2.next();
  %>
        <li><%= module.getDescription() %></li>
  <%
  }
  %>
    </ul>
  </div>
  <%
  	m++;
  }
  %>
</div>
<div id="tabs2">
  <ul>
    <li>
	    <label class="goalselect" for="tabs2-1">Set Schedule</label>
    	<input class="goalselectToggle" type="radio" name="scheduleSelectRadio" id="tabs2-1" value="1" <% if (userScheduleIntention != null && userScheduleIntention.equals("1")) { %>checked <% } %> >
    	<a href="#tabs2-tabs2-1">One hour per week</a>
    </li>
    <li>
	    <label class="goalselect" for="tabs2-2">Set Schedule</label>
    	<input class="goalselectToggle" type="radio" name="scheduleSelectRadio" id="tabs2-2" value="2" <% if (userScheduleIntention != null && userScheduleIntention.equals("2")) { %>checked <% } %> >
    	<a href="#tabs2-tabs2-2">Two hours per week</a>
    </li>
    <li>
	    <label class="goalselect" for="tabs2-3">Set Schedule</label>
    	<input class="goalselectToggle" type="radio" name="scheduleSelectRadio" id="tabs2-3" value="3" <% if (userScheduleIntention != null && userScheduleIntention.equals("3")) { %>checked <% } %> >
    	<a href="#tabs2-tabs2-3">Three hours per week</a>
    </li>
    <li>
	    <label class="goalselect" for="tabs2-4">Set Schedule</label>
    	<input class="goalselectToggle" type="radio" name="scheduleSelectRadio" id="tabs2-4" value="4" <% if (userScheduleIntention != null && userScheduleIntention.equals("4")) { %>checked <% } %> >
    	<a href="#tabs2-tabs2-4">Four hours per week</a>
    </li>
    <li>
	    <label class="goalselect" for="tabs2-5">Set Schedule</label>
    	<input class="goalselectToggle" type="radio" name="scheduleSelectRadio" id="tabs2-5" value="5" <% if (userScheduleIntention != null && userScheduleIntention.equals("5")) { %>checked <% } %> >
    	<a href="#tabs2-tabs2-5">Five hours per week</a>
    </li>
  </ul>
  <div id="tabs2-tabs2-1">
    <h2>One hour per week</h2>
    <p>I intend to spend one hour or less per week on this course</p>
  </div>
  <div id="tabs2-tabs2-2">
    <h2>Two hours per week</h2>
    <p>I intend to spend two hours per week on this course</p>
  </div>
  <div id="tabs2-tabs2-3">
    <h2>Three hours per week</h2>
    <p>I intend to spend three hours per week on this course</p>
  </div>
  <div id="tabs2-tabs2-4">
    <h2>Four hours per week</h2>
    <p>I intend to spend four hours per week on this course</p>
  </div>
  <div id="tabs2-tabs2-5">
    <h2>Five hours per week</h2>
    <p>I intend to spend five hours or more per week on this course</p>
  </div>
</div>
  <div id="selectedGoals"></div>
 		<div id="confirm">
			<input type="button" id="cancel" value="Cancel"></input>
			<input type="submit" id="ok" name="submit" value="Save"></input>
		<% 
		if (userGoal != null) {
		%>
			<input type="submit" id="continue" name="submit" value="Continue"></input>
		<%
		}
		%>	
		</div>
		<div id="selectedGoal">
		<% 
		if (userGoal != null) {
		%>
		<p>Your selected intention: <%= userGoal.getTitle() %></p>
		<p>Your estimated learning time: <%= userGoal.getGoalDuration().toHours() %> hours.</p>
		<p>Your estimated time per week: <%= userGoal.getPlannedTimePerWeekAsInt() %> hours.</p>
		<p>Your estimated time to goal achievement: <%= (int)Math.ceil((double)userGoal.getGoalDuration().toHours()/userGoal.getPlannedTimePerWeekAsInt()) %> weeks</p>
		<%
		} else {
		%>
		<p>No goal selected yet.</p>	
		<%
		}
		%>	
		</div>
</form>
 

</body>
</html>