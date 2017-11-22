

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;

import stimulatedplanning.*;

/**
 * Servlet implementation class DataTrackerServlet
 */
@WebServlet("/DataTrackerServlet")
public class DataTrackerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DataTrackerServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		CourseDescriptor course = (CourseDescriptor)session.getAttribute("course");
		if (course == null) {
			course = CourseDescriptor.generateTestCourse();
			session.setAttribute("course", course);
		}
		  
		response.setContentType("text/javascript");
		
		String userName = request.getParameter("userName");
		session.setAttribute("userName", userName);
		String userid = request.getParameter("userid");
		session.setAttribute("userid", userid);
		
		User user = (User)session.getAttribute("user");
		if (user == null) {
			user = new User(userName, userid);
			session.setAttribute("user", user);
		} else {
			user.setId(userid);
			user.setName(userName);
		}
		
		// TODO: store retrieved parameters in DB
		
		Gson gson = new Gson();
		String jsonObject = gson.toJson(request.getParameterMap());
		String callback = request.getParameter("callback");
		if (callback != null && !"".equals(callback)) {
			jsonObject = callback + "(" + jsonObject + ");";
		}
				
		PrintWriter out = response.getWriter();
		out.println(jsonObject);
		out.flush();
		System.out.println(jsonObject);
		// TODO Auto-generated method stub
		// response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
