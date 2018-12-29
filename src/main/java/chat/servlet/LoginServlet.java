package chat.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import chat.*;
import stimulatedplanning.StimulatedPlanningFactory;
import stimulatedplanning.User;

@WebServlet("/chat/servlet/LoginServlet")
public class LoginServlet extends HttpServlet
{
	private String contextPath = "";
	/** This method just redirects user to a login page.*/
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		doPost(request, response);
		//contextPath = request.getContextPath();
		//response.sendRedirect(contextPath + "/chat/login.jsp");		
	}
	/** Performs some validation and if everything is ok sends user to a page which displays a list of
	* rooms available.
	*/
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		HttpSession session = StimulatedPlanningFactory.initializeSession(request, response);
		User user = (User)session.getAttribute("user");

		contextPath = request.getContextPath();		
		//String nickname = request.getParameter("nickname");
		String nickname = user.getName();
		//nickname = nickname.trim().toLowerCase();
//		String sex = request.getParameter("sex");
//		if (sex.length() > 0)
//		{
//			sex = sex.trim().toLowerCase();
//		}
		if ((nickname != null && nickname.length() > 3))
		{
			try
			{
				//ChatRoomList roomlist = (ChatRoomList)getServletContext().getAttribute("chatroomlist");
				ChatRoomList roomlist = StimulatedPlanningFactory.getChatRoomListForUser(user);
				boolean chatterexists = roomlist.chatterExists(nickname);
				if (chatterexists)
				{
					response.sendRedirect(contextPath + "/chat/login.jsp?d=t&n="+nickname);
				}
				else
				{
					//HttpSession session = request.getSession(true);
//					int timeout = 1800; // 30 minutes
//					String t = getServletContext().getInitParameter("sessionTimeout"); // gets Minutes
//					if (t != null)
//					{
//						try
//						{
//							timeout = Integer.parseInt(t);
//							timeout = timeout * 60;
//						}
//						catch (NumberFormatException nfe)
//						{							
//						}
//					}
//					session.setMaxInactiveInterval(timeout);
					session.setAttribute("nickname", nickname);
					// Because Chatter objects are stored in room.
					// So before user selects any room he is added to a temporary room "StartUp"
					ChatRoom chatRoom = roomlist.getRoom("StartUp"); 
					//nickname = nickname.toLowerCase();
					Chatter chatter = null;
//					if ("m".equals(sex))
//					{
//						sex = "Male";
//					}
//					else
//					{
//						sex = "Female";
//					}
					chatter = new Chatter(nickname, user.getAvatarUrl(), new java.util.Date().getTime(), user);
					chatRoom.addChatter(chatter);
					response.sendRedirect(contextPath + "/chat/listrooms.jsp");

				}
			}
			catch(Exception exception)
			{
				System.out.println("Exception thrown in LoginServlet: " + exception.getMessage());
				exception.printStackTrace();
				response.sendRedirect(contextPath + "/chat/error.jsp");
			}
		}
		else
		{
			response.sendRedirect(contextPath + "/chat/login.jsp?ic=t");
		}
	}
}