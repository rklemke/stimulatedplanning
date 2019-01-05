package chat.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import chat.*;
import stimulatedplanning.User;

/**
Allows users to save their personal information.
*/
@WebServlet("/chat/servlet/SaveInfoServlet")
public class SaveInfoServlet extends HttpServlet
{
	String nickname = null;
	String userId = null;
	int age = -1;
	String email = null;
	String comment = null;
	HttpSession session = null;
	String contextPath = null;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
	{
		nickname = request.getParameter("nickname");
		userId = request.getParameter("userId");
		contextPath = request.getContextPath();
		try
		{
			Integer a = Integer.valueOf(request.getParameter("age"));
			age = a.intValue();
		}
		catch(NumberFormatException nfe)
		{
			age = -1;
		}
		email = request.getParameter("email");
		comment = request.getParameter("comment");
		session = request.getSession(true);
		try
		{
			ChatRoomList roomList = (ChatRoomList) getServletContext().getAttribute("chatroomlist");
			ChatRoom chatRoom = roomList.getRoomOfChatter(nickname);

			
			if (chatRoom != null)
			{
				User chatter = chatRoom.getChatter(nickname);
				if (chatter == null) {
					chatter = chatRoom.getChatter(userId);
				}
				//chatter.setAge(age);
				//chatter.setEmail(email);
				//chatter.setComment(comment);
				response.setContentType("text/html");
				PrintWriter out = response.getWriter();
				out.write("<html>\n<head>\n<title>Information Saved</title>\n</head>\n<body>\n");
				out.write("<b>Information Saved</b>\n<div align=\"center\">\n<form name=\"closing\">\n");
				out.write("<input type=\"button\" onClick=\"window.close()\" value=\"Close\">\n");
				out.write("</form>\n</div>\n</body>\n</html>");
				out.close();
			}
		}
		catch(Exception ex)
		{
			
			ex.printStackTrace();
			System.out.println("Exception: " + ex.getMessage());
			response.sendRedirect(contextPath + "/error.jsp");
		}
	
	}
	

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
	{
		doGet(request, response);
	}
}