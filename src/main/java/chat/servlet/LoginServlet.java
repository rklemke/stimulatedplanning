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
		String userId = user.getId();
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
				ChatRoomList roomlist = StimulatedPlanningFactory.getChatRoomListForUser(user);
				ChatRoom chatRoom = roomlist.getRoomOfChatter(userId);
				if (chatRoom == null || "StartUp".equalsIgnoreCase(chatRoom.getName()))
				{
					chatRoom = roomlist.getRoomListArray()[0];
				}
				session.setAttribute("nickname", nickname);
				if (!chatRoom.chatterExists(nickname)) {
					chatRoom.addChatter(user);
				}
				response.sendRedirect(contextPath + "/chat/chat.jsp");
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