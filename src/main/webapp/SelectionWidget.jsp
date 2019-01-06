<%@ page 
	language="java" 
	contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
	import="stimulatedplanning.*, stimulatedplanning.util.*, senseofcommunity.*, java.util.*" %>
<%
  session = StimulatedPlanningFactory.initializeSession(request, response);

  User user = (User)session.getAttribute("user");
  UserOnlineStatus userStatus = user.getOnlineStatus();
  CourseDescriptor course = (CourseDescriptor)session.getAttribute("course");
  UserPlan userPlan = (UserPlan)session.getAttribute("userPlan");
  Clan userClan = null;
  Clan otherClan = null;
  int otherClanSize = 1;
  int otherClanOnline = 0;
  int otherClanRecent = 0;
  int otherClanOffline = 0;
  
  String currentUserId = "";
  
  HashArrayList<UserOnlineStatus> onlineUsers = new HashArrayList<>();
  HashArrayList<UserOnlineStatus> recentUsers = new HashArrayList<>();
  HashArrayList<UserOnlineStatus> offlineUsers = new HashArrayList<>();
  if (user.isTreatmentGroup()) {
	  userClan = user.getClan();
	  otherClan = StimulatedPlanningFactory.getOtherClan(userClan);
	  onlineUsers = userClan.getOnlineUsersSorted(userStatus);
	  recentUsers = userClan.getRecentUsersSorted(userStatus);
	  offlineUsers = userClan.getOfflineUsersSorted(userStatus);
	  otherClanSize = otherClan.userCount()+1;
	  otherClanOnline = otherClan.getOnlineUsers().size()+1;
	  otherClanRecent = otherClan.getRecentUsers().size()+1;
	  otherClanOffline = otherClan.getOfflineUsers().size()+1;
  }
  
  ContentDescriptor contentDescriptor = (ContentDescriptor)session.getAttribute("contentDescriptor");
  List<InformationObject> informationObjectList = (List<InformationObject>)session.getAttribute("informationObjectList");
  SelectionObject currentSelectionObject = (SelectionObject)session.getAttribute("currentInformationObject");
  int currentInformationObjectIdx = 0;
  Object idxS = (Object)session.getAttribute("currentInformationObjectIdx");
  if (idxS != null) {
	  currentInformationObjectIdx = ((Integer)idxS).intValue();
  }

	String checkboxType = "radio";
	if (currentSelectionObject.isMultipleChoiceTest() || currentSelectionObject.isMultiVoting()) {
		checkboxType = "checkbox";
	}
	
	boolean isExpired = currentSelectionObject.isExpired();

%>


<!DOCTYPE html>
<html>
<head>
    <title>Selection Widget</title>
    <link rel="stylesheet" href="css/SelectionWidgetStyling.css">
    <link rel="stylesheet" href="css/ClanMembersStyling.css">
    <!--  link rel="stylesheet" href="https://code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css" -->
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
		$(".selectionOption").checkboxRadio();
	});

	</script>

</head>

	<body>
	
	<div class="container">
	
	<div style="display: inline-block; text-align: center; width: 98%; height: 10%">
	<strong style=" font-size: 24px"><%= currentSelectionObject.getTitle() %></strong>
	</div>
	
	<div class="column">
	<form id="selectionForm" method="POST" action="InformationObjectServlet_SoC">
	<input type="hidden" id="submitIndicator" name="submitIndicator" value="true">
	<ol id="selectable">
	<% 
	List<SelectionOption> options = currentSelectionObject.getOptionList();
	if (currentSelectionObject.isClanIdentityPurpose()) {
		ArrayList<SelectionOption> temp = new ArrayList<>();
		if (userClan != null) {
			if (Clan.CLAN_1_ID.equals(userClan.getId())) {
				for (int i=0; i<options.size()/2; i++) {
					temp.add(options.get(i));
				}
				options = temp;
			} else if (Clan.CLAN_2_ID.equals(userClan.getId())) {
				for (int i=options.size()/2; i<options.size(); i++) {
					temp.add(options.get(i));
				}
				options = temp;
			}
		}
	}
	for (SelectionOption option : options) { 
		ArrayList<UserSelectedOption> selectedOptions = new ArrayList<>();
		UserSelectedOption userSelectedOption = PersistentStore.readUserSelectionOption(user, currentSelectionObject, option);
		int optionCount = 0;
		int clanSize = 1;
		boolean isSelected = (userSelectedOption!=null);
		if (user.isTreatmentGroup()) {
			selectedOptions = StimulatedPlanningFactory.readClanSelectionOption(user.getClan(), currentSelectionObject, option);
			optionCount = selectedOptions.size();
			clanSize = Math.max(clanSize, user.getClan().userCount());
		}
		
	%>
	  <li class="ui-widget-content" id="li-<%= option.getId() %>">
    	<input 
    		class="selectionOption" 
    		type="<%= checkboxType %>" 
    		name="selectionRadio" 
    		id="so-<%= option.getId() %>" 
    		value="<%= option.getId() %>" 
    		<% if (isSelected) { %>checked <% } %> 
    		<% if (isExpired) { %>disabled <% } %> 
    	>
    	<label 
    		for="so-<%= option.getId() %>"
			<% if (currentSelectionObject.isAvatarPurpose()) { %>
    		title="<img src='<%= option.getUrl() %>' width='75' height='75'>"
    	<% } else { %>
    		title="<%= option.getDescription() %>"
	  	<% } %>
	  	   ><% if (currentSelectionObject.isAvatarPurpose()) { %>
    	<img src="<%= option.getUrl() %>" width="25" height="25">
    	<% } else { %>
	  	<%= option.getTitle() %>
	  	<% } %></label><%
		if (user.isTreatmentGroup() && currentSelectionObject.isClan()) {
			int count = 0;
			for (UserSelectedOption selectedOption: selectedOptions) {
				count++;
				if (count <=5) {
		      		currentUserId = "user"+selectedOption.getUser().getId();
		      		session.setAttribute(currentUserId, selectedOption.getUser());
				%><jsp:include page="UserIconDisplay.jsp" >
    				<jsp:param name="userId" value="<%= currentUserId %>" />
			 	</jsp:include><%
				} else {
					%> ... [<%= selectedOptions.size() %>] <%
					break;
				}
	        } 
		 } 
		 %></li>
	<% } %>
	</ol>
	</form>
	
	</div><!--column-->
	
	</div><!--container-->
	
	</body>

</html>
