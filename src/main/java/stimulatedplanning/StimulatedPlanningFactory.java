package stimulatedplanning;

import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;

import stimulatedplanning.util.HashArrayList;

public class StimulatedPlanningFactory {
	public static final StimulatedPlanningFactory instance = new StimulatedPlanningFactory();
	public static Random random = new Random();

	private StimulatedPlanningFactory() {
		// TODO Auto-generated constructor stub
	}
	
	private CourseDescriptor testCourse;
	private static final String testCourseId = "ICS18";
	private static final String testCourseBaseURL = "https://ou.edia.nl/courses/course-v1:OUNL+ICS18+2018_1/";
	public static final String userUnknown = "unknown";
	public static final String userGuest = "Guest";
	
	public static final String ACTIVITY_TYPE_ACCESS = "access";
	public static final String ACTIVITY_TYPE_COMPLETE = "complete";
	
	private HashMap<String, GenericDescriptor> courseObjects = new HashMap<>();
	
	private CourseDescriptor retrievTestCourse() {
		if (instance.testCourse != null) {
			return instance.testCourse;
		} else {
			CourseDescriptor course = PersistentStore.readCourse(testCourseId);
			if (course != null) {
				instance.testCourse = course;
				return course;
			}
		}
		return null;
	}

	private void storeTestCourse(CourseDescriptor course) {
		instance.testCourse = course;
		try {
			PersistentStore.writeDescriptor(course);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static boolean hasObject(String id) {
		return instance.courseObjects.containsKey(id);
	}
	
	public static void addObject(GenericDescriptor object) {
		if (!(object instanceof GenericUserObject)) {
			instance.courseObjects.put(object.getId(), object);
		} else {
			System.out.println("trying to add GenericUserObject to courseObjects: "+object.getClass().getName()+", "+object.getId());
		}
	}
	
	public static GenericDescriptor getObject(String id) {
		if (!instance.courseObjects.containsKey(id)) {
			System.out.println("Warning: trying to retrieve object not in Map: "+id);
			new Exception().printStackTrace();
			return null;
		}
		return instance.courseObjects.get(id);
	}
	
	public static String getUUID() {
		UUID uuid = UUID.randomUUID();
        return uuid.toString();
	}

	/**
	 * generate the structure for the course to be used according to ICS18 structure.
	 * @return
	 */
	public static CourseDescriptor generateTestCourse() {
		CourseDescriptor course = instance.retrievTestCourse();
		if (course == null) {
			course = new CourseDescriptor(instance.testCourseId, "Introduction to Computer Security", "Introduction to Computer Security", testCourseBaseURL+"info");
			ModuleDescriptor module1 = new ModuleDescriptor(getUUID(), "Basic computer security principles", "Basic computer security principles", "");
			course.addModule(module1);

			LessonDescriptor lesson11 = new LessonDescriptor(getUUID() ,"The fundamental concepts (C.I.A.)","The fundamental concepts (C.I.A.)","");
			module1.addLesson(lesson11);	
			ContentDescriptor content111 = new ContentDescriptor(getUUID(), "The fundamental concepts (C.I.A.)", "The fundamental concepts (C.I.A.)", testCourseBaseURL+"courseware/174c8dadf4964842bfee47a8242dc530/48fbe0cee4684603bf2660afd51ca53a/?activate_block_id=block-v1%3AOUNL%2BICS18%2B2018_1%2Btype%40sequential%2Bblock%4048fbe0cee4684603bf2660afd51ca53a");
			lesson11.addContent(content111);
			
			LessonDescriptor lesson12 = new LessonDescriptor(getUUID() ,"Assurance, Authenticity, and Anonymity","Assurance, Authenticity, and Anonymity","");
			module1.addLesson(lesson12);
			ContentDescriptor content121 = new ContentDescriptor(getUUID(), "Assurance, Authenticity, and Anonymity", "Assurance, Authenticity, and Anonymity", testCourseBaseURL+"courseware/174c8dadf4964842bfee47a8242dc530/ab58463db1e24ad9b885d8abc40f5a44/?activate_block_id=block-v1%3AOUNL%2BICS18%2B2018_1%2Btype%40sequential%2Bblock%40ab58463db1e24ad9b885d8abc40f5a44");
			lesson12.addContent(content121);

			LessonDescriptor lesson13 = new LessonDescriptor(getUUID() ,"Security principles","Security principles","");
			module1.addLesson(lesson13);
			ContentDescriptor content131 = new ContentDescriptor(getUUID(), "Security principles", "Security principles", testCourseBaseURL+"courseware/174c8dadf4964842bfee47a8242dc530/119612eb80e94835a5c8228aefad1a54/?activate_block_id=block-v1%3AOUNL%2BICS18%2B2018_1%2Btype%40sequential%2Bblock%40119612eb80e94835a5c8228aefad1a54");
			lesson13.addContent(content131);

			LessonDescriptor lesson14 = new LessonDescriptor(getUUID() ,"Cryptography contents","Cryptography contents","");
			module1.addLesson(lesson14);
			ContentDescriptor content141 = new ContentDescriptor(getUUID(), "Cryptography contents", "Cryptography contents", testCourseBaseURL+"courseware/174c8dadf4964842bfee47a8242dc530/e67b556c491441108300988be46a67ac/?activate_block_id=block-v1%3AOUNL%2BICS18%2B2018_1%2Btype%40sequential%2Bblock%40e67b556c491441108300988be46a67ac");
			lesson14.addContent(content141);

			LessonDescriptor lesson15 = new LessonDescriptor(getUUID() ,"Putting cryptography into use","Putting cryptography into use","");
			module1.addLesson(lesson15);
			ContentDescriptor content151 = new ContentDescriptor(getUUID(), "Putting cryptography into use", "Putting cryptography into use", testCourseBaseURL+"courseware/174c8dadf4964842bfee47a8242dc530/e67b556c491441108300988be46a67ac/?activate_block_id=block-v1%3AOUNL%2BICS18%2B2018_1%2Btype%40sequential%2Bblock%40e67b556c491441108300988be46a67ac");
			lesson15.addContent(content151);

			
			ModuleDescriptor module2 = new ModuleDescriptor(getUUID(), "Understand how to secure your PC", "Understand how to secure your PC", "");
			course.addModule(module2);
			LessonDescriptor lesson21 = new LessonDescriptor(getUUID() ,"Threats and Attacks - basic concept of Malware","Threats and Attacks - basic concept of Malware","");
			module2.addLesson(lesson21);	
			ContentDescriptor content211 = new ContentDescriptor(getUUID(), "Threats and Attacks - basic concept of Malware", "Threats and Attacks - basic concept of Malware", testCourseBaseURL+"courseware/7f3c8908698a4fa785c2bab76dc403c9/83d79097d5d94304a6fa9a5aed25dce3/?activate_block_id=block-v1%3AOUNL%2BICS18%2B2018_1%2Btype%40sequential%2Bblock%4083d79097d5d94304a6fa9a5aed25dce3");
			lesson21.addContent(content211);

			LessonDescriptor lesson23 = new LessonDescriptor(getUUID() ,"How passwords work?","What happens on the network when you enter a password","");
			module2.addLesson(lesson23);
			ContentDescriptor content231 = new ContentDescriptor(getUUID(), "How passwords work?", "What happens on the network when you enter a password", testCourseBaseURL+"courseware/7f3c8908698a4fa785c2bab76dc403c9/0437f85591cc4e0c964437b4d1bfad1e/?activate_block_id=block-v1%3AOUNL%2BICS18%2B2018_1%2Btype%40sequential%2Bblock%400437f85591cc4e0c964437b4d1bfad1e");
			lesson23.addContent(content231);

			LessonDescriptor lesson24 = new LessonDescriptor(getUUID() ,"How passwords can be attacked?","How passwords can be attacked?","");
			module2.addLesson(lesson24);
			ContentDescriptor content241 = new ContentDescriptor(getUUID(), "How passwords can be attacked?", "How passwords can be attacked?", testCourseBaseURL+"courseware/7f3c8908698a4fa785c2bab76dc403c9/a8735d7566884b6bb5a05851a0836204/1?activate_block_id=block-v1%3AOUNL%2BICS18%2B2018_1%2Btype%40vertical%2Bblock%40c38c05ecb5ee40e4bcb21f1a3f1f5289");
			lesson24.addContent(content241);

			LessonDescriptor lesson25 = new LessonDescriptor(getUUID() ,"Good password checklist","Good password checklist","");
			module2.addLesson(lesson25);
			ContentDescriptor content251 = new ContentDescriptor(getUUID(), "Good password checklist", "Good password checklist", testCourseBaseURL+"courseware/7f3c8908698a4fa785c2bab76dc403c9/a97c3e16628d458a933cff6bc7f6a492/?activate_block_id=block-v1%3AOUNL%2BICS18%2B2018_1%2Btype%40sequential%2Bblock%40a97c3e16628d458a933cff6bc7f6a492");
			lesson25.addContent(content251);

			LessonDescriptor lesson26 = new LessonDescriptor(getUUID() ,"Password managers","Password managers","");
			module2.addLesson(lesson26);
			ContentDescriptor content261 = new ContentDescriptor(getUUID(), "Password managers", "Password managers", testCourseBaseURL+"courseware/7f3c8908698a4fa785c2bab76dc403c9/d75f8af0def547d4b547ef4056a03b43/?activate_block_id=block-v1%3AOUNL%2BICS18%2B2018_1%2Btype%40sequential%2Bblock%40d75f8af0def547d4b547ef4056a03b43");
			lesson26.addContent(content261);

			
			ModuleDescriptor module3 = new ModuleDescriptor(getUUID(), "Network security", "Network security", "");
			course.addModule(module3);
			LessonDescriptor lesson31 = new LessonDescriptor(getUUID() ,"Firewall policies","Firewall policies","");
			module3.addLesson(lesson31);	
			ContentDescriptor content311 = new ContentDescriptor(getUUID(), "Firewall policies", "Firewall policies", testCourseBaseURL+"courseware/78b706f2fb434606a4c342fb8db156be/0b957f040f954b6ab1f4e64b533ba65b/?activate_block_id=block-v1%3AOUNL%2BICS18%2B2018_1%2Btype%40sequential%2Bblock%400b957f040f954b6ab1f4e64b533ba65b");
			lesson31.addContent(content311);

			LessonDescriptor lesson32 = new LessonDescriptor(getUUID() ,"Tunneling","Tunneling","");
			module3.addLesson(lesson32);
			ContentDescriptor content321 = new ContentDescriptor(getUUID(), "Tunneling", "Tunneling", testCourseBaseURL+"courseware/78b706f2fb434606a4c342fb8db156be/b09ecadafbe541ba8876acfe92933139/?activate_block_id=block-v1%3AOUNL%2BICS18%2B2018_1%2Btype%40sequential%2Bblock%40b09ecadafbe541ba8876acfe92933139");
			lesson32.addContent(content321);

			LessonDescriptor lesson33 = new LessonDescriptor(getUUID() ,"Intrusion detection","Intrusion detection","");
			module3.addLesson(lesson33);
			ContentDescriptor content331 = new ContentDescriptor(getUUID(), "Intrusion detection", "Intrusion detection", testCourseBaseURL+"courseware/78b706f2fb434606a4c342fb8db156be/ee78d00f4a1a4780a9c0e72db9eae198/?activate_block_id=block-v1%3AOUNL%2BICS18%2B2018_1%2Btype%40sequential%2Bblock%40ee78d00f4a1a4780a9c0e72db9eae198");
			lesson33.addContent(content331);

			LessonDescriptor lesson34 = new LessonDescriptor(getUUID() ,"Wireless networking","Wireless networking","");
			module3.addLesson(lesson34);
			ContentDescriptor content341 = new ContentDescriptor(getUUID(), "Wireless networking", "Wireless networking", testCourseBaseURL+"courseware/78b706f2fb434606a4c342fb8db156be/f617869c98694cf9b6922d10cfa47695/?activate_block_id=block-v1%3AOUNL%2BICS18%2B2018_1%2Btype%40sequential%2Bblock%40f617869c98694cf9b6922d10cfa47695");
			lesson34.addContent(content341);

			
			ModuleDescriptor module4 = new ModuleDescriptor(getUUID(), "Online unfair behaviour", "Online unfair behaviour", "");
			course.addModule(module4);
			LessonDescriptor lesson41 = new LessonDescriptor(getUUID() ,"Risk  for social networks users: cyberbullying, phishing, and others","Risk  for social networks users: cyberbullying, phishing, and others","");
			module4.addLesson(lesson41);	
			ContentDescriptor content411 = new ContentDescriptor(getUUID(), "Risk  for social networks users: cyberbullying, phishing, and others", "Risk  for social networks users: cyberbullying, phishing, and others", testCourseBaseURL+"courseware/7fe10f9a5dd04ccfb094d49a903e7326/a43cb0f2df3645f7944e9d070dd89f5b/?activate_block_id=block-v1%3AOUNL%2BICS18%2B2018_1%2Btype%40sequential%2Bblock%40a43cb0f2df3645f7944e9d070dd89f5b");
			lesson41.addContent(content411);

			LessonDescriptor lesson42 = new LessonDescriptor(getUUID() ,"To share or not to share? That is the question","Practical advice on how to avoid unfair behaviours on social networks (content)","");
			module4.addLesson(lesson42);
			ContentDescriptor content421 = new ContentDescriptor(getUUID(), "To share or not to share? That is the question","Practical advice on how to avoid unfair behaviours on social networks (content)", testCourseBaseURL+"courseware/7fe10f9a5dd04ccfb094d49a903e7326/529b40350aaf4d8eada786eadfb88b31/?activate_block_id=block-v1%3AOUNL%2BICS18%2B2018_1%2Btype%40sequential%2Bblock%40529b40350aaf4d8eada786eadfb88b31");
			lesson42.addContent(content421);

			GoalDescriptor goal2 = new GoalDescriptor(getUUID(), module1.getTitle(), "I intend to participate in the course activities to learn about "+module1.getTitle(), "");
			ListIterator<LessonDescriptor> iterator = module1.getLessons();
			while (iterator.hasNext()) {
				goal2.addLesson(iterator.next());
			}
			course.addGoal(goal2);
			
			GoalDescriptor goal3 = new GoalDescriptor(getUUID(), module2.getTitle(), "I intend to participate in the course activities to learn about "+module2.getTitle(), "");
			iterator = module2.getLessons();
			while (iterator.hasNext()) {
				goal3.addLesson(iterator.next());
			}
			course.addGoal(goal3);
			
			GoalDescriptor goal4 = new GoalDescriptor(getUUID(), module3.getTitle(), "I intend to participate in the course activities to learn about "+module3.getTitle(), "");
			iterator = module3.getLessons();
			while (iterator.hasNext()) {
				goal4.addLesson(iterator.next());
			}
			course.addGoal(goal4);
			
			GoalDescriptor goal5 = new GoalDescriptor(getUUID(), module4.getTitle(), "I intend to participate in the course activities to learn about "+module4.getTitle(), "");
			iterator = module4.getLessons();
			while (iterator.hasNext()) {
				goal5.addLesson(iterator.next());
			}
			course.addGoal(goal5);

			GoalDescriptor goal1 = new GoalDescriptor(getUUID(), "Browsing the Course", "I intend to browse around", "");
			goal1.addCompletionGoal("100", "all materials (100%)");
			goal1.addCompletionGoal("70", "most materials (70%)");
			goal1.addCompletionGoal("40", "some materials (40%)");
			goal1.addCompletionGoal("10", "less than 10%");
			goal1.addCompletionGoal("0", "I have not decided yet");
			course.addGoal(goal1);
			
			instance.storeTestCourse(course);
		}
		
		return course;
	}
	
	
	public static User getUser(String userid, String userName) {
		User user = null;
		try {
			user = PersistentStore.getUser(userid);
		} catch (Exception e) {
			//e.printStackTrace();
			user = null;
		}
		if (user == null) {
			user = new User(userName, userid);
			user.setTreatmentGroup(random.nextBoolean());
			if (!userGuest.equals(user.getName()) && !userUnknown.equals(userid)) {
				try {
					PersistentStore.writeUser(user);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return user;
		
	}
	
	
	public static UserPlan getUserPlan(User user, CourseDescriptor course) {
		UserPlan userPlan = PersistentStore.readUserPlan(user, course);
		if (userPlan == null) {
			userPlan = createUserPlan(user, course);
		}
		userPlan.calculateAchievementRates();
		return userPlan;
	}
	
	
	public static UserPlan createUserPlan(User user, CourseDescriptor course) {
		System.out.println("create userPlan for "+user.getName());
		UserPlan userPlan = new UserPlan(getUUID(), user);
		userPlan.setCourse(course);
		
		return userPlan;
	}
	
	public static UserGoal createUserGoal(UserPlan userPlan, GoalDescriptor goal) {
		UserGoal userGoal = new UserGoal(getUUID(), userPlan.getUser(), goal);
		
		return userGoal;
	}
	
	public static UserLesson createUserLesson(UserGoal goal, LessonDescriptor lesson) {
		UserLesson userLesson = new UserLesson(getUUID(), goal.getUser(), lesson);
		
		for (ContentDescriptor content : lesson.contents) {
			UserContent userContent = new UserContent(getUUID(), goal.getUser(), content);
			userLesson.addContent(userContent);
		}
		
		return userLesson;
	}
	
	public static UserContent createUserContent(UserLesson lesson, ContentDescriptor content) {
		UserContent userContent = new UserContent(getUUID(), lesson.getUser(), content);
		
		return userContent;
	}
	
	
	public static void trackAndLogEvent(HttpServletRequest request, HttpServletResponse response, String logType) {
		HttpSession session = request.getSession();
		User user = (User)session.getAttribute("user");
		CourseDescriptor course = (CourseDescriptor)session.getAttribute("course");
		UserPlan userPlan = (UserPlan)session.getAttribute("userPlan");
		
		HashMap<String, String[]> logParameters = new HashMap<>();
		logParameters.put("user", new String[] {user.getId()});
		logParameters.put("course", new String[] {course.getId()});
		logParameters.put("userPlan", new String[] {userPlan.getId()});
		logParameters.put("logType", new String[] {logType});
		logParameters.put("timestamp", new String[] {(new Date()).toString()});
		
		logParameters.putAll(request.getParameterMap());
		
		Gson gson = new Gson();
		String jsonObject = gson.toJson(logParameters);

		System.out.println("[LOG] " + jsonObject + " [/LOG]");
		if ("intention".equals(logType)) {
			if (logParameters.containsKey("goalSelectRadio")) {
				String[] goals = logParameters.get("goalSelectRadio");
				for (String goal: goals) {
					logParameters.remove("goal"+goal);
				}
			}
		}
		try {
			PersistentStore.writeLog(logParameters);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// TODO: update user plan based on event, if it is of logType "track"
		if ("track".equals(logType)) {
			String activityType = request.getParameter("activityType");
			if (activityType == null) {
				activityType = StimulatedPlanningFactory.ACTIVITY_TYPE_ACCESS;
			}
			String page = request.getParameter("page");
			if (page != null) {
				if (userPlan.trackLearningProgress(page, activityType)) {
					try {
						PersistentStore.writeDescriptor(userPlan);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			
		}
	}
	

	
	public static HttpSession initializeSession(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		String userNameR = request.getParameter("userName");
		String useridR = request.getParameter("userid");

		if (userNameR == null || useridR == null) {
			userNameR = userGuest;
			useridR = userUnknown;
		}
		
//		System.out.println("userNameR: "+userNameR+", useridR: "+useridR);
		
		String userName = (String)session.getAttribute("userName");
		String userid = (String)session.getAttribute("userid");
		
//		System.out.println("userName: "+userName+", userid: "+userid);

		if (userName == null || userid == null 
				|| (!userName.equals(userNameR) && !userNameR.equals(userGuest)) 
				|| (!userid.equals(useridR) && !useridR.equals(userUnknown))) {
			userName = userNameR;
			userid = useridR;
			session.setAttribute("userName", userName);
			session.setAttribute("userid", userid);
		}

//		System.out.println("userName(2): "+userName+", userid(2): "+userid);

		User user = (User)session.getAttribute("user");
//		if (user != null) {
//			System.out.println("user.name(1): "+user.getName()+", user.id(1): "+user.getId());
//		} else {
//			System.out.println("user.name(1): null, user.id(1): null");
//		}
		
		if (user == null 
				|| ((userid != null && !userid.equals(user.getId()) && !userid.equals(userUnknown)) 
				|| (userName != null && !userName.equals(user.getName()) && !userName.equals(userGuest)))) {
			user = getUser(userid, userName);
			session.setAttribute("user", user);
		}
		
		System.out.println("user.name: "+user.getName()+", user.id: "+user.getId());

		CourseDescriptor course = (CourseDescriptor)session.getAttribute("course");
		if (course == null) {
			course = StimulatedPlanningFactory.generateTestCourse();
			session.setAttribute("course", course);
		}
		
		UserPlan userPlan = (UserPlan)session.getAttribute("userPlan");
		if (userPlan == null || !user.getId().equals(userPlan.getUser().getId())) {
			userPlan = StimulatedPlanningFactory.getUserPlan(user, course);
			session.setAttribute("userPlan", userPlan);
		}
	
		HashArrayList<GoalDescriptor> selectedGoals = new HashArrayList<GoalDescriptor>();
		HashArrayList<LessonDescriptor> selectedLessons = new HashArrayList<LessonDescriptor>();

		for (UserGoal ugoal : userPlan.goals) {
			selectedGoals.add(ugoal.getGoalDescriptor());
			for (UserLesson ulesson : ugoal.lessons) {
				selectedLessons.add(ulesson.getLesson());
			}
		}
		
		System.out.println("userGoals: "+userPlan.goals.size()+", planItems: "+userPlan.planItems.size());
		System.out.println("selectedGoals: "+selectedGoals.size()+", selectedLessons: "+selectedLessons.size());
		
		session.setAttribute("selectedGoals", selectedGoals);
		session.setAttribute("selectedLessons", selectedLessons);

		return session;
	}

	public static HttpSession clearSession(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		
		Enumeration<String> attributes = session.getAttributeNames();
		ArrayList<String> attributesToRemove = new ArrayList<>();
		
		while (attributes.hasMoreElements()) {
			String attribute = attributes.nextElement();
			attributesToRemove.add(attribute);
		}
		
		for (String attribute : attributesToRemove) {
			session.removeAttribute(attribute);
		}
		
		// TODO remove again!
		// PersistentStore.clearLog();
		
		return session;
	}

}
