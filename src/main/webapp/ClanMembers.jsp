<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="stimulatedplanning.*, stimulatedplanning.util.*, senseofcommunity.*, java.util.*" %>
<%
  session = StimulatedPlanningFactory.initializeSession(request, response);
  User user = (User)session.getAttribute("user");
    
%>


<!DOCTYPE html>
<html>
<% if (user.isTreatmentGroup()) { %>
<head>
    <link rel="stylesheet" href="css/ClanMembersStyling.css">
    <link rel="stylesheet" href="css/jquery-ui.css">
    <script src="https://code.jquery.com/jquery-1.12.4.js"></script>
    <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
    <script type="text/javascript">
    
	    $(document).ready(function () {
    	    $(document).tooltip({
    	    	content: function() {
    	    		return $(this).prop('title');
    	    	}
    	    });
			setTimeout(clan_tickerRequest, clan_timeoutInterval);
		});

		var clan_timeoutInterval = 5000;
		
		function clan_tickerRequest() {
			$.ajax({
				url: '/ClanMembersPart.jsp?userid=<%= user.getId() %>&userName=<%= user.getName() %>',
				success: function(result) {
					$( '#AW_myClanFrame' ).html(result);
				},
				complete: function() {
					// Schedule the next request when the current one's complete
					setTimeout(clan_tickerRequest, clan_timeoutInterval);
				}
			});
		}
			
	</script>
</head>
<body>
	<div class="container" id="AW_myClanFrame">
		<jsp:include page="ClanMembersPart.jsp" />
	</div>
</body>
 <% } %>
</html>
