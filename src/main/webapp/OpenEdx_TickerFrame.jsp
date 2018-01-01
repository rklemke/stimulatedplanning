<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="stimulatedplanning.*, stimulatedplanning.util.*, java.util.*" %>
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

</head>
<body>


<script type="text/javascript">
	$('#loadTracker').ready(function() {
		// run the first time; all subsequent calls will take care of themselves
		$('#loadTracker').load( "/mooc-integration/openedx_tracker.html" );
	});

</script>
<div id="loadTracker">Loading ...</div>




</body>
</html>