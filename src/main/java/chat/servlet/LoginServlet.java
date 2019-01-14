package chat.servlet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.logging.Logger;

import chat.*;
import stimulatedplanning.StimulatedPlanningFactory;
import stimulatedplanning.User;

@WebServlet("/chat/servlet/LoginServlet")
public class LoginServlet extends HttpServlet
{
	private static final Logger log = Logger.getLogger(LoginServlet.class.getName());   
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
//			try
//			{
				ChatRoomList roomlist = StimulatedPlanningFactory.getChatRoomListForUser(user);
				ChatRoom chatRoom = roomlist.getRoomOfChatter(userId);
				if (chatRoom == null || "StartUp".equalsIgnoreCase(chatRoom.getName()))
				{
					chatRoom = roomlist.getRoomListArray()[0];
				}
				session.setAttribute("nickname", nickname);
				if (!chatRoom.chatterExists(nickname)) {
					chatRoom.addChatter(user);
					chatRoom.addMessage(StimulatedPlanningFactory.createMessage(null, nickname + " has joined.", new java.util.Date().getTime(), chatRoom, roomlist));
					//chatter.setEnteredInRoomAt(new java.util.Date().getTime());
					StimulatedPlanningFactory.trackAndLogEvent(request, response, "chat.room.enter");
				} else {
					StimulatedPlanningFactory.trackAndLogEvent(request, response, "chat.room.re-enter");
				}
				RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/chat/chat.jsp");
				dispatcher.forward(request,response);
//			}
//			catch(Exception exception)
//			{
//				log.info("Exception thrown in LoginServlet: " + exception.getMessage());
//				
//				RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/chat/error.jsp");
//				dispatcher.forward(request,response);
//			}
		}
		else
		{
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/chat/login.jsp?ic=t");
			dispatcher.forward(request,response);
			//response.sendRedirect(contextPath + "/chat/login.jsp?ic=t");
		}
	}
}