<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="stimulatedplanning.*, java.util.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <%
  session = StimulatedPlanningFactory.initializeSession(request, response);

  User user = (User)session.getAttribute("user");
  CourseDescriptor course = (CourseDescriptor)session.getAttribute("course");
  UserPlan userPlan = (UserPlan)session.getAttribute("userPlan");

  GoalDescriptor userGoal = (GoalDescriptor)session.getAttribute("userGoal");
  String userScheduleIntention = (String)session.getAttribute("selectedSchedule");

  String intentionStep = (String)session.getAttribute("intentionStep");
  if (intentionStep == null || "".equals(intentionStep)) {
	  intentionStep = PlanningSteps.intentionSteps[0];
	  session.setAttribute("intentionStep", intentionStep);
  }

  ListIterator<GoalDescriptor> iterator = course.getGoals();
  int m=0;
  
  %>
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
//    $( "#tabs2" ).tabs().addClass( "ui-tabs-vertical ui-helper-clearfix" );
//    $( "#tabs2 li" ).removeClass( "ui-corner-top" ).addClass( "ui-corner-left" );
    $( "input" ).checkboxradio({
      icon: true,
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
  
  function toggleGoalSelectCB(id, t) {
	  $( '#tabs' ).tabs( 'option', 'active', t);
	  var x = $('.goal_'+id);
	  $(':checkbox.goal_'+id).prop('checked', $('#gsc-'+id).prop('checked'));    		  
  }
    
  </script>
  <style>
  .ui-tabs-vertical { width: 65em; }
  .ui-tabs-vertical .ui-tabs-nav { padding: .2em .1em .2em .2em; float: left; width: 25em; }
  .ui-tabs-vertical .ui-tabs-nav li { clear: left; width: 100%; border-bottom-width: 1px !important; border-right-width: 0 !important; margin: 0 -1px .2em 0; }
  .ui-tabs-vertical .ui-tabs-nav li a { display:block; }
  .ui-tabs-vertical .ui-tabs-nav li.ui-tabs-active { padding-bottom: 0; padding-right: .1em; border-right-width: 1px; }
  .ui-tabs-vertical .ui-tabs-panel { padding: .2em; float: right; width: 36em;}
  
  .ui-checkboxradio-checked-orange .ui-state-active { background: #ff9800; }
  .ui-checkboxradio-checked-orange { background: #ff9800; }
  .ui-checkboxradio-checked-green .ui-state-active { background: #008800; }
  .ui-checkboxradio-checked-green { background: #008800; }
  .ui-state-active { background: #ff9800; }
  .goalselect { float: right; }
  .goalselectToggle { float: right; }
  .ul-goals { list-style-type: none; }
  .a-tab { width: 85%}
 
  <%
  iterator = course.getGoals();
  m=0;
  while(iterator.hasNext()) {
	  GoalDescriptor goal = iterator.next();
	  String grey = Integer.toHexString(14-m);
	  grey = grey + grey + grey + grey + grey + grey;
	  String blue = Integer.toHexString(13-2*m);
	  blue = "00" + blue + "fff";
  %>	
  	#li-<%= goal.getId() %> {background-image: none; background-color: #<%= grey %>; }
  	#li-<%= goal.getId() %>.ui-tabs-active {background-image : none; background-color : #<%= blue %>; font-weight : bolder;}
  	#p-<%= goal.getId() %> {padding: 3px; background-image : none; color: white; background-color : #<%= blue %>; font-weight : bolder;}
  <%
  	m++;
  }
  %>


	.confirm {
		float: right;
	}

.ui-button:active, .ui-button.ui-state-active:hover {
    background: #ff9800;
}
  </style>
</head>
<body>


 
<form id="goalSelectForm" method="POST" action="GoalSettingServlet">
<h2>Your intentions with this course</h2>
<% if("intention.topic".equals(intentionStep)) { %>
<p>Please indicate your intentions with respect to this course's content offer.</p>
<div id="tabs">
  <ul>
  <%
  iterator = course.getGoals();
  m=0;
  while(iterator.hasNext()) {
	  GoalDescriptor goal = iterator.next();
  %>
    <li class="tabs-<%= goal.getId() %>" id="li-<%= goal.getId() %>">
    	<a class="a-tab" href="#tabs-<%= goal.getId() %>"><%= goal.getTitle() %></a>
    	<input class="goalselectToggle" type="checkbox" name="goalSelectRadio" id="gsc-<%= goal.getId() %>" value="<%= goal.getId() %>" onClick="toggleGoalSelectCB('<%= goal.getId() %>', <%= m %>);" <% if (userGoal != null && userGoal.getId().equals(goal.getId())) { %>checked <% } %> >
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
    <p id="p-<%= goal.getId() %>"><b><%= goal.getTitle() %></b></p>
    <p><%= goal.getDescription() %></p>
  <%
  ListIterator<LessonDescriptor> iterator2 = goal.getLessons();
  if(iterator2.hasNext()) {
  %>
    <p>Recommended Lessons: (total estimated learning time: <%= goal.getGoalDuration().toHours() %> hours)
    </p>
  <%
  }
  %>
    <ul class="ul-goals">
  <%
  while(iterator2.hasNext()) {
	  LessonDescriptor lesson = iterator2.next();
  %>
        <li><input class="goal_<%= goal.getId() %> moduleselectToggle" type="checkbox" name="moduleSelectCB" id="lesson<%= lesson.getId() %>" value="<%= lesson.getId() %>" <% if (userGoal != null && userGoal.getId().equals(goal.getId())) { %>checked <% } %> >
        <label for="lesson<%= lesson.getId() %>"><%= lesson.getTitle() %></label></li>
  <%
  }
  %>
  <%
  Set<String> set = goal.getCompletionGoalKeys();
  Iterator<String> iterator3 = set.iterator();
  while(iterator3.hasNext()) {
	  String key = iterator3.next();
	  String desc = goal.getCompletionGoal(key);
  %>
        <li><input class="moduleselectToggle" type="radio" name="completionSelectRB" id="completion<%= key %>" value="<%= key %>" <% if (userGoal != null && userGoal.getId().equals(goal.getId())) { %>checked <% } %> >
        <label for="completion<%= key %>"><%= desc %></label></li>
  <%
  }
  %>
    </ul>
    <div class="confirm">
 			<input type="button" id="ok_<%= goal.getId() %>" name="ok_<%= goal.getId() %>" value="OK"></input>
    </div>
  </div>
  <%
  	m++;
  }
  %>
</div>

<% } else if("intention.schedule".equals(intentionStep)) { %>

<p>Please indicate your intentions with respect to the estimated time you intent to spend on this course's activities.</p>
<div>
  <ul>
    <li>
    	<input class="goalselectToggle" type="radio" name="scheduleSelectRadio" id="tabs2-1" value="1" <% if (userScheduleIntention != null && userScheduleIntention.equals("1")) { %>checked <% } %> ><label for="tabs2-1">I intend to spend about <b>one hour</b> per week on this course</label>
    </li>
    <li>
    	<input class="goalselectToggle" type="radio" name="scheduleSelectRadio" id="tabs2-2" value="2" <% if (userScheduleIntention != null && userScheduleIntention.equals("2")) { %>checked <% } %> ><label for="tabs2-2">I intend to spend about <b>two hours</b> per week on this course</label>
    </li>
    <li>
    	<input class="goalselectToggle" type="radio" name="scheduleSelectRadio" id="tabs2-3" value="3" <% if (userScheduleIntention != null && userScheduleIntention.equals("3")) { %>checked <% } %> ><label for="tabs2-3">I intend to spend about <b>three hours</b> per week on this course</label>
    </li>
    <li>
    	<input class="goalselectToggle" type="radio" name="scheduleSelectRadio" id="tabs2-4" value="4" <% if (userScheduleIntention != null && userScheduleIntention.equals("4")) { %>checked <% } %> ><label for="tabs2-4">I intend to spend about <b>four hours</b> per week on this course</label>
    </li>
    <li>
    	<input class="goalselectToggle" type="radio" name="scheduleSelectRadio" id="tabs2-5" value="5" <% if (userScheduleIntention != null && userScheduleIntention.equals("5")) { %>checked <% } %> ><label for="tabs2-5">I intend to spend about <b>five hours</b> per week on this course</label>
    </li>
  </ul>
  <div id="selectedGoals"></div>
</div>

<% } else if("intention.feedback".equals(intentionStep)) { %>

		<div id="selectedGoal">
		<% 
		if (userGoal != null) {
		%>
		<p>Your selection:</p>
		<ul>
		<li>Your intention: <%= userGoal.getTitle() %></li>
		<li>Your estimated time per week: <%= userGoal.getPlannedTimePerWeekAsInt() %> hours.</li>
		</ul>
		<p>Our learning effort estimation for you:</p>
		<ul>
		<li>To complete your intention you need a total learning time of: <%= userGoal.getGoalDuration().toHours() %> hours.</li>
		<li>Your estimated time to goal achievement: <%= (int)Math.ceil((double)userGoal.getGoalDuration().toHours()/userGoal.getPlannedTimePerWeekAsInt()) %> weeks</li>
		</ul>
		<%
		} else {
		%>
		<p>No goal selected yet.</p>	
		<%
		}
		%>	
		</div>
<% } %>
 		<div class="confirm">
		<!-- <input type="button" id="cancel" value="Cancel"></input>  -->
		<% 
		if (!PlanningSteps.intentionSteps[0].equals(intentionStep)) {
		%>
			<input type="submit" id="ok" name="submit" value="Previous"></input>
		<%
		}
		if (!PlanningSteps.intentionSteps[PlanningSteps.intentionSteps.length-1].equals(intentionStep)) {
		%>
			<input type="submit" id="ok" name="submit" value="Next"></input>
		<%
		}
		if (userGoal != null && PlanningSteps.intentionSteps[PlanningSteps.intentionSteps.length-1].equals(intentionStep)) {
		%>
			<input type="submit" id="continue" name="submit" value="Continue"></input>
		<%
		}
		%>	
		</div>

</form>
 

</body>
</html>