<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="stimulatedplanning.*, java.util.ListIterator, java.util.Collection" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Stimulated Planning</title>
<link href="fullcalendar-3.6.2/fullcalendar.min.css" rel="stylesheet">
<link href="fullcalendar-3.6.2/fullcalendar.print.min.css" rel="stylesheet" media="print">
<link rel="stylesheet" href="https://code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
<link rel="stylesheet" href="https://jqueryui.com/resources/demos/style.css">
<script src="fullcalendar-3.6.2/lib/moment.min.js"></script>
<script src="https://code.jquery.com/jquery-1.12.4.js"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
<script src="fullcalendar-3.6.2/fullcalendar.min.js"></script>
<script>
  $( function() {
    $( "#accordion" ).accordion({
      heightStyle: "auto",
      collapsible: true
    });
  } );
</script>
<%

  session = StimulatedPlanningFactory.initializeSession(request, response);

  User user = (User)session.getAttribute("user");
  CourseDescriptor course = (CourseDescriptor)session.getAttribute("course");
  UserPlan userPlan = (UserPlan)session.getAttribute("userPlan");

  String selectedGoalProfile = "";
  ListIterator<ModuleDescriptor> modIterator;
  ListIterator<UserGoal> goalIterator;
  ListIterator<GoalDescriptor> courseGoalIterator;
  boolean hasPlannableGoals = userPlan.hasPlannableGoals();
  goalIterator = userPlan.getGoals();
  courseGoalIterator = course.getGoals();
  selectedGoalProfile = "Course: "+course.getTitle();
  
  int m=0;
  
%>

<script>

	$(document).ready(function() {


		/* initialize the external events
		-----------------------------------------------------------------*/

		$('.external-events .fc-event').each(function() {
			
			var plan = "";
			if ($(this).hasClass("plan-a")) {
				plan = "plan-a";
			}
			if ($(this).hasClass("plan-b")) {
				plan = "plan-b";
			}

			// store data so the calendar knows to render an event upon drop
			$(this).data('event', {
				title: $.trim($(this).text()), // use the element's text as the event title
				stick: true, // maintain when user navigates (see docs on the renderEvent method)
				id: ($(this).attr('id')),
				url: ($(this).attr('data-url')),
				className: plan
			});

			// make the event draggable using jQuery UI
			$(this).draggable({
				zIndex: 999,
				revert: true,      // will cause the event to go back to its
				revertDuration: 0  //  original position after the drag
			});

		});

		$('.plan-b').each(function() {
			

			// make the event draggable using jQuery UI
			$(this).draggable('disable');

		});


		/* initialize the calendar
		-----------------------------------------------------------------*/

		$('#calendar').fullCalendar({
			header: {
				left: 'prev,next today',
				center: 'title',
				right: 'agendaWeek,agendaDay'
    		},
			id: ($(this).attr('id')),
			eventOverlap: false,
			allDaySlot: false,
    		height: 500,
    		firstDay: 1,
    		defaultView: 'agendaWeek',
    		//defaultDate: $.fullCalendar.moment('2018-02-01'),
    	    validRange: {
    	        start: '2018-02-19',
    	        end: '2018-03-12'
    	    },
			eventStartEditable: true,
			eventDurationEditable: false,
			droppable: true, // this allows things to be dropped onto the calendar
    	    drop: function() {
				var id_rem = $(this).attr('id');
		    	console.log("event drop: "+id_rem);
				if (id_rem.startsWith("late_")) {
					$(this).attr('id', 'tempdragid');
					$('#calendar').fullCalendar('removeEvents', id_rem.substring(5));
					$(this).attr('id', id_rem);
				}
				// is the "remove after drop" checkbox checked?
				if ($('#drop-remove').is(':checked')) {
					// if so, remove the element from the "Draggable Events" list
					$(this).removeClass('plan-a');
					$(this).removeClass('plan-ok');
					$(this).removeClass('plan-late');
					$(this).addClass('plan-b');
					$(this).draggable('disable');
					//$(this).remove();
				}
			},
			events: [
<%
	ListIterator<PlanItem> planIterator = userPlan.getPlanItems();
	int i=0;
	while(planIterator.hasNext()) {
		PlanItem item = planIterator.next();
%>
<%= item.getJsonPlanItem() %>
<%= (planIterator.hasNext() ? "," : "") %>
<%
	}
%>
			]
		});


	});

	function retrieveAllEvents() {
		var eventlist = $('#calendar').fullCalendar('clientEvents');
		
		var seen = []; 
		var replacer = function(key, value) {
			if (value != null && typeof value == "object") {
			  if (seen.indexOf(value) >= 0) {
			    return;
			  }
			  seen.push(value);
			}
			return value;
		};
		
		var eventlisttext = JSON.stringify(eventlist, replacer);
		
		$('input[name="calenderItems"]').attr('value', eventlisttext);
	}
	
</script>
<style>

	body {
		margin-top: 40px;
		text-align: center;
		font-size: 14px;
		font-family: "Lucida Grande",Helvetica,Arial,Verdana,sans-serif;
	}
		
	#wrap {
		width: 1040px;
		margin: 0 auto;
	}
		
	#leftbar {
		float: left;
		text-align: left;
		width: 250px;
	}
		
	#accordion {
		float: left;
		width: 250px;
		height: 360px;
		padding: 0 10px;
		border: 1px solid #ccc;
		background: #eee;
		text-align: left;
	}
		
	.external-events {
	/*	float: left;
		width: 150px;
		padding: 0 10px;
		border: 1px solid #ccc;
		background: #eee;
		text-align: left; */
	}
		
	.external-events h4 {
		font-size: 16px;
		margin-top: 0;
		padding-top: 1em;
	}
		
	.external-events .fc-event {
		margin: 10px 0;
		cursor: pointer;
	}
		
	.external-events p {
		margin: 1.5em 0;
		font-size: 11px;
		color: #666;
	}
		
	.external-events p input {
		margin: 0;
		vertical-align: middle;
	}

	#calendar {
		float: right;
		width: 740px;
	}
	
	.confirm {
		float: right;
	}
	
	.ui-accordion .ui-accordion-content {
        overflow: initial;
	}
	
	.plan-a {
	}
	
	.plan-b {
	    background-color: #afafaf;
	}
	
	.plan-done {
	    background-color: #afafaf;
	}
	
	.plan-late {
	    background-color: #cd0000;
	}
	
	.plan-ok {
	    background-color: #3a87ad;
	}

	#feedbackMessage {
		width: 600px;
		margin: 0 auto;
		padding: 3px;
		float: center;
		text-align: left;
		border: 2px solid black;
	}
	
  .ui-wrapper { overflow: auto; width: 1048px;}
	

</style>
</head>
<body>

<div id="wrap" class="ui-wrapper">

<div id="leftbar">
<!-- h4>Your Intention: <%= selectedGoalProfile %></h4 -->
<p>Plan your activities by dragging them from the list below to the calendar.</p>
<div id="accordion">
<%
	if (hasPlannableGoals) {
		while(goalIterator.hasNext()) {
			UserGoal goal = goalIterator.next();
			ListIterator<UserLesson> lessonIterator = goal.getLessons();
			if (lessonIterator.hasNext()) {
%>
		  <h3><%= goal.getGoalDescriptor().getTitle() %></h3>
		  <div>
				<div class="external-events">
		<%
				while(lessonIterator.hasNext()) {
					UserLesson userLesson = lessonIterator.next();
					LessonDescriptor lesson = userLesson.getLesson();
					if (!userPlan.hasPlanItemForLesson(lesson)) {
		%>
						<div 
							class="fc-event plan-a plan-ok ui-draggable ui-draggable-handle" 
							id="<%= lesson.getId() %>" 
							data-duration="<%= lesson.getLessonDurationString() %>"
						><%= lesson.getTitle() %></div>
		<%
					} else {
						PlanItem item = userPlan.getPlanItemForLesson(lesson);
						if (!PlanCompletionStatus.DELAYED.equals(item.getPlanCompletionStatus())) {
%>
							<div 
								class="fc-event plan-b ui-draggable ui-draggable-handle" 
								id="<%= lesson.getId() %>" 
								data-duration="<%= lesson.getLessonDurationString() %>"
							><%= lesson.getTitle() %></div>
<%
						} else {
%>
							<div 
								class="fc-event plan-a plan-late ui-draggable ui-draggable-handle" 
								id="<%= "late_"+lesson.getId() %>" 
								data-duration="<%= lesson.getLessonDurationString() %>"
							><%= lesson.getTitle() %></div>
<%
						}
					}
				}
		%>
				</div>
		  </div>
<%
			}
		}
	} else {
		while(courseGoalIterator.hasNext()) {
			GoalDescriptor goal = courseGoalIterator.next();
			ListIterator<LessonDescriptor> lessonIterator = goal.getLessons();
			if (lessonIterator.hasNext()) {
%>
		  <h3><%= goal.getTitle() %></h3>
		  <div>
				<div class="external-events">
		<%
				while(lessonIterator.hasNext()) {
					LessonDescriptor lesson = lessonIterator.next();
					if (!userPlan.hasPlanItemForLesson(lesson)) {
		%>
					<div 
						class="fc-event plan-a plan-ok ui-draggable ui-draggable-handle" 
						id="<%= lesson.getId() %>" 
						data-duration="<%= lesson.getLessonDurationString() %>"
					><%= lesson.getTitle() %></div>
		<%
					} else {
						PlanItem item = userPlan.getPlanItemForLesson(lesson);
						if (!PlanCompletionStatus.DELAYED.equals(item.getPlanCompletionStatus())) {
%>
							<div 
								class="fc-event plan-b ui-draggable ui-draggable-handle" 
								id="<%= lesson.getId() %>" 
								data-duration="<%= lesson.getLessonDurationString() %>"
							><%= lesson.getTitle() %></div>
<%
						} else {
%>
							<div 
								class="fc-event plan-a plan-late ui-draggable ui-draggable-handle" 
								id="<%= "late_"+lesson.getId() %>" 
								data-duration="<%= lesson.getLessonDurationString() %>"
							><%= lesson.getTitle() %></div>
<%
						}
					}
				}
		%>
				</div>
		  </div>
<%
			}
		}
	}
%>
 </div>
<div style="display:none;">
	<p>
		<input type="checkbox" id="drop-remove" checked>
		<label for="drop-remove">remove after drop</label>
	</p>
</div>
</div>


		<!-- <div id="calendar" class="fc fc-unthemed fc-ltr"><div class="fc-toolbar fc-header-toolbar"><div class="fc-left"><div class="fc-button-group"><button type="button" class="fc-prev-button fc-button fc-state-default fc-corner-left"><span class="fc-icon fc-icon-left-single-arrow"></span></button><button type="button" class="fc-next-button fc-button fc-state-default fc-corner-right"><span class="fc-icon fc-icon-right-single-arrow"></span></button></div><button type="button" class="fc-today-button fc-button fc-state-default fc-corner-left fc-corner-right fc-state-disabled" disabled="">today</button></div><div class="fc-right"><div class="fc-button-group"><button type="button" class="fc-month-button fc-button fc-state-default fc-corner-left fc-state-active">month</button><button type="button" class="fc-agendaWeek-button fc-button fc-state-default">week</button><button type="button" class="fc-agendaDay-button fc-button fc-state-default fc-corner-right">day</button></div></div><div class="fc-center"><h2>November 2017</h2></div><div class="fc-clear"></div></div><div class="fc-view-container" style=""><div class="fc-view fc-month-view fc-basic-view" style=""><table class=""><thead class="fc-head"><tr><td class="fc-head-container fc-widget-header"><div class="fc-row fc-widget-header"><table class=""><thead><tr><th class="fc-day-header fc-widget-header fc-sun"><span>Sun</span></th><th class="fc-day-header fc-widget-header fc-mon"><span>Mon</span></th><th class="fc-day-header fc-widget-header fc-tue"><span>Tue</span></th><th class="fc-day-header fc-widget-header fc-wed"><span>Wed</span></th><th class="fc-day-header fc-widget-header fc-thu"><span>Thu</span></th><th class="fc-day-header fc-widget-header fc-fri"><span>Fri</span></th><th class="fc-day-header fc-widget-header fc-sat"><span>Sat</span></th></tr></thead></table></div></td></tr></thead><tbody class="fc-body"><tr><td class="fc-widget-content"><div class="fc-scroller fc-day-grid-container" style="overflow: hidden; height: 647px;"><div class="fc-day-grid fc-unselectable"><div class="fc-row fc-week fc-widget-content" style="height: 107px;"><div class="fc-bg"><table class=""><tbody><tr><td class="fc-day fc-widget-content fc-sun fc-other-month fc-past" data-date="2017-10-29"></td><td class="fc-day fc-widget-content fc-mon fc-other-month fc-past" data-date="2017-10-30"></td><td class="fc-day fc-widget-content fc-tue fc-other-month fc-past" data-date="2017-10-31"></td><td class="fc-day fc-widget-content fc-wed fc-past" data-date="2017-11-01"></td><td class="fc-day fc-widget-content fc-thu fc-past" data-date="2017-11-02"></td><td class="fc-day fc-widget-content fc-fri fc-past" data-date="2017-11-03"></td><td class="fc-day fc-widget-content fc-sat fc-past" data-date="2017-11-04"></td></tr></tbody></table></div><div class="fc-content-skeleton"><table><thead><tr><td class="fc-day-top fc-sun fc-other-month fc-past" data-date="2017-10-29"><span class="fc-day-number">29</span></td><td class="fc-day-top fc-mon fc-other-month fc-past" data-date="2017-10-30"><span class="fc-day-number">30</span></td><td class="fc-day-top fc-tue fc-other-month fc-past" data-date="2017-10-31"><span class="fc-day-number">31</span></td><td class="fc-day-top fc-wed fc-past" data-date="2017-11-01"><span class="fc-day-number">1</span></td><td class="fc-day-top fc-thu fc-past" data-date="2017-11-02"><span class="fc-day-number">2</span></td><td class="fc-day-top fc-fri fc-past" data-date="2017-11-03"><span class="fc-day-number">3</span></td><td class="fc-day-top fc-sat fc-past" data-date="2017-11-04"><span class="fc-day-number">4</span></td></tr></thead><tbody><tr><td></td><td></td><td></td><td></td><td></td><td></td><td></td></tr></tbody></table></div></div><div class="fc-row fc-week fc-widget-content" style="height: 107px;"><div class="fc-bg"><table class=""><tbody><tr><td class="fc-day fc-widget-content fc-sun fc-today " data-date="2017-11-05"></td><td class="fc-day fc-widget-content fc-mon fc-future" data-date="2017-11-06"></td><td class="fc-day fc-widget-content fc-tue fc-future" data-date="2017-11-07"></td><td class="fc-day fc-widget-content fc-wed fc-future" data-date="2017-11-08"></td><td class="fc-day fc-widget-content fc-thu fc-future" data-date="2017-11-09"></td><td class="fc-day fc-widget-content fc-fri fc-future" data-date="2017-11-10"></td><td class="fc-day fc-widget-content fc-sat fc-future" data-date="2017-11-11"></td></tr></tbody></table></div><div class="fc-content-skeleton"><table><thead><tr><td class="fc-day-top fc-sun fc-today " data-date="2017-11-05"><span class="fc-day-number">5</span></td><td class="fc-day-top fc-mon fc-future" data-date="2017-11-06"><span class="fc-day-number">6</span></td><td class="fc-day-top fc-tue fc-future" data-date="2017-11-07"><span class="fc-day-number">7</span></td><td class="fc-day-top fc-wed fc-future" data-date="2017-11-08"><span class="fc-day-number">8</span></td><td class="fc-day-top fc-thu fc-future" data-date="2017-11-09"><span class="fc-day-number">9</span></td><td class="fc-day-top fc-fri fc-future" data-date="2017-11-10"><span class="fc-day-number">10</span></td><td class="fc-day-top fc-sat fc-future" data-date="2017-11-11"><span class="fc-day-number">11</span></td></tr></thead><tbody><tr><td></td><td></td><td></td><td></td><td></td><td></td><td></td></tr></tbody></table></div></div><div class="fc-row fc-week fc-widget-content" style="height: 107px;"><div class="fc-bg"><table class=""><tbody><tr><td class="fc-day fc-widget-content fc-sun fc-future" data-date="2017-11-12"></td><td class="fc-day fc-widget-content fc-mon fc-future" data-date="2017-11-13"></td><td class="fc-day fc-widget-content fc-tue fc-future" data-date="2017-11-14"></td><td class="fc-day fc-widget-content fc-wed fc-future" data-date="2017-11-15"></td><td class="fc-day fc-widget-content fc-thu fc-future" data-date="2017-11-16"></td><td class="fc-day fc-widget-content fc-fri fc-future" data-date="2017-11-17"></td><td class="fc-day fc-widget-content fc-sat fc-future" data-date="2017-11-18"></td></tr></tbody></table></div><div class="fc-content-skeleton"><table><thead><tr><td class="fc-day-top fc-sun fc-future" data-date="2017-11-12"><span class="fc-day-number">12</span></td><td class="fc-day-top fc-mon fc-future" data-date="2017-11-13"><span class="fc-day-number">13</span></td><td class="fc-day-top fc-tue fc-future" data-date="2017-11-14"><span class="fc-day-number">14</span></td><td class="fc-day-top fc-wed fc-future" data-date="2017-11-15"><span class="fc-day-number">15</span></td><td class="fc-day-top fc-thu fc-future" data-date="2017-11-16"><span class="fc-day-number">16</span></td><td class="fc-day-top fc-fri fc-future" data-date="2017-11-17"><span class="fc-day-number">17</span></td><td class="fc-day-top fc-sat fc-future" data-date="2017-11-18"><span class="fc-day-number">18</span></td></tr></thead><tbody><tr><td></td><td></td><td></td><td></td><td></td><td></td><td></td></tr></tbody></table></div></div><div class="fc-row fc-week fc-widget-content" style="height: 107px;"><div class="fc-bg"><table class=""><tbody><tr><td class="fc-day fc-widget-content fc-sun fc-future" data-date="2017-11-19"></td><td class="fc-day fc-widget-content fc-mon fc-future" data-date="2017-11-20"></td><td class="fc-day fc-widget-content fc-tue fc-future" data-date="2017-11-21"></td><td class="fc-day fc-widget-content fc-wed fc-future" data-date="2017-11-22"></td><td class="fc-day fc-widget-content fc-thu fc-future" data-date="2017-11-23"></td><td class="fc-day fc-widget-content fc-fri fc-future" data-date="2017-11-24"></td><td class="fc-day fc-widget-content fc-sat fc-future" data-date="2017-11-25"></td></tr></tbody></table></div><div class="fc-content-skeleton"><table><thead><tr><td class="fc-day-top fc-sun fc-future" data-date="2017-11-19"><span class="fc-day-number">19</span></td><td class="fc-day-top fc-mon fc-future" data-date="2017-11-20"><span class="fc-day-number">20</span></td><td class="fc-day-top fc-tue fc-future" data-date="2017-11-21"><span class="fc-day-number">21</span></td><td class="fc-day-top fc-wed fc-future" data-date="2017-11-22"><span class="fc-day-number">22</span></td><td class="fc-day-top fc-thu fc-future" data-date="2017-11-23"><span class="fc-day-number">23</span></td><td class="fc-day-top fc-fri fc-future" data-date="2017-11-24"><span class="fc-day-number">24</span></td><td class="fc-day-top fc-sat fc-future" data-date="2017-11-25"><span class="fc-day-number">25</span></td></tr></thead><tbody><tr><td></td><td></td><td></td><td></td><td></td><td></td><td></td></tr></tbody></table></div></div><div class="fc-row fc-week fc-widget-content" style="height: 107px;"><div class="fc-bg"><table class=""><tbody><tr><td class="fc-day fc-widget-content fc-sun fc-future" data-date="2017-11-26"></td><td class="fc-day fc-widget-content fc-mon fc-future" data-date="2017-11-27"></td><td class="fc-day fc-widget-content fc-tue fc-future" data-date="2017-11-28"></td><td class="fc-day fc-widget-content fc-wed fc-future" data-date="2017-11-29"></td><td class="fc-day fc-widget-content fc-thu fc-future" data-date="2017-11-30"></td><td class="fc-day fc-widget-content fc-fri fc-other-month fc-future" data-date="2017-12-01"></td><td class="fc-day fc-widget-content fc-sat fc-other-month fc-future" data-date="2017-12-02"></td></tr></tbody></table></div><div class="fc-content-skeleton"><table><thead><tr><td class="fc-day-top fc-sun fc-future" data-date="2017-11-26"><span class="fc-day-number">26</span></td><td class="fc-day-top fc-mon fc-future" data-date="2017-11-27"><span class="fc-day-number">27</span></td><td class="fc-day-top fc-tue fc-future" data-date="2017-11-28"><span class="fc-day-number">28</span></td><td class="fc-day-top fc-wed fc-future" data-date="2017-11-29"><span class="fc-day-number">29</span></td><td class="fc-day-top fc-thu fc-future" data-date="2017-11-30"><span class="fc-day-number">30</span></td><td class="fc-day-top fc-fri fc-other-month fc-future" data-date="2017-12-01"><span class="fc-day-number">1</span></td><td class="fc-day-top fc-sat fc-other-month fc-future" data-date="2017-12-02"><span class="fc-day-number">2</span></td></tr></thead><tbody><tr><td></td><td></td><td></td><td></td><td></td><td></td><td></td></tr></tbody></table></div></div><div class="fc-row fc-week fc-widget-content" style="height: 112px;"><div class="fc-bg"><table class=""><tbody><tr><td class="fc-day fc-widget-content fc-sun fc-other-month fc-future" data-date="2017-12-03"></td><td class="fc-day fc-widget-content fc-mon fc-other-month fc-future" data-date="2017-12-04"></td><td class="fc-day fc-widget-content fc-tue fc-other-month fc-future" data-date="2017-12-05"></td><td class="fc-day fc-widget-content fc-wed fc-other-month fc-future" data-date="2017-12-06"></td><td class="fc-day fc-widget-content fc-thu fc-other-month fc-future" data-date="2017-12-07"></td><td class="fc-day fc-widget-content fc-fri fc-other-month fc-future" data-date="2017-12-08"></td><td class="fc-day fc-widget-content fc-sat fc-other-month fc-future" data-date="2017-12-09"></td></tr></tbody></table></div><div class="fc-content-skeleton"><table><thead><tr><td class="fc-day-top fc-sun fc-other-month fc-future" data-date="2017-12-03"><span class="fc-day-number">3</span></td><td class="fc-day-top fc-mon fc-other-month fc-future" data-date="2017-12-04"><span class="fc-day-number">4</span></td><td class="fc-day-top fc-tue fc-other-month fc-future" data-date="2017-12-05"><span class="fc-day-number">5</span></td><td class="fc-day-top fc-wed fc-other-month fc-future" data-date="2017-12-06"><span class="fc-day-number">6</span></td><td class="fc-day-top fc-thu fc-other-month fc-future" data-date="2017-12-07"><span class="fc-day-number">7</span></td><td class="fc-day-top fc-fri fc-other-month fc-future" data-date="2017-12-08"><span class="fc-day-number">8</span></td><td class="fc-day-top fc-sat fc-other-month fc-future" data-date="2017-12-09"><span class="fc-day-number">9</span></td></tr></thead><tbody><tr><td></td><td></td><td></td><td></td><td></td><td></td><td></td></tr></tbody></table></div></div></div></div></td></tr></tbody></table></div></div></div> -->
		<div id="calendar"></div>
		<div style="clear:both"></div>
		
<!-- 		<div id="feedbackMessage">
			Dear Lousie Learner,
			<ul>
			<li>You are running late for your plan and did not complete lesson 1.4 in time</li>
			<li>You also did not yet plan lesson 1.5 onwards.</li>
			<li>Please replan your activities accordingly!</li>
			</ul>
		</div>   -->
<div class="confirm">
	<form id="planningForm" method="POST" action="StimulatedPlanningServlet">
		<input type="hidden" name="calenderItems" id="calenderItems" value="">
		<!-- button type="submit" id="ok" name="submit" value="OK" onclick="retrieveAllEvents();">Save</button -->
		<button type="submit" id="next" name="submit" value="Next" onclick="retrieveAllEvents();">Continue</button>
	</form>
</div>


</div>

</body>
</html>