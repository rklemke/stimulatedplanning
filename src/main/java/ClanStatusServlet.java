

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;

import senseofcommunity.Clan;
import stimulatedplanning.*;

/**
 * Servlet implementation class DataTrackerServlet
 */
@WebServlet("/ClanStatusServlet")
public class ClanStatusServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(ClanStatusServlet.class.getName());   
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ClanStatusServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//log.info("DataTrackerServlet called.");
		HttpSession session = StimulatedPlanningFactory.initializeSession(request, response);
		
		User user = (User)session.getAttribute("user");

		response.setContentType("text/javascript");
		
        HashMap<String, String> clanStatusMap = new HashMap<>();

        int userClanSize = 1;
        int userClanOnline = 0;
        int userClanRecent = 0;
        int userClanOffline = 0;
        int otherClanSize = 1;
        int otherClanOnline = 0;
        int otherClanRecent = 0;
        int otherClanOffline = 0;

        if (user.isTreatmentGroup()) {
        	Clan userClan = user.getClan();
        	Clan otherClan = StimulatedPlanningFactory.getOtherClan(userClan);
        	
        	  userClanSize = userClan.userCount();
        	  if (userClanSize == 0) {
        		  userClanSize = 1;
        	  }
        	  userClanOnline = userClan.getOnlineUsers().size();
        	  userClanRecent = userClan.getRecentUsers().size();
        	  userClanOffline = userClan.getOfflineUsers().size();

        	  otherClanSize = otherClan.userCount();
        	  if (otherClanSize == 0) {
        		  otherClanSize = 1;
        	  }
        	  otherClanOnline = otherClan.getOnlineUsers().size();
        	  otherClanRecent = otherClan.getRecentUsers().size();
        	  otherClanOffline = otherClan.getOfflineUsers().size();        	
        }

    	clanStatusMap.put("userClanSize", String.valueOf(userClanSize));
    	clanStatusMap.put("userClanOnline", String.valueOf(userClanOnline));
    	clanStatusMap.put("userClanRecent", String.valueOf(userClanRecent));
    	clanStatusMap.put("userClanOffline", String.valueOf(userClanOffline));
    	clanStatusMap.put("otherClanSize", String.valueOf(otherClanSize));
    	clanStatusMap.put("otherClanOnline", String.valueOf(otherClanOnline));
    	clanStatusMap.put("otherClanRecent", String.valueOf(otherClanRecent));
    	clanStatusMap.put("otherClanOffline", String.valueOf(otherClanOffline));
    	
        //log.info("FeedbackFrame: "+content);
		
		Gson gson = new Gson();
		String jsonObject = gson.toJson(clanStatusMap);

		String callback = request.getParameter("callback");
		if (callback != null && !"".equals(callback)) {
			jsonObject = callback + "(" + jsonObject + ");";
		}
				
		PrintWriter out = response.getWriter();
		out.println(jsonObject);
		out.flush();
		log.info("ClanStatusServlet: " + jsonObject);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
