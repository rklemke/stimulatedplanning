

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
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
		System.out.println("DataTrackerServlet called.");
		HttpSession session = StimulatedPlanningFactory.initializeSession(request, response);
		
		User user = (User)session.getAttribute("user");
		CourseDescriptor course = (CourseDescriptor)session.getAttribute("course");
		UserPlan userPlan = (UserPlan)session.getAttribute("userPlan");

		response.setContentType("text/javascript");
		
		
		try {
			StimulatedPlanningFactory.trackAndLogEvent(request, response, "track");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
        HttpServletResponseWrapper responseWrapper = new HttpServletResponseWrapper(response) {
            private final StringWriter sw = new StringWriter();

            @Override
            public PrintWriter getWriter() throws IOException {
                return new PrintWriter(sw);
            }

            @Override
            public String toString() {
                return sw.toString();
            }
        };
        
        request.getRequestDispatcher("FeedbackFrame.jsp").include(request, responseWrapper);
        String content = responseWrapper.toString();

        HashMap<String, String> completionStatusMap = userPlan.getCompletionStatusMap();
        completionStatusMap.put("feedbackFrame", content);
        System.out.println("FeedbackFrame: "+content);
		
		Gson gson = new Gson();
		String jsonObject = gson.toJson(completionStatusMap);

		String callback = request.getParameter("callback");
		if (callback != null && !"".equals(callback)) {
			jsonObject = callback + "(" + jsonObject + ");";
		}
				
		PrintWriter out = response.getWriter();
		out.println(jsonObject);
		out.flush();
		System.out.println(jsonObject);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
