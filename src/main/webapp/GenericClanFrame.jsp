<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="stimulatedplanning.*, stimulatedplanning.util.*, senseofcommunity.*, java.util.*" %>
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
  InformationObject currentInformationObject = (InformationObject)session.getAttribute("currentInformationObject");
  int currentInformationObjectIdx = 0;
  Object idxS = (Object)session.getAttribute("currentInformationObjectIdx");
  if (idxS != null) {
	  currentInformationObjectIdx = ((Integer)idxS).intValue();
  }
  
  
%>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Test User Navigation</title>
    <link rel="stylesheet" href="css/IdentityWidgetStyling.css">
  <link rel="stylesheet" href="https://code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
  <link rel="stylesheet" href="https://jqueryui.com/resources/demos/style.css">
  <script src="https://code.jquery.com/jquery-1.12.4.js"></script>
  <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
    <script type="text/javascript">
	    $(document).ready(function () {
		//this is temp and the frameholder src should be replaced by the active link
		$("#frameHolder").attr("src","InformationObjectServlet_SoC");
		
		function user (name, status, avatarURL, altText) {
		this.name=name;
		this.GetName = function() {
        return this.name;
		};
		this.status=status;
		this.GetStatus = function() {
        return this.status;
		};
		this.avatarURL=avatarURL;
		this.GetAvatarURL = function() {
        return this.avatarURL;
		};
		this.altText =  altText;
		this.getAltText = function(){
			return this.altText;
		};
		}

		var users = [
		new user("A", "inCurrentPage", "server/images/profileImage.png", "Apple"),
		new user("B", "inCurrentPage", "server/images/profileImage.png", "Ball"),
		new user("C", "inOtherPage", "server/images/avatar.png", "Cat"),
		new user("D", "inCurrentPage", "server/images/profileImage.png", "Dog"),
		new user("E", "inCurrentPage", "server/images/profileImage.png", "Elephant"),
		new user("F", "Offline", "server/images/profileImage.png", "Fan"),
		new user("G", "inCurrentPage", "server/images/profileImage.png", "Gun"),
		new user("H", "inOtherPage", "server/images/profileImage.png", "Horse"),
		new user("I", "inCurrentPage", "server/images/profileImage.png", "India"),
		new user("J", "inCurrentPage", "server/images/profileImage.png", "Jelly"),
		new user("K", "Offline", "server/images/profileImage.png", "Kathmandu"),
		new user("L", "inCurrentPage", "server/images/profileImage.png", "Lavender")];

        $(".links").click(function () {
                alert("Chat page");
            });
			
        $(document).tooltip();
		
		$.each(users, function (index, value) {
		//console.log(value.GetName());
		var color;
		if (value.GetStatus() == "inCurrentPage") {
		color = 'green';
		} 
		else if (value.GetStatus() == "inOtherPage") {
		color = 'blue';
		} 
		else {
		color = 'grey';
		}
		loadImage(value.GetAvatarURL(), color,"#AW_myClanFrame", value.getAltText());
		});
		
		function loadImage(path, color, target, altText) {
		var userDP = '<div class="links" ' + 'style= "border-color: '+ color+';'+'background-color: '+ color +'"' +'>' +
		'<a href="#" '+'title= "'+ altText +'">'+
		'<img src="'+ path +'"'+' width="25px" height="25px" '+'">' +
		'</a>' +
		'</div>';
		console.log(userDP);
		$(userDP).appendTo(target);
		};
		})
	</script>
</head>
<body>
	<div class="container">
	
	<div class = "columnOne"  >
	<img id="ClanLogo"  src="server/images/ClanDefault.png"/>
	
	<div  id="AW_myClanFrame" >
	
	</div><!--columnOne-->

	</div><!--ClanLogo-->
	
	<div class="columnTwo">
	<iframe id="frameHolder"></iframe>
	</div><!--columnTwo-->
	<div class="columnTwo">
	<% if (informationObjectList != null) { %>
	<%    if (currentInformationObjectIdx > 0) { %>
	[PREV]
	<%    } %>
	<%    if (currentInformationObject instanceof SelectionObject) { %>
	[VOTE]
	<%    } else if (currentInformationObjectIdx < informationObjectList.size()-1) { %>
	[NEXT]
	<%    } %>
	<% } %>
	</div><!--columnTwo-->

	</div><!--container-->
</body>
</html>