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
<title>Thank you</title>
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
<form id="thankYouForm" method="POST" action="ThankYouServlet">
	<h2>Thank you for your participation</h2>
	<p>You are now well prepared to follow your course!</p>
	<div class="ui-widget ui-widget-content ui-wrapper">
		<p>Maybe a nice picture here?</p>
	</div>
	<div class="confirm">
		<button type="submit" id="next" name="submit" value="Next" >Next</button>
	</div>
	
</form>
</div>

</body>
</html>