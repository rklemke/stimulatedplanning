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
  
  
%>


<!DOCTYPE html>
<html>
<head>
    <title>Awareness Widget</title>
    <link rel="stylesheet" href="css/WidgetStyling.css">
    <link rel="stylesheet" href="css/jquery-ui.css">
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
    <script type="text/javascript">
        //when the document has finished loading. "$" sign calls the jquery
        $(document).ready(function () {
		
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
        $("#online").progressbar({
            value: <%= 100 * otherClanOnline / otherClanSize %>
        });
        $("#recent").progressbar({
            value: <%= 100 * otherClanRecent / otherClanSize %>
        });
        $("#offline").progressbar({
            value: <%= 100 * otherClanOffline / otherClanSize %>
        });
        $(document).tooltip();
		
		$.each(users, function (index, value) {
		//console.log(value.GetName());
		var color;
		if (value.GetStatus() == "inCurrentPage") {
		color = 'green';
		} 
		else if (value.GetStatus() == "inOtherPage") {
		color = 'red';
		} 
		else {
		color = 'grey';
		}
		//var userDP = '<div class="links" ' + 'style="border-color' + color.toString()+'"'+ '>' + value.GetName() + '</div>';
		loadImage(value.GetAvatarURL(), color,"#AW_myClanFrame", value.getAltText());
		//$(userDP).appendTo($("#AW_myClanFrame"));
		});
		
		function loadImage(path, color, target, altText) {
		var userDP = '<div class="links" ' + 'style= "border-color: '+ color+';'+'background-color: '+ color +'"' +'>' +
		'<a href="#" '+'title= "'+ altText +'">'+
		'<img src="'+ path +'"'+' width=25 height =25'+'">' +
		'</a>' +
		'</div>';
		console.log(userDP);
		$(userDP).appendTo(target);
		};
})
    </script>

</head>

<body>

<% if (user.isTreatmentGroup()) { %>
    <div class="container">
	<div style="margin: 0.5%"><strong>Awareness Widget</strong></div>
        <div class="column" id="AW_myClanFrame">
            <div style="margin: 0.5%"><strong>My Clan: <%= userClan.getTitle() + " " + userClan.userCount() %></strong></div>
            	<% for (UserOnlineStatus status: onlineUsers) { %>
            		<div class="links" style="background-color:lawngreen"><%= status.getUser().getName() %></div>
            	<% } %>
            	<% for (UserOnlineStatus status: recentUsers) { %>
            		<div class="links" style="background-color:red"><%= status.getUser().getName() %></div>
            	<% } %>
            	<% for (UserOnlineStatus status: offlineUsers) { %>
            		<div class="links" style="background-color:grey"><%= status.getUser().getName() %></div>
            	<% } %>

        </div>
        <div class="column" id="AW_otherClanFrame">
            <div><strong>Other Clan: <%= otherClan.getTitle() + " " + otherClan.userCount() %></strong></div>
            <div><Strong>Online members: </strong></div>
			<div id="online"></div>
            <div><Strong>Offline members: </strong></div>
			<div id="offline"></div>
        </div>
        <div class="column" id="AW_ledger">
		<div style="padding: 0.5%"><strong style="color: lawngreen">Online in your page</strong></div>
        <div style="padding: 0.5%"><strong style="color: red">Online in other pages</strong></div>
        <div style="padding: 0.5%"><strong style="color: dimgrey">Offline</strong></div>
        </div>

        <div class="column" id="AW_ajaxresult">
        </div>
    </div>
<% } %>

</body>

</html>
