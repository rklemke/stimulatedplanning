package chat.servlet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;
import java.util.Enumeration;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import chat.*;

/** Allows users to add new rooms.
* At server startup this servlet is initialised.
*/
@WebServlet("/chat/servlet/ManageChatServlet")
public class ManageChatServlet extends HttpServlet
{
	ChatRoomList rooms = new ChatRoomList();
	Properties props = null;
	/** Reads chat.properties file creates an object of ChatRoomList and stores it in ServletContext
	*/
	public void init() throws ServletException
	{
		try
		{
			String path = "";
			path = "/WEB-INF/"+getServletContext().getInitParameter("chatpropertyfile");
			String realPath;
			realPath = getServletContext().getRealPath(path);
			
			/*InputStream fis = getServletContext().getResourceAsStream(path);

			if (fis != null)
			{*/
			if (realPath != null)
			{
				InputStream fis = new FileInputStream(realPath);

				props = new Properties();
				props.load(fis);
				Enumeration keys = props.keys();
				while (keys.hasMoreElements())
				{
					String roomName = (String)keys.nextElement();
					String roomDescr = (String)props.getProperty(roomName);
					addNewRoom(rooms, roomName, roomDescr);
				}
				fis.close();
				getServletContext().setAttribute("chatroomlist", rooms);
				System.err.println("Room List Created");
			}
			else
			{
				System.out.println("Unable to get realpath of chatproperty file " + path + ".\nCheck that application war file is expanded and file can be read.\nChat application won't work.");
			}
		}
		catch(FileNotFoundException fnfe)
		{
			System.err.println("Properites file not found:" + fnfe.getMessage());
		}
		catch(IOException ioe)
		{
			System.out.print("Unable to load Properties File: " + ioe.getMessage());
		}		
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("Room List Created");
		out.close();
	}
	
	/**	Allows users to add new rooms after performing minimum validation.
	* Also saves information to chat.properties files if required by initialization parameter <code>saveRooms</code>.
	*/
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		String roomName = request.getParameter("rn");
		String roomDescr = request.getParameter("rd");
		if (roomName == null || (roomName = roomName.trim()).length() == 0 || roomDescr == null || (roomDescr = roomDescr.trim()).length() == 0)
		{
			request.setAttribute("error", "Please specify the room name and room description");
			getServletContext().getRequestDispatcher("/addRoom.jsp").forward(request, response);
			return;

		}
		else if (roomName != null && roomName.indexOf(" ") != -1)
		{
			request.setAttribute("error", "Room Name cannot contain spaces");
			getServletContext().getRequestDispatcher("/addRoom.jsp").forward(request, response);
			return;
		}
		try
		{
			if (rooms != null)
			{
				addNewRoom(rooms, roomName, roomDescr);
			}
			
			
			String s = getServletContext().getInitParameter("saveRooms");
			boolean save = false;
			if (s != null && "true".equals(s) )
			{
				Boolean b = Boolean.valueOf(s);
				save = b.booleanValue();
			}
			if (save)
			{
				if (props != null)
				{
					props.put(roomName, roomDescr);
					String path = "/WEB-INF/"+getServletContext().getInitParameter("chatpropertyfile");
					String realPath = getServletContext().getRealPath(path);
					OutputStream fos = new FileOutputStream(realPath);
					props.store(fos, "List of Rooms");
					fos.close();	
				}
				else
				{
					response.setContentType("text/html");
					PrintWriter out = response.getWriter();
					out.println("Properties are null");
				}
			}
			response.sendRedirect(request.getContextPath() + "/listrooms.jsp");
		}
		catch (Exception e)
		{
				System.err.println("Exception: " + e.getMessage());
		}		
	}
	/**
	* Adds a new Room to ChatRoomList object and saves it to chat.properties file if required.
	*/
	public void addNewRoom(ChatRoomList list, String roomName, String roomDescr)
	{
		String s = getServletContext().getInitParameter("maxNoOfMessages");
		int maxMessages = 25;
		if (s != null)
		{
			try
			{
				maxMessages = Integer.parseInt(s);
			}
			catch (NumberFormatException nfe)
			{
				
			}
		}
		ChatRoom room = new ChatRoom(roomName, roomDescr);
		room.setMaximumNoOfMessages(maxMessages);
		rooms.addRoom(room);		
	}

	/** Called when servlet is being destroyed */

	public void destroy()
	{
		System.err.println("Destroying all rooms");
	}
}