<%@ page isErrorPage="true" import="java.io.PrintWriter" %>
<HTML>
<HEAD>
<TITLE>Error Occurred</TITLE>
<META http-equiv="pragma" content="no-cache">
<LINK rel="stylesheet" href="/css/chat/chat.css" type="text/css">
</HEAD>
<BODY bgcolor="#FFFFFF">
<h2>Some error occurred</h2>
<pre>
<%
if (exception != null)
{
	//System.out.println("Exception: " +exception.getMessage());
	out.write("<span class=\"error\">Exception: " +exception.getMessage() + "</span><br>");
	exception.printStackTrace( new PrintWriter(out));
}
%>
</pre>
<p>Report to
<%
String admin = null;
if ( ( admin = application.getInitParameter("adminEmail") ) != null )
{
	out.write("<a href=\"mailto:" + admin + "\">" + admin + "</a>");
}

else
{
	out.write("administrator of this web site");
}
%>.
</BODY>
</HTML>