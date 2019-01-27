<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="stimulatedplanning.*, stimulatedplanning.util.*, senseofcommunity.*, java.util.*" %>
<%
  session = StimulatedPlanningFactory.initializeSession(request, response);
  User user = (User)session.getAttribute("user");
    
%>
    <script type="text/javascript">
    
	    $(document).ready(function () {
    	    $(document).tooltip({
    	    	content: function() {
    	    		return $(this).prop('title');
    	    	}
    	    });
			setTimeout(clan_tickerRequest, clan_timeoutInterval);
		});

		var clan_timeoutInterval = 30000;
		
		function clan_tickerRequest() {
			$.ajax({
				url: '<%= StimulatedPlanningFactory.applicationHome %>/ClanMembersPart.jsp?userid=<%= user.getId() %>&userName=<%= user.getName() %>',
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
	<div id="AW_myClanFrame" style="width:100%;">
		<jsp:include page="ClanMembersPart.jsp" />
	</div>
