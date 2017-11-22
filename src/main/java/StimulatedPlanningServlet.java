

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import stimulatedplanning.*;

/**
 * Servlet implementation class StimulatedPlanningServlet
 */
@WebServlet("/StimulatedPlanningServlet")
public class StimulatedPlanningServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public StimulatedPlanningServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
//		String user = (String)session.getAttribute("user");
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
		
		
		CourseDescriptor course = (CourseDescriptor)session.getAttribute("course");
		if (course == null) {
			course = CourseDescriptor.generateTestCourse();
			session.setAttribute("course", course);
		}
		
		GoalDescriptor userGoal = (GoalDescriptor)session.getAttribute("userGoal");
		
		UserPlan userPlan = (UserPlan)session.getAttribute("userPlan");
		if (userPlan == null) {
			userPlan = new UserPlan(user);
			if (userGoal != null) {
				userPlan.setGoal(userGoal);
			}
			session.setAttribute("userPlan", userPlan);
		}

		String calenderItems = request.getParameter("calenderItems");
		System.out.println("calenderItems: "+calenderItems);
		
		GsonBuilder builder = new GsonBuilder();
		Object o = builder.create().fromJson(calenderItems, Object.class);
		
		if (o instanceof ArrayList) {
			ArrayList list = (ArrayList)o;
			for(Object e : list) {
				if (e instanceof Map) {
					Map evt = (Map)e;
					evt.remove("_id");
					evt.remove("source");
					String id = (String)evt.get("id");
					
					if (id != null && !"".equals(id)) {
						LessonDescriptor lesson = course.retrieveLessonById(id);
						if (lesson != null) {
							PlanItem item = new PlanItem(id, lesson, builder.create().toJson(evt));
							userPlan.addPlanItem(item);
						}
					}
					
					
				}
			}
		}

		System.out.println("o: "+o.toString());
		System.out.println("o.class: "+o.getClass().getName()+", o[0].class"+((ArrayList)o).get(0).getClass().getName());
		System.out.println("o[0].id"+((Map)((ArrayList)o).get(0)).get("id"));
		
		String nextJSP = "/StimulatedPlanning.jsp";
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextJSP);
		dispatcher.forward(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
