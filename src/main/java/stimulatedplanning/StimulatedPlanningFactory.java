package stimulatedplanning;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.appengine.api.datastore.Entity;
import com.google.gson.Gson;

import senseofcommunity.Clan;
import senseofcommunity.SelectionObject;
import senseofcommunity.SelectionOption;
import senseofcommunity.SoC_CourseCreationFactory;
import senseofcommunity.SoC_ProductionCourseCreationFactory;
import senseofcommunity.UserOnlineStatus;
import senseofcommunity.UserSelectedOption;
import stimulatedplanning.util.HashArrayList;
import stimulatedplanning.util.IObjectWithId;
import stimulatedplanning.util.UserProfileCVS;

import chat.*;

public class StimulatedPlanningFactory {
	public static final StimulatedPlanningFactory instance = new StimulatedPlanningFactory();
	public static Random random = new Random();
	private static final Logger log = Logger.getLogger(StimulatedPlanningFactory.class.getName());   

	private StimulatedPlanningFactory() {
		// TODO Auto-generated constructor stub
	}
	
	private CourseDescriptor testCourse;
	//public static final String platformHomeUrl = "https://ou.edia.nl/";
	//public static final String planningUrl = "https://ou.edia.nl/courses/course-v1:OUNL+ICS18+2018_1/courseware/c440e614880f44cab61666f5783994c3/9c1b1985a20e4289b02f5e95c61e4485/?activate_block_id=block-v1%3AOUNL%2BICS18%2B2018_1%2Btype%40sequential%2Bblock%409c1b1985a20e4289b02f5e95c61e4485";
	//public static final String platformHomeUrl = "https://edge.edx.org/";
	//public static final String planningUrl = "https://edge.edx.org/courses/course-v1:DelftX+Sandbox_Welten+2018/courseware/ca508c0a998744d9baca503243547577/a42163024f11463eb492c34128bc859d/1?activate_block_id=block-v1%3ADelftX%2BSandbox_Welten%2B2018%2Btype%40vertical%2Bblock%4059532b2c3bf34650a960d73198deedb0";
	public static final String platformHomeUrl = "https://localhost/";
	public static final String planningUrl = "https://localhost/courses/course-v1:DelftX+Sandbox_Welten+2018/courseware/ca508c0a998744d9baca503243547577/a42163024f11463eb492c34128bc859d/1?activate_block_id=block-v1%3ADelftX%2BSandbox_Welten%2B2018%2Btype%40vertical%2Bblock%4059532b2c3bf34650a960d73198deedb0";
	//private static final String testCourseId = "ICS18";
	//public static final String testCourseId = "SBW18";
	//public static final String testCourseId = "TCC01";
	//public static final String testCourseId = SoC_ProductionCourseCreationFactory.prodCourseId;
	public static final String testCourseId = SP_TLA_ProductionCourseCreationFactory.prodCourseId;
	//public static final String testCourseId = SP_CircularX_ProductionCourseCreationFactory.prodCourseId;
	//private static final String testCourseBaseURL = "https://ou.edia.nl/courses/course-v1:OUNL+ICS18+2018_1/";
	//public static final String testCourseBaseURL = "https://edge.edx.org/courses/course-v1:DelftX+Sandbox_Welten+2018/";
	//public static final String testCourseBaseURL = "https://localhost/courses/course-v1:DelftX+Sandbox_Welten+2018/";
	//public static final String testCourseBaseURL = "https://ou.acc.edia.nl/courses/course-v1:OUNL+TCC01+2019_01/courseware/";
	public static final String testCourseBaseURL = SP_TLA_ProductionCourseCreationFactory.prodCourseBaseURL;
	//public static final String testCourseBaseURL = SP_CircularX_ProductionCourseCreationFactory.prodCourseBaseURL;

	public static final String testCourseEditURL = SP_TLA_ProductionCourseCreationFactory.prodCourseEditURL;
	//public static final String testCourseEditURL = SP_CircularX_ProductionCourseCreationFactory.prodCourseEditURL;

	//public static final String applicationHome = "http://localhost:8080";
	//public static final String applicationHome = "https://stimulatedplanning-circularx.appspot.com";
	public static final String applicationHome = "https://stimulatedplanning-tla.appspot.com";
	//public static final String applicationHome = "https://senseofcommunity-225200.appspot.com";
	//public static final String applicationHome = "https://senseofcommunity-test.appspot.com";
	
	public static final String userUnknown = "unknown";
	public static final String userGuest = "Guest";
	
	public static final String ACTIVITY_TYPE_ACCESS = "access";
	public static final String ACTIVITY_TYPE_COMPLETE = "complete";
	
	private HashMap<String, CacheableDatabaseObject> courseObjects = new HashMap<>();
	private HashArrayList<Clan> clans = new HashArrayList<>();
	private HashArrayList<User> controlUsers = new HashArrayList<>();
	
	private CourseDescriptor retrieveTestCourse() {
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
	
	/**
	 * generate the structure for the course to be used according to the acc staging platform test structure.
	 * @return
	 */
	public static CourseDescriptor generateProductionCourse() {
		return SoC_ProductionCourseCreationFactory.generateProductionCourse();
	}

	/**
	 * generate the structure for the course to be used according to dev test structure.
	 * @return
	 */
	public static CourseDescriptor generateDevTestCourse() {
		getOrGenerateClans();
		CourseDescriptor course = instance.retrieveTestCourse();
		if (course == null) {
			
			// Course
			
			course = new CourseDescriptor(testCourseId, 
					"Security Dev Test Course", 
					"Security Dev Test Course", 
					testCourseBaseURL+"course/");
	
			//
			// Week 1
			//
			
			SoC_CourseCreationFactory.generateWeek1(course);
			
			//
			// Module 2
			//
			
			SoC_CourseCreationFactory.generateWeek2(course);
			
			//
			// Module 3
			//
			
			SoC_CourseCreationFactory.generateWeek3(course);
			
			//
			// Module 4
			//
			
			SoC_CourseCreationFactory.generateWeek4(course);
			
			//
			// Goal
			//
			
			GoalDescriptor goal = new GoalDescriptor(getUUID(), "Browsing the Course", "I intend to browse around", "");
			goal.addCompletionGoal("100", "all materials (100%)");
			goal.addCompletionGoal("70", "most materials (70%)");
			goal.addCompletionGoal("40", "some materials (40%)");
			goal.addCompletionGoal("10", "less than 10%");
			goal.addCompletionGoal("0", "I have not decided yet");
			course.addGoal(goal);
			
			instance.storeTestCourse(course);
		}
	
		// Browsing goal for course
	
		return course;
	}

	/**
	 * generate the structure for the course to be used according to the acc staging platform test structure.
	 * @return
	 */
	public static CourseDescriptor generateAccTestCourse() {
		getOrGenerateClans();
		
		SoC_CourseCreationFactory.week1deadline = new GregorianCalendar(2019, 0, 27, 12, 0).getTime();
		SoC_CourseCreationFactory.week2deadline = new GregorianCalendar(2019, 1, 3, 12, 0).getTime();
		SoC_CourseCreationFactory.week3deadline = new GregorianCalendar(2019, 1, 10, 12, 0).getTime();
		SoC_CourseCreationFactory.week4deadline = new GregorianCalendar(2019, 1, 17, 12, 0).getTime();
		
		CourseDescriptor course = instance.retrieveTestCourse();
		if (course == null) {
			
			// Course
			
			course = new CourseDescriptor(SoC_CourseCreationFactory.accCourseId, 
					"How Cryptography Keeps The Internet Secure", 
					"How Cryptography Keeps The Internet Secure", 
					SoC_CourseCreationFactory.accCourseBaseURL);
	
			//
			// Week 1
			//
			
			SoC_CourseCreationFactory.generateAccWeek1(course);
			
			//
			// Module 2
			//
			
			SoC_CourseCreationFactory.generateAccWeek2(course);
			
			//
			// Module 3
			//
			
			SoC_CourseCreationFactory.generateAccWeek3(course);
			
			//
			// Module 4
			//
			
			SoC_CourseCreationFactory.generateAccWeek4(course);
			
			//
			// Goal
			//
			
			GoalDescriptor goal = new GoalDescriptor(getUUID(), "Browsing the Course", "I intend to browse around", "");
			goal.addCompletionGoal("100", "all materials (100%)");
			goal.addCompletionGoal("70", "most materials (70%)");
			goal.addCompletionGoal("40", "some materials (40%)");
			goal.addCompletionGoal("10", "less than 10%");
			goal.addCompletionGoal("0", "I have not decided yet");
			course.addGoal(goal);
			
			instance.storeTestCourse(course);
		}
		
		return course;
	}

	public static boolean hasObject(String className, String id) {
		return instance.courseObjects.containsKey(className+"_"+id);
	}
	
	public static void addObject(IObjectWithId object, Entity entity) {
		CacheableDatabaseObject cdo = null;
		String key = object.getClass().getName()+"_"+object.getId();
		if (instance.courseObjects.containsKey(key)) {
			cdo = instance.courseObjects.get(key);
		}
		if (cdo == null || cdo.getObject() != object || cdo.getEntity() != entity || cdo.isExpired()) {
			cdo = new CacheableDatabaseObject(entity, object);
			if (object instanceof UserSelectedOption) {
				cdo.setExpires(20);
			} else if (object instanceof GenericUserObject) {
				cdo.setExpires(60);
			} else if (object instanceof User) {
				cdo.setExpires(180);
			}
			instance.courseObjects.put(cdo.getCacheId(), cdo);
		}
	}
	
	public static void removeObject(String className, String id) {
		String key = className+"_"+id;
		if (instance.courseObjects.containsKey(key)) {
			instance.courseObjects.remove(key);
		}
	}
	
	public static IObjectWithId getObject(String className, String id) {
		String key = className+"_"+id;
		if (!instance.courseObjects.containsKey(key)) {
			log.info("Warning: trying to retrieve object not in Map: "+key);
			//new Exception().printStackTrace();
			return null;
		}
		if (instance.courseObjects.get(key).isExpired()) {
			log.info("trying to update object that is expired: "+key);
			CacheableDatabaseObject cdo = instance.courseObjects.get(key);
			instance.courseObjects.remove(key);
			PersistentStore.updateCacheableObject(cdo);
			if (!instance.courseObjects.containsKey(key)) {
				log.info("updating cacheable object failed: "+key);
				new Exception().printStackTrace();
				return null;
			}
		}
		return instance.courseObjects.get(key).getObject();
	}
	
	public static Entity getEntity(String className, String id) {
		String key = className+"_"+id;
		if (!instance.courseObjects.containsKey(key)) {
			log.info("Warning: trying to retrieve entity not in Map: "+key);
			return null;
		}
		return instance.courseObjects.get(key).getEntity();
	}
	
	public static void addClan(Clan clan) {
		instance.clans.add(clan);
	}
	
	public static Clan getClan(String id) {
		if (instance.clans.size() == 0) {
			getOrGenerateClans();
		}
		return instance.clans.get(id);
	}
	
	public static int getNoOfClans() {
		return instance.clans.size();
	}
	
	public static HashArrayList<Clan> getClans(String id) {
		if (instance.clans.size() == 0) {
			getOrGenerateClans();
		}
		return instance.clans;
	}
	
	public static Clan getOtherClan(Clan clan) {
		if (instance.clans.size() == 0) {
			getOrGenerateClans();
		}
		for (Clan otherClan : instance.clans) {
			if (!clan.getId().equals(otherClan.getId())) {
				return otherClan;
			}
		}
		return null;
	}
	
	public static String getUUID() {
		
		UUID uuid = UUID.randomUUID();
        return uuid.toString();
	}


	/**
	 * generate the structure for the course to be used according to given structure.
	 * @return
	 */
	public static CourseDescriptor generateTestCourse() {
		getOrGenerateClans();
		CourseDescriptor course = instance.retrieveTestCourse();
		if (course == null) {
			// Course
			//course = SP_CircularX_ProductionCourseCreationFactory.generateProductionCourse();
			course = SP_TLA_ProductionCourseCreationFactory.generateProductionCourse();
			//course = generateProductionCourse();
			//course = StimulatedPlanningFactory.generateAccTestCourse();
			//course = generateDevTestCourse();
			instance.storeTestCourse(course);
		}		
		return course;
	}	

	protected static HashArrayList<Clan> getOrGenerateClans() {
		if (instance.clans.size() == 0) {
			instance.clans = PersistentStore.readAllClans();
		}
		if (instance.clans.size() == 0) {
			Clan clan1 = new Clan(Clan.CLAN_1_ID, 
					"Defenders", 
					"Defenders", "");
			instance.clans.add(clan1);
			
			Clan clan2 = new Clan(Clan.CLAN_2_ID, 
					"Hackers", 
					"Hackers", "");
			instance.clans.add(clan2);
			try {
				PersistentStore.writeDescriptor(clan1);
				PersistentStore.writeDescriptor(clan2);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return instance.clans;
	}

	
	private static HashArrayList<User> getOrGenerateControlUsers() {
		if (instance.controlUsers.size() == 0) {
			instance.controlUsers = PersistentStore.readAllControlUsers();
		}
		return instance.controlUsers;
	}
	
	public static void addControlUser(User controlUser) {
		instance.controlUsers.add(controlUser);
	}

	
	





	

	
	// WEEK 1 INTERACTIVE ELEMENTS
	
	
	
	
	// WEEK 2 interactive elements



	/**
	 * generate the structure for the course to be used according to Sandbox structure.
	 * @return
	 */
	public static CourseDescriptor generateTestCourseSandbox() {
		CourseDescriptor course = instance.retrieveTestCourse();
		if (course == null) {
			course = new CourseDescriptor(instance.testCourseId, 
					"Sandbox Welten Institute", 
					"Sandbox Welten Institute", 
					testCourseBaseURL+"course/");
//			ModuleDescriptor module1 = new ModuleDescriptor(getUUID(), 
//					"Introduction to the course", 
//					"Introduction to the course", "");
//			course.addModule(module1);
//
//			LessonDescriptor lesson11 = new LessonDescriptor(getUUID() ,
//					"Video Intro",
//					"Video Intro","");
//			module1.addLesson(lesson11);	
//			ContentDescriptor content111 = new ContentDescriptor(getUUID(), 
//					"INTRODUCTION", 
//					"INTRODUCTION", 
//					testCourseBaseURL+"courseware/ca508c0a998744d9baca503243547577/a79ca281987e4b1e81abea208a99c567/"); // ?activate_block_id=block-v1%3AOUNL%2BICS18%2B2018_1%2Btype%40sequential%2Bblock%4048fbe0cee4684603bf2660afd51ca53a");
//			lesson11.addContent(content111);
//			
//			LessonDescriptor lesson12 = new LessonDescriptor(getUUID() ,
//					"Make your plan",
//					"Make your plan","");
//			module1.addLesson(lesson12);
//			ContentDescriptor content121 = new ContentDescriptor(getUUID(), 
//					"What would you like to achieve in this course?", 
//					"What would you like to achieve in this course?", 
//					testCourseBaseURL+"courseware/ca508c0a998744d9baca503243547577/a42163024f11463eb492c34128bc859d/"); //?activate_block_id=block-v1%3AOUNL%2BICS18%2B2018_1%2Btype%40sequential%2Bblock%40ab58463db1e24ad9b885d8abc40f5a44");
//			lesson12.addContent(content121);

//			LessonDescriptor lesson13 = new LessonDescriptor(getUUID() ,
//					"Security principles",
//					"Security principles","");
//			module1.addLesson(lesson13);
//			ContentDescriptor content131 = new ContentDescriptor(getUUID(), 
//					"Security principles", 
//					"Security principles", 
//					testCourseBaseURL+"courseware/174c8dadf4964842bfee47a8242dc530/119612eb80e94835a5c8228aefad1a54/"); //?activate_block_id=block-v1%3AOUNL%2BICS18%2B2018_1%2Btype%40sequential%2Bblock%40119612eb80e94835a5c8228aefad1a54");
//			lesson13.addContent(content131);
//
//			LessonDescriptor lesson14 = new LessonDescriptor(getUUID() ,
//					"Cryptography concepts",
//					"Cryptography concepts","");
//			module1.addLesson(lesson14);
//			ContentDescriptor content141 = new ContentDescriptor(getUUID(), 
//					"Cryptography concepts", 
//					"Cryptography concepts", 
//					testCourseBaseURL+"courseware/174c8dadf4964842bfee47a8242dc530/e67b556c491441108300988be46a67ac/"); //?activate_block_id=block-v1%3AOUNL%2BICS18%2B2018_1%2Btype%40sequential%2Bblock%40e67b556c491441108300988be46a67ac");
//			lesson14.addContent(content141);
//
//			LessonDescriptor lesson15 = new LessonDescriptor(getUUID() ,
//					"Cryptography into use",
//					"Cryptography into use","");
//			module1.addLesson(lesson15);
//			ContentDescriptor content151 = new ContentDescriptor(getUUID(), 
//					"Cryptography into use", 
//					"Cryptography into use", 
//					testCourseBaseURL+"courseware/174c8dadf4964842bfee47a8242dc530/a0956c0634614c039f6a6b6d6dcea3fd/"); //?activate_block_id=block-v1%3AOUNL%2BICS18%2B2018_1%2Btype%40sequential%2Bblock%40e67b556c491441108300988be46a67ac");
//			lesson15.addContent(content151);

			
			ModuleDescriptor module2 = new ModuleDescriptor(getUUID(), 
					"Module 1- Computer Security Principles", 
					"Module 1- Computer Security Principles", "");
			course.addModule(module2);
			LessonDescriptor lesson21 = new LessonDescriptor(getUUID() ,
					"module1. lesson 1",
					"module1. lesson 1","");
			module2.addLesson(lesson21);	
			ContentDescriptor content211 = new ContentDescriptor(getUUID(), 
					"The Security Principles", 
					"The Security Principles", 
					testCourseBaseURL+"courseware/4cb17259b1024410901476642c28df19/1ff9b47cae3b4d51bbfa22b458c4a25d/"); //?activate_block_id=block-v1%3AOUNL%2BICS18%2B2018_1%2Btype%40sequential%2Bblock%4083d79097d5d94304a6fa9a5aed25dce3");
			lesson21.addContent(content211);

//			LessonDescriptor lesson23 = new LessonDescriptor(getUUID() ,
//					"Passwords",
//					"What happens on the network when you enter a password","");
//			module2.addLesson(lesson23);
//			ContentDescriptor content231 = new ContentDescriptor(getUUID(), 
//					"How passwords work? How can they be attacked? And how can we setup them correctly?", 
//					"How passwords work? How can they be attacked? And how can we setup them correctly?", 
//					testCourseBaseURL+"courseware/7f3c8908698a4fa785c2bab76dc403c9/0437f85591cc4e0c964437b4d1bfad1e/"); //?activate_block_id=block-v1%3AOUNL%2BICS18%2B2018_1%2Btype%40sequential%2Bblock%400437f85591cc4e0c964437b4d1bfad1e");
//			lesson23.addContent(content231);
//
//			LessonDescriptor lesson26 = new LessonDescriptor(getUUID() ,
//					"Password managers",
//					"Password managers","");
//			module2.addLesson(lesson26);
//			ContentDescriptor content261 = new ContentDescriptor(getUUID(), 
//					"Password managers", 
//					"Password managers", 
//					testCourseBaseURL+"courseware/7f3c8908698a4fa785c2bab76dc403c9/d75f8af0def547d4b547ef4056a03b43/"); //?activate_block_id=block-v1%3AOUNL%2BICS18%2B2018_1%2Btype%40sequential%2Bblock%40d75f8af0def547d4b547ef4056a03b43");
//			lesson26.addContent(content261);

			
			ModuleDescriptor module3 = new ModuleDescriptor(getUUID(), 
					"Module 2- Tips on how to protect your computer", 
					"Module 2- Tips on how to protect your computer", "");
			course.addModule(module3);
			LessonDescriptor lesson31 = new LessonDescriptor(getUUID() ,
					"Threats and attacks",
					"Threats and attacks","");
			module3.addLesson(lesson31);	
			ContentDescriptor content311 = new ContentDescriptor(getUUID(), 
					"The basic concept of malware", 
					"The basic concept of malware", 
					testCourseBaseURL+"courseware/651e1c7c25404fe0b445da92d7f76aba/5141a1c901e842f8bfb186a365cef36b/"); //?activate_block_id=block-v1%3AOUNL%2BICS18%2B2018_1%2Btype%40sequential%2Bblock%400b957f040f954b6ab1f4e64b533ba65b");
			lesson31.addContent(content311);
			ContentDescriptor content312 = new ContentDescriptor(getUUID(), 
					"Passwords", 
					"Passwords", 
					testCourseBaseURL+"courseware/651e1c7c25404fe0b445da92d7f76aba/5141a1c901e842f8bfb186a365cef36b/"); //?activate_block_id=block-v1%3AOUNL%2BICS18%2B2018_1%2Btype%40sequential%2Bblock%400b957f040f954b6ab1f4e64b533ba65b");
			lesson31.addContent(content312);
			ContentDescriptor content313 = new ContentDescriptor(getUUID(), 
					"Passwords Managers", 
					"What are and how passwords manager programs work?", 
					testCourseBaseURL+"courseware/651e1c7c25404fe0b445da92d7f76aba/5141a1c901e842f8bfb186a365cef36b/"); //?activate_block_id=block-v1%3AOUNL%2BICS18%2B2018_1%2Btype%40sequential%2Bblock%400b957f040f954b6ab1f4e64b533ba65b");
			lesson31.addContent(content313);

//			LessonDescriptor lesson32 = new LessonDescriptor(getUUID() ,
//					"Firewall",
//					"Firewall","");
//			module3.addLesson(lesson32);
//			ContentDescriptor content321 = new ContentDescriptor(getUUID(), 
//					"Firewall", 
//					"Firewall", 
//					testCourseBaseURL+"courseware/78b706f2fb434606a4c342fb8db156be/b09ecadafbe541ba8876acfe92933139/"); //?activate_block_id=block-v1%3AOUNL%2BICS18%2B2018_1%2Btype%40sequential%2Bblock%40b09ecadafbe541ba8876acfe92933139");
//			lesson32.addContent(content321);
//
//			LessonDescriptor lesson33 = new LessonDescriptor(getUUID() ,
//					"Tunneling",
//					"Tunneling","");
//			module3.addLesson(lesson33);
//			ContentDescriptor content331 = new ContentDescriptor(getUUID(), 
//					"Tunneling", 
//					"Tunneling", 
//					testCourseBaseURL+"courseware/78b706f2fb434606a4c342fb8db156be/ee78d00f4a1a4780a9c0e72db9eae198/"); //?activate_block_id=block-v1%3AOUNL%2BICS18%2B2018_1%2Btype%40sequential%2Bblock%40ee78d00f4a1a4780a9c0e72db9eae198");
//			lesson33.addContent(content331);
//
//			LessonDescriptor lesson34 = new LessonDescriptor(getUUID() ,
//					"Intrusion detection",
//					"Intrusion detection","");
//			module3.addLesson(lesson34);
//			ContentDescriptor content341 = new ContentDescriptor(getUUID(), 
//					"Intrusion detection", 
//					"Intrusion detection", 
//					testCourseBaseURL+"courseware/78b706f2fb434606a4c342fb8db156be/f617869c98694cf9b6922d10cfa47695/"); //?activate_block_id=block-v1%3AOUNL%2BICS18%2B2018_1%2Btype%40sequential%2Bblock%40f617869c98694cf9b6922d10cfa47695");
//			lesson34.addContent(content341);
//
//			
//			ModuleDescriptor module4 = new ModuleDescriptor(getUUID(), 
//					"Risks for Social Networks Users", 
//					"Risks for Social Networks Users", "");
//			course.addModule(module4);
//			LessonDescriptor lesson41 = new LessonDescriptor(getUUID() ,
//					"Risk  for social networks users",
//					"Risk  for social networks users","");
//			module4.addLesson(lesson41);	
//			ContentDescriptor content411 = new ContentDescriptor(getUUID(), 
//					"Risk  for social networks users", 
//					"Risk  for social networks users", 
//					testCourseBaseURL+"courseware/7fe10f9a5dd04ccfb094d49a903e7326/a43cb0f2df3645f7944e9d070dd89f5b/"); //?activate_block_id=block-v1%3AOUNL%2BICS18%2B2018_1%2Btype%40sequential%2Bblock%40a43cb0f2df3645f7944e9d070dd89f5b");
//			lesson41.addContent(content411);


//			GoalDescriptor goal2 = new GoalDescriptor(getUUID(), module1.getTitle(), 
//					"I intend to participate in the course activities to learn about "+module1.getTitle(), "");
//			ListIterator<LessonDescriptor> iterator = module1.getLessons();
//			while (iterator.hasNext()) {
//				goal2.addLesson(iterator.next());
//			}
//			course.addGoal(goal2);
			
			GoalDescriptor goal3 = new GoalDescriptor(getUUID(), module2.getTitle(), 
					"I intend to participate in the course activities to learn about "+module2.getTitle(), "");
			ListIterator<LessonDescriptor> iterator = module2.getLessons();
			while (iterator.hasNext()) {
				goal3.addLesson(iterator.next());
			}
			course.addGoal(goal3);
			
			GoalDescriptor goal4 = new GoalDescriptor(getUUID(), module3.getTitle(), 
					"I intend to participate in the course activities to learn about "+module3.getTitle(), "");
			iterator = module3.getLessons();
			while (iterator.hasNext()) {
				goal4.addLesson(iterator.next());
			}
			course.addGoal(goal4);
			
//			GoalDescriptor goal5 = new GoalDescriptor(getUUID(), module4.getTitle(), 
//					"I intend to participate in the course activities to learn about "+module4.getTitle(), "");
//			iterator = module4.getLessons();
//			while (iterator.hasNext()) {
//				goal5.addLesson(iterator.next());
//			}
//			course.addGoal(goal5);

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
	
	
	public static CourseDescriptor generateTestCourseICS18() {
		CourseDescriptor course = instance.retrieveTestCourse();
		if (course == null) {
			course = new CourseDescriptor(instance.testCourseId, 
					"Introduction to Computer Security", 
					"Introduction to Computer Security", 
					testCourseBaseURL+"info");
			ModuleDescriptor module1 = new ModuleDescriptor(getUUID(), 
					"Introduction to Computer Security", 
					"Introduction to Computer Security", "");
			course.addModule(module1);

			LessonDescriptor lesson11 = new LessonDescriptor(getUUID() ,
					"The fundamental concepts (C.I.A.)",
					"The fundamental concepts (C.I.A.)","");
			module1.addLesson(lesson11);	
			ContentDescriptor content111 = new ContentDescriptor(getUUID(), 
					"The fundamental concepts (C.I.A.)", 
					"The fundamental concepts (C.I.A.)", 
					testCourseBaseURL+"courseware/174c8dadf4964842bfee47a8242dc530/48fbe0cee4684603bf2660afd51ca53a/"); // ?activate_block_id=block-v1%3AOUNL%2BICS18%2B2018_1%2Btype%40sequential%2Bblock%4048fbe0cee4684603bf2660afd51ca53a");
			lesson11.addContent(content111);
			
			LessonDescriptor lesson12 = new LessonDescriptor(getUUID() ,
					"Assurance, Authenticity, and Anonymity",
					"Assurance, Authenticity, and Anonymity","");
			module1.addLesson(lesson12);
			ContentDescriptor content121 = new ContentDescriptor(getUUID(), 
					"Assurance, Authenticity, and Anonymity", 
					"Assurance, Authenticity, and Anonymity", 
					testCourseBaseURL+"courseware/174c8dadf4964842bfee47a8242dc530/ab58463db1e24ad9b885d8abc40f5a44/"); //?activate_block_id=block-v1%3AOUNL%2BICS18%2B2018_1%2Btype%40sequential%2Bblock%40ab58463db1e24ad9b885d8abc40f5a44");
			lesson12.addContent(content121);

			LessonDescriptor lesson13 = new LessonDescriptor(getUUID() ,
					"Security principles",
					"Security principles","");
			module1.addLesson(lesson13);
			ContentDescriptor content131 = new ContentDescriptor(getUUID(), 
					"Security principles", 
					"Security principles", 
					testCourseBaseURL+"courseware/174c8dadf4964842bfee47a8242dc530/119612eb80e94835a5c8228aefad1a54/"); //?activate_block_id=block-v1%3AOUNL%2BICS18%2B2018_1%2Btype%40sequential%2Bblock%40119612eb80e94835a5c8228aefad1a54");
			lesson13.addContent(content131);

			LessonDescriptor lesson14 = new LessonDescriptor(getUUID() ,
					"Cryptography concepts",
					"Cryptography concepts","");
			module1.addLesson(lesson14);
			ContentDescriptor content141 = new ContentDescriptor(getUUID(), 
					"Cryptography concepts", 
					"Cryptography concepts", 
					testCourseBaseURL+"courseware/174c8dadf4964842bfee47a8242dc530/e67b556c491441108300988be46a67ac/"); //?activate_block_id=block-v1%3AOUNL%2BICS18%2B2018_1%2Btype%40sequential%2Bblock%40e67b556c491441108300988be46a67ac");
			lesson14.addContent(content141);

			LessonDescriptor lesson15 = new LessonDescriptor(getUUID() ,
					"Cryptography into use",
					"Cryptography into use","");
			module1.addLesson(lesson15);
			ContentDescriptor content151 = new ContentDescriptor(getUUID(), 
					"Cryptography into use", 
					"Cryptography into use", 
					testCourseBaseURL+"courseware/174c8dadf4964842bfee47a8242dc530/a0956c0634614c039f6a6b6d6dcea3fd/"); //?activate_block_id=block-v1%3AOUNL%2BICS18%2B2018_1%2Btype%40sequential%2Bblock%40e67b556c491441108300988be46a67ac");
			lesson15.addContent(content151);

			
			ModuleDescriptor module2 = new ModuleDescriptor(getUUID(), 
					"Understand how to secure your PC", 
					"Understand how to secure your PC", "");
			course.addModule(module2);
			LessonDescriptor lesson21 = new LessonDescriptor(getUUID() ,
					"Threats and Attacks - basic concept of Malware",
					"Threats and Attacks - basic concept of Malware","");
			module2.addLesson(lesson21);	
			ContentDescriptor content211 = new ContentDescriptor(getUUID(), 
					"Threats and Attacks - basic concept of Malware", 
					"Threats and Attacks - basic concept of Malware", 
					testCourseBaseURL+"courseware/7f3c8908698a4fa785c2bab76dc403c9/83d79097d5d94304a6fa9a5aed25dce3/"); //?activate_block_id=block-v1%3AOUNL%2BICS18%2B2018_1%2Btype%40sequential%2Bblock%4083d79097d5d94304a6fa9a5aed25dce3");
			lesson21.addContent(content211);

			LessonDescriptor lesson23 = new LessonDescriptor(getUUID() ,
					"Passwords",
					"What happens on the network when you enter a password","");
			module2.addLesson(lesson23);
			ContentDescriptor content231 = new ContentDescriptor(getUUID(), 
					"How passwords work? How can they be attacked? And how can we setup them correctly?", 
					"How passwords work? How can they be attacked? And how can we setup them correctly?", 
					testCourseBaseURL+"courseware/7f3c8908698a4fa785c2bab76dc403c9/0437f85591cc4e0c964437b4d1bfad1e/"); //?activate_block_id=block-v1%3AOUNL%2BICS18%2B2018_1%2Btype%40sequential%2Bblock%400437f85591cc4e0c964437b4d1bfad1e");
			lesson23.addContent(content231);

			LessonDescriptor lesson26 = new LessonDescriptor(getUUID() ,
					"Password managers",
					"Password managers","");
			module2.addLesson(lesson26);
			ContentDescriptor content261 = new ContentDescriptor(getUUID(), 
					"Password managers", 
					"Password managers", 
					testCourseBaseURL+"courseware/7f3c8908698a4fa785c2bab76dc403c9/d75f8af0def547d4b547ef4056a03b43/"); //?activate_block_id=block-v1%3AOUNL%2BICS18%2B2018_1%2Btype%40sequential%2Bblock%40d75f8af0def547d4b547ef4056a03b43");
			lesson26.addContent(content261);

			
			ModuleDescriptor module3 = new ModuleDescriptor(getUUID(), 
					"Network security", 
					"Network security", "");
			course.addModule(module3);
			LessonDescriptor lesson31 = new LessonDescriptor(getUUID() ,
					"Introductory Concept of Network Security",
					"Introductory Concept of Network Security","");
			module3.addLesson(lesson31);	
			ContentDescriptor content311 = new ContentDescriptor(getUUID(), 
					"Introductory Concept of Network Security", 
					"Introductory Concept of Network Security", 
					testCourseBaseURL+"courseware/78b706f2fb434606a4c342fb8db156be/0b957f040f954b6ab1f4e64b533ba65b/"); //?activate_block_id=block-v1%3AOUNL%2BICS18%2B2018_1%2Btype%40sequential%2Bblock%400b957f040f954b6ab1f4e64b533ba65b");
			lesson31.addContent(content311);

			LessonDescriptor lesson32 = new LessonDescriptor(getUUID() ,
					"Firewall",
					"Firewall","");
			module3.addLesson(lesson32);
			ContentDescriptor content321 = new ContentDescriptor(getUUID(), 
					"Firewall", 
					"Firewall", 
					testCourseBaseURL+"courseware/78b706f2fb434606a4c342fb8db156be/b09ecadafbe541ba8876acfe92933139/"); //?activate_block_id=block-v1%3AOUNL%2BICS18%2B2018_1%2Btype%40sequential%2Bblock%40b09ecadafbe541ba8876acfe92933139");
			lesson32.addContent(content321);

			LessonDescriptor lesson33 = new LessonDescriptor(getUUID() ,
					"Tunneling",
					"Tunneling","");
			module3.addLesson(lesson33);
			ContentDescriptor content331 = new ContentDescriptor(getUUID(), 
					"Tunneling", 
					"Tunneling", 
					testCourseBaseURL+"courseware/78b706f2fb434606a4c342fb8db156be/ee78d00f4a1a4780a9c0e72db9eae198/"); //?activate_block_id=block-v1%3AOUNL%2BICS18%2B2018_1%2Btype%40sequential%2Bblock%40ee78d00f4a1a4780a9c0e72db9eae198");
			lesson33.addContent(content331);

			LessonDescriptor lesson34 = new LessonDescriptor(getUUID() ,
					"Intrusion detection",
					"Intrusion detection","");
			module3.addLesson(lesson34);
			ContentDescriptor content341 = new ContentDescriptor(getUUID(), 
					"Intrusion detection", 
					"Intrusion detection", 
					testCourseBaseURL+"courseware/78b706f2fb434606a4c342fb8db156be/f617869c98694cf9b6922d10cfa47695/"); //?activate_block_id=block-v1%3AOUNL%2BICS18%2B2018_1%2Btype%40sequential%2Bblock%40f617869c98694cf9b6922d10cfa47695");
			lesson34.addContent(content341);

			
			ModuleDescriptor module4 = new ModuleDescriptor(getUUID(), 
					"Risks for Social Networks Users", 
					"Risks for Social Networks Users", "");
			course.addModule(module4);
			LessonDescriptor lesson41 = new LessonDescriptor(getUUID() ,
					"Risk  for social networks users",
					"Risk  for social networks users","");
			module4.addLesson(lesson41);	
			ContentDescriptor content411 = new ContentDescriptor(getUUID(), 
					"Risk  for social networks users", 
					"Risk  for social networks users", 
					testCourseBaseURL+"courseware/7fe10f9a5dd04ccfb094d49a903e7326/a43cb0f2df3645f7944e9d070dd89f5b/"); //?activate_block_id=block-v1%3AOUNL%2BICS18%2B2018_1%2Btype%40sequential%2Bblock%40a43cb0f2df3645f7944e9d070dd89f5b");
			lesson41.addContent(content411);


			GoalDescriptor goal2 = new GoalDescriptor(getUUID(), module1.getTitle(), 
					"I intend to participate in the course activities to learn about "+module1.getTitle(), "");
			ListIterator<LessonDescriptor> iterator = module1.getLessons();
			while (iterator.hasNext()) {
				goal2.addLesson(iterator.next());
			}
			course.addGoal(goal2);
			
			GoalDescriptor goal3 = new GoalDescriptor(getUUID(), module2.getTitle(), 
					"I intend to participate in the course activities to learn about "+module2.getTitle(), "");
			iterator = module2.getLessons();
			while (iterator.hasNext()) {
				goal3.addLesson(iterator.next());
			}
			course.addGoal(goal3);
			
			GoalDescriptor goal4 = new GoalDescriptor(getUUID(), module3.getTitle(), 
					"I intend to participate in the course activities to learn about "+module3.getTitle(), "");
			iterator = module3.getLessons();
			while (iterator.hasNext()) {
				goal4.addLesson(iterator.next());
			}
			course.addGoal(goal4);
			
			GoalDescriptor goal5 = new GoalDescriptor(getUUID(), module4.getTitle(), 
					"I intend to participate in the course activities to learn about "+module4.getTitle(), "");
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
			user = PersistentStore.getUser(userid, new HashMap<String, Object>());
		} catch (Exception e) {
			//e.printStackTrace();
			user = null;
		}
		if (user == null) {
			user = new User(userName, userid);
			UserOnlineStatus status = getUserOnlineStatus(user);
			user.setOnlineStatus(status);
			boolean treatment = random.nextFloat() >= 0.47; // (53% treatment, 47% control)
			user.setTreatmentGroup(treatment);
			if (user.isTreatmentGroup()) {
				if (instance.clans.size()==0) {
					getOrGenerateClans();
				}
				if (instance.clans.size()>0) {
					int clanNumber = random.nextInt(instance.clans.size());
					if (instance.clans.size()>1) { // don't allow one clan to grow too fast
						if (instance.clans.get(0).userCount() > instance.clans.get(1).userCount()+1) {
							clanNumber = 1;
						} else if (instance.clans.get(1).userCount() > instance.clans.get(0).userCount()+1) {
							clanNumber = 0;
						}
					}
					Clan clan = instance.clans.get(clanNumber);
					clan.addUser(user);
					user.setClan(clan);
					try {
						PersistentStore.writeDescriptor(clan);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				// TODO: retrieve all clans and randomly assign to one
			} else {
				addControlUser(user);
			}
			if (!userGuest.equals(user.getName()) && !userUnknown.equals(userid)) {
				try {
					PersistentStore.writeUser(user);
					PersistentStore.writeDescriptor(status);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return user;
		
	}

	
	/**
	 * Retrieves all users of the clan, that have been active in the given time interval.
	 * if maxSecondsAgo == 0, every activity earlier than minSeconds ago is counted.
	 * if minSecondsAgo == 0, every activity later than maxSeconds ago is counted.
	 * @param maxSecondsAgo positive amount of seconds or 0 to ignore border
	 * @param minSecondsAgo positive amount of seconds or 0 to ignore border
	 * @return
	 */
	protected static HashArrayList<UserOnlineStatus> getControlUsersInTimeframe(int maxSecondsAgo, int minSecondsAgo) {
		HashArrayList<User> users = getOrGenerateControlUsers();
		//HashArrayList<User> users = PersistentStore.readAllControlUsers();
		Date now = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(now);
		cal.add(Calendar.SECOND, -maxSecondsAgo);
		Date max = cal.getTime();
		cal.setTime(now);
		cal.add(Calendar.SECOND, -minSecondsAgo);
		Date min = cal.getTime();
		HashArrayList<UserOnlineStatus> recentUsers = new HashArrayList<>();
		for (User user: users) {
			UserOnlineStatus status = user.getOnlineStatus();
			if ((maxSecondsAgo == 0 || status.getLastAccess().after(max)) 
					&& (minSecondsAgo == 0 || status.getLastAccess().before(min))) {
				recentUsers.add(status);
			}
		}
		return recentUsers;
	}
	
	public static HashArrayList<UserOnlineStatus> getOnlineControlUsers() {
		return getControlUsersInTimeframe(UserOnlineStatus.ONLINE_SECONDS, 0);
	}

	public static HashArrayList<UserOnlineStatus> getRecentControlUsers() {
		return getControlUsersInTimeframe(UserOnlineStatus.RECENT_SECONDS, UserOnlineStatus.ONLINE_SECONDS);
	}

	public static HashArrayList<UserOnlineStatus> getOfflineControlUsers() {
		return getControlUsersInTimeframe(0, UserOnlineStatus.RECENT_SECONDS);
	}

	public static HashArrayList<UserOnlineStatus> getOnlineControlUsersSorted(UserOnlineStatus userStatus) {
		HashArrayList<UserOnlineStatus> sorted = getOnlineControlUsers();
		if (userStatus != null && sorted != null) {
			sorted.sort(userStatus.relativeComparator());
		}
		return sorted;
	}

	public static HashArrayList<UserOnlineStatus> getRecentControlUsersSorted(UserOnlineStatus userStatus) {
		HashArrayList<UserOnlineStatus> sorted = getRecentControlUsers();
		if (userStatus != null && sorted != null) {
			sorted.sort(userStatus.relativeComparator());
		}
		return sorted;
	}

	public static HashArrayList<UserOnlineStatus> getOfflineControlUsersSorted(UserOnlineStatus userStatus) {
		HashArrayList<UserOnlineStatus> sorted = getOfflineControlUsers();
		if (userStatus != null && sorted != null) {
			sorted.sort(userStatus.relativeComparator());
		}
		return sorted;
	}

	
	
	
	public static ArrayList<UserProfile> getUserProfiles() {
		ArrayList<UserProfile> userProfiles = PersistentStore.getUserProfiles();
		if (userProfiles == null || userProfiles.isEmpty()) {
			userProfiles = new ArrayList<UserProfile>();
			
			for (String[] userProfileStrings : UserProfileCVS.getUserProfiles("WEB-INF/profileResources/OUNL_ICS18_2018_1_student_profile_info.csv")) {
				User user = StimulatedPlanningFactory.getUser(userProfileStrings[1], userProfileStrings[1]);
				UserProfile userProfile;
				if (user != null) {
					userProfile = StimulatedPlanningFactory.createUserProfile(user, userProfileStrings[3]);
					userProfile.setFullName(userProfileStrings[2]);
					userProfiles.add(userProfile);
					try {
						PersistentStore.writeDescriptor(userProfile);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			
		}
		
		return userProfiles;


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
		log.info("create userPlan for "+user.getName());
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
	
	
	public static PlanItem createPlanItem(User user, LessonDescriptor lesson, String jsonPlanItem ) {
		PlanItem planItem = new PlanItem(getUUID(), user, lesson, jsonPlanItem);
		
		return planItem;
	}
	
	
	public static UserProfile createUserProfile(User user, String email) {
		UserProfile userProfile = new UserProfile(getUUID(), user, email);
		
		return userProfile;
	}

	
	public static UserOnlineStatus getUserOnlineStatus(User user) {
		UserOnlineStatus userOnlineStatus = PersistentStore.readUserOnlineStatus(user);
		if (userOnlineStatus == null) {
			userOnlineStatus = createUserOnlineStatus(user);
		}
		return userOnlineStatus;
	}
	
	
	public static UserOnlineStatus createUserOnlineStatus(User user) {
		log.info("create userOnlineStatus for "+user.getName());
		UserOnlineStatus userOnlineStatus = new UserOnlineStatus(StimulatedPlanningFactory.getUUID(), user);
		
		try {
			PersistentStore.writeDescriptor(userOnlineStatus);
		} catch (Exception exc) {
			exc.printStackTrace();
		}
		
		return userOnlineStatus;
	}
	

	public static UserSelectedOption createUserSelectedOption(User user, SelectionObject selectionObject, SelectionOption selectionOption) {
		log.info("create userSelectedOption for "+user.getName());
		UserSelectedOption userSelectedOption = new UserSelectedOption(StimulatedPlanningFactory.getUUID(), user);
		userSelectedOption.setSelectionObject(selectionObject);
		userSelectedOption.setSelectedOption(selectionOption);
		userSelectedOption.setLastAccess(new Date());
		return userSelectedOption;
	}
	

	public static ArrayList<UserSelectedOption> readClanSelectionOption(Clan clan, SelectionObject selectionObject, SelectionOption selectionOption) {
		ArrayList<UserSelectedOption> selectedOptions = new ArrayList<>();
		UserSelectedOption selectedOption = null;
		try {
			for (UserOnlineStatus status: clan.getOnlineUsers()) {
				//selectedOption = PersistentStore.readUserSelectionOption(status.getUser(), selectionObject, selectionOption);
				selectedOption = selectionOption.getUserSelectedOption(status.getUser(), selectionObject);  //PersistentStore.readUserSelectionOption(status.getUser(), selectionObject, selectionOption);
				if (selectedOption != null) {
					selectedOptions.add(selectedOption);
				}						
			}
			for (UserOnlineStatus status: clan.getRecentUsers()) {
				//selectedOption = PersistentStore.readUserSelectionOption(status.getUser(), selectionObject, selectionOption);
				selectedOption = selectionOption.getUserSelectedOption(status.getUser(), selectionObject);  //PersistentStore.readUserSelectionOption(status.getUser(), selectionObject, selectionOption);
				if (selectedOption != null) {
					selectedOptions.add(selectedOption);
				}						
			}
			for (UserOnlineStatus status: clan.getOfflineUsers()) {
				//selectedOption = PersistentStore.readUserSelectionOption(status.getUser(), selectionObject, selectionOption);
				selectedOption = selectionOption.getUserSelectedOption(status.getUser(), selectionObject);  //PersistentStore.readUserSelectionOption(status.getUser(), selectionObject, selectionOption);
				if (selectedOption != null) {
					selectedOptions.add(selectedOption);
				}						
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return selectedOptions;
	}
	
	
	private HashMap<String, ChatRoomList> chatRoomListMap = new HashMap<>();
	public static HashMap<String, ChatRoomList> getChatRoomLists() {
		initialiseChatRooms();
		return instance.chatRoomListMap;
	}
	private static void initialiseChatRooms() {
		String[] clanIds = {
				Clan.CLAN_1_ID,
				Clan.CLAN_2_ID,
				"control"
		};
		
		ChatRoomList list = null;
		for (String clanId: clanIds) {
			list = instance.chatRoomListMap.get(clanId);
			if (list == null) {
				log.info("create new chat room list for: "+clanId);
				list = new ChatRoomList(clanId);
				list.addRoom(new ChatRoom("I need help", "Ask your peers for help."));
				if (!"control".equals(clanId)) {
					list.addRoom(new ChatRoom("I need a challenge", "Tackle challenges together"));
				}
				list.addRoom(new ChatRoom("I need a teacher", "Ask your teacher for support"));
				list.addRoom(new ChatRoom("Just want to chat", "Open chat, but follow the rules"));
				list.addRoom(new ChatRoom("StartUp", "Startup chat room. Chatter is added to this after he logs in."));
				instance.chatRoomListMap.put(clanId, list);
			}
		}
	}
	public static ChatRoomList getChatRoomListForUser(User user) {
		ChatRoomList list = null;
		String clanId = null;
		initialiseChatRooms();
		if (user != null && user.isTreatmentGroup()) {
			clanId = user.getClan().getId();
		} else {
			clanId = "control";
		}
		list = instance.chatRoomListMap.get(clanId);
		return list;
	}
	
	
	public static Message createMessage(User user, String message, long timeStamp, ChatRoom room, ChatRoomList roomList) {
		Message chatMessage = new Message(user, message, timeStamp, room, roomList, getUUID());
		PersistentStore.writeMessage(chatMessage);
		return chatMessage;
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
		logParameters.put("treatment", new String[] {String.valueOf(user.isTreatmentGroup())});
		if (user.isTreatmentGroup()) {
			logParameters.put("clan", new String[] {user.getClan().getId()});
		}
		
		logParameters.putAll(request.getParameterMap());
		
		Gson gson = new Gson();
		String jsonObject = gson.toJson(logParameters);

		log.info("[LOG] " + jsonObject + " [/LOG]");
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
		if ("track".equals(logType) || "trackClan".equals(logType)) {
			String activityType = request.getParameter("activityType");
			if (activityType == null) {
				activityType = StimulatedPlanningFactory.ACTIVITY_TYPE_ACCESS;
			}
			String page = request.getParameter("page");
			String contentId = request.getParameter("contentId");
			if (page != null) {
				if ("track".equals(logType) && userPlan.trackLearningProgress(page, contentId, activityType)) {
					try {
						PersistentStore.writeDescriptor(userPlan);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				// RK: addition for SoC experiment
				if ("trackClan".equals(logType) && user.getOnlineStatus() != null) {
					user.getOnlineStatus().updateOnlineStatus(page, course.indexInCourse(page));
					if (user.isTreatmentGroup()) {
						Clan clan = user.getClan();
						clan.updateUserOnlineStatus(user.getOnlineStatus());
					}
					try {
						PersistentStore.writeDescriptor(user.getOnlineStatus());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			
		}
	}
	

	
	public static HttpSession initializeSession(HttpServletRequest request, HttpServletResponse response) {
		//log.info("init session 0: call stack");
		//(new Exception()).printStackTrace();

		HttpSession session = request.getSession();
		
		String loginData = "";
		
		String userNameR = request.getParameter("userName");
		String useridR = request.getParameter("userid");

		//log.info("init session 1: userNameR: "+userNameR+", useridR: "+useridR);
		if (userNameR == null || useridR == null) {
			userNameR = userGuest;
			useridR = userUnknown;
		}
		
		//log.info("init session 2: userNameR: "+userNameR+", useridR: "+useridR);
		loginData += " | userNameR: "+userNameR+", useridR: "+useridR;
		
		String userName = (String)session.getAttribute("userName");
		String userid = (String)session.getAttribute("userid");
		
		//log.info("init session 3: userName: "+userName+", userid: "+userid);
		loginData += " | userName: "+userName+", userid: "+userid;

		if (userName == null || userid == null 
				|| (!userName.equals(userNameR) && !userNameR.equals(userGuest)) 
				|| (!userid.equals(useridR) && !useridR.equals(userUnknown))) {
			userName = userNameR;
			userid = useridR;
			session.setAttribute("userName", userName);
			session.setAttribute("userid", userid);
		}

		//log.info("init session 4: userName(2): "+userName+", userid(2): "+userid);
		loginData += " | userName(2): "+userName+", userid(2): "+userid;

		User user = (User)session.getAttribute("user");
		if (user != null) {
			//log.info("init session 5a: userName(3a): "+userName+", userid(3a): "+userid);
			loginData += " | user.name(1): "+user.getName()+", user.id(1): "+user.getId();
		} else {
			//log.info("init session 5b: userName(3b): null, userid(3b): null");
			loginData += " | user.name(1): null, user.id(1): null";
		}
		
		if (user == null 
				|| ((userid != null && !userid.equals(user.getId()) && !userid.equals(userUnknown)) 
				|| (userName != null && !userName.equals(user.getName()) && !userName.equals(userGuest)))) {
			user = getUser(userid, userName);
			session.setAttribute("user", user);
		}
		
		//log.info("init session 6: user.name(4): "+user.getName()+", user.id(4): "+user.getId());
		loginData += " | user.name: "+user.getName()+", user.id: "+user.getId();
		
		if (userUnknown.equals(user.getId()) || userGuest.equals(user.getId()) || userUnknown.equals(user.getName()) || userGuest.equals(user.getName())) {
			log.info("init session finally: user.name: "+user.getName()+", user.id: "+user.getId()+", trace:  "+loginData);
		}

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
	
		UserOnlineStatus userOnlineStatus = (UserOnlineStatus)session.getAttribute("userOnlineStatus");
		if (userOnlineStatus == null || !user.getId().equals(userOnlineStatus.getUser().getId())) {
			userOnlineStatus = StimulatedPlanningFactory.getUserOnlineStatus(user);
			session.setAttribute("userOnlineStatus", userOnlineStatus);
		}
	
		HashArrayList<GoalDescriptor> selectedGoals = new HashArrayList<GoalDescriptor>();
		HashArrayList<LessonDescriptor> selectedLessons = new HashArrayList<LessonDescriptor>();

		for (UserGoal ugoal : userPlan.goals) {
			selectedGoals.add(ugoal.getGoalDescriptor());
			for (UserLesson ulesson : ugoal.lessons) {
				selectedLessons.add(ulesson.getLesson());
			}
			if (ugoal.getCompletionGoal() != null && ugoal.getCompletionGoal().length()>0) {
				session.setAttribute("completionSelectRB", ugoal.getCompletionGoal());
			}
		}
		
		//log.info("userGoals: "+userPlan.goals.size()+", planItems: "+userPlan.planItems.size());
		//log.info("selectedGoals: "+selectedGoals.size()+", selectedLessons: "+selectedLessons.size());
		
		session.setAttribute("selectedGoals", selectedGoals);
		session.setAttribute("selectedLessons", selectedLessons);
		session.setAttribute("loginData", loginData);

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
