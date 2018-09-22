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

  //GoalDescriptor userGoal = (GoalDescriptor)session.getAttribute("userGoal");
  HashArrayList<GoalDescriptor> selectedGoals = (HashArrayList<GoalDescriptor>)session.getAttribute("selectedGoals");
  HashArrayList<LessonDescriptor> selectedLessons = (HashArrayList<LessonDescriptor>)session.getAttribute("selectedLessons");
  //String userScheduleIntention = (String)session.getAttribute("selectedSchedule");
  String userScheduleIntention = userPlan.getPlannedTimePerWeek();
  String completionSelectRB = (String)session.getAttribute("completionSelectRB");

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
    $( "input .goalselectToggle" ).checkboxradio({
      icon: true,
      classes: {
      	"ui-checkboxradio-checked": "ui-checkboxradio-checked-orange"
      }
    });

    $( "input .moduleselectToggle" ).checkboxradio({
        icon: true,
        classes: {
        	"ui-checkboxradio-checked": "ui-checkboxradio-checked-orange"
        }
    });

    $( "input .goalselectToggle" ).checkboxradio({
        icon: true,
        classes: {
        	"ui-checkboxradio-checked": "ui-checkboxradio-checked-orange"
        }
    });
    $( document).tooltip({
    	track: true
    });
    $( "button" ).button();

  } );
  
  var goals = {
  	cbModule1: "",
  	cbModule2: "",
  	cbModule3: "",
  	cbModule4: ""
  }
  
  function toggleGoalSelectCB(id, t) {
	  if (id != '0') {
		  $( '#tabs' ).tabs( 'option', 'active', t);
		  //var x = $('.goal_'+id);
		  $(':checkbox.goal_'+id).prop('checked', $('#gsc-'+id).prop('checked'));    		  
	  } else {
		  $( '#tabs' ).tabs( 'option', 'active', t);
		  <%
		  iterator = course.getGoals();
		  m=1;
		  while(iterator.hasNext()) {
			  GoalDescriptor goal = iterator.next();
			  if (goal.getLessons().hasNext()) {
		  %>
		  $('#gsc-<%= goal.getId() %>').prop('checked', $('#gsc-0').prop('checked'));    		  
		  $(':checkbox.goal_<%= goal.getId() %>').prop('checked', $('#gsc-<%= goal.getId() %>').prop('checked'));    		  
		  <%
		  
			  }
		  	  m++;
		  }
		  %>
	  }
  }
    
  </script>
  <style>
 
 /*@import url(http://fonts.googleapis.com/css?family=Satisfy);*/

  
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
  .goalSelectLabel { width: 480px; text-align: left; }
  .ul-goals { list-style-type: none; }
  .a-tab { width: 85%}
  .ui-frame { width: 65em; }
 
  .ui-wrapper { overflow: auto; width: 1048px;}
  .ui-tooltip {
    border: 1px solid white;
    background: #111;
    color: white;
  }

	#wrap {
		width: 1048px;
		margin: 0 auto;
	}
		
 
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

/*
Code by Creative Punch
 http://creative-punch.net/2014/02/create-css3-post-it-note/#comments
Create a CSS3 post-it note without images
*/

.quote-container {
  margin-top: 50px;
  position: relative;
}

.note {
  color: #333;
  position: relative;
  width: 500px;
  margin: 0 auto;
  padding: 20px;
  font-family: "Lucida Sans Unicode", "Lucida Grande", sans-serif;
  font-size: 20px;
  font-weight: bold;
  /* Firefox */
  -moz-box-shadow:5px 5px 7px rgba(33,33,33,1);
  /* Safari+Chrome */
  -webkit-box-shadow: 5px 5px 7px rgba(33,33,33,.7);
  /* Opera */
  box-shadow: 5px 5px 7px rgba(33,33,33,.7);
}

.note .author {
  display: block;
  margin: 40px 0 0 0;
  text-align: right;
}

.yellow {
  background: #eae672;
  -webkit-transform: rotate(2deg);
  -moz-transform: rotate(2deg);
  -o-transform: rotate(2deg);
  -ms-transform: rotate(2deg);
  transform: rotate(2deg);
}

.pin {
  background-color: #aaa;
  display: block;
  height: 32px;
  width: 2px;
  position: absolute;
  left: 50%;
  top: -16px;
  z-index: 1;
}

.pin:after {
  background-color: #A31;
  background-image: radial-gradient(25% 25%, circle, hsla(0,0%,100%,.3), hsla(0,0%,0%,.3));
  border-radius: 50%;
  box-shadow: inset 0 0 0 1px hsla(0,0%,0%,.1),
              inset 3px 3px 3px hsla(0,0%,100%,.2),
              inset -3px -3px 3px hsla(0,0%,0%,.2),
              23px 20px 3px hsla(0,0%,0%,.15);
  content: '';
  height: 12px;
  left: -5px;
  position: absolute;
  top: -10px;
  width: 12px;
}

.pin:before {
  background-color: hsla(0,0%,0%,0.1);
  box-shadow: 0 0 .25em hsla(0,0%,0%,.1);
  content: '';
  height: 24px;
  width: 2px;
  left: 0;
  position: absolute;
  top: 8px;
  transform: rotate(57.5deg);
  -moz-transform: rotate(57.5deg);
  -webkit-transform: rotate(57.5deg);
  -o-transform: rotate(57.5deg);
  -ms-transform: rotate(57.5deg);
  transform-origin: 50% 100%;
  -moz-transform-origin: 50% 100%;
  -webkit-transform-origin: 50% 100%;
  -ms-transform-origin: 50% 100%;
  -o-transform-origin: 50% 100%;
}

  </style>
</head>
<body>

<div id="wrap" class="ui-wrapper">

<!--  <div class="ui-frame">  -->
<form id="goalSelectForm" method="POST" action="GoalSettingServlet">
<!-- h2>Your intentions with this course</h2 -->
<!--  (<%= user.getName() %>) [<%= session.getAttribute("loginData") %>] -->
<% if(!userPlan.isIntentionCompleted() && "intention.topic".equals(intentionStep)) { %>
<p>Select your activities.</p>
<div id="tabs">
  <ul>
    <li class="tabs-0" id="li-0">
    	<a class="a-tab" href="#tabs-0">I am interested to complete the course</a>
    	<input class="goalselectToggle" type="checkbox" name="goalSelectRadio" id="gsc-0" value="0" onClick="toggleGoalSelectCB('0', 0);"  <% if (userPlan.isAllCourseIntention()) { %>checked <% } %> >
    </li>
  <%
  iterator = course.getGoals();
  m=1;
  while(iterator.hasNext()) {
	  GoalDescriptor goal = iterator.next();
  %>
    <li class="tabs-<%= goal.getId() %>" id="li-<%= goal.getId() %>">
    	<a class="a-tab" href="#tabs-<%= goal.getId() %>"><%= goal.getTitle() %></a>
    	<input class="goalselectToggle" type="checkbox" name="goalSelectRadio" id="gsc-<%= goal.getId() %>" value="<%= goal.getId() %>" onClick="toggleGoalSelectCB('<%= goal.getId() %>', <%= m %>);" <% if (selectedGoals != null && selectedGoals.containsKey(goal.getId())) { %>checked <% } %> >
    </li>
  <%
  	m++;
  }
  %>
  </ul>
  <div id="tabs-0">
    <p id="p-0"><b>I am interested to complete the course</b></p>
    <p>By selecting this check-box you chose to follow the complete course.</p>
    <p>Please click on the Next button to plan further details.</p>
    <p>(The estimated learning time for all course is a total of <%= course.getCourseDuration().toHours() %> hours)
    </p>
  </div>
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
        <li><input class="goal_<%= goal.getId() %> moduleselectToggle" type="checkbox" name="goal<%= goal.getId() %>" id="lesson<%= lesson.getId() %>" value="<%= lesson.getId() %>" onClick="$( '#gsc-<%= goal.getId() %>').prop('checked', true);" <% if (selectedLessons != null && selectedLessons.containsKey(lesson.getId())) { %>checked <% } %> >
        <label for="lesson<%= lesson.getId() %>"><%= lesson.getTitle() %></label></li>
  <%
  }
  %>
  <%
  ArrayList<String> list = goal.getCompletionGoalKeys();
  for(String key : list) {
	  String desc = goal.getCompletionGoal(key);
  %>
        <li><input class="moduleselectToggle" type="radio" name="completionSelectRB" id="completion<%= key %>" value="<%= key %>" <% if (key.equals(completionSelectRB)) { %>checked <% } %> >
        <label for="completion<%= key %>"><%= desc %></label></li>
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

<% } else if(!userPlan.isIntentionCompleted() && "intention.schedule".equals(intentionStep)) { %>

<p>How much time per week can you invest in this course?</p>
<div class="ui-widget ui-widget-content">
  <ul class="ul-goals">
    <li>
    	<input class="scheduleselectToggle" type="radio" name="scheduleSelectRadio" id="tabs2-1" value="1" <% if (userScheduleIntention != null && userScheduleIntention.equals("1")) { %>checked <% } %> ><label class="goalSelectLabel" for="tabs2-1">I intend to spend about <b>one hour</b> per week on this course</label>
    </li>
    <li>
    	<input class="scheduleselectToggle" type="radio" name="scheduleSelectRadio" id="tabs2-2" value="2" <% if (userScheduleIntention != null && userScheduleIntention.equals("2")) { %>checked <% } %> ><label class="goalSelectLabel" for="tabs2-2">I intend to spend about <b>two hours</b> per week on this course</label>
    </li>
    <li>
    	<input class="scheduleselectToggle" type="radio" name="scheduleSelectRadio" id="tabs2-3" value="3" <% if (userScheduleIntention != null && userScheduleIntention.equals("3")) { %>checked <% } %> ><label class="goalSelectLabel" for="tabs2-3">I intend to spend about <b>three hours</b> per week on this course</label>
    </li>
    <li>
    	<input class="scheduleselectToggle" type="radio" name="scheduleSelectRadio" id="tabs2-4" value="4" <% if (userScheduleIntention != null && userScheduleIntention.equals("4")) { %>checked <% } %> ><label class="goalSelectLabel" for="tabs2-4">I intend to spend about <b>four hours</b> per week on this course</label>
    </li>
    <li>
    	<input class="scheduleselectToggle" type="radio" name="scheduleSelectRadio" id="tabs2-5" value="5" <% if (userScheduleIntention != null && userScheduleIntention.equals("5")) { %>checked <% } %> ><label class="goalSelectLabel" for="tabs2-5">I intend to spend about <b>five hours</b> per week on this course</label>
    </li>
  </ul>
  <div id="selectedGoals"></div>
</div>

<% } else if(userPlan.isIntentionCompleted() || "intention.feedback".equals(intentionStep)) { %>

<div class="quote-container">
      <i class="pin"></i>
  <blockquote class="note yellow">
<p>Memo</p>
<!-- div class="ui-widget ui-widget-content" -->
		<div id="selectedGoal">
			<%			
		if (selectedGoals != null) {
			String separator = "";
		%>
		<!-- p>Your selection:</p -->
		<ul>
		<% 
		if (userPlan.isAllCourseIntention()) {
			%>
			<li>Your intention is to complete all the course.</li>
			<% } else if (selectedGoals != null && selectedGoals.size() > 0) { 
					ListIterator<UserGoal> userGoalIterator = userPlan.getGoals(); 
					%>
				<li>Your intentions:</li><ul> <% 
					while (userGoalIterator.hasNext()) {
						UserGoal userGoal = userGoalIterator.next();
					%><li><%= separator+userGoal.getGoalDescriptor().getTitle() %><%
						//separator = ", ";
						if (userGoal.getLessons().hasNext()) {
							%> (<%
							String separator2 = "";
							ListIterator<UserLesson> lessonIterator = userGoal.getLessons();
							while (lessonIterator.hasNext()) {
								LessonDescriptor lesson = lessonIterator.next().getLesson();
								%><%= separator2+lesson.getTitle() %><%
								separator2 = ", ";
							}
							%>)</li><%
							
						}
					} %></ul><%
			} else {
				%>
				<li>You did not indicate any intention. <% if (!userPlan.isIntentionCompleted()) { %>You may consider to go back and select intentions.<% } %> </li>
				<%				
			} %><li>Your estimated time per week: <%= userPlan.getPlannedTimePerWeekAsInt() %> hours.</li>
		</ul>
		<% if (userPlan.getPlanDuration().toHours() > 0) { %>
		<p>Our learning effort estimation for you:</p>
		<ul>
		<li>To complete your intention you need a total learning time of: <%= userPlan.getPlanDuration().toHours() %> hours.</li>
		<li>Your estimated time to goal achievement: <%= (int)Math.ceil((double)(userPlan.getPlanDuration().toHours())/userPlan.getPlannedTimePerWeekAsInt()) %> weeks</li>
		</ul>
		<%
			}
		} else {
		%>
		<p>No goal selected yet.</p>	
		<%
		}
		%>	
		</div>
  </blockquote>

<!-- /div -->
</div>
<% } %>
 		<div class="confirm">
		<!-- <input type="button" id="cancel" value="Cancel"></input>  -->
		<% 
		if (!userPlan.isIntentionCompleted() && !PlanningSteps.intentionSteps[0].equals(intentionStep)) {
		%>
			<button type="submit" id="prev" name="submit" value="Previous" title="Click continue to go to the previous page. Your selection will be saved automatically.">Go Back</button>
		<%
		}
		if (!userPlan.isIntentionCompleted() && !PlanningSteps.intentionSteps[PlanningSteps.intentionSteps.length-1].equals(intentionStep)) {
		%>
			<button type="submit" id="next" name="submit" value="Next" title="Click continue to go to the next page. Your selection will be saved automatically.">Continue</button>
		<%
		}
		if (userPlan.isIntentionCompleted() || (selectedGoals != null && PlanningSteps.intentionSteps[PlanningSteps.intentionSteps.length-1].equals(intentionStep))) {
		%>
			<button type="submit" id="continue" name="submit" value="Continue" title="Click continue to go to the next page. Your selection will be saved automatically.">Continue</button>
		<%
		}
		%>	
		</div>

</form>
<!-- </div>  --> 

</div>

</body>
</html>