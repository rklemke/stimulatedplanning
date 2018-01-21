

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.CompositeFilter;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.Properties;
import java.util.logging.Logger;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


import stimulatedplanning.*;
import stimulatedplanning.util.*;


/**
 * Servlet implementation class Cron_MailNotificationServlet
 */
@WebServlet("/Cron_MailNotificationServlet")
public class Cron_MailNotificationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(Cron_MailNotificationServlet.class.getName());   
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Cron_MailNotificationServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		// TODO: read all users
		// TODO: check mail address for user
		// TODO: read plan for user
		// TODO: check delayed plan items
		// TODO: if delayed plan items available: send report
		
		ArrayList<UserProfile> userProfiles = StimulatedPlanningFactory.getUserProfiles();
		log.info("Cron_MailNotificationServlet: found userProfiles: "+userProfiles.size());
		CourseDescriptor course = StimulatedPlanningFactory.generateTestCourse();
		
		User user = null;
		UserPlan plan = null;
		PlanItem item = null;
		HashMap<String, String> completionStatusMap = null;
		String message = null;
		
		for (UserProfile profile : userProfiles) {
			log.info("Cron_MailNotificationServlet: check userProfile: "+profile.getFullName());
			user = profile.getUser();
			String userName = profile.getFullName();
			if (userName == null || "".equals(userName)) {
				userName = user.getName();
			}
			message = "Dear "+userName+"\n\n";
			if (user.isTreatmentGroup()) {
				boolean sendMail = false;
				plan = StimulatedPlanningFactory.getUserPlan(user, course);
				completionStatusMap = plan.getCompletionStatusMap();
				
				if (plan.hasPlannableGoals()) {
					LocalDateTime now = LocalDateTime.now();
					int planned = Integer.valueOf(completionStatusMap.get("raw.plannedItemsTotal"));
					int late = Integer.valueOf(completionStatusMap.get("raw.planAchievementDelayed"));
					int achievement = Integer.valueOf(completionStatusMap.get("raw.planAchievementCompleted"));
					int achievementTotal = Integer.valueOf(completionStatusMap.get("raw.achievedLessons"));
					int intended = Integer.valueOf(completionStatusMap.get("raw.plannableItems"));
					if (planned > 0) {
						String planItemsMsg = "";
						int count = 0;
						ListIterator<PlanItem> iterator = plan.getPlanItems();
						while (iterator.hasNext() && count < 3) {
							item = iterator.next();
							if (item.getStartDate().isAfter(now) && item.getPlanCompletionStatus().equals(PlanCompletionStatus.OPEN)) {
								count++;
								planItemsMsg += " • "+item.getStartDate().toLocalDate()+" "+item.getStartDate().toLocalTime()+" - "+item.getLesson().getTitle()+"\n";
							}
						}
						if (count > 0 ) {
							sendMail = true;
							message += "Your next planned activities:\n" + planItemsMsg;
							message += "Show your plan in your calendar: https://goo.gl/T9CmTi \n\n";
						}
					}
					
					if (planned < intended) {
						sendMail = true;
						message += "You have not yet planned "+(intended - planned)+" of your "+intended+" intended activities.\n";
						message += "You can plan your activities in the calendar: https://goo.gl/T9CmTi \n\n";
					}
					
					if (achievement > 0) {
						sendMail = true;
						message += "You have completed "+achievement+" of your "+planned+" planned activities.\n";
						if (late == 0) {
							message += "You are doing very well! Continue like this!\n";
						}
						message += "\n";
					}
					
					if (achievement < achievementTotal) {
						sendMail = true;
						message += "You have completed "+(achievementTotal - achievement)+" of your "+intended+" intended activities, which you didn’t plan.\n";
						message += "Would you like to re-plan? If yes please follow this link: https://goo.gl/T9CmTi \n\n";
					}

					if (late > 0) {
						sendMail = true;
						message += "We have noticed that you are late on "+late+" of your "+planned+" planned activities.\n";
						if ((plan.getObstacles() != null && !"".equals(plan.getObstacles())) || (plan.getCopingPlan() != null && !"".equals(plan.getCopingPlan())) ) {
							message += "your plan b is: \n";
							message += plan.getObstacles() + "\n";
							message += plan.getCopingPlan() + "\n";
						} else {
							message += "your plan b is: empty. \n";
						}
						message += "Would you like to re-plan?\n";
						message += "If yes, please follow this link: https://goo.gl/T9CmTi \n\n";
					}
					
				}
				if (sendMail) {
					sendMail(profile.getEmail(), "Progress report", message);
				}
			}
		}
				
		response.getWriter().append("Nothing to see.").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	
	protected void sendMail(String toMail, String subject, String body) {

	    Properties props = new Properties();
	    Session session = Session.getDefaultInstance(props, null);
	    
	    String bodyFooter = "\n";
	    bodyFooter += "On behalf of the OUNL (Open University of the Netherlands) that provided this course we thank you for choosing us.\n";
	    bodyFooter += "During the course some features will be tested. All the data is collected in anonymous form.\n";
	    bodyFooter += "In case of any questions, please contact our support team (mailto:support.mooc@ou.nl).\n";
	    bodyFooter += "To access to the course click on the following link https://ou.edia.nl/\n";
	    bodyFooter += "If you want to sign-out please send an email to (mailto:support.mooc@ou.nl).\n";
	    bodyFooter += "Enjoy the course.\n\n";
	    bodyFooter += "Warm Regards,\n";
	    bodyFooter += "The OUNL team\n";    
	    try {
	      Message msg = new MimeMessage(session);
	      msg.setFrom(new InternetAddress("no-reply@stimulatedplanning.appspotmail.com", "ICS18 - Introduction to Computer Security - Open Universiteit"));
	      msg.addRecipient(Message.RecipientType.TO,
	                       new InternetAddress(toMail, toMail));
	      msg.setSubject(subject + " - ICS18 - Introduction to Computer Security - Open Universiteit");
	      msg.setText(body + bodyFooter);
	      log.info("sendMail: "+toMail+", "+subject+", "+body + bodyFooter);
	      Transport.send(msg);
	    } catch (AddressException e) {
	    	e.printStackTrace();
	    } catch (MessagingException e) {
	    	e.printStackTrace();
	    } catch (UnsupportedEncodingException e) {
	    	e.printStackTrace();
	    }

	}
	

}
