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
<title>Coping Plan</title>
  <link rel="stylesheet" href="https://code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
  <link rel="stylesheet" href="https://jqueryui.com/resources/demos/style.css">
  <script src="https://code.jquery.com/jquery-1.12.4.js"></script>
  <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
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
  .ui-frame { width: 65em; }
	.confirm {
		float: right;
	}
  .ui-wrapper { overflow: auto;}

	#wrap {
		width: 1040px;
		margin: 0 auto;
	}

.ui-button:active, .ui-button.ui-state-active:hover {
    background: #ff9800;
}
  </style>
</head>
<body>
<div id="wrap" class="ui-frame"> 
<form id="copingPlanForm" method="POST" action="CopingPlanServlet">

	<h2>Make your Plan B</h2>
	<p>Think about any inconvenience that could undermine your plan of completing the activities you selected within this course.</p>
	<div class="ui-widget ui-widget-content ui-wrapper">
		<div style="width: 640px; float: left; overflow: auto;">
			<div id="selectedGoal" style="margin-left: 10px;">
				<%			
			if (selectedGoals != null) {
				String separator = "";
				%>
				<p>Here we remind you what you selected:</p>
				<ul>
				<% 
				if (userPlan.isAllCourseIntention()) {
					%><li>Your intention is to complete all the course.</li>
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
				} %><li>Your estimated time per week: <%= userPlan.getPlannedTimePerWeekAsInt() %> hours.</li>
			</ul><%	} %>
			</div>
			<div>
				<div style="float: left; width: 300px; margin-left: 10px;">
					<label><b>IF</b> I meet one of the following situations:</label>
					<textarea style="width: 300px; height: 150px;" id="obstacles" name="obstacles" ><%= userPlan.getObstacles() %></textarea>
				</div>
				<div style="float: left; width: 300px; margin-left: 10px;">
					<label><b>THEN</b> I will perform the following action:</label> 
					<textarea style="width: 300px; height: 150px;" id="copingPlan" name="copingPlan" ><%= userPlan.getCopingPlan() %></textarea>
				</div>
			</div>
		</div>
		<div style="float: left; width: 360px; margin-left: 10px; margin-top: 10px;">
			<label><b>Examples</b></label>
			<ul>
				<li><b>IF</b> I realise that I need more time for an activity <BR>
				<b>THEN</b> I'll not plan any activities immediately after the other to take all the time I need.</li>
				<li><b>IF</b> the Internet connection will not work properly at home <br>
				<b>THEN</b> I'll use the mobile data of my telephone to follow the MOOC.</li>
				<li><b>IF</b> the location I chose  (i.e. a coffee) is too busy to find concentration <br>
				<b>THEN</b> I'll change it (by going home).</li>
				<li><b>IF</b> I come later home from work and I miss my planned course activity<br>
				<b>THEN</b> I will re-plan my course activity to the next possible day.<br></li>
				<li><b>IF</b> I am too tired to follow another course activity on the same day<br>
				<b>THEN</b> I will re-plan my course activities to have less activities on the same day.<br></li>
			</ul> 
		</div>
	</div>
	<div class="confirm">
		<button type="submit" id="ok" name="submit" value="OK" >Save</button>
		<button type="submit" id="next" name="submit" value="Next" >Save and Next</button>
	</div>
	
</form>
</div>

</body>
</html>