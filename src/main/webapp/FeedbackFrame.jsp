<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="stimulatedplanning.*, stimulatedplanning.util.*, java.util.*" %>
<%
  session = StimulatedPlanningFactory.initializeSession(request, response);

  User user = (User)session.getAttribute("user");
  CourseDescriptor course = (CourseDescriptor)session.getAttribute("course");
  UserPlan userPlan = (UserPlan)session.getAttribute("userPlan");
  
  userPlan.calculateAchievementRates();

  HashMap<String, String> completionStatusMap = userPlan.getCompletionStatusMap();
  
  
  
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

  <link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
  <script src="https://code.jquery.com/jquery-1.12.4.js"></script>
  <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
  <script>
  $( function() {
    $( "#progressbar1" ).progressbar({
      value: <%= completionStatusMap.get("calc.userPlannedTotalRate") %>
    });
    $( "#progressbar2" ).progressbar({
        value: <%= completionStatusMap.get("calc.userPlanAchievementRate") %>
      });
    $( "#progressbar3" ).progressbar({
        value: <%= completionStatusMap.get("calc.userPlanDelayedRate") %>
      });
  } );
  </script>


<% if (user.isTreatmentGroup()) { %>
	<table>
		<tr> <!-- Calendar row -->
			<td></td>
			<td><div id="progressbar1"></div><%= completionStatusMap.get("calc.userPlannedTotalRate") %> %</td>
			<td>You have planned <%= completionStatusMap.get("raw.plannedItemsTotal") %> of <%= completionStatusMap.get("raw.plannableItems") %> activities of your course.</td>
		</tr>
		<tr> <!-- Items completed row -->
			<td></td>
			<td><div id="progressbar2"></div><%= completionStatusMap.get("calc.userPlanAchievementRate") %> %</td>
			<td>You have completed <%= completionStatusMap.get("raw.planAchievementCompleted") %> of your <%= completionStatusMap.get("raw.plannableItems") %> course activities.</td>
		</tr>
		<tr> <!-- Items delayed row -->
			<td></td>
			<td><div id="progressbar3"></div><%= completionStatusMap.get("calc.userPlanDelayedRate") %> %</td>
			<td>You have not yet completed <%= completionStatusMap.get("raw.planAchievementDelayed") %> activities at their planned time. You might consider re-planning these.</td>
		</tr>
	</table>
<% } %>

<div id="ajaxresult">Loading data ...</div>

</body>
</html>