<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="stimulatedplanning.*, stimulatedplanning.util.*, java.util.*" %>
<%
  session = StimulatedPlanningFactory.initializeSession(request, response);

  User user = (User)session.getAttribute("user");
  CourseDescriptor course = (CourseDescriptor)session.getAttribute("course");
  UserPlan userPlan = (UserPlan)session.getAttribute("userPlan");
  
  //userPlan.calculateAchievementRates();

  HashMap<String, String> completionStatusMap = userPlan.getCompletionStatusMap();
%>
  <link rel="stylesheet" href="https://code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
  <style>
  .ui-progressbar {
    position: relative;
  }
  .progress-label {
    position: absolute;
    left: 50%;
    top: 4px;
    font-weight: bold;
    text-shadow: 1px 1px 0 #fff;
  }
  #progressbar1 > div.ui-progressbar-value {
    background: #afafaf;
  }
  #progressbar2 > div.ui-progressbar-value {
    background: #199c21;
  }
  #progressbar3 > div.ui-progressbar-value {
    background: #cd0000;
  }
  #progressbar4 > div.ui-progressbar-value {
    background: #3a87ad;
  }
  </style>
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
    $( "#progressbar4" ).progressbar({
        value: <%= completionStatusMap.get("calc.userPlanDelayedRate") %>
      });
  } );
  </script>


<% if (user.isTreatmentGroup()) { %>
	<table>
		<tr> <!-- Calendar row -->
			<td>
				<a href="https://ou.edia.nl/courses/course-v1:OUNL+ICS18+2018_1/courseware/c440e614880f44cab61666f5783994c3/9c1b1985a20e4289b02f5e95c61e4485/?activate_block_id=block-v1%3AOUNL%2BICS18%2B2018_1%2Btype%40sequential%2Bblock%409c1b1985a20e4289b02f5e95c61e4485">
					<img src="/img/calendar.png" height="32">
				</a>
			</td>
			<td width="100"><div id="progressbar1"><div class="progress-label"><%= completionStatusMap.get("calc.userPlannedTotalRate") %> %</div></div></td>
			<td>You intend to complete <%= completionStatusMap.get("raw.plannableItems") %> activities of this course. You have planned <%= completionStatusMap.get("raw.plannedItemsTotal") %> of them.</td>
		</tr>
		<tr> <!-- Items completed row -->
			<td>
				<a href="https://ou.edia.nl/courses/course-v1:OUNL+ICS18+2018_1/courseware/c440e614880f44cab61666f5783994c3/9c1b1985a20e4289b02f5e95c61e4485/?activate_block_id=block-v1%3AOUNL%2BICS18%2B2018_1%2Btype%40sequential%2Bblock%409c1b1985a20e4289b02f5e95c61e4485">
					<img src="/img/onTime_image.png" height="32">
				</a>
			</td>
			<td><div id="progressbar2"><div class="progress-label"><%= completionStatusMap.get("calc.userPlanAchievementRate") %> %</div></div></td>
			<td>You have completed <%= completionStatusMap.get("raw.planAchievementCompleted") %> of your <%= completionStatusMap.get("raw.plannableItems") %> intended course activities.</td>
		</tr>
		<tr> <!-- Items delayed row -->
			<td>
				<a href="https://ou.edia.nl/courses/course-v1:OUNL+ICS18+2018_1/courseware/c440e614880f44cab61666f5783994c3/9c1b1985a20e4289b02f5e95c61e4485/?activate_block_id=block-v1%3AOUNL%2BICS18%2B2018_1%2Btype%40sequential%2Bblock%409c1b1985a20e4289b02f5e95c61e4485">
					<img src="/img/DelayImage.png" height="32">
				</a>
			</td>
			<td><div id="progressbar3"><div class="progress-label"><%= completionStatusMap.get("calc.userPlanDelayedRate") %> %</div></div></td>
			<td>You have not yet completed <%= completionStatusMap.get("raw.planAchievementDelayed") %> of your <%= completionStatusMap.get("raw.plannedItemsTotal") %> planned activities at their planned time.</td>
		</tr>
		<tr> <!-- Items delayed row -->
			<td>
				<a href="https://ou.edia.nl/courses/course-v1:OUNL+ICS18+2018_1/courseware/c440e614880f44cab61666f5783994c3/9c1b1985a20e4289b02f5e95c61e4485/?activate_block_id=block-v1%3AOUNL%2BICS18%2B2018_1%2Btype%40sequential%2Bblock%409c1b1985a20e4289b02f5e95c61e4485">
					<img src="/img/planAgainImage.png" height="32">
				</a>
			</td>
			<td><div id="progressbar4"><div class="progress-label"><%= completionStatusMap.get("calc.userPlanNotPlannedRate") %> %</div></div></td>
			<td>You have not yet planned <%= completionStatusMap.get("calc.userPlanNotPlanned") %> of your <%= completionStatusMap.get("raw.plannableItems") %> intended activities at their planned time. You can re-plan to improve plan accuracy.</td>
		</tr>
	</table>
<% } %>
