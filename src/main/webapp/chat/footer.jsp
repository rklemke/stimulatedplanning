<DIV align="center">
<CENTER>
<TABLE width="100%" border="0" align="center" cellpadding="3" cellspacing="0">
	<TR>
		<TD width="100%" align="center"><%
		String n = (String)session.getAttribute("nickname");
		if (n != null && n.length() > 0)
		{
			out.write("<a href=\"logout.jsp\">Logout</a>");
			out.write(" <a href=\"listrooms.jsp\">List of Rooms</a>");
		}
		%> </TD>
	</TR>
</TABLE>
</CENTER>
</DIV>