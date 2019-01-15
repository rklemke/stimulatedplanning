<%@ page 
	language="java" 
	contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
	import="stimulatedplanning.*, stimulatedplanning.util.*, senseofcommunity.*, java.util.*, java.util.logging.Logger, javax.servlet.RequestDispatcher" %>
<%! static Logger log = Logger.getLogger("SelectionWidget_jsp"); %>
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
  
	ContentDescriptor contentDescriptor = null;
	String contentId = request.getParameter("contentId");
	String contentName = request.getParameter("contentName");
	String pageurl = request.getParameter("pageurl");
	if (contentId != null) {
		contentDescriptor = (ContentDescriptor)StimulatedPlanningFactory.getObject(contentId);
	} 
	if (contentDescriptor == null && pageurl != null) {
		contentDescriptor = course.getContentByUrl(pageurl);
	} 
	if (contentDescriptor == null) {
		contentDescriptor = (ContentDescriptor)session.getAttribute("contentDescriptor");
	}
	
	List<InformationObject> informationObjectList = (List<InformationObject>)session.getAttribute("informationObjectList");
	if (informationObjectList == null && contentDescriptor != null) {
		informationObjectList = contentDescriptor.getAllInformationObjectList();
	}

	int currentInformationObjectIdx = 0;
	Object idxS = session.getAttribute("currentInformationObjectIdx");
	if (idxS == null) {
		idxS = request.getParameter("currentInformationObjectIdx");
	}
	if (idxS != null) {
		currentInformationObjectIdx = Integer.valueOf(idxS.toString());
		session.setAttribute("currentInformationObjectIdx", currentInformationObjectIdx);
	}

    SelectionObject currentSelectionObject = (SelectionObject)session.getAttribute("currentInformationObject");
	if (currentSelectionObject == null && informationObjectList != null && informationObjectList.size() > currentInformationObjectIdx) {
		currentSelectionObject = (SelectionObject)informationObjectList.get(currentInformationObjectIdx);
	}


	String checkboxType = "radio";
	if (currentSelectionObject.isMultipleChoiceTest() || currentSelectionObject.isMultiVoting()) {
		checkboxType = "checkbox";
	}
	
	boolean isExpired = currentSelectionObject.isExpired();

	  String submitIndicator = request.getParameter("submitIndicator");
	  String submitForward = (String)request.getAttribute("selectionSubmitForward");

	  String logString = "contentDescriptor: "+(contentDescriptor==null?"null":contentDescriptor.getId());
	  logString += ", informationObjectList: "+(informationObjectList==null?"null":informationObjectList.size());
	  logString += ", contentId: "+contentId;
	  logString += ", currentInformationObjectIdx: "+currentInformationObjectIdx;
	  logString += ", submitIndicator: "+submitIndicator;
	  log.info(logString);
	  
		if (submitIndicator != null && submitForward == null) { // we handle a form submission
			String[] selectedOptionIds = request.getParameterValues("selectionRadio");
			for (SelectionOption option : currentSelectionObject.getOptionList()) {
				UserSelectedOption userOption = PersistentStore.readUserSelectionOption(user, currentSelectionObject, option);
				boolean foundId = false;
				if (selectedOptionIds != null) {
					for (String optionId: selectedOptionIds) {
						log.info("option: "+option.getId()+", optionId: "+optionId);
						if (option.getId().equals(optionId)) {
							foundId = true;
							break;
						}
					}
				}
				
				// checking and storing the selection
				if (!foundId && userOption != null) {
					PersistentStore.deleteGenericEntity(userOption);
				} else if (foundId && userOption == null) {
					userOption = StimulatedPlanningFactory.createUserSelectedOption(user, currentSelectionObject, option);
					try {
						PersistentStore.writeDescriptor(userOption);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
				// checking and storing effects for special purpose cases
				if (foundId && userOption != null && currentSelectionObject.isUserAvatarPurpose()) {
					user.setAvatarUrl(userOption.getSelectedOption().getUrl());
					log.info("user "+user.getName()+" gets avatar "+userOption.getSelectedOption().getUrl());
					try {
						PersistentStore.writeUser(user);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else if (foundId && userOption != null && currentSelectionObject.isUserIdentityPurpose()) {
					UserOnlineStatus status = user.getOnlineStatus();
					status.setTitle(userOption.getSelectedOption().getTitle());
					status.setDescription(userOption.getSelectedOption().getDescription());
					log.info("user "+user.getName()+" gets identity "+userOption.getSelectedOption().getTitle());
					try {
						PersistentStore.writeDescriptor(status);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else if (currentSelectionObject.isClanAvatarPurpose()) {
					if (user.isTreatmentGroup()) {
						Clan clan = user.getClan();
						SelectionOption preferredOption = currentSelectionObject.getClanPreferredOption(clan);
						if (preferredOption != null) {
							clan.setClanLogo(preferredOption.getUrl());
							log.info("clan "+clan.getTitle()+" gets icon "+preferredOption.getUrl());
							try {
								PersistentStore.writeDescriptor(clan);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				} else if (currentSelectionObject.isClanIdentityPurpose()) {
					if (user.isTreatmentGroup()) {
						Clan clan = user.getClan();
						SelectionOption preferredOption = currentSelectionObject.getClanPreferredOption(clan);
						if (preferredOption != null) {
							log.info("clan "+clan.getTitle()+" will be updated to "+preferredOption.getTitle());
							clan.setTitle(preferredOption.getTitle());
							clan.setDescription(preferredOption.getDescription());
							try {
								PersistentStore.writeDescriptor(clan);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
			RequestDispatcher rd = request.getRequestDispatcher("/GenericClanFrameServlet_SoC");
			request.setAttribute("selectionSubmitForward", "true");
			rd.forward(request, response);			
		}

%>


<!DOCTYPE html>
<html>
<head>
    <title>Selection Widget</title>
    <link rel="stylesheet" href="css/SelectionWidgetStyling.css">
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
  	    
  	  $("div#RowOne Input[type=<%= checkboxType %>]" ).checkboxradio({
        icon: false
  	    });
  	  
  	  if(<%=user.isTreatmentGroup()%> && <%=currentSelectionObject.isClan()%>){
  		  $("div#RowOne form label").css({
  			"display":"block",
  			"text-align":"left",
  			"vertical-align": "middle"
  		  });
  	  }
	});
	</script>

</head>

	<body>
	
	<div class="container">
	
	<div class="row" id="RowOne">
	
	<fieldset>
	<legend> <%= currentSelectionObject.getTitle() %> </legend>
	<form id="selectionForm" method="POST" action="InformationObjectServlet_SoC">
		<input type="hidden" id="submitIndicator" name="submitIndicator" value="true">
		<input type="hidden" id="userid" name="userid" value="<%= user.getId() %>"></input>
		<input type="hidden" id="userName" name="userName" value="<%= user.getName() %>"></input>
		<input type="hidden" id="contentId" name="contentId" value="<%= contentId %>"></input>
		<input type="hidden" id="currentInformationObjectIdx" name="currentInformationObjectIdx" value="<%= currentInformationObjectIdx %>"></input>
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
    	<img style="width:1.5em; height:1.5em;" src="<%= option.getUrl() %>" >
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
		 %> 
	<% } %>
	</form>
	</fieldset><!--  -->
	</div><!-- rowOne -->
	
	</div><!--container-->
	
	</body>

</html>
