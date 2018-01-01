<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Ticker Frame</title>
  <link rel="stylesheet" href="https://code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
  <link rel="stylesheet" href="https://jqueryui.com/resources/demos/style.css">
  <link rel="stylesheet" href="../tablesorter/themes/blue/style.css">

<script src="https://code.jquery.com/jquery-1.12.4.js"></script>
<script src="../tablesorter/jquery.tablesorter.min.js"></script>

<script type="text/javascript">
	var timeoutInterval = 30000;
	var userNameDefault = "Guest";
	var userIdDefault = "unknown";
	var pageUrlDefault = window.parent.location.href;
	var plannerFrameLoaded = false;
	var feedbackFrameLoaded = false;

	function getUserName() {
	  var tmpName = $('.label-username', window.parent.document).html();
	  if (tmpName == null) {
	    tmpName = userNameDefault;
	  }
	  return tmpName;
	}

	function getUserId() {
	  return getUserName();
	}

	function getPageUrl() {
		var tmpId = window.parent.location.href;
		if (tmpId == null) {
		  tmpId = pageUrlDefault;
		}
		return tmpId;
	}
	
	function tickerRequest() {
		  var userName = getUserName();
		  var userId = getUserId(); 
		  var pageUrl = getPageUrl(); 
		  $.ajax({
		    dataType: 'jsonp',
		    url: '/DataTrackerServlet',
		    method: 'POST',
		    data: {
			userName: userName,
			userid: userId,
			page: pageUrl
		    }, 
		    success: function(data) {
		    	if ( plannerFrameLoaded == false && $( "#stimulatedPlanningFrame", window.parent.document).length ) {
		    		$( "#stimulatedPlanningFrame", window.parent.document).attr("src", "/GoalSettingServlet");
		    		plannerFrameLoaded = true;
		    	}
		    	if ( feedbackFrameLoaded == false && $( "#feedbackFrame", window.parent.document).length ) {
		    		$( "#feedbackFrame", window.parent.document).attr("src", "/FeedbackFrame.jsp");
		    		feedbackFrameLoaded = true;
		    	}
		    	
		    	if (!plannerFrameLoaded && !feedbackFrameLoaded) {
					var datatxt = "<div width='48%' style='float:left;'>";
					   datatxt += "<table id='feedbacktable' class='tablesorter'>";
					   datatxt += "<thead><tr><th>Tracked attribute</th><th>Tracked value</th></tr></thead>";
					$.each(data,function(key,val){
					 if (key.startsWith("calc")) {
					  val += " %";
					  datatxt += "<tr><td align='top'>" + key + "</td><td align='top'>" + val + "</td></tr>";
					 }
					});
					datatxt += "</table>";
					datatxt += "</div><div width='48%' style='float:left; margin-left: 20px;'>";
					datatxt += "<table id='feedbacktable2' class='tablesorter'>";
					datatxt += "<thead><tr><th>Raw attribute</th><th>Raw value</th></tr></thead>";
					$.each(data,function(key,val){
					 if (!key.startsWith("calc")) {
					  datatxt += "<tr><td>" + key + "</td><td>" + val + "</td></tr>";
					 }
					});
					datatxt += "</table>";
					datatxt += "</div>";
					if (!feedbackFrameLoaded) {
						$( '#ajaxresult' ).html(datatxt);
						$( '#feedbacktable' ).tablesorter( {sortList: [[0,0], [1,0]]} );
						$( '#feedbacktable2' ).tablesorter( {sortList: [[0,0], [1,0]]} );
					} else {
						var feedbackframe = $( "#feedbackFrame", window.parent.document); // or some other selector to get the iframe
						feedbackframe.contents().find('#ajaxresult').html(datatxt);
						feedbackframe.contents().find( '#feedbacktable' ).tablesorter( {sortList: [[0,0], [1,0]]} );
						feedbackframe.contents().find( '#feedbacktable2' ).tablesorter( {sortList: [[0,0], [1,0]]} );
					}
		    	} else {
					$( '#ajaxresult' ).html("Connected.");
		    	}
		    },
		    complete: function() {
		      // Schedule the next request when the current one's complete
		      // but only, when we are not in planning mode
		      if (!planningFrameLoaded) {
			      timeoutInterval += timeoutInterval; // get slower, when user just stays on page.
			      setTimeout(tickerRequest, timeoutInterval);
		      }
		    }
		  });
		}

		$('#trackerFrame').ready(function() {
		  // run the first time; all subsequent calls will take care of themselves
		  setTimeout(tickerRequest, 20);
		});

</script>
</head>
<body>



<div id="ajaxresult">Loading data ...</div>


</body>
</html>