<%@ page
	language="java" 
	contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"
	import="java.text.DateFormat,chat.*, stimulatedplanning.*, stimulatedplanning.util.*, senseofcommunity.*, java.util.*"
%><%
	session = request.getSession();

	User user = (User)session.getAttribute("user");
	Message message = (Message)session.getAttribute("currentMessage");
	User chatter = message.getUser();
  
	String chatterName = message.getChatterName();
	String strmsg = message.getDisplayMessage();
	long time = message.getTimeStamp();
	Date date = new Date(time);
	String currentUserId = "";

	if (chatterName.equalsIgnoreCase((String)session.getAttribute("nickname")))
	{
		currentUserId = "user"+chatter.getId();
		session.setAttribute(currentUserId, chatter);
		%><p align="right"><% 
		
		out.write("<font face=\"Arial\" size=\"2\" color=\"blue\"><b>" + chatterName + " ("+ DateFormat.getTimeInstance().format(date)+ ")&gt;</b></font> " + strmsg);
		%></p><%
	}
	else if (chatterName.equalsIgnoreCase("system"))
	{
		out.write("<p><span class=\"error\">" + strmsg + "</span></p>\n");
	}
	else
	{
		currentUserId = "user"+chatter.getId();
		session.setAttribute(currentUserId, chatter);
		%><p><jsp:include page="/UserIconDisplay.jsp" >
		<jsp:param name="userId" value="<%= currentUserId %>" />
		<jsp:param name="size" value="25px" />
		</jsp:include><% 
		
		out.write("<font face=\"Arial\" size=\"2\"><b>"+chatterName + " ("+ DateFormat.getTimeInstance().format(date)+ ")&gt;</b></font> " + strmsg);
		%></p><%
	}			

%>

