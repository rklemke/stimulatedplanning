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

import com.google.gson.Gson;

import senseofcommunity.Clan;
import senseofcommunity.InformationObject;
import senseofcommunity.SelectionObject;
import senseofcommunity.SelectionObjectPurpose;
import senseofcommunity.SelectionObjectType;
import senseofcommunity.SelectionOption;
import senseofcommunity.UserOnlineStatus;
import senseofcommunity.UserSelectedOption;
import stimulatedplanning.util.HashArrayList;
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
	public static final String testCourseId = "TCC01";
	//private static final String testCourseBaseURL = "https://ou.edia.nl/courses/course-v1:OUNL+ICS18+2018_1/";
	//public static final String testCourseBaseURL = "https://edge.edx.org/courses/course-v1:DelftX+Sandbox_Welten+2018/";
	//public static final String testCourseBaseURL = "https://localhost/courses/course-v1:DelftX+Sandbox_Welten+2018/";
	public static final String testCourseBaseURL = "https://ou.acc.edia.nl/courses/course-v1:OUNL+TCC01+2019_01/courseware/";

	public static final String accCourseId = "TCC01";
	public static final String accCourseBaseURL = "https://ou.acc.edia.nl/courses/course-v1:OUNL+TCC01+2019_01/courseware/";
	
	//public static final String applicationHome = "http://localhost:8080";
	public static final String applicationHome = "https://senseofcommunity-225200.appspot.com";
	
	public static final String userUnknown = "unknown";
	public static final String userGuest = "Guest";
	
	public static final String ACTIVITY_TYPE_ACCESS = "access";
	public static final String ACTIVITY_TYPE_COMPLETE = "complete";
	
	private HashMap<String, GenericDescriptor> courseObjects = new HashMap<>();
	private HashArrayList<Clan> clans = new HashArrayList<>();
	
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
	
	public static boolean hasObject(String id) {
		return instance.courseObjects.containsKey(id);
	}
	
	public static void addObject(GenericDescriptor object) {
		if (!(object instanceof GenericUserObject)) {
			instance.courseObjects.put(object.getId(), object);
		} else {
			log.info("trying to add GenericUserObject to courseObjects: "+object.getClass().getName()+", "+object.getId());
		}
	}
	
	public static GenericDescriptor getObject(String id) {
		if (!instance.courseObjects.containsKey(id)) {
			log.info("Warning: trying to retrieve object not in Map: "+id);
			new Exception().printStackTrace();
			return null;
		}
		return instance.courseObjects.get(id);
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
		return generateAccTestCourse();
		//return generateDevTestCourse();
	}	

	private static HashArrayList<Clan> getOrGenerateClans() {
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

	protected static Date week1deadline;
	protected static Date week2deadline;
	protected static Date week3deadline;
	protected static Date week4deadline;


	
	/**
	 * generate the structure for the course to be used according to the acc staging platform test structure.
	 * @return
	 */
	public static CourseDescriptor generateAccTestCourse() {
		getOrGenerateClans();
		
		week1deadline = new GregorianCalendar(2019, 0, 27, 12, 0).getTime();
		week2deadline = new GregorianCalendar(2019, 1, 3, 12, 0).getTime();
		week3deadline = new GregorianCalendar(2019, 1, 10, 12, 0).getTime();
		week4deadline = new GregorianCalendar(2019, 1, 17, 12, 0).getTime();
		
		CourseDescriptor course = instance.retrieveTestCourse();
		if (course == null) {
			
			// Course
			
			course = new CourseDescriptor(instance.accCourseId, 
					"How Cryptography Keeps The Internet Secure", 
					"How Cryptography Keeps The Internet Secure", 
					accCourseBaseURL);

			//
			// Week 1
			//
			
			generateAccWeek1(course);
			
			//
			// Module 2
			//
			
			generateAccWeek2(course);
			
			//
			// Module 3
			//
			
			generateAccWeek3(course);
			
			//
			// Module 4
			//
			
			generateAccWeek4(course);
			
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
	
	
	protected static ModuleDescriptor generateAccWeek1(CourseDescriptor course) {
		ModuleDescriptor module = null;
		LessonDescriptor lesson = null;
		ContentDescriptor content = null;
		InformationObject info = null;
		SelectionObject sele = null;
				
		module = new ModuleDescriptor(getUUID(), 
				"General Introduction", 
				"General Introduction", "");
		course.addModule(module);


		// Content		
		lesson = new LessonDescriptor(getUUID() ,
				"Welcome",
				"Welcome","");
		module.addLesson(lesson);	

		content = new ContentDescriptor("99afb1a25d4a4d4b87712f67fe313bde/50d96a7ac43243bba77b330f36e6c29b/", 
				"Welcome", 
				"Welcome", 
				accCourseBaseURL+"99afb1a25d4a4d4b87712f67fe313bde/50d96a7ac43243bba77b330f36e6c29b/");
		lesson.addContent(content);

		lesson = new LessonDescriptor(getUUID() ,
				"The Origins",
				"The Origins","");
		module.addLesson(lesson);	

		content = new ContentDescriptor("99afb1a25d4a4d4b87712f67fe313bde/924936fcf16f4b319f2e7bdee9dd18f1/", 
				"History and terminology", 
				"History and terminology", 
				accCourseBaseURL+"99afb1a25d4a4d4b87712f67fe313bde/924936fcf16f4b319f2e7bdee9dd18f1/");
		lesson.addContent(content);


		// Assignment
		lesson = new LessonDescriptor(getUUID() ,
				"Week 1 Assignment",
				"Week 1 Assignment","");
		module.addLesson(lesson);	

		content = new ContentDescriptor("99afb1a25d4a4d4b87712f67fe313bde/d51e8b0e73c147f0beca638a09c1b3da/", 
				"Week 1 Assignment", 
				"Week 1 Assignment", 
				accCourseBaseURL+"99afb1a25d4a4d4b87712f67fe313bde/d51e8b0e73c147f0beca638a09c1b3da/");
		lesson.addContent(content);


		// Assignment content
		info = generateControlWeek1IntroductionText();
		content.addInformationObject(info);

		info = generateTreatmentWeek1IntroductionText();
		content.addInformationObject(info);

		sele = generateUserAvatarSelection();
		sele.setDeadline(week1deadline);
		content.addInformationObject(sele);
		
		sele = generateClanLogoSelection();
		sele.setDeadline(week1deadline);
		content.addInformationObject(sele);
		
		sele = generateClanIdentitySelection();
		sele.setDeadline(week1deadline);
		content.addInformationObject(sele);
		
		sele = generateUserIdentitySelection();
		sele.setDeadline(week1deadline);
		content.addInformationObject(sele);
		
		sele = generateClanARulesSelection();
		sele.setDeadline(week1deadline);
		content.addInformationObject(sele);
		
		sele = generateClanBRulesSelection();
		sele.setDeadline(week1deadline);
		content.addInformationObject(sele);
		
		info = generateTreatmentWeek1TestIntroduction();
		content.addInformationObject(info);
		
		sele = generateWeek1KnowledgeTest1();
		sele.setDeadline(week1deadline);
		content.addInformationObject(sele);

		sele = generateWeek1KnowledgeTest2();
		sele.setDeadline(week1deadline);
		content.addInformationObject(sele);

		sele = generateWeek1KnowledgeTest3();
		sele.setDeadline(week1deadline);
		content.addInformationObject(sele);

		sele = generateWeek1KnowledgeTest4();
		sele.setDeadline(week1deadline);
		content.addInformationObject(sele);

		sele = generateWeek1KnowledgeTest5();
		sele.setDeadline(week1deadline);
		content.addInformationObject(sele);

		sele = generateWeek1KnowledgeTest6();
		sele.setDeadline(week1deadline);
		content.addInformationObject(sele);

		info = generateWeek1TestConclusion();
		content.addInformationObject(info);
		
		// Goal for Module 1
		
		GoalDescriptor goal = new GoalDescriptor(getUUID(), module.getTitle(), 
				"I intend to participate in the course activities to learn about "+module.getTitle(), "");
		ListIterator<LessonDescriptor> iterator = module.getLessons();
		while (iterator.hasNext()) {
			goal.addLesson(iterator.next());
		}
		course.addGoal(goal);

		
		return module;
	}

	protected static ModuleDescriptor generateAccWeek2(CourseDescriptor course) {
		ModuleDescriptor module = null;
		LessonDescriptor lesson = null;
		ContentDescriptor content = null;
		InformationObject info = null;
		SelectionObject sele = null;
				
		module = new ModuleDescriptor(getUUID(), 
				"Symmetric Encryption", 
				"Symmetric Encryption", "");
		course.addModule(module);


		// Content		
		lesson = new LessonDescriptor(getUUID() ,
				"Caesar and Cryptanalysis",
				"Caesar and Cryptanalysis","");
		module.addLesson(lesson);	

		content = new ContentDescriptor("8e4e29d1f93b459bb40d11f93f77a85a/657cb604d580479796cd253af89a809b/", 
				"Caesar Cipher", 
				"Caesar Cipher", 
				accCourseBaseURL+"8e4e29d1f93b459bb40d11f93f77a85a/657cb604d580479796cd253af89a809b/");
		lesson.addContent(content);

		lesson = new LessonDescriptor(getUUID() ,
				"Vigenère and cryptanalysis",
				"Vigenère and cryptanalysis","");
		module.addLesson(lesson);	

		content = new ContentDescriptor("8e4e29d1f93b459bb40d11f93f77a85a/13925f19c0644d6a80ed77c74b2c7505/", 
				"Vigenère Part 1 and 2", 
				"Vigenère Part 1 and 2", 
				accCourseBaseURL+"8e4e29d1f93b459bb40d11f93f77a85a/13925f19c0644d6a80ed77c74b2c7505/");
		lesson.addContent(content);

		lesson = new LessonDescriptor(getUUID() ,
				"Playfair",
				"Playfair","");
		module.addLesson(lesson);	

		content = new ContentDescriptor("8e4e29d1f93b459bb40d11f93f77a85a/e8053ac8660340c4967a88b0381fe265/", 
				"Playfair", 
				"Playfair", 
				accCourseBaseURL+"8e4e29d1f93b459bb40d11f93f77a85a/e8053ac8660340c4967a88b0381fe265/");
		lesson.addContent(content);

		lesson = new LessonDescriptor(getUUID() ,
				"Unbreakable encryption: one time pad",
				"Unbreakable encryption: one time pad","");
		module.addLesson(lesson);	

		content = new ContentDescriptor("8e4e29d1f93b459bb40d11f93f77a85a/51532bfb08fa40d8a111af0d0630717e/", 
				"Unbreakable Encryption: One Time Pad", 
				"Unbreakable Encryption: One Time Pad", 
				accCourseBaseURL+"8e4e29d1f93b459bb40d11f93f77a85a/51532bfb08fa40d8a111af0d0630717e/");
		lesson.addContent(content);

		lesson = new LessonDescriptor(getUUID() ,
				"Modern Symmetric Cryptography",
				"Modern Symmetric Cryptography","");
		module.addLesson(lesson);	

		content = new ContentDescriptor("8e4e29d1f93b459bb40d11f93f77a85a/2a2dc9c5b4804017b6fb9b5c4a9d8f5b/", 
				"Modern Symmetric Cryptography", 
				"Modern Symmetric Cryptography", 
				accCourseBaseURL+"8e4e29d1f93b459bb40d11f93f77a85a/2a2dc9c5b4804017b6fb9b5c4a9d8f5b/");
		lesson.addContent(content);


		// Assignment
		lesson = new LessonDescriptor(getUUID() ,
				"Week 2 Assignment",
				"Week 2 Assignment","");
		module.addLesson(lesson);	

		content = new ContentDescriptor("8e4e29d1f93b459bb40d11f93f77a85a/bfc34daba1ec4aa09dd4f88e00aa5cd0/", 
				"Week 2 Assignment", 
				"Week 2 Assignment", 
				accCourseBaseURL+"8e4e29d1f93b459bb40d11f93f77a85a/bfc34daba1ec4aa09dd4f88e00aa5cd0/");
		lesson.addContent(content);


		// Assignment content
		info = new InformationObject(getUUID(), 
				"Communicate with the opponents", 
				"Communicate with the opponents", 
				"",
				false, true, true);
		content.addInformationObject(info);
		info.setContent("");
		
		sele = generateEncryptedMessageSelection();
		sele.setDeadline(week2deadline);
		content.addInformationObject(sele);
		
		info = new InformationObject(getUUID(), 
				"Encrypt your message", 
				"Communicate with the opponents", 
				"",
				false, true, true);
		content.addInformationObject(info);
		info.setContent("");
		
		sele = generateEncryptionMethodSelectionClan1();
		sele.setDeadline(week2deadline);
		content.addInformationObject(sele);
		
		sele = generateEncryptionMethodSelectionClan2();
		sele.setDeadline(week2deadline);
		content.addInformationObject(sele);
		
		info = new InformationObject(getUUID(), 
				"Encrypted Message received", 
				"How do you decrypt it?", 
				"",
				false, true, true);
		content.addInformationObject(info);
		info.setContent("");
		
		sele = generateDecryptionSelection();
		sele.setDeadline(week2deadline);
		content.addInformationObject(sele);
		
		info = new InformationObject(getUUID(), 
				"Clan challenge results", 
				"How'd you do?", 
				"",
				false, true, true);
		content.addInformationObject(info);
		info.setContent("");
		
		sele = generateWeek2KnowledgeTest1();
		content.addInformationObject(sele);
		sele.setDeadline(week2deadline);
		
		sele = generateWeek2KnowledgeTest2();
		content.addInformationObject(sele);
		sele.setDeadline(week2deadline);
				
		sele = generateWeek2KnowledgeTest3();
		content.addInformationObject(sele);
		sele.setDeadline(week2deadline);
				
		sele = generateWeek2KnowledgeTest4();
		content.addInformationObject(sele);
		sele.setDeadline(week2deadline);
				
		sele = generateWeek2KnowledgeTest5();
		content.addInformationObject(sele);
		sele.setDeadline(week2deadline);
				
		sele = generateWeek2KnowledgeTest6();
		content.addInformationObject(sele);
		sele.setDeadline(week2deadline);
				
		sele = generateWeek2KnowledgeTest7();
		content.addInformationObject(sele);
		sele.setDeadline(week2deadline);
				
		sele = generateWeek2KnowledgeTest8();
		content.addInformationObject(sele);
		sele.setDeadline(week2deadline);
				
		sele = generateWeek2KnowledgeTest9();
		content.addInformationObject(sele);
		sele.setDeadline(week2deadline);
				
		sele = generateWeek2KnowledgeTest10();
		content.addInformationObject(sele);
		sele.setDeadline(week2deadline);
				
		
		// Goal for Module 2
		
		GoalDescriptor goal = new GoalDescriptor(getUUID(), module.getTitle(), 
				"I intend to participate in the course activities to learn about "+module.getTitle(), "");
		ListIterator<LessonDescriptor> iterator = module.getLessons();
		while (iterator.hasNext()) {
			goal.addLesson(iterator.next());
		}
		course.addGoal(goal);

		
		return module;
	}

	protected static ModuleDescriptor generateAccWeek3(CourseDescriptor course) {
		ModuleDescriptor module = null;
		LessonDescriptor lesson = null;
		ContentDescriptor content = null;
		InformationObject info = null;
		SelectionObject sele = null;
				
		module = new ModuleDescriptor(getUUID(), 
				"Asymmetric Encryption", 
				"Asymmetric Encryption", "");
		course.addModule(module);


		// Content		
		lesson = new LessonDescriptor(getUUID() ,
				"Intro to week 3- Asymmetric Encryption",
				"Intro to week 3- Asymmetric Encryption","");
		module.addLesson(lesson);	

		content = new ContentDescriptor("085e5e7bbd294467a5dfc7adee0dc8e7/f075fc2c2e834471ac7c645fb2ff2ca2/", 
				"Intro to Week 3", 
				"Intro to Week 3", 
				accCourseBaseURL+"085e5e7bbd294467a5dfc7adee0dc8e7/f075fc2c2e834471ac7c645fb2ff2ca2/");
		lesson.addContent(content);

		lesson = new LessonDescriptor(getUUID() ,
				"Clock Arithmetic",
				"Clock Arithmetic","");
		module.addLesson(lesson);	

		content = new ContentDescriptor("085e5e7bbd294467a5dfc7adee0dc8e7/8abacf327658455abd7f6312250a5bcb/", 
				"Counting with clocks", 
				"Counting with clocks", 
				accCourseBaseURL+"085e5e7bbd294467a5dfc7adee0dc8e7/8abacf327658455abd7f6312250a5bcb/");
		lesson.addContent(content);

		lesson = new LessonDescriptor(getUUID() ,
				"RSA",
				"RSA","");
		module.addLesson(lesson);	

		content = new ContentDescriptor("085e5e7bbd294467a5dfc7adee0dc8e7/52dcb3e8bc3c4de1a3caa0873fa5e251/", 
				"How to use it and proof of correctness", 
				"How to use it and proof of correctness", 
				accCourseBaseURL+"085e5e7bbd294467a5dfc7adee0dc8e7/52dcb3e8bc3c4de1a3caa0873fa5e251/");
		lesson.addContent(content);

		lesson = new LessonDescriptor(getUUID() ,
				"Diffie-Hellman",
				"Diffie-Hellman","");
		module.addLesson(lesson);	

		content = new ContentDescriptor("085e5e7bbd294467a5dfc7adee0dc8e7/4b198ca8530d488eadef85cf1af008f1/", 
				"(Exponential) arithmetic with letters and Diffie-Hellman key exchange", 
				"(Exponential) arithmetic with letters and Diffie-Hellman key exchange", 
				accCourseBaseURL+"085e5e7bbd294467a5dfc7adee0dc8e7/4b198ca8530d488eadef85cf1af008f1/");
		lesson.addContent(content);

		lesson = new LessonDescriptor(getUUID() ,
				"ElGamal",
				"ElGamal","");
		module.addLesson(lesson);	

		content = new ContentDescriptor("085e5e7bbd294467a5dfc7adee0dc8e7/14df7edafc7e472a9fa455247159e227/", 
				"ElGamal", 
				"ElGamal", 
				accCourseBaseURL+"085e5e7bbd294467a5dfc7adee0dc8e7/14df7edafc7e472a9fa455247159e227/");
		lesson.addContent(content);


		// Assignment
		lesson = new LessonDescriptor(getUUID() ,
				"Week 3 Assignment",
				"Week 3 Assignment","");
		module.addLesson(lesson);	

		content = new ContentDescriptor("085e5e7bbd294467a5dfc7adee0dc8e7/6778b583bc084d87824bf443a739f8e7/", 
				"Week 3 Assignment", 
				"Week 3 Assignment", 
				accCourseBaseURL+"085e5e7bbd294467a5dfc7adee0dc8e7/6778b583bc084d87824bf443a739f8e7/");
		lesson.addContent(content);


		// Assignment content
		info = new InformationObject(getUUID(), 
				"Test your knowledge", 
				"Test your knowledge", 
				"",
				true, true, true);
		content.addInformationObject(info);
		info.setContent("");
		
		sele = generateWeek3KnowledgeTest1();
		sele.setDeadline(week3deadline);
		content.addInformationObject(sele);
		
		sele = generateWeek3KnowledgeTest2();
		sele.setDeadline(week3deadline);
		content.addInformationObject(sele);
				
		sele = generateWeek3KnowledgeTest3();
		sele.setDeadline(week3deadline);
		content.addInformationObject(sele);
				
		sele = generateWeek3KnowledgeTest4();
		sele.setDeadline(week3deadline);
		content.addInformationObject(sele);
				
		sele = generateWeek3KnowledgeTest5();
		sele.setDeadline(week3deadline);
		content.addInformationObject(sele);
				
		sele = generateWeek3KnowledgeTest6();
		sele.setDeadline(week3deadline);
		content.addInformationObject(sele);
				
		info = new InformationObject(getUUID(), 
				"Your results", 
				"Your results", 
				"",
				true, true, true);
		content.addInformationObject(info);
		info.setContent("");
		

		// Goal for Module 3
		
		GoalDescriptor goal = new GoalDescriptor(getUUID(), module.getTitle(), 
				"I intend to participate in the course activities to learn about "+module.getTitle(), "");
		ListIterator<LessonDescriptor> iterator = module.getLessons();
		while (iterator.hasNext()) {
			goal.addLesson(iterator.next());
		}
		course.addGoal(goal);

		
		return module;
	}

	protected static ModuleDescriptor generateAccWeek4(CourseDescriptor course) {
		ModuleDescriptor module = null;
		LessonDescriptor lesson = null;
		ContentDescriptor content = null;
		InformationObject info = null;
		SelectionObject sele = null;
				
		module = new ModuleDescriptor(getUUID(), 
				"Using Cryptography", 
				"Using Cryptography", "");
		course.addModule(module);


		// Content		
		lesson = new LessonDescriptor(getUUID() ,
				"Digital signatures",
				"Digital signatures","");
		module.addLesson(lesson);	

		content = new ContentDescriptor("f916b1af5d144670a980c5d5e0fee3a7/b2913720de144705ad1bd5a6e637cb9f/", 
				"Digital signatures", 
				"Digital signatures", 
				accCourseBaseURL+"f916b1af5d144670a980c5d5e0fee3a7/b2913720de144705ad1bd5a6e637cb9f/");
		lesson.addContent(content);

		lesson = new LessonDescriptor(getUUID() ,
				"PKI and certificate chains",
				"PKI and certificate chains","");
		module.addLesson(lesson);	

		content = new ContentDescriptor("f916b1af5d144670a980c5d5e0fee3a7/aa28fa12231d4cea8923564f63aeafcf/", 
				"PKI and certificate chains", 
				"PKI and certificate chains", 
				accCourseBaseURL+"f916b1af5d144670a980c5d5e0fee3a7/aa28fa12231d4cea8923564f63aeafcf/");
		lesson.addContent(content);

		lesson = new LessonDescriptor(getUUID() ,
				"Homomorphic cryptography",
				"Homomorphic cryptography","");
		module.addLesson(lesson);	

		content = new ContentDescriptor("f916b1af5d144670a980c5d5e0fee3a7/3b66d6481cd74e9e811cf6be9ed6a5de/", 
				"Homomorphic cryptography", 
				"Homomorphic cryptography", 
				accCourseBaseURL+"f916b1af5d144670a980c5d5e0fee3a7/3b66d6481cd74e9e811cf6be9ed6a5de/");
		lesson.addContent(content);

		lesson = new LessonDescriptor(getUUID() ,
				"mixnets",
				"mixnets","");
		module.addLesson(lesson);	

		content = new ContentDescriptor("f916b1af5d144670a980c5d5e0fee3a7/ebfa676671b94b518e1d525adfe4ddd0/", 
				"mixnets", 
				"mixnets", 
				accCourseBaseURL+"f916b1af5d144670a980c5d5e0fee3a7/ebfa676671b94b518e1d525adfe4ddd0/");
		lesson.addContent(content);

		lesson = new LessonDescriptor(getUUID() ,
				"hash chains and merkle trees",
				"hash chains and merkle trees","");
		module.addLesson(lesson);	

		content = new ContentDescriptor("f916b1af5d144670a980c5d5e0fee3a7/f903c3afa93f431cb5db9016c44712d7/", 
				"hash chains and merkle trees", 
				"hash chains and merkle trees", 
				accCourseBaseURL+"f916b1af5d144670a980c5d5e0fee3a7/f903c3afa93f431cb5db9016c44712d7/");
		lesson.addContent(content);


		// Assignment
		lesson = new LessonDescriptor(getUUID() ,
				"Week 4 Assignment",
				"Week 4 Assignment","");
		module.addLesson(lesson);	

		content = new ContentDescriptor("f916b1af5d144670a980c5d5e0fee3a7/f7e5d9bef8304d09bf28e5713b680466/", 
				"Week 4 Assignment", 
				"Week 4 Assignment", 
				accCourseBaseURL+"f916b1af5d144670a980c5d5e0fee3a7/f7e5d9bef8304d09bf28e5713b680466/");
		lesson.addContent(content);


		// Assignment content
		info = new InformationObject(getUUID(), 
				"Discuss about cryptography", 
				"Discuss about cryptography", 
				testCourseBaseURL+"courseware/w4challenge/",
				true, true, true);
		content.addInformationObject(info);
		info.setContent("");

		sele = generateWeek4KnowledgeTest1();
		sele.setDeadline(week4deadline);
		content.addInformationObject(sele);
				
		sele = generateWeek4KnowledgeTest2();
		sele.setDeadline(week4deadline);
		content.addInformationObject(sele);
				
		sele = generateWeek4KnowledgeTest3();
		sele.setDeadline(week4deadline);
		content.addInformationObject(sele);
				
		sele = generateWeek4KnowledgeTest4();
		sele.setDeadline(week4deadline);
		content.addInformationObject(sele);
				
		sele = generateWeek4KnowledgeTest5();
		sele.setDeadline(week4deadline);
		content.addInformationObject(sele);
				
		sele = generateWeek4KnowledgeTest6();
		sele.setDeadline(week4deadline);
		content.addInformationObject(sele);
				
		sele = generateWeek4KnowledgeTest7();
		sele.setDeadline(week4deadline);
		content.addInformationObject(sele);
				
		sele = generateWeek4KnowledgeTest8();
		sele.setDeadline(week4deadline);
		content.addInformationObject(sele);
				
		sele = generateWeek4KnowledgeTest9();
		sele.setDeadline(week4deadline);
		content.addInformationObject(sele);
				
		sele = generateWeek4KnowledgeTest10();
		sele.setDeadline(week4deadline);
		content.addInformationObject(sele);
				

		// Goal for Module 1
		
		GoalDescriptor goal = new GoalDescriptor(getUUID(), module.getTitle(), 
				"I intend to participate in the course activities to learn about "+module.getTitle(), "");
		ListIterator<LessonDescriptor> iterator = module.getLessons();
		while (iterator.hasNext()) {
			goal.addLesson(iterator.next());
		}
		course.addGoal(goal);

		
		return module;
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
			
			course = new CourseDescriptor(instance.testCourseId, 
					"Security Dev Test Course", 
					"Security Dev Test Course", 
					testCourseBaseURL+"course/");

			//
			// Week 1
			//
			
			generateWeek1(course);
			
			//
			// Module 2
			//
			
			generateWeek2(course);
			
			//
			// Module 3
			//
			
			generateWeek3(course);
			
			//
			// Module 4
			//
			
			generateWeek4(course);
			
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
	
	
	protected static ModuleDescriptor generateWeek1(CourseDescriptor course) {
		ModuleDescriptor module = new ModuleDescriptor(getUUID(), 
				"Week 1", 
				"Week 1 - Introduction", "");
		course.addModule(module);
		LessonDescriptor lesson = new LessonDescriptor(getUUID() ,
				"module1. lesson 1",
				"module1. lesson 1","");
		module.addLesson(lesson);	
		ContentDescriptor content = new ContentDescriptor(getUUID(), 
				"Week 1 content", 
				"Week 1 content", 
				testCourseBaseURL+"courseware/w1content/"); //?activate_block_id=block-v1%3AOUNL%2BICS18%2B2018_1%2Btype%40sequential%2Bblock%4083d79097d5d94304a6fa9a5aed25dce3");
		lesson.addContent(content);

		content = new ContentDescriptor(getUUID(), 
				"Week 1 challenge", 
				"Week 1 challenge", 
				testCourseBaseURL+"courseware/w1challenge/"); //?activate_block_id=block-v1%3AOUNL%2BICS18%2B2018_1%2Btype%40sequential%2Bblock%4083d79097d5d94304a6fa9a5aed25dce3");
		lesson.addContent(content);

		InformationObject info = generateControlWeek1IntroductionText();
		content.addInformationObject(info);

		info = generateTreatmentWeek1IntroductionText();
		content.addInformationObject(info);

		SelectionObject sele = generateUserAvatarSelection();
		content.addInformationObject(sele);
		
		sele = generateClanLogoSelection();
		content.addInformationObject(sele);
		
		sele = generateClanIdentitySelection();
		content.addInformationObject(sele);
		
		sele = generateUserIdentitySelection();
		content.addInformationObject(sele);
		
		info = new InformationObject(getUUID(), 
				"Clan rules", 
				"Clan rules", 
				testCourseBaseURL+"courseware/w1challenge/",
				false, true, true);
		content.addInformationObject(info);
		info.setContent("<OL>"+
			"<LI>Do not use the chat or other tools provided in the course to harm or hurt others (such as the members of your clan and or/and the other clan).</LI>"+
			"<LI>Respect other opinion.</LI>"+
			"<LI>Contributions within the clan must be civil and tasteful.</LI>"+
			"<LI>No disruptive, offensive or abusive behaviour: contributions must be constructive and polite, not mean-spirited or contributed with the intention of causing trouble.</LI>"+
			"<LI>No spamming or off-topic material can be shared.</LI>"+
			"<LI>On a more Safety level: We advise that you never reveal any personal information about yourself or anyone else (for example: telephone number, home address or email address).</LI>"+
			"</OL>");
		
		info = generateTreatmentWeek1TestIntroduction();
		content.addInformationObject(info);


		// Goal for Module 1
		
		GoalDescriptor goal = new GoalDescriptor(getUUID(), module.getTitle(), 
				"I intend to participate in the course activities to learn about "+module.getTitle(), "");
		ListIterator<LessonDescriptor> iterator = module.getLessons();
		while (iterator.hasNext()) {
			goal.addLesson(iterator.next());
		}
		course.addGoal(goal);

		
		return module;
	}
	
	protected static ModuleDescriptor generateWeek2(CourseDescriptor course) {
		ModuleDescriptor module = new ModuleDescriptor(getUUID(), 
				"Week2", 
				"Week 2 - Tips on how to protect your computer", "");
		course.addModule(module);
		
		LessonDescriptor lesson = new LessonDescriptor(getUUID() ,
				"Threats and attacks",
				"Threats and attacks","");
		module.addLesson(lesson);
		
		ContentDescriptor content = new ContentDescriptor(getUUID(), 
				"Week 2 content", 
				"Week 2 content", 
				testCourseBaseURL+"courseware/w2content/"); //?activate_block_id=block-v1%3AOUNL%2BICS18%2B2018_1%2Btype%40sequential%2Bblock%400b957f040f954b6ab1f4e64b533ba65b");
		lesson.addContent(content);
		
		content = new ContentDescriptor(getUUID(), 
				"Week 2 challenge", 
				"Week 2 challenge", 
				testCourseBaseURL+"courseware/w2challenge/"); //?activate_block_id=block-v1%3AOUNL%2BICS18%2B2018_1%2Btype%40sequential%2Bblock%400b957f040f954b6ab1f4e64b533ba65b");
		lesson.addContent(content);
		
		InformationObject info = new InformationObject(getUUID(), 
				"Communicate with the opponents", 
				"Communicate with the opponents", 
				testCourseBaseURL+"courseware/w2challenge/",
				true, true, true);
		content.addInformationObject(info);
		info.setContent("");
		
		info = new InformationObject(getUUID(), 
				"Encrypt your message", 
				"Communicate with the opponents", 
				testCourseBaseURL+"courseware/w2challenge/",
				true, true, true);
		content.addInformationObject(info);
		info.setContent("");
		
		info = new InformationObject(getUUID(), 
				"Encrypted Message received", 
				"How do you decrypt it?", 
				testCourseBaseURL+"courseware/w2challenge/",
				true, true, true);
		content.addInformationObject(info);
		info.setContent("");
		
		SelectionObject sele = generateDecryptionSelection();
		content.addInformationObject(sele);
		
		info = new InformationObject(getUUID(), 
				"Clan challenge results", 
				"How'd you do?", 
				testCourseBaseURL+"courseware/w2challenge/",
				true, true, true);
		content.addInformationObject(info);
		info.setContent("");
		
		// Goal for module 2
		
		GoalDescriptor goal = new GoalDescriptor(getUUID(), module.getTitle(), 
				"I intend to participate in the course activities to learn about "+module.getTitle(), "");
		ListIterator<LessonDescriptor> iterator = module.getLessons();
		while (iterator.hasNext()) {
			goal.addLesson(iterator.next());
		}
		course.addGoal(goal);
		
		return module;
	}
	
	protected static ModuleDescriptor generateWeek3(CourseDescriptor course) {
		ModuleDescriptor module = new ModuleDescriptor(getUUID(), 
				"Week 3", 
				"Week 3 - Tips on how to protect your computer", "");
		course.addModule(module);
		
		LessonDescriptor lesson = new LessonDescriptor(getUUID() ,
				"Threats and attacks",
				"Threats and attacks","");
		module.addLesson(lesson);
		
		ContentDescriptor content = new ContentDescriptor(getUUID(), 
				"Week 3 content", 
				"Week 3 content", 
				testCourseBaseURL+"courseware/w3content/"); //?activate_block_id=block-v1%3AOUNL%2BICS18%2B2018_1%2Btype%40sequential%2Bblock%400b957f040f954b6ab1f4e64b533ba65b");
		lesson.addContent(content);
		
		content = new ContentDescriptor(getUUID(), 
				"Week 3 challenge", 
				"Week 3 challenge", 
				testCourseBaseURL+"courseware/w3challenge/"); //?activate_block_id=block-v1%3AOUNL%2BICS18%2B2018_1%2Btype%40sequential%2Bblock%400b957f040f954b6ab1f4e64b533ba65b");
		lesson.addContent(content);

		InformationObject info = new InformationObject(getUUID(), 
				"Test your knowledge", 
				"Test your knowledge", 
				testCourseBaseURL+"courseware/w3challenge/",
				true, true, true);
		content.addInformationObject(info);
		info.setContent("");
		
		SelectionObject sele = generateWeek2KnowledgeTest1();
		content.addInformationObject(sele);
		sele.setDeadline(week2deadline);
		
		sele = generateWeek2KnowledgeTest2();
		content.addInformationObject(sele);
		sele.setDeadline(week2deadline);
				
		sele = generateWeek2KnowledgeTest3();
		content.addInformationObject(sele);
		sele.setDeadline(week2deadline);
				
		sele = generateWeek2KnowledgeTest4();
		content.addInformationObject(sele);
		sele.setDeadline(week2deadline);
				
		sele = generateWeek2KnowledgeTest5();
		content.addInformationObject(sele);
		sele.setDeadline(week2deadline);
				
		sele = generateWeek2KnowledgeTest6();
		content.addInformationObject(sele);
		sele.setDeadline(week2deadline);
				
		sele = generateWeek2KnowledgeTest7();
		content.addInformationObject(sele);
		sele.setDeadline(week2deadline);
				
		sele = generateWeek2KnowledgeTest8();
		content.addInformationObject(sele);
		sele.setDeadline(week2deadline);
				
		sele = generateWeek2KnowledgeTest9();
		content.addInformationObject(sele);
		sele.setDeadline(week2deadline);
				
		sele = generateWeek2KnowledgeTest10();
		content.addInformationObject(sele);
		sele.setDeadline(week2deadline);
				
		info = new InformationObject(getUUID(), 
				"Your results", 
				"Your results", 
				testCourseBaseURL+"courseware/w3challenge/",
				true, true, true);
		content.addInformationObject(info);
		info.setContent("");
		
		// Goal for module 2
		
		GoalDescriptor goal = new GoalDescriptor(getUUID(), module.getTitle(), 
				"I intend to participate in the course activities to learn about "+module.getTitle(), "");
		ListIterator<LessonDescriptor> iterator = module.getLessons();
		while (iterator.hasNext()) {
			goal.addLesson(iterator.next());
		}
		course.addGoal(goal);
		
		return module;
	}
	
	protected static ModuleDescriptor generateWeek4(CourseDescriptor course) {
		ModuleDescriptor module = new ModuleDescriptor(getUUID(), 
				"Week 4", 
				"Week 4 - Tips on how to protect your computer", "");
		course.addModule(module);
		
		LessonDescriptor lesson = new LessonDescriptor(getUUID() ,
				"Threats and attacks",
				"Threats and attacks","");
		module.addLesson(lesson);
		
		ContentDescriptor content = new ContentDescriptor(getUUID(), 
				"Week 4 content", 
				"Week 4 content", 
				testCourseBaseURL+"courseware/w4content/"); //?activate_block_id=block-v1%3AOUNL%2BICS18%2B2018_1%2Btype%40sequential%2Bblock%400b957f040f954b6ab1f4e64b533ba65b");
		lesson.addContent(content);
		
		content = new ContentDescriptor(getUUID(), 
				"Week 4 challenge", 
				"Week 4 challenge", 
				testCourseBaseURL+"courseware/w4challenge/"); //?activate_block_id=block-v1%3AOUNL%2BICS18%2B2018_1%2Btype%40sequential%2Bblock%400b957f040f954b6ab1f4e64b533ba65b");
		lesson.addContent(content);

		InformationObject info = new InformationObject(getUUID(), 
				"Discuss about cryptography", 
				"Discuss about cryptography", 
				testCourseBaseURL+"courseware/w4challenge/",
				true, true, true);
		content.addInformationObject(info);
		info.setContent("");
		
		// Goal for module 2
		
		GoalDescriptor goal = new GoalDescriptor(getUUID(), module.getTitle(), 
				"I intend to participate in the course activities to learn about "+module.getTitle(), "");
		ListIterator<LessonDescriptor> iterator = module.getLessons();
		while (iterator.hasNext()) {
			goal.addLesson(iterator.next());
		}
		course.addGoal(goal);
		
		return module;
	}

	
	// WEEK 1 INTERACTIVE ELEMENTS
	
	protected static InformationObject generateControlWeek1IntroductionText() {
		InformationObject info = new InformationObject(getUUID(), 
				"Test your knowledge!", 
				"Hello, there!", 
				"",
				true, false, false);

		info.setContent("Welcome😊 to the first week assignment, click on next 👉🏽 to take the test.<BR>" + 
				"<BR>" + 
				"Thank you in advance!<BR>" + 
				"<BR>" + 
				"The OUNL Team<BR>" + 
				"(Alessandra, Hugo and Roland)<BR>" + 
				"");
		
		return info;
	}
	
	
	protected static InformationObject generateTreatmentWeek1IntroductionText() {
		InformationObject info = new InformationObject(getUUID(), 
				"Explore your clan!", 
				"Hello, there!", 
				"",
				false, true, true);

		info.setContent("You have been selected among the participants of this course to join our clan 😊<BR>" + 
				"You will decide to which degree you would like to be involved.<BR>" + 
				"<BR>" + 
				"At the moment we do not have a name and neither a logo that define who we are. It is our task this week to agree on these and define as well as our rules.<BR>" + 
				"<BR>" + 
				"Be aware that there will be another clan, they will act against us, we need to act together for defeating them. <BR>" + 
				"<BR>" + 
				"Are you ready? Let’s do this 💪🏽<BR>" + 
				"<BR>" + 
				"The OUNL Team<BR>" + 
				"(Alessandra, Hugo and Roland)<BR>" + 
				"");
		
		return info;
	}
	
	
	protected static SelectionObject generateUserAvatarSelection() {
		String[] userIconFiles = {
				"001-boy.png",
				"002-girl.png",
				"003-boy-1.png",
				"004-woman.png",
				"005-boy-2.png",
				"006-man.png",
				"007-old-man.png",
				"008-girl-1.png",
				"009-man-1.png",
				"010-policeman.png",
				"011-girl-2.png",
				"012-man-2.png",
				"013-waiter.png",
				"014-woman-1.png",
				"015-man-3.png",
				"016-girl-3.png",
				"017-man-4.png",
				"018-girl-4.png",
				"019-boy-3.png",
				"020-woman-2.png",
				"021-girl-5.png",
				"022-postman.png",
				"023-girl-6.png",
				"024-clown.png",
				"025-woman-3.png",
				"026-boy-4.png",
				"027-old-woman.png",
				"028-santa-claus.png",
				"029-waitress.png",
				"030-man-5.png",
				"031-man-6.png",
				"032-burglar.png",
				"033-woman-4.png",
				"034-man-7.png",
				"035-man-8.png",
				"036-man-9.png",
				"037-old-woman-1.png",
				"038-man-10.png",
				"039-woman-5.png",
				"040-boy-5.png",
				"041-woman-6.png",
				"042-boy-6.png",
				"043-girl-7.png",
				"044-boy-7.png",
				"045-builder.png",
				"046-man-11.png",
				"047-woman-7.png",
				"048-boy-8.png",
				"049-man-12.png",
				"050-woman-8.png",
				"001-man-13.png",
				"002-woman-14.png",
				"003-woman-13.png",
				"004-woman-12.png",
				"005-woman-11.png",
				"006-woman-10.png",
				"007-woman-9.png",
				"008-woman-8.png",
				"009-woman-7.png",
				"010-woman-6.png",
				"011-woman-5.png",
				"012-woman-4.png",
				"013-woman-3.png",
				"014-man-12.png",
				"015-man-11.png",
				"016-man-10.png",
				"017-man-9.png",
				"018-man-8.png",
				"019-man-7.png",
				"020-man-6.png",
				"021-man-5.png",
				"022-man-4.png",
				"023-man-3.png",
				"024-man-2.png",
				"025-man-1.png",
				"026-man.png",
				"027-boy-6.png",
				"028-boy-5.png",
				"029-boy-4.png",
				"030-boy-3.png",
				"031-boy-2.png",
				"032-boy-1.png",
				"033-boy.png",
				"034-woman-2.png",
				"035-woman-1.png",
				"036-woman.png",
				"user.png"
			};
		
		SelectionObject sele = new SelectionObject(getUUID(), 
				"Your choice", 
				"Select your avatar", 
				"",
				false, true, true);
		sele.setType(SelectionObjectType.SINGLE_USER_SELECTION);
		sele.setPurpose(SelectionObjectPurpose.USER_AVATAR);
		
		SelectionOption option = null;
		for (String url : userIconFiles) {
			option = new SelectionOption(getUUID(), 
					url, 
					url, 
					"/img/profile/"+url);
			sele.addOption(option);
		}
		
		return sele;
	}

	protected static SelectionObject generateUserIdentitySelection() {
		String[] userTitles = {
				"Leader",
				"Fuel",
				"Moderator",
				"Buddy",
				"Member"
			};
		
		String[] userDescriptions = {
				"By leading by example you make all the other follow you.",
				"You are the energiser of the group, you keep all engaged.",
				"You take care that the communication within the clan flows and go smooth without conflicts.",
				"Do you need help in understanding or find something in the course? I am your guy, we can do together.",
				"You don’t have any active role but this will not stop you in participating."
			};
		
		SelectionObject sele = new SelectionObject(getUUID(), 
				"Your choice", 
				"Hello there! Please select one of the roles you want to cover in your clan:", 
				"",
				false, true, true);
		sele.setType(SelectionObjectType.SINGLE_USER_SELECTION);
		sele.setPurpose(SelectionObjectPurpose.USER_IDENTITY);
		
		SelectionOption option = null;
		for (int i=0; i<userTitles.length; i++) {
			option = new SelectionOption(getUUID(), 
					userTitles[i], 
					userDescriptions[i], 
					"");
			sele.addOption(option);
		}
		
		return sele;
	}

	protected static SelectionObject generateClanLogoSelection() {
		String[] clanLogoFiles = {
				"defender/broken-zone.png",
				"defender/lock.png",
				"defender/lock1.png",
				"defender/security.png",
				"defender/server.png",
				"defender/settings.png",
				"defender/shield.png",
				"defender/shield1.png",
				"defender/spyware.png",
				"defender/vpn.png",
				"hacker/browser.png",
				"hacker/coding.png",
				"hacker/hacker.png",
				"hacker/hacker1.png",
				"hacker/hacker2.png",
				"hacker/hacker3.png",
				"hacker/hacking.png",
				"hacker/key.png",
				"hacker/padlock.png",
				"hacker/web-security.png"
		};
		
		SelectionObject sele = new SelectionObject(getUUID(), 
				"Clan vote", 
				"Vote for your preferred clan logos", 
				"",
				false, true, true);
		sele.setType(SelectionObjectType.CLAN_SELECTION);
		sele.setPurpose(SelectionObjectPurpose.CLAN_AVATAR);
		
		SelectionOption option = null;
		for (String url : clanLogoFiles) {
			option = new SelectionOption(getUUID(), 
					url, 
					url, 
					"/img/clan/"+url);
			sele.addOption(option);
		}
		
		return sele;
	}

	protected static SelectionObject generateClanIdentitySelection() {
		String[] clanTitles = {
				"The Guardians", 
				"Legion of CODEfenders", 
				"Bureau Of SecretS (BOSS)",
				"Security Squad",
				"The Ethical Hackers",
				"Anonymous",
				"Ghost Squad",
				"The Unknown"
			};
		
		String[] clanDescriptions = {
				"We are the guardians of the Internet users’ data. We act anonymously to secure them.",
				"We act together to prevent malicious code attacks",
				"Our community aims to protect the secrets of the Internet users, independently from their aim (good of bad).",
				"Our group patrol the Internet, making sure that information is transferred encrypted ",
				"We are skilled like the hackers but we use our knowledge for good.",
				"Our identity is secret and we act on the Internet as we wish, even if this goes to the detriment of others.",
				"We move in the shadow to gain information on the Internet about people we want to attack/harm",
				"Our identity is unknown and we aim to find the vulnerability of the Internet before the bad guys."
			};
		
		SelectionObject sele = new SelectionObject(getUUID(), 
				"Clan vote", 
				"Vote for your preferred clan names", 
				"",
				false, true, true);
		sele.setType(SelectionObjectType.CLAN_SELECTION);
		sele.setPurpose(SelectionObjectPurpose.CLAN_IDENTITY);
		
		SelectionOption option = null;
		for (int i=0; i<clanTitles.length; i++) {
			option = new SelectionOption(getUUID(), 
					clanTitles[i], 
					clanDescriptions[i], 
					"");
			sele.addOption(option);
		}
		
		return sele;
	}


	protected static SelectionObject generateClanARulesSelection() {
		String[] clanTitles = {
				"Our mission is to make the Internet a secure place for everyone (bad and good guys).", 
				"We defend all secrets shared on the Internet, whether good or bad.",
				"Our common purpose is to to defend users’ information independently from their nature.",
				"We act in solo missions pursuing the common purpose.",
				"Our common purpose is to present hackers to harm people or create damage in general.",
				"We work together as a group.", 
				"Our strength is our knowledge.",
				"Our mission is to make the Internet a secure and ethical place."
			};
		
		String[] clanDescriptions = {
				"Our mission is to make the Internet a secure place for everyone (bad and good guys).", 
				"We defend all secrets shared on the Internet, whether good or bad.",
				"Our common purpose is to to defend users’ information independently from their nature.",
				"We act in solo missions pursuing the common purpose.",
				"Our common purpose is to present hackers to harm people or create damage in general.",
				"We work together as a group.", 
				"Our strength is our knowledge.",
				"Our mission is to make the Internet a secure and ethical place."
			};
		
		SelectionObject sele = new SelectionObject(getUUID(), 
				"Clan vote", 
				"Vote for your preferred clan rules", 
				"",
				false, true, false);
		sele.setType(SelectionObjectType.CLAN_SELECTION);
		sele.setPurpose(SelectionObjectPurpose.CLAN_VOTING);
		
		SelectionOption option = null;
		for (int i=0; i<clanTitles.length; i++) {
			option = new SelectionOption(getUUID(), 
					clanTitles[i], 
					clanDescriptions[i], 
					"");
			sele.addOption(option);
		}
		
		return sele;
	}


	protected static SelectionObject generateClanBRulesSelection() {
		String[] clanTitles = {
				"We are ethical hackers, we use our knowledge to disable the “real” hackers from harming people or creating damage, in general.", 
				"We want to do whatever we want on the Internet without caring of the ethical issues.", 
				"We steal information from the bad guys to protect the good, like Robin Hood.", 
				"Our identities are anonymous. ",
				"We work together as a group. ", 
				"Our common purpose is to find the flaws in the information systems before the real hackers.",
				"Our common purpose is to create chaos on the Internet.",
				"We act in solo missions pursuing the common purpose."
			};
		
		String[] clanDescriptions = {
				"We are ethical hackers, we use our knowledge to disable the “real” hackers from harming people or creating damage, in general.", 
				"We want to do whatever we want on the Internet without caring of the ethical issues.", 
				"We steal information from the bad guys to protect the good, like Robin Hood.", 
				"Our identities are anonymous. ",
				"We work together as a group. ", 
				"Our common purpose is to find the flaws in the information systems before the real hackers.",
				"Our common purpose is to create chaos on the Internet.",
				"We act in solo missions pursuing the common purpose."
			};
		
		SelectionObject sele = new SelectionObject(getUUID(), 
				"Clan vote", 
				"Vote for your preferred clan rules", 
				"",
				false, false, true);
		sele.setType(SelectionObjectType.CLAN_SELECTION);
		sele.setPurpose(SelectionObjectPurpose.CLAN_VOTING);
		
		SelectionOption option = null;
		for (int i=0; i<clanTitles.length; i++) {
			option = new SelectionOption(getUUID(), 
					clanTitles[i], 
					clanDescriptions[i], 
					"");
			sele.addOption(option);
		}
		
		return sele;
	}


	protected static InformationObject generateTreatmentWeek1TestIntroduction() {
		InformationObject info = new InformationObject(getUUID(), 
				"Let’s now challenge your ability to work together!", 
				"Hello, there!", 
				"",
				false, true, true);

		info.setContent("Your mission is to solve the following knowledge test!<BR>" + 
				"Remember it is a group effort: only the most voted answer will count, be sure you all select the correct one!<BR>" + 
				"<BR>" + 
				"Work well on this simple test because we are going to level up in the next one<BR>" + 
				"<BR>" + 
				"The OUNL team<BR>" + 
				"");
		
		return info;
	}
	
	
	protected static InformationObject generateWeek1TestConclusion() {
		InformationObject info = new InformationObject(getUUID(), 
				"We have saved your answers!", 
				"Hello, there!", 
				"",
				true, true, true);

		info.setContent("Are you curious to know how did you perform?<BR> "
				+ "If yes check out the “Result page week 1”  that will visible from next Monday morning.<BR> "
				+ "<BR>"
				+ "The OUNL team\n" + 
				"");
		
		return info;
	}
	

	protected static SelectionObject generateWeek1KnowledgeTest1() {
		SelectionObject sele = new SelectionObject(getUUID(), 
				"Test", 
				"What does cryptography mean?", 
				"",
				true, true, true);
		sele.setType(SelectionObjectType.CLAN_SINGLE_TEST);
		sele.setPurpose(SelectionObjectPurpose.TEST);
		
		SelectionOption option = new SelectionOption(getUUID(), 
				"Hidden words", 
				"Hidden words", 
				"");
		option.setCorrect(false);
		sele.addOption(option);
		
		option = new SelectionOption(getUUID(), 
				"Hidden writing", 
				"Hidden writing", 
				"");
		option.setCorrect(true);
		sele.addOption(option);
		
		option = new SelectionOption(getUUID(), 
				"Hidden secret", 
				"Hidden secret", 
				"");
		option.setCorrect(false);
		sele.addOption(option);

		option = new SelectionOption(getUUID(), 
				"Hidden letters", 
				"Hidden letters", 
				"");
		option.setCorrect(false);
		sele.addOption(option);
				
		return sele;
	}
	
	protected static SelectionObject generateWeek1KnowledgeTest2() {
		SelectionObject sele = new SelectionObject(getUUID(), 
				"Test", 
				"Cryptography dates back to?", 
				"",
				true, true, true);
		sele.setType(SelectionObjectType.CLAN_SINGLE_TEST);
		sele.setPurpose(SelectionObjectPurpose.TEST);
		
		SelectionOption option = new SelectionOption(getUUID(), 
				"The Egyptian (Prior to 3100 BC)", 
				"The Egyptian (Prior to 3100 BC)", 
				"");
		option.setCorrect(false);
		sele.addOption(option);
		
		option = new SelectionOption(getUUID(), 
				"Creation of the Internet  (1960)", 
				"Creation of the Internet  (1960)", 
				"");
		option.setCorrect(false);
		sele.addOption(option);
		
		option = new SelectionOption(getUUID(), 
				"The Greek (600 BC)", 
				"The Greek (600 BC)", 
				"");
		option.setCorrect(true);
		sele.addOption(option);
		
		option = new SelectionOption(getUUID(), 
				"The Roman (27 BC)", 
				"The Roman (27 BC)", 
				"");
		option.setCorrect(false);
		sele.addOption(option);
				
		return sele;
	}
	
	
	protected static SelectionObject generateWeek1KnowledgeTest3() {
		SelectionObject sele = new SelectionObject(getUUID(), 
				"Test", 
				"Which of the following statements  is correct?", 
				"",
				true, true, true);
		sele.setType(SelectionObjectType.CLAN_MULTI_TEST);
		sele.setPurpose(SelectionObjectPurpose.TEST);
		
		SelectionOption option = new SelectionOption(getUUID(), 
				"The plaintext is the input for encryption.", 
				"The plaintext is the input for encryption.", 
				"");
		option.setCorrect(true);
		sele.addOption(option);
		
		option = new SelectionOption(getUUID(), 
				"The ciphertext is the input for encryption.", 
				"The ciphertext is the input for encryption.", 
				"");
		option.setCorrect(false);
		sele.addOption(option);
		
		option = new SelectionOption(getUUID(), 
				"Encrypting results in a key.", 
				"Encrypting results in a key.", 
				"");
		option.setCorrect(false);
		sele.addOption(option);
				
		option = new SelectionOption(getUUID(), 
				"You cannot decrypt a ciphertext with the same key it was encrypted with.", 
				"You cannot decrypt a ciphertext with the same key it was encrypted with.", 
				"");
		option.setCorrect(false);
		sele.addOption(option);
		
		return sele;
	}
	
	
	protected static SelectionObject generateWeek1KnowledgeTest4() {
		SelectionObject sele = new SelectionObject(getUUID(), 
				"Test", 
				"What is a cipher?", 
				"",
				true, true, true);
		sele.setType(SelectionObjectType.CLAN_SINGLE_TEST);
		sele.setPurpose(SelectionObjectPurpose.TEST);
		
		SelectionOption option = new SelectionOption(getUUID(), 
				"the (unreadable) message of the sender", 
				"the (unreadable) message of the sender", 
				"");
		option.setCorrect(false);
		sele.addOption(option);
		
		option = new SelectionOption(getUUID(), 
				"the (unreadable) message once decrypted", 
				"the (unreadable) message once decrypted", 
				"");
		option.setCorrect(true);
		sele.addOption(option);
		
		option = new SelectionOption(getUUID(), 
				"the (unreadable) message of the cipher", 
				"the (unreadable) message of the cipher", 
				"");
		option.setCorrect(false);
		sele.addOption(option);
		
		option = new SelectionOption(getUUID(), 
				"the (unreadable) message once encrypted", 
				"the (unreadable) message once encrypted", 
				"");
		option.setCorrect(true);
		sele.addOption(option);
				
		return sele;
	}
	
	
	protected static SelectionObject generateWeek1KnowledgeTest5() {
		SelectionObject sele = new SelectionObject(getUUID(), 
				"Test", 
				"Which of the below describes a cipher?", 
				"",
				true, true, true);
		sele.setType(SelectionObjectType.CLAN_SINGLE_TEST);
		sele.setPurpose(SelectionObjectPurpose.TEST);
		
		SelectionOption option = new SelectionOption(getUUID(), 
				"The pictorial instructions on a washing label", 
				"The pictorial instructions on a washing label", 
				"");
		option.setCorrect(false);
		sele.addOption(option);
		
		option = new SelectionOption(getUUID(), 
				"The NATO phonetic alphabet (Alpha, Bravo, Charlie, Delta, Echo, …)", 
				"The NATO phonetic alphabet (Alpha, Bravo, Charlie, Delta, Echo, …)", 
				"");
		option.setCorrect(true);
		sele.addOption(option);
		
		option = new SelectionOption(getUUID(), 
				"Barcodes on supermarket items", 
				"Barcodes on supermarket items", 
				"");
		option.setCorrect(false);
		sele.addOption(option);
		
		option = new SelectionOption(getUUID(), 
				"Descriptions in a crossword puzzle", 
				"Descriptions in a crossword puzzle", 
				"");
		option.setCorrect(false);
		sele.addOption(option);
				
		return sele;
	}
	

	protected static SelectionObject generateWeek1KnowledgeTest6() {
		SelectionObject sele = new SelectionObject(getUUID(), 
				"Test", 
				"What is Kerckhoffs’ principle?", 
				"",
				true, true, true);
		sele.setType(SelectionObjectType.CLAN_SINGLE_TEST);
		sele.setPurpose(SelectionObjectPurpose.TEST);
		
		SelectionOption option = new SelectionOption(getUUID(), 
				"Something in French", 
				"Something in French", 
				"");
		option.setCorrect(false);
		sele.addOption(option);
		
		option = new SelectionOption(getUUID(), 
				"That you must keep the system secret from the adversary", 
				"That you must keep the system secret from the adversary", 
				"");
		option.setCorrect(false);
		sele.addOption(option);
		
		option = new SelectionOption(getUUID(), 
				"That the key should be kept secret", 
				"That the key should be kept secret", 
				"");
		option.setCorrect(false);
		sele.addOption(option);
		
		option = new SelectionOption(getUUID(), 
				"That the system is secure if the enemy can’t decrypt without the key", 
				"That the system is secure if the enemy can’t decrypt without the key", 
				"");
		option.setCorrect(true);
		sele.addOption(option);
				
		return sele;
	}
	
	
	// WEEK 2 interactive elements


	protected static SelectionObject generateEncryptedMessageSelection() {
		SelectionObject sele = new SelectionObject(getUUID(), 
				"Send an encrypted message!", 
				"decide your plaintext/ the message", 
				"",
				false, true, true);
		sele.setType(SelectionObjectType.CLAN_SELECTION);
		sele.setPurpose(SelectionObjectPurpose.CLAN_VOTING);
		
		SelectionOption option = new SelectionOption(getUUID(), 
				"Make them aware ...", 
				"“One of the most singular characteristics of the art of deciphering is the strong conviction possessed by every person, even moderately acquainted with it, that he is able to construct a cipher which nobody else can decipher. I have also observed that the cleverer the person, the more intimate is his conviction.” <BR>\n" + 
				"― Charles Babbage", 
				"");
		sele.addOption(option);
		
		option = new SelectionOption(getUUID(), 
				"Whisper...", 
				"In this age of communications that span both distance and time, the only tool we have that approximates a 'whisper' is encryption. When I cannot whisper in my wife's ear or the ears of my business partners, and have to communicate electronically, then encryption is our tool to keep our secrets secret. <BR>"
				+ "― John McAfee", 
				"");
		sele.addOption(option);
		
		option = new SelectionOption(getUUID(), 
				"Give them a good advice...", 
				"“Cybercriminals can use personal details, such as your favorite color, the last four digits of your credit card and your email addresses, to make educated guesses about your sign-in credentials. They might contact a service provider posing as a user, provide identifying details and gain even greater access to accounts. This is why it is so important to prevent your security credentials from becoming stagnant. Rotate your password every few months and consider using new security questions and answers, too.” <BR>\n"
				+ "– Larry Alton", 
				"");
		sele.addOption(option);
		
		option = new SelectionOption(getUUID(), 
				"Tease them...", 
				"“All warfare is based on deception. Hence, when we are able to attack, we must seem unable; when using our forces, we must appear inactive; when we are near, we must make the enemy believe we are far away; when far away, we must make him believe we are near.” <BR>\n" + 
				"― Sun tzu, The Art of War", 
				"");
		sele.addOption(option);
		
		return sele;
	}
	
	protected static SelectionObject generateEncryptionMethodSelectionClan1() {
		SelectionObject sele = new SelectionObject("encryptionMethod-"+Clan.CLAN_1_ID, 
				"Send an encrypted message!", 
				"decide your encryption mechanism", 
				"",
				false, true, false);
		sele.setType(SelectionObjectType.CLAN_SELECTION);
		sele.setPurpose(SelectionObjectPurpose.CLAN_VOTING);
		
		SelectionOption option = new SelectionOption(getUUID(), 
				"Caesar shift of 5", 
				"tsj tk ymj rtxy xnslzqfw hmfwfhyjwnxynhx tk ymj fwy tk ijhnumjwnsl nx ymj xywtsl htsanhynts utxxjxxji gd jajwd ujwxts, jajs rtijwfyjqd fhvzfnsyji bnym ny, ymfy mj nx fgqj yt htsxywzhy f hnumjw bmnhm stgtid jqxj hfs ijhnumjw. n mfaj fqxt tgxjwaji ymfy ymj hqjajwjw ymj ujwxts, ymj rtwj nsynrfyj nx mnx htsanhynts ― hmfwqjx gfggflj", 
				"");
		sele.addOption(option);
		
		option = new SelectionOption(getUUID(), 
				"Caesar shift of 12", 
				"azq ar ftq yaef euzsgxmd otmdmofqduefuoe ar ftq mdf ar pqoubtqduzs ue ftq efdazs oazhuofuaz baeeqeeqp nk qhqdk bqdeaz, qhqz yapqdmfqxk mocgmuzfqp iuft uf, ftmf tq ue mnxq fa oazefdgof m oubtqd ituot zanapk qxeq omz pqoubtqd. u tmhq mxea aneqdhqp ftmf ftq oxqhqdqd ftq bqdeaz, ftq yadq uzfuymfq ue tue oazhuofuaz \n" + 
				"― otmdxqe nmnnmsq", 
				"");
		sele.addOption(option);
		
		option = new SelectionOption(getUUID(), 
				"Caesar shift of 17", 
				"fev fw kyv dfjk jzexlcri tyrirtkvizjkztj fw kyv rik fw uvtzgyvizex zj kyv jkifex tfemztkzfe gfjjvjjvu sp vmvip gvijfe, vmve dfuvirkvcp rthlrzekvu nzky zk, kyrk yv zj rscv kf tfejkiltk r tzgyvi nyzty efsfup vcjv tre uvtzgyvi. z yrmv rcjf fsjvimvu kyrk kyv tcvmvivi kyv gvijfe, kyv dfiv zekzdrkv zj yzj tfemztkzfe \n" + 
				"― tyricvj srssrxv", 
				"");
		sele.addOption(option);
		
		option = new SelectionOption(getUUID(), 
				"Caesar shift of 22", 
				"kja kb pda ikop oejcqhwn ydwnwypaneopeyo kb pda wnp kb zayeldanejc eo pda opnkjc ykjreypekj lkooaooaz xu aranu lanokj, araj ikzanwpahu wymqwejpaz sepd ep, pdwp da eo wxha pk ykjopnqyp w yeldan sdeyd jkxkzu ahoa ywj zayeldan. e dwra whok kxoanraz pdwp pda yharanan pda lanokj, pda ikna ejpeiwpa eo deo ykjreypekj \n" + 
				"― ydwnhao xwxxwca", 
				"");
		sele.addOption(option);
		
		return sele;
	}
	
	protected static SelectionObject generateEncryptionMethodSelectionClan2() {
		SelectionObject sele = new SelectionObject("encryptionMethod-"+Clan.CLAN_2_ID, 
				"Send an encrypted message!", 
				"decide your encryption mechanism", 
				"",
				false, false, true);
		sele.setType(SelectionObjectType.CLAN_SELECTION);
		sele.setPurpose(SelectionObjectPurpose.CLAN_VOTING);
		
		SelectionOption option = new SelectionOption(getUUID(), 
				"Caesar shift of 5", 
				"tsj tk ymj rtxy xnslzqfw hmfwfhyjwnxynhx tk ymj fwy tk ijhnumjwnsl nx ymj xywtsl htsanhynts utxxjxxji gd jajwd ujwxts, jajs rtijwfyjqd fhvzfnsyji bnym ny, ymfy mj nx fgqj yt htsxywzhy f hnumjw bmnhm stgtid jqxj hfs ijhnumjw. n mfaj fqxt tgxjwaji ymfy ymj hqjajwjw ymj ujwxts, ymj rtwj nsynrfyj nx mnx htsanhynts ― hmfwqjx gfggflj", 
				"");
		sele.addOption(option);
		
		option = new SelectionOption(getUUID(), 
				"Caesar shift of 12", 
				"azq ar ftq yaef euzsgxmd otmdmofqduefuoe ar ftq mdf ar pqoubtqduzs ue ftq efdazs oazhuofuaz baeeqeeqp nk qhqdk bqdeaz, qhqz yapqdmfqxk mocgmuzfqp iuft uf, ftmf tq ue mnxq fa oazefdgof m oubtqd ituot zanapk qxeq omz pqoubtqd. u tmhq mxea aneqdhqp ftmf ftq oxqhqdqd ftq bqdeaz, ftq yadq uzfuymfq ue tue oazhuofuaz \n" + 
				"― otmdxqe nmnnmsq", 
				"");
		sele.addOption(option);
		
		option = new SelectionOption(getUUID(), 
				"Caesar shift of 17", 
				"fev fw kyv dfjk jzexlcri tyrirtkvizjkztj fw kyv rik fw uvtzgyvizex zj kyv jkifex tfemztkzfe gfjjvjjvu sp vmvip gvijfe, vmve dfuvirkvcp rthlrzekvu nzky zk, kyrk yv zj rscv kf tfejkiltk r tzgyvi nyzty efsfup vcjv tre uvtzgyvi. z yrmv rcjf fsjvimvu kyrk kyv tcvmvivi kyv gvijfe, kyv dfiv zekzdrkv zj yzj tfemztkzfe \n" + 
				"― tyricvj srssrxv", 
				"");
		sele.addOption(option);
		
		option = new SelectionOption(getUUID(), 
				"Caesar shift of 22", 
				"kja kb pda ikop oejcqhwn ydwnwypaneopeyo kb pda wnp kb zayeldanejc eo pda opnkjc ykjreypekj lkooaooaz xu aranu lanokj, araj ikzanwpahu wymqwejpaz sepd ep, pdwp da eo wxha pk ykjopnqyp w yeldan sdeyd jkxkzu ahoa ywj zayeldan. e dwra whok kxoanraz pdwp pda yharanan pda lanokj, pda ikna ejpeiwpa eo deo ykjreypekj \n" + 
				"― ydwnhao xwxxwca", 
				"");
		sele.addOption(option);
		
		return sele;
	}
	
	protected static SelectionObject generateDecryptionSelection() {
		SelectionObject sele = new SelectionObject(getUUID(), 
				"You received a secret message! It is encrypted? What does it mean?", 
				"Decide on the meaning of this message", 
				"",
				false, true, true);
		sele.setType(SelectionObjectType.CLAN_SELECTION);
		sele.setPurpose(SelectionObjectPurpose.CLAN_VOTING);
		sele.setContent("");
		
		SelectionOption option = new SelectionOption(getUUID(), 
				"We got you", 
				"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam nisl urna, tempus non congue eu, vulputate at nibh. Nullam maximus, ex ac aliquet eleifend, odio libero mollis est, ut vulputate.", 
				"");
		sele.addOption(option);
		
		option = new SelectionOption(getUUID(), 
				"We are after you", 
				"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam nisl urna, tempus non congue eu, vulputate at nibh. Nullam maximus, ex ac aliquet eleifend, odio libero mollis est, ut vulputate.", 
				"");
		sele.addOption(option);
		
		option = new SelectionOption(getUUID(), 
				"You will never be able to read this", 
				"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam nisl urna, tempus non congue eu, vulputate at nibh. Nullam maximus, ex ac aliquet eleifend, odio libero mollis est, ut vulputate.", 
				"");
		sele.addOption(option);
		
		option = new SelectionOption(getUUID(), 
				"You should be warned", 
				"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam nisl urna, tempus non congue eu, vulputate at nibh. Nullam maximus, ex ac aliquet eleifend, odio libero mollis est, ut vulputate.", 
				"");
		sele.addOption(option);
		
		option = new SelectionOption(getUUID(), 
				"Read carefully!", 
				"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam nisl urna, tempus non congue eu, vulputate at nibh. Nullam maximus, ex ac aliquet eleifend, odio libero mollis est, ut vulputate.", 
				"");
		sele.addOption(option);
		
		return sele;
	}
	
/*
1. Which of the below statements is correct?

The text “MJQQT BTWQI” is a Caesar encryption of an English sentence.
There are 26 possible keys for Caesar.
When using permutations, there are twice as many keys as in standard Caesar.
Caesar satisfies Kerckhoffs’ Principle.

 */
	protected static SelectionObject generateWeek2KnowledgeTest1() {
		SelectionObject sele = new SelectionObject(getUUID(), 
				"Test", 
				"Which of the below statements is correct?", 
				"",
				true, true, true);
		sele.setType(SelectionObjectType.CLAN_SINGLE_TEST);
		sele.setPurpose(SelectionObjectPurpose.TEST);
		
		SelectionOption option = new SelectionOption(getUUID(), 
				"The text “MJQQT BTWQI” is a Caesar encryption of an English sentence.", "The text “MJQQT BTWQI” is a Caesar encryption of an English sentence.", "");
		option.setCorrect(true);
		sele.addOption(option);
		
		option = new SelectionOption(getUUID(), 
				"There are 26 possible keys for Caesar.", "There are 26 possible keys for Caesar.", "");
		option.setCorrect(false);
		sele.addOption(option);
		
		option = new SelectionOption(getUUID(), 
				"When using permutations, there are twice as many keys as in standard Caesar.", "When using permutations, there are twice as many keys as in standard Caesar.", "");
		option.setCorrect(false);
		sele.addOption(option);
		
		option = new SelectionOption(getUUID(), 
				"Caesar satisfies Kerckhoffs’ Principle.", "Caesar satisfies Kerckhoffs’ Principle.", "");
		option.setCorrect(false);
		sele.addOption(option);
				
		return sele;
	}

/*
2. Which of the below statements is correct?

The e is the most frequent letter in a ciphertext.
Caesar encryption hides the structure of the plaintext.
Caesar merely shifts the distribution of the frequency of letters.
Frequency analysis does not work against Caesar when using permutation keys.

	
 */
	protected static SelectionObject generateWeek2KnowledgeTest2() {
		SelectionObject sele = new SelectionObject(getUUID(), 
				"Test", 
				"Which of the below statements is correct?", 
				"",
				true, true, true);
		sele.setType(SelectionObjectType.CLAN_SINGLE_TEST);
		sele.setPurpose(SelectionObjectPurpose.TEST);
		
		SelectionOption option = new SelectionOption(getUUID(), 
				"The e is the most frequent letter in a ciphertext.", "The e is the most frequent letter in a ciphertext.", "");
		option.setCorrect(false);
		sele.addOption(option);
		
		option = new SelectionOption(getUUID(), 
				"Caesar encryption hides the structure of the plaintext.", "Caesar encryption hides the structure of the plaintext.", "");
		option.setCorrect(false);
		sele.addOption(option);
		
		option = new SelectionOption(getUUID(), 
				"Caesar merely shifts the distribution of the frequency of letters.", "Caesar merely shifts the distribution of the frequency of letters.", "");
		option.setCorrect(true);
		sele.addOption(option);
		
		option = new SelectionOption(getUUID(), 
				"Frequency analysis does not work against Caesar when using permutation keys.", "Frequency analysis does not work against Caesar when using permutation keys.", "");
		option.setCorrect(false);
		sele.addOption(option);
				
		return sele;
	}

/*
3.  The plaintext “HELLO” is encrypted with Vigenère. Which of the following is not a possible ciphertext?

EARTH
AAAAA
LLLLLL
CLOWN
 */
	protected static SelectionObject generateWeek2KnowledgeTest3() {
		SelectionObject sele = new SelectionObject(getUUID(), 
				"Test", 
				"The plaintext “HELLO” is encrypted with Vigenère. Which of the following is not a possible ciphertext?", 
				"",
				true, true, true);
		sele.setType(SelectionObjectType.CLAN_SINGLE_TEST);
		sele.setPurpose(SelectionObjectPurpose.TEST);
		
		SelectionOption option = new SelectionOption(getUUID(), 
				"EARTH", "EARTH", "");
		option.setCorrect(false);
		sele.addOption(option);
		
		option = new SelectionOption(getUUID(), 
				"AAAAA", "AAAAA", "");
		option.setCorrect(false);
		sele.addOption(option);
		
		option = new SelectionOption(getUUID(), 
				"LLLLLL", "LLLLLL", "");
		option.setCorrect(true);
		sele.addOption(option);
		
		option = new SelectionOption(getUUID(), 
				"CLOWN", "CLOWN", "");
		option.setCorrect(false);
		sele.addOption(option);
				
		return sele;
	}

/*
4. What key should you use in Vigenère to encrypt “BAGEL” as “DONUT”?

COHQI
EOTYE
MBLRT
FUCAK
	
 */
	protected static SelectionObject generateWeek2KnowledgeTest4() {
		SelectionObject sele = new SelectionObject(getUUID(), 
				"Test", 
				"What key should you use in Vigenère to encrypt “BAGEL” as “DONUT”?", 
				"",
				true, true, true);
		sele.setType(SelectionObjectType.CLAN_SINGLE_TEST);
		sele.setPurpose(SelectionObjectPurpose.TEST);
		
		SelectionOption option = new SelectionOption(getUUID(), 
				"COHQI", "COHQI", "");
		option.setCorrect(true);
		sele.addOption(option);
		
		option = new SelectionOption(getUUID(), 
				"EOTYE", "EOTYE", "");
		option.setCorrect(false);
		sele.addOption(option);
		
		option = new SelectionOption(getUUID(), 
				"MBLRT", "MBLRT", "");
		option.setCorrect(false);
		sele.addOption(option);
		
		option = new SelectionOption(getUUID(), 
				"FUCAK", "FUCAK", "");
		option.setCorrect(false);
		sele.addOption(option);
				
		return sele;
	}

/*
5. Suppose you want to encrypt a plaintext with Vigenère, first with the key “YES” and then with the key “NO”. How could you do this?

The only option is to encrypt twice with Vigenère
You can encrypt in one go with the key “YESNO”
You can encrypt in one go with the key “NOYES”
You can encrypt in one go with the key “LSFMRG”
	
 */
	protected static SelectionObject generateWeek2KnowledgeTest5() {
		SelectionObject sele = new SelectionObject(getUUID(), 
				"Test", 
				"Suppose you want to encrypt a plaintext with Vigenère, first with the key “YES” and then with the key “NO”. How could you do this?", 
				"",
				true, true, true);
		sele.setType(SelectionObjectType.CLAN_SINGLE_TEST);
		sele.setPurpose(SelectionObjectPurpose.TEST);
		
		SelectionOption option = new SelectionOption(getUUID(), 
				"The only option is to encrypt twice with Vigenère", "The only option is to encrypt twice with Vigenère", "");
		option.setCorrect(false);
		sele.addOption(option);
		
		option = new SelectionOption(getUUID(), 
				"You can encrypt in one go with the key “YESNO”", "You can encrypt in one go with the key “YESNO”", "");
		option.setCorrect(false);
		sele.addOption(option);
		
		option = new SelectionOption(getUUID(), 
				"You can encrypt in one go with the key “NOYES”", "You can encrypt in one go with the key “NOYES”", "");
		option.setCorrect(false);
		sele.addOption(option);
		
		option = new SelectionOption(getUUID(), 
				"You can encrypt in one go with the key “LSFMRG”", "You can encrypt in one go with the key “LSFMRG”", "");
		option.setCorrect(true);
		sele.addOption(option);
				
		return sele;
	}

/*
6. Which of the following statements about Vigenère keys is correct?

A password is always long enough.
A book key is best done using an obscure, unknown book.
Autokey results in a key as long as the plaintext.
When using autokey, you can decrypt the ciphertext with Caesar.
	
 */
	protected static SelectionObject generateWeek2KnowledgeTest6() {
		SelectionObject sele = new SelectionObject(getUUID(), 
				"Test", 
				"Which of the following statements about Vigenère keys is correct?", 
				"",
				true, true, true);
		sele.setType(SelectionObjectType.CLAN_SINGLE_TEST);
		sele.setPurpose(SelectionObjectPurpose.TEST);
		
		SelectionOption option = new SelectionOption(getUUID(), 
				"A password is always long enough.", "A password is always long enough.", "");
		option.setCorrect(false);
		sele.addOption(option);
		
		option = new SelectionOption(getUUID(), 
				"A book key is best done using an obscure, unknown book.", "A book key is best done using an obscure, unknown book.", "");
		option.setCorrect(false);
		sele.addOption(option);
		
		option = new SelectionOption(getUUID(), 
				"Autokey results in a key as long as the plaintext.", "Autokey results in a key as long as the plaintext.", "");
		option.setCorrect(true);
		sele.addOption(option);
		
		option = new SelectionOption(getUUID(), 
				"When using autokey, you can decrypt the ciphertext with Caesar.", "When using autokey, you can decrypt the ciphertext with Caesar.", "");
		option.setCorrect(false);
		sele.addOption(option);
				
		return sele;
	}

/*
7. Is frequency analysis useless against Vigenère?

Yes.
No, you can easily spot the shift.
No, but you have to first determine the key length.
No, but you first have to break the Caesar encryption.

	
 */
	protected static SelectionObject generateWeek2KnowledgeTest7() {
		SelectionObject sele = new SelectionObject(getUUID(), 
				"Test", 
				"Is frequency analysis useless against Vigenère?", 
				"",
				true, true, true);
		sele.setType(SelectionObjectType.CLAN_SINGLE_TEST);
		sele.setPurpose(SelectionObjectPurpose.TEST);
		
		SelectionOption option = new SelectionOption(getUUID(), 
				"Yes.", "Yes.", "");
		option.setCorrect(false);
		sele.addOption(option);
		
		option = new SelectionOption(getUUID(), 
				"No, you can easily spot the shift.", "No, you can easily spot the shift.", "");
		option.setCorrect(false);
		sele.addOption(option);
		
		option = new SelectionOption(getUUID(), 
				"No, but you have to first determine the key length.", "No, but you have to first determine the key length.", "");
		option.setCorrect(true);
		sele.addOption(option);
		
		option = new SelectionOption(getUUID(), 
				"No, but you first have to break the Caesar encryption.", "No, but you first have to break the Caesar encryption.", "");
		option.setCorrect(false);
		sele.addOption(option);
				
		return sele;
	}

/*
8. Which of the below statements is correct?

Caesar and Vigenère are both monoalphabetic ciphers.
Vigenère and Playfair are both monoalphabetic ciphers.
Caesar and Vigenère are both polyalphabetic ciphers.
Vigenère and Playfair are both polyalphabetic ciphers.
	
 */
	protected static SelectionObject generateWeek2KnowledgeTest8() {
		SelectionObject sele = new SelectionObject(getUUID(), 
				"Test", 
				"Which of the below statements is correct?", 
				"",
				true, true, true);
		sele.setType(SelectionObjectType.CLAN_SINGLE_TEST);
		sele.setPurpose(SelectionObjectPurpose.TEST);
		
		SelectionOption option = new SelectionOption(getUUID(), 
				"Caesar and Vigenère are both monoalphabetic ciphers.", "Caesar and Vigenère are both monoalphabetic ciphers.", "");
		option.setCorrect(false);
		sele.addOption(option);
		
		option = new SelectionOption(getUUID(), 
				"Vigenère and Playfair are both monoalphabetic ciphers.", "Vigenère and Playfair are both monoalphabetic ciphers.", "");
		option.setCorrect(false);
		sele.addOption(option);
		
		option = new SelectionOption(getUUID(), 
				"Caesar and Vigenère are both polyalphabetic ciphers.", "Caesar and Vigenère are both polyalphabetic ciphers.", "");
		option.setCorrect(false);
		sele.addOption(option);
		
		option = new SelectionOption(getUUID(), 
				"Vigenère and Playfair are both polyalphabetic ciphers.", "Vigenère and Playfair are both polyalphabetic ciphers.", "");
		option.setCorrect(true);
		sele.addOption(option);
				
		return sele;
	}

/*
9. What is the Playfair encryption of the plaintext “TEST” with the key “hello”?

UDTU
ROTU
DUUT
ORUT
	
 */
	protected static SelectionObject generateWeek2KnowledgeTest9() {
		SelectionObject sele = new SelectionObject(getUUID(), 
				"Test", 
				"What is the Playfair encryption of the plaintext “TEST” with the key “hello”?", 
				"",
				true, true, true);
		sele.setType(SelectionObjectType.CLAN_SINGLE_TEST);
		sele.setPurpose(SelectionObjectPurpose.TEST);
		
		SelectionOption option = new SelectionOption(getUUID(), 
				"UDTU", "UDTU", "");
		option.setCorrect(false);
		sele.addOption(option);
		
		option = new SelectionOption(getUUID(), 
				"ROTU", "ROTU", "");
		option.setCorrect(true);
		sele.addOption(option);
		
		option = new SelectionOption(getUUID(), 
				"DUUT", "DUUT", "");
		option.setCorrect(false);
		sele.addOption(option);
		
		option = new SelectionOption(getUUID(), 
				"ORUT", "ORUT", "");
		option.setCorrect(false);
		sele.addOption(option);
				
		return sele;
	}

/*
10. Is the One Time Pad completely secure?

Yes, unless you have a really powerful computer.
Yes, unless you are forced to reveal the plaintext.
Yes, unless you re-use the key.
Yes, unless the key is completely random.
 */
	protected static SelectionObject generateWeek2KnowledgeTest10() {
		SelectionObject sele = new SelectionObject(getUUID(), 
				"Test", 
				"Is the One Time Pad completely secure?", 
				"",
				true, true, true);
		sele.setType(SelectionObjectType.CLAN_SINGLE_TEST);
		sele.setPurpose(SelectionObjectPurpose.TEST);
		
		SelectionOption option = new SelectionOption(getUUID(), 
				"Yes, unless you have a really powerful computer.", "Yes, unless you have a really powerful computer.", "");
		option.setCorrect(false);
		sele.addOption(option);
		
		option = new SelectionOption(getUUID(), 
				"Yes, unless you are forced to reveal the plaintext.", "Yes, unless you are forced to reveal the plaintext.", "");
		option.setCorrect(false);
		sele.addOption(option);
		
		option = new SelectionOption(getUUID(), 
				"Yes, unless you re-use the key.", "Yes, unless you re-use the key.", "");
		option.setCorrect(true);
		sele.addOption(option);
		
		option = new SelectionOption(getUUID(), 
				"Yes, unless the key is completely random.", "Yes, unless the key is completely random.", "");
		option.setCorrect(false);
		sele.addOption(option);
				
		return sele;
	}

/*
1. What is 5 * 17 mod 13?
a. 5
b. 6
c. 7
d. 8

 */
	protected static SelectionObject generateWeek3KnowledgeTest1() {
		SelectionObject sele = new SelectionObject(getUUID(), 
				"Test", 
				"What is 5 * 17 mod 13?", 
				"",
				true, true, true);
		sele.setType(SelectionObjectType.CLAN_SINGLE_TEST);
		sele.setPurpose(SelectionObjectPurpose.TEST);
		
		SelectionOption option = new SelectionOption(getUUID(), 
				"5", "5", "");
		option.setCorrect(false);
		sele.addOption(option);
		
		option = new SelectionOption(getUUID(), 
				"6", "6", "");
		option.setCorrect(false);
		sele.addOption(option);
		
		option = new SelectionOption(getUUID(), 
				"7", "7", "");
		option.setCorrect(false);
		sele.addOption(option);
		
		option = new SelectionOption(getUUID(), 
				"8", "8", "");
		option.setCorrect(false);
		sele.addOption(option);
				
		return sele;
	}

	/*
2. What value of x makes this equation work? 5x = 1 mod 11
a. 6
b. 7
c. 8
d. 9
	 */
		protected static SelectionObject generateWeek3KnowledgeTest2() {
			SelectionObject sele = new SelectionObject(getUUID(), 
					"Test", 
					"What value of x makes this equation work? 5x = 1 mod 11", 
					"",
					true, true, true);
			sele.setType(SelectionObjectType.CLAN_SINGLE_TEST);
			sele.setPurpose(SelectionObjectPurpose.TEST);
			
			SelectionOption option = new SelectionOption(getUUID(), 
					"6", "6", "");
			option.setCorrect(false);
			sele.addOption(option);
			
			option = new SelectionOption(getUUID(), 
					"7", "7", "");
			option.setCorrect(false);
			sele.addOption(option);
			
			option = new SelectionOption(getUUID(), 
					"8", "8", "");
			option.setCorrect(false);
			sele.addOption(option);
			
			option = new SelectionOption(getUUID(), 
					"9", "9", "");
			option.setCorrect(true);
			sele.addOption(option);
					
			return sele;
		}

/*
3. Can you set up RSA with p = 6 and q = 8?
Yes
No, because p and q cannot both be even.
No, because q should be smaller than p.
No, because p and q should both be prime numbers.

 */
	protected static SelectionObject generateWeek3KnowledgeTest3() {
		SelectionObject sele = new SelectionObject(getUUID(), 
				"Test", 
				"Can you set up RSA with p = 6 and q = 8?", 
				"",
				true, true, true);
		sele.setType(SelectionObjectType.CLAN_SINGLE_TEST);
		sele.setPurpose(SelectionObjectPurpose.TEST);
		
		SelectionOption option = new SelectionOption(getUUID(), 
				"Yes", "Yes", "");
		option.setCorrect(false);
		sele.addOption(option);
		
		option = new SelectionOption(getUUID(), 
				"No, because p and q cannot both be even.", "No, because p and q cannot both be even.", "");
		option.setCorrect(false);
		sele.addOption(option);
		
		option = new SelectionOption(getUUID(), 
				"No, because q should be smaller than p.", "No, because q should be smaller than p.", "");
		option.setCorrect(false);
		sele.addOption(option);
		
		option = new SelectionOption(getUUID(), 
				"No, because p and q should both be prime numbers.", "No, because p and q should both be prime numbers.", "");
		option.setCorrect(true);
		sele.addOption(option);
				
		return sele;
	}

/*
4. Suppose we have RSA set up with e = 9 and n = 1189. What is the correct encryption of M = 19?

517
13
719
1113

 */
	protected static SelectionObject generateWeek3KnowledgeTest4() {
		SelectionObject sele = new SelectionObject(getUUID(), 
				"Test", 
				"Suppose we have RSA set up with e = 9 and n = 1189. What is the correct encryption of M = 19?", 
				"",
				true, true, true);
		sele.setType(SelectionObjectType.CLAN_SINGLE_TEST);
		sele.setPurpose(SelectionObjectPurpose.TEST);
		
		SelectionOption option = new SelectionOption(getUUID(), 
				"517", "517", "");
		option.setCorrect(false);
		sele.addOption(option);
		
		option = new SelectionOption(getUUID(), 
				"13", "13", "");
		option.setCorrect(false);
		sele.addOption(option);
		
		option = new SelectionOption(getUUID(), 
				"719", "719", "");
		option.setCorrect(false);
		sele.addOption(option);
		
		option = new SelectionOption(getUUID(), 
				"1113", "1113", "");
		option.setCorrect(true);
		sele.addOption(option);
				
		return sele;
	}

/*
5. In practice, how long should be an RSA key for decent security?

Slightly more than 1000 bits
Slightly more than 2000 bits
Slightly more than 3000 bits
Slightly more than 4000 bits


 */
	protected static SelectionObject generateWeek3KnowledgeTest5() {
		SelectionObject sele = new SelectionObject(getUUID(), 
				"Test", 
				"In practice, how long should be an RSA key for decent security?", 
				"",
				true, true, true);
		sele.setType(SelectionObjectType.CLAN_SINGLE_TEST);
		sele.setPurpose(SelectionObjectPurpose.TEST);
		
		SelectionOption option = new SelectionOption(getUUID(), 
				"Slightly more than 1000 bits", "Slightly more than 1000 bits", "");
		option.setCorrect(false);
		sele.addOption(option);
		
		option = new SelectionOption(getUUID(), 
				"Slightly more than 2000 bits", "Slightly more than 2000 bits", "");
		option.setCorrect(false);
		sele.addOption(option);
		
		option = new SelectionOption(getUUID(), 
				"Slightly more than 3000 bits", "Slightly more than 3000 bits", "");
		option.setCorrect(true);
		sele.addOption(option);
		
		option = new SelectionOption(getUUID(), 
				"Slightly more than 4000 bits", "Slightly more than 4000 bits", "");
		option.setCorrect(false);
		sele.addOption(option);
				
		return sele;
	}

/*
6. What is the theoretical reason for RSA’s security?

Padding with OAEP
Factoring is hard
Discrete log is hard
Euler’s generalisation of Fermat’s little theorem
 */
	protected static SelectionObject generateWeek3KnowledgeTest6() {
		SelectionObject sele = new SelectionObject(getUUID(), 
				"Test", 
				"What is the theoretical reason for RSA’s security?", 
				"",
				true, true, true);
		sele.setType(SelectionObjectType.CLAN_SINGLE_TEST);
		sele.setPurpose(SelectionObjectPurpose.TEST);
		
		SelectionOption option = new SelectionOption(getUUID(), 
				"Padding with OAEP", "Padding with OAEP", "");
		option.setCorrect(false);
		sele.addOption(option);
		
		option = new SelectionOption(getUUID(), 
				"Factoring is hard", "Factoring is hard", "");
		option.setCorrect(true);
		sele.addOption(option);
		
		option = new SelectionOption(getUUID(), 
				"Discrete log is hard", "Discrete log is hard", "");
		option.setCorrect(false);
		sele.addOption(option);
		
		option = new SelectionOption(getUUID(), 
				"Euler’s generalisation of Fermat’s little theorem", "Euler’s generalisation of Fermat’s little theorem", "");
		option.setCorrect(false);
		sele.addOption(option);
				
		return sele;
	}


/*
1. When is authenticity satisfied?

If the used key(s) is/are kept secret.
If no outsider learns the message.
If the message arrives as it was sent.
If the message was indeed sent by the apparent sender.
 */
	protected static SelectionObject generateWeek4KnowledgeTest1() {
		SelectionObject sele = new SelectionObject(getUUID(), 
				"Test", 
				"When is authenticity satisfied?", 
				"",
				true, true, true);
		sele.setType(SelectionObjectType.CLAN_SINGLE_TEST);
		sele.setPurpose(SelectionObjectPurpose.TEST);
		
		SelectionOption option = new SelectionOption(getUUID(), 
				"If the used key(s) is/are kept secret.", "If the used key(s) is/are kept secret.", "");
		option.setCorrect(false);
		sele.addOption(option);
		
		option = new SelectionOption(getUUID(), 
				"If no outsider learns the message.", "If no outsider learns the message.", "");
		option.setCorrect(false);
		sele.addOption(option);
		
		option = new SelectionOption(getUUID(), 
				"If the message arrives as it was sent.", "If the message arrives as it was sent.", "");
		option.setCorrect(false);
		sele.addOption(option);
		
		option = new SelectionOption(getUUID(), 
				"If the message was indeed sent by the apparent sender.", "If the message was indeed sent by the apparent sender.", "");
		option.setCorrect(true);
		sele.addOption(option);
				
		return sele;
	}

/*
2. How can you achieve confidentiality for messages sent over the Internet?

You need encryption.
You need encryption and hash functions.
You need encryption and digital signatures.
You need encryption, hash functions and digital signatures.
 */
	protected static SelectionObject generateWeek4KnowledgeTest2() {
		SelectionObject sele = new SelectionObject(getUUID(), 
				"Test", 
				"How can you achieve confidentiality for messages sent over the Internet?", 
				"",
				true, true, true);
		sele.setType(SelectionObjectType.CLAN_SINGLE_TEST);
		sele.setPurpose(SelectionObjectPurpose.TEST);
		
		SelectionOption option = new SelectionOption(getUUID(), 
				"You need encryption.", "You need encryption.", "");
		option.setCorrect(true);
		sele.addOption(option);
		
		option = new SelectionOption(getUUID(), 
				"You need encryption and hash functions.", "You need encryption and hash functions.", "");
		option.setCorrect(false);
		sele.addOption(option);
		
		option = new SelectionOption(getUUID(), 
				"You need encryption and digital signatures.", "You need encryption and digital signatures.", "");
		option.setCorrect(false);
		sele.addOption(option);
		
		option = new SelectionOption(getUUID(), 
				"You need encryption, hash functions and digital signatures.", "You need encryption, hash functions and digital signatures.", "");
		option.setCorrect(false);
		sele.addOption(option);
				
		return sele;
	}

	/*
3. Consider a function “length” that returns the length of the input string. Is this function suitable as a hash function?

Yes.
No, it violates pre-image resistance.
No, it violates collision resistance.
No, it violates both pre-image resistance and collision resistance.
	 */
	protected static SelectionObject generateWeek4KnowledgeTest3() {
		SelectionObject sele = new SelectionObject(getUUID(), 
				"Test", 
				"Consider a function “length” that returns the length of the input string. Is this function suitable as a hash function?", 
				"",
				true, true, true);
		sele.setType(SelectionObjectType.CLAN_SINGLE_TEST);
		sele.setPurpose(SelectionObjectPurpose.TEST);
		
		SelectionOption option = new SelectionOption(getUUID(), 
				"Yes.", "Yes.", "");
		option.setCorrect(false);
		sele.addOption(option);
		
		option = new SelectionOption(getUUID(), 
				"No, it violates pre-image resistance.", "No, it violates pre-image resistance.", "");
		option.setCorrect(false);
		sele.addOption(option);
		
		option = new SelectionOption(getUUID(), 
				"No, it violates collision resistance.", "No, it violates collision resistance.", "");
		option.setCorrect(true);
		sele.addOption(option);
		
		option = new SelectionOption(getUUID(), 
				"No, it violates both pre-image resistance and collision resistance.", "No, it violates both pre-image resistance and collision resistance.", "");
		option.setCorrect(false);
		sele.addOption(option);
				
		return sele;
	}

	/*
4. Suppose there is a blockchain, and an attacker makes a change to the 3-but-newest block. When would this be detected?

When a new block is created.
When someone verifies correctness of the new block (and only the new block).
When someone verifies correctness of the entire blockchain.
This is never discovered.

	 */
	protected static SelectionObject generateWeek4KnowledgeTest4() {
		SelectionObject sele = new SelectionObject(getUUID(), 
				"Test", 
				"Suppose there is a blockchain, and an attacker makes a change to the 3-but-newest block. When would this be detected?", 
				"",
				true, true, true);
		sele.setType(SelectionObjectType.CLAN_SINGLE_TEST);
		sele.setPurpose(SelectionObjectPurpose.TEST);
		
		SelectionOption option = new SelectionOption(getUUID(), 
				"When a new block is created.", "When a new block is created.", "");
		option.setCorrect(false);
		sele.addOption(option);
		
		option = new SelectionOption(getUUID(), 
				"When someone verifies correctness of the new block (and only the new block).", "When someone verifies correctness of the new block (and only the new block).", "");
		option.setCorrect(false);
		sele.addOption(option);
		
		option = new SelectionOption(getUUID(), 
				"When someone verifies correctness of the entire blockchain.", "When someone verifies correctness of the entire blockchain.", "");
		option.setCorrect(true);
		sele.addOption(option);
		
		option = new SelectionOption(getUUID(), 
				"This is never discovered.", "This is never discovered.", "");
		option.setCorrect(false);
		sele.addOption(option);
				
		return sele;
	}

	/*
5. Suppose Bob receives an encrypted message, Encpk(bob)(m), and the hash of the message, hash(m). It seems to have come from Alice. What are all security guarantees that Bob has?

Nothing, this may have been sent by an attacker
Confidentiality: no one but Alice and Bob know the message
Integrity: no one could have changed the message in transit
Authenticity: Alice is indeed the person who sent the message
	 */
	protected static SelectionObject generateWeek4KnowledgeTest5() {
		SelectionObject sele = new SelectionObject(getUUID(), 
				"Test", 
				"Suppose Bob receives an encrypted message, Encpk(bob)(m), and the hash of the message, hash(m). It seems to have come from Alice. What are all security guarantees that Bob has?", 
				"",
				true, true, true);
		sele.setType(SelectionObjectType.CLAN_SINGLE_TEST);
		sele.setPurpose(SelectionObjectPurpose.TEST);
		
		SelectionOption option = new SelectionOption(getUUID(), 
				"Nothing, this may have been sent by an attacker", "Nothing, this may have been sent by an attacker", "");
		option.setCorrect(true);
		sele.addOption(option);
		
		option = new SelectionOption(getUUID(), 
				"Confidentiality: no one but Alice and Bob know the message", "Confidentiality: no one but Alice and Bob know the message", "");
		option.setCorrect(false);
		sele.addOption(option);
		
		option = new SelectionOption(getUUID(), 
				"Integrity: no one could have changed the message in transit", "Integrity: no one could have changed the message in transit", "");
		option.setCorrect(false);
		sele.addOption(option);
		
		option = new SelectionOption(getUUID(), 
				"Authenticity: Alice is indeed the person who sent the message", "Authenticity: Alice is indeed the person who sent the message", "");
		option.setCorrect(false);
		sele.addOption(option);
				
		return sele;
	}

	/*
6. Suppose you visit a website “yahooogle.com” and it claims to be a company started by Google and Yahoo. What information in the certificate would make you trust the website’s security?

If the name in the certificate is “yahooogle.com”.
Answer a AND the certificate is valid.
Answer b AND your browser trusts the certificate.
Answer b and yahooogle signed the certificate.
	 */
	protected static SelectionObject generateWeek4KnowledgeTest6() {
		SelectionObject sele = new SelectionObject(getUUID(), 
				"Test", 
				"Suppose you visit a website “yahooogle.com” and it claims to be a company started by Google and Yahoo. What information in the certificate would make you trust the website’s security?", 
				"",
				true, true, true);
		sele.setType(SelectionObjectType.CLAN_SINGLE_TEST);
		sele.setPurpose(SelectionObjectPurpose.TEST);
		
		SelectionOption option = new SelectionOption(getUUID(), 
				"If the name in the certificate is “yahooogle.com”.", "If the name in the certificate is “yahooogle.com”.", "");
		option.setCorrect(false);
		sele.addOption(option);
		
		option = new SelectionOption(getUUID(), 
				"Answer a AND the certificate is valid.", "Answer a AND the certificate is valid.", "");
		option.setCorrect(false);
		sele.addOption(option);
		
		option = new SelectionOption(getUUID(), 
				"Answer b AND your browser trusts the certificate.", "Answer b AND your browser trusts the certificate.", "");
		option.setCorrect(true);
		sele.addOption(option);
		
		option = new SelectionOption(getUUID(), 
				"Answer b and yahooogle signed the certificate.", "Answer b and yahooogle signed the certificate.", "");
		option.setCorrect(false);
		sele.addOption(option);

		return sele;
	}

	/*
7. Which mixnets can straightforwardly be used for routing?
Neither.
Re-encryption mixnets
Decryption mixnets
Both.
	 */
	protected static SelectionObject generateWeek4KnowledgeTest7() {
		SelectionObject sele = new SelectionObject(getUUID(), 
				"Test", 
				"Which mixnets can straightforwardly be used for routing?", 
				"",
				true, true, true);
		sele.setType(SelectionObjectType.CLAN_SINGLE_TEST);
		sele.setPurpose(SelectionObjectPurpose.TEST);
		
		SelectionOption option = new SelectionOption(getUUID(), 
				"Neither.", "Neither.", "");
		option.setCorrect(false);
		sele.addOption(option);
		
		option = new SelectionOption(getUUID(), 
				"Re-encryption mixnets", "Re-encryption mixnets", "");
		option.setCorrect(false);
		sele.addOption(option);
		
		option = new SelectionOption(getUUID(), 
				"Decryption mixnets", "Decryption mixnets", "");
		option.setCorrect(true);
		sele.addOption(option);
		
		option = new SelectionOption(getUUID(), 
				"Both.", "Both.", "");
		option.setCorrect(false);
		sele.addOption(option);
				
		return sele;
	}

	/*
8. How does TOR preserve anonymity of your traffic?

Entry nodes don’t know to which site the sender is talking.
Exit nodes don’t know who is talking to the recipient site
Routing nodes don’t know who sender is, nor who recipient is.
All of the above.

	 */
	protected static SelectionObject generateWeek4KnowledgeTest8() {
		SelectionObject sele = new SelectionObject(getUUID(), 
				"Test", 
				"How does TOR preserve anonymity of your traffic?", 
				"",
				true, true, true);
		sele.setType(SelectionObjectType.CLAN_SINGLE_TEST);
		sele.setPurpose(SelectionObjectPurpose.TEST);
		
		SelectionOption option = new SelectionOption(getUUID(), 
				"Entry nodes don’t know to which site the sender is talking.", "Entry nodes don’t know to which site the sender is talking.", "");
		option.setCorrect(false);
		sele.addOption(option);
		
		option = new SelectionOption(getUUID(), 
				"Exit nodes don’t know who is talking to the recipient site", "Exit nodes don’t know who is talking to the recipient site", "");
		option.setCorrect(false);
		sele.addOption(option);
		
		option = new SelectionOption(getUUID(), 
				"Routing nodes don’t know who sender is, nor who recipient is.", "Routing nodes don’t know who sender is, nor who recipient is.", "");
		option.setCorrect(false);
		sele.addOption(option);
		
		option = new SelectionOption(getUUID(), 
				"All of the above.", "All of the above.", "");
		option.setCorrect(true);
		sele.addOption(option);
				
		return sele;
	}

	/*
9. Which kinds of cryptography (of those treated in the course) can quantum computers break?

All cryptography
Only cryptography that is not information-theoretically secure.
Only cryptography that is not computationally secure.
Only cryptography that is symbolically secure.

	 */
	protected static SelectionObject generateWeek4KnowledgeTest9() {
		SelectionObject sele = new SelectionObject(getUUID(), 
				"Test", 
				"Which kinds of cryptography (of those treated in the course) can quantum computers break?", 
				"",
				true, true, true);
		sele.setType(SelectionObjectType.CLAN_SINGLE_TEST);
		sele.setPurpose(SelectionObjectPurpose.TEST);
		
		SelectionOption option = new SelectionOption(getUUID(), 
				"All cryptography", "All cryptography", "");
		option.setCorrect(false);
		sele.addOption(option);
		
		option = new SelectionOption(getUUID(), 
				"Only cryptography that is not information-theoretically secure.", "Only cryptography that is not information-theoretically secure.", "");
		option.setCorrect(true);
		sele.addOption(option);
		
		option = new SelectionOption(getUUID(), 
				"Only cryptography that is not computationally secure.", "Only cryptography that is not computationally secure.", "");
		option.setCorrect(false);
		sele.addOption(option);
		
		option = new SelectionOption(getUUID(), 
				"Only cryptography that is symbolically secure.", "Only cryptography that is symbolically secure.", "");
		option.setCorrect(false);
		sele.addOption(option);
				
		return sele;
	}

	/*
10. Which of the below is true?

If a cryptosystem satisfies IND-CPA, it will satisfy IND-CCA.
If a cryptosystem satisfies IND-CCA, it will be information-theoretically secure.
The One Time Pad is computationally secure.
Computationally secure cryptosystems may be broken by advances in theory.

	 */
	protected static SelectionObject generateWeek4KnowledgeTest10() {
		SelectionObject sele = new SelectionObject(getUUID(), 
				"Test", 
				"Which of the below is true?", 
				"",
				true, true, true);
		sele.setType(SelectionObjectType.CLAN_SINGLE_TEST);
		sele.setPurpose(SelectionObjectPurpose.TEST);
		
		SelectionOption option = new SelectionOption(getUUID(), 
				"If a cryptosystem satisfies IND-CPA, it will satisfy IND-CCA.", "If a cryptosystem satisfies IND-CPA, it will satisfy IND-CCA.", "");
		option.setCorrect(false);
		sele.addOption(option);
		
		option = new SelectionOption(getUUID(), 
				"If a cryptosystem satisfies IND-CCA, it will be information-theoretically secure.", "If a cryptosystem satisfies IND-CCA, it will be information-theoretically secure.", "");
		option.setCorrect(false);
		sele.addOption(option);
		
		option = new SelectionOption(getUUID(), 
				"The One Time Pad is computationally secure.", "The One Time Pad is computationally secure.", "");
		option.setCorrect(false);
		sele.addOption(option);
		
		option = new SelectionOption(getUUID(), 
				"Computationally secure cryptosystems may be broken by advances in theory.", "Computationally secure cryptosystems may be broken by advances in theory.", "");
		option.setCorrect(true);
		sele.addOption(option);
				
		return sele;
	}

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
			boolean treatment = random.nextFloat() > 0.4; // (60% treatment, 40% control)
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
		HashArrayList<User> users = PersistentStore.readAllControlUsers();
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
				selectedOption = PersistentStore.readUserSelectionOption(status.getUser(), selectionObject, selectionOption);
				if (selectedOption != null) {
					selectedOptions.add(selectedOption);
				}						
			}
			for (UserOnlineStatus status: clan.getRecentUsers()) {
				selectedOption = PersistentStore.readUserSelectionOption(status.getUser(), selectionObject, selectionOption);
				if (selectedOption != null) {
					selectedOptions.add(selectedOption);
				}						
			}
			for (UserOnlineStatus status: clan.getOfflineUsers()) {
				selectedOption = PersistentStore.readUserSelectionOption(status.getUser(), selectionObject, selectionOption);
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
	public static ChatRoomList getChatRoomListForUser(User user) {
		ChatRoomList list = null;
		String clanId = null;
		if (user != null && user.isTreatmentGroup()) {
			clanId = user.getClan().getId();
			list = instance.chatRoomListMap.get(clanId);
			if (list == null) {
				log.info("create new chat room list for: "+clanId);
				list = new ChatRoomList(clanId);
				list.addRoom(new ChatRoom("I need help", "Ask your peers for help."));
				list.addRoom(new ChatRoom("I need a challenge", "Tackle challenges together"));
				list.addRoom(new ChatRoom("I need a teacher", "Ask your teacher for support"));
				list.addRoom(new ChatRoom("Just want to chat", "Open chat, but follow the rules"));
				list.addRoom(new ChatRoom("StartUp", "Startup chat room. Chatter is added to this after he logs in."));
				instance.chatRoomListMap.put(clanId, list);
			}
		} else {
			clanId = "control";
			list = instance.chatRoomListMap.get(clanId);
			if (list == null) {
				log.info("create new chat room list for: "+clanId);
				list = new ChatRoomList(clanId);
				list.addRoom(new ChatRoom("I need help", "Ask your peers for help."));
				list.addRoom(new ChatRoom("I need a teacher", "Ask your teacher for support"));
				list.addRoom(new ChatRoom("Just want to chat", "Open chat, but follow the rules"));
				list.addRoom(new ChatRoom("StartUp", "Startup chat room. Chatter is added to this after he logs in."));
				instance.chatRoomListMap.put(clanId, list);
			}
		}
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
			if (page != null) {
				if ("track".equals(logType) && userPlan.trackLearningProgress(page, activityType)) {
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
		
		//Enumeration<String> valnames = session.getAttributeNames();
		//if (valnames.hasMoreElements()) {
		//	for (String valname = valnames.nextElement(); valnames.hasMoreElements(); valname = valnames.nextElement() ) {
		//		log.info("session attribute: "+valname+", "+session.getAttribute(valname));
		//	}
		//}
		
		String loginData = "";
		
		String userNameR = request.getParameter("userName");
		String useridR = request.getParameter("userid");
		//if (useridR == null) {
		//	useridR = request.getParameter("userId");
		//}

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
