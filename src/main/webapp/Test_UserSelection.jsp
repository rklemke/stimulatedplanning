<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="stimulatedplanning.*, stimulatedplanning.util.*, java.util.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <%
  session = StimulatedPlanningFactory.clearSession(request, response);

    %>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Test User Selection</title>
  <link rel="stylesheet" href="https://code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
  <link rel="stylesheet" href="https://jqueryui.com/resources/demos/style.css">
  <script src="https://code.jquery.com/jquery-1.12.4.js"></script>
  <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
  <script>
  $( function() {
	    var userid = $( "#userid" );
	    var userName = $( "#userName" );
 
    $( "#userSelect" ).selectmenu({
      change: function( event, data ) {
        userid.val(data.item.value);
        userName.val($( "#userSelect option:selected" ).text());
      }
     });
 
  } );
  </script>
</head>
<body>
<H2>Select a user to test with.</H2>
<form id="goalSelectForm" method="POST" action="Test_UserSelectionServlet">
<label for="userSelect">Select a user:</label>
<select name="userSelect" id="userSelect">
  <option value="unknown">Guest</option>
<optgroup label="Alessandra's Test Users">
  <option value="a1">Luke-1</option>
  <option value="a2">Luke-2</option>
  <option value="a3">Luke-3</option>
  <option value="a4">Luke-4</option>
  <option value="a5">Luke-5</option>
</optgroup>
<optgroup label="Roland's Test Users">
  <option value="r1">Darth-1</option>
  <option value="r2">Darth-2</option>
  <option value="r3">Darth-3</option>
  <option value="r4">Darth-4</option>
  <option value="r5">Darth-5</option>
</optgroup>
</select>
<input type="hidden" value="unknown" id="userid" name="userid">
<input type="hidden" value="Guest" id="userName" name="userName">
<input type="submit" id="submitIntention" name="submitIntention" value="Intention"></input>
<input type="submit" id="submitPlanning" name="submitPlanning" value="Planning"></input>
<input type="submit" id="submitPlanning" name="submitCoping" value="Coping Plan"></input>
<input type="submit" id="submitLearning" name="submitLearning" value="Learning"></input>
</form>
</body>
</html>