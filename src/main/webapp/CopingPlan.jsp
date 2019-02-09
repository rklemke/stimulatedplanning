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
  <script>

  	var icons = {
      header: "iconClosed",
      activeHeader: "iconOpen"
    };
    
    $( function() {

    $( document).tooltip({
    	track: true
    });
    $( "button" ).button();
    $( 'input[type="button"]' ).button();
    $( 'input[type="button"]' ).click(function(e) {
    	addCopingPlan();
	});
	$( ".exampleItem" ).click(function(e) {
		selectExample(this.id);
	});
	updatePlanForm();

  } );
  

	function guid() {
	  function s4() {
		return Math.floor((1 + Math.random()) * 0x10000)
		  .toString(16)
		  .substring(1);
	  }
	  return s4() + s4() + '-' + s4() + '-' + s4() + '-' + s4() + '-' + s4() + s4() + s4();
	}

	function updatePlanForm() {
		$("#plan_table").val($( "#copingPlan tbody" ).html());
		//alert($("#plan_table").val());
	}
	
    function addCopingPlan() {
      var valid = (($("#plan_if").val().length > 0) && ($("#plan_then").val().length > 0));
 	  var uuid = guid();
 	  var editCall = 'selectAndRemoveExample( "'+uuid+'" );';
 	  var removeCall = 'removeExample( "'+uuid+'" );'; //'$( "#'+uuid+' " ).remove()';
 	  
      if ( valid ) {
        $( "#copingPlan tbody" ).append( "<tr id='"+uuid+"'>" +
          "<td id='"+uuid+"-if'>" + $("#plan_if").val() + "</td>" +
          "<td id='"+uuid+"-then'>" + $("#plan_then").val() + "</td>" +
          "<td>" + 
          	"<img src='/img/pencil.png' onclick='"+editCall+"' title='Edit this line.' />" + 
          	"<img src='/img/cross-mark-on-a-black-circle-background.png' onclick='"+removeCall+"' title='Remove this line.' />" + 
          "</td>" +
        "</tr>" );
        $( "#completeCopingPlan" ).val($( "#copingPlan tbody" ).html());
        $("#plan_if").val("");
        $("#plan_then").val("");
        
        updatePlanForm();
        
      }
      return valid;
    }
    
    
    function removeExample(id) {
    	$("#"+id).remove();
        updatePlanForm();
    }
  
    function selectAndRemoveExample(id) {
    	$("#plan_if").val($("#"+id+"-if").html());
    	$("#plan_then").val($("#"+id+"-then").html());
    	$("#"+id).remove();
        updatePlanForm();
    }
  
    function selectExample(id) {
    	$("#plan_if").val($("#"+id+"-if").html());
    	$("#plan_then").val($("#"+id+"-then").html());
        updatePlanForm();
    }
  
  function checkAll(t, ...ids) {
	  for (var id of ids) {
		  $(id).prop('checked', t);
		  if (id.startsWith('.')) {
			  $(id).checkboxradio('refresh');
		  }
	  }
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
  .ui-frame { width: 65em; }
	.confirm {
		float: right;
	}
  .ui-wrapper { overflow: auto;}
  .ui-tooltip {
    border: 1px solid white;
    background: #111;
    color: white;
  }
  

	#wrap {
		width: 1040px;
		margin: 0 auto;
	}

	.ui-button:active, .ui-button.ui-state-active:hover {
		background: #ff9800;
	}

	.exampleItem:hover {
		text-decoration: underline;	
		cursor: pointer;
	}

	textarea {
	  resize: vertical; /* user can resize vertically, but width is fixed */
	}
  </style>
</head>
<body>
<div id="wrap" class="ui-frame"> 
<form id="copingPlanForm" method="POST" action="CopingPlanServlet">
	<input type="hidden" name="userName" id="userName" value="<%= user.getName() %>">
	<input type="hidden" name="userid" id="userid" value="<%= user.getId() %>">

	<h2>Make your Plan B</h2>
	<h3>Think about any inconvenience that could undermine your plan of completing the activities you selected within this course.</h3>
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
				<li>You selected the following activities:</li><ul> <% 
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
				} %><li>You can dedicate to this course <b><%= userPlan.getPlannedTimePerWeekAsInt() %> hours</b> per week.</li>
			</ul><%	} %>
			</div>
			<div>
			  <table id="copingPlan" class="ui-widget ui-widget-content">
				<thead>
				  <tr class="ui-widget-header ">
					<th title="IF I meet one of the following situations - click on the examples on the right for inspiration."><b>IF</b></th>
					<th title="THEN I will perform the following action - click on the examples on the right for inspiration."><b>THEN</b></th>
					<th></th>
				  </tr>
				</thead>
				<tbody>
					<%= userPlan.getCopingPlan() %>
				</tbody>
				<thead>
				  <tr>
					<td><textarea style="width: 250px; height: 100px;" name="plan_if" id="plan_if"  title="Enter a situation or click on an example on the right." ></textarea></td>
					<td><textarea style="width: 250px; height: 100px;" name="plan_then" id="plan_then"  title="Enter an action or click on an example on the right." ></textarea></td>
					<td><input type="button" id="addCopingPlan" name="addCopingPlan" value="Add" title="Add the IF and THEN plan to the list." /></td>
				  </tr>
				</thead>
			  </table><input type="hidden" name="plan_table" id="plan_table" value=""> 
			</div>
		</div>
		<div style="float: left; width: 360px; margin-left: 10px; margin-top: 10px;">
			<label title="Click on the examples to reuse them."><b>Examples</b></label>
			<ul id="exampleList">
				<li class="ui-widget"><span class="exampleItem" id="example-1" title="Click on the examples to reuse them."><b>IF</b> <span id="example-1-if">I realise that I need more time for an activity</span> <br>
				<b>THEN</b> <span id="example-1-then">I'll not plan any activities immediately after the other to take all the time I need.</span></span></li>
				<li><span class="exampleItem" id="example-2" title="Click on the examples to reuse them."><b>IF</b> <span id="example-2-if">the Internet connection will not work properly at home</span> <br>
				<b>THEN</b> <span id="example-2-then">I'll use the mobile data of my telephone to follow the MOOC.</span></span></li>
				<li><span class="exampleItem" id="example-3" title="Click on the examples to reuse them."><b>IF</b> <span id="example-3-if">the location I chose  (i.e. a café) is too busy to find concentration</span> <br>
				<b>THEN</b> <span id="example-3-then">I'll change it (by going home).</span></span></li>
				<li><span class="exampleItem" id="example-4" title="Click on the examples to reuse them."><b>IF</b> <span id="example-4-if">I come later home from work and I miss my planned course activity</span><br>
				<b>THEN</b> <span id="example-4-then">I will re-plan my course activity to the next possible day.</span></span></li>
				<li><span class="exampleItem" id="example-5" title="Click on the examples to reuse them."><b>IF</b> <span id="example-5-if">I am too tired to follow another course activity on the same day</span><br>
				<b>THEN</b> <span id="example-5-then">I will re-plan my course activities to have less activities on the same day.</span></span></li>
			</ul> 
		</div>
	</div>
	<div class="confirm">
		<!-- button type="submit" id="ok" name="submit" value="OK" >Save</button -->
		<button type="submit" id="next" name="submit" value="Next" title="Click continue to go to the next page. Your selection will be saved automatically.">Continue</button>
	</div>
	
</form>
</div>

</body>
</html>