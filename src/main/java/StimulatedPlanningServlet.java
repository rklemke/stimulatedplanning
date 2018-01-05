

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
		HttpSession session = StimulatedPlanningFactory.initializeSession(request, response);
		
		User user = (User)session.getAttribute("user");
		CourseDescriptor course = (CourseDescriptor)session.getAttribute("course");
		UserPlan userPlan = (UserPlan)session.getAttribute("userPlan");
		boolean userPlanDirty = false;

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
						if (id.startsWith("late_")) {
							id = id.substring(5);
							evt.put("id", id);
						}
						LessonDescriptor lesson = course.retrieveLessonById(id);
						if (lesson != null) {
							userPlanDirty = true;
							PlanItem item = null;
							if (userPlan.hasPlanItemForLesson(lesson)) { // event exists in calendar: update
								item = userPlan.getPlanItemForLesson(lesson);
								item.setUser(user);
								item.setJsonPlanItem(builder.create().toJson(evt));
								item.trackPlanStatus();
							} else { // event doesn't exist in calendar: create
								item = new PlanItem(id, user, lesson, builder.create().toJson(evt));
								userPlan.addPlanItem(item);
								item.trackPlanStatus();
							}
						}
					}
					
					
				}
			}
		}

		if (o != null) {
			System.out.println("o: "+o.toString());
			System.out.println("o.class: "+o.getClass().getName());
		} else {
			System.out.println("o: null");
		}
		
		if (userPlanDirty) {
			System.out.println("writing user plan for " + user.getName() + ", " + course.getId() + ", " + userPlan.getId());
			try {
				PersistentStore.writeDescriptor(userPlan);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		try {
			StimulatedPlanningFactory.trackAndLogEvent(request, response, "planning");
			//PersistentStore.writeLog(request.getParameterMap());
		} catch (Exception e) {
			e.printStackTrace();
		}

		String submit = request.getParameter("submit");
		String nextJSP = "/StimulatedPlanning.jsp";
		if (submit != null && submit.equals("Next")) {
			nextJSP = "/CopingPlan.jsp";
		}
		
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
