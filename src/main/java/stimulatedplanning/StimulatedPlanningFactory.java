package stimulatedplanning;

import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
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
	public static final String testCourseBaseURL = "https://localhost/courses/course-v1:DelftX+Sandbox_Welten+2018/";

	public static final String accCourseId = "TCC01";
	public static final String accCourseBaseURL = "https://ou.acc.edia.nl/courses/course-v1:OUNL+TCC01+2019_01/courseware/";
	
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

	/**
	 * generate the structure for the course to be used according to the acc staging platform test structure.
	 * @return
	 */
	public static CourseDescriptor generateAccTestCourse() {
		getOrGenerateClans();
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
		info = new InformationObject(getUUID(), 
				"Intro text", 
				"Intro text", 
				"");
		content.addInformationObject(info);

		sele = generateUserAvatarSelection();
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
				"");
		content.addInformationObject(info);
		info.setContent("<OL>"+
			"<LI>Do not use the chat or other tools provided in the course to harm or hurt others (such as the members of your clan and or/and the other clan).</LI>"+
			"<LI>Respect other opinion.</LI>"+
			"<LI>Contributions within the clan must be civil and tasteful.</LI>"+
			"<LI>No disruptive, offensive or abusive behaviour: contributions must be constructive and polite, not mean-spirited or contributed with the intention of causing trouble.</LI>"+
			"<LI>No spamming or off-topic material can be shared.</LI>"+
			"<LI>On a more Safety level: We advise that you never reveal any personal information about yourself or anyone else (for example: telephone number, home address or email address).</LI>"+
			"</OL>");

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
				"");
		content.addInformationObject(info);
		info.setContent("");
		
		sele = generateEncryptedMessageSelection();
		content.addInformationObject(sele);
		
		info = new InformationObject(getUUID(), 
				"Encrypt your message", 
				"Communicate with the opponents", 
				"");
		content.addInformationObject(info);
		info.setContent("");
		
		sele = generateEncryptionMethodSelection();
		content.addInformationObject(sele);
		
		info = new InformationObject(getUUID(), 
				"Encrypted Message received", 
				"How do you decrypt it?", 
				"");
		content.addInformationObject(info);
		info.setContent("");
		
		sele = generateDecryptionSelection();
		content.addInformationObject(sele);
		
		info = new InformationObject(getUUID(), 
				"Clan challenge results", 
				"How'd you do?", 
				"");
		content.addInformationObject(info);
		info.setContent("");
		
		
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
				"");
		content.addInformationObject(info);
		info.setContent("");
		
		sele = generateKnowledgeTest1();
		content.addInformationObject(sele);
		
		sele = generateKnowledgeTest2();
		content.addInformationObject(sele);
				
		info = new InformationObject(getUUID(), 
				"Your results", 
				"Your results", 
				"");
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
				testCourseBaseURL+"courseware/w4challenge/");
		content.addInformationObject(info);
		info.setContent("");


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

		InformationObject info = new InformationObject(getUUID(), 
				"Intro text", 
				"Intro text", 
				testCourseBaseURL+"courseware/w1challenge/");
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
				testCourseBaseURL+"courseware/w1challenge/");
		content.addInformationObject(info);
		info.setContent("<OL>"+
			"<LI>Do not use the chat or other tools provided in the course to harm or hurt others (such as the members of your clan and or/and the other clan).</LI>"+
			"<LI>Respect other opinion.</LI>"+
			"<LI>Contributions within the clan must be civil and tasteful.</LI>"+
			"<LI>No disruptive, offensive or abusive behaviour: contributions must be constructive and polite, not mean-spirited or contributed with the intention of causing trouble.</LI>"+
			"<LI>No spamming or off-topic material can be shared.</LI>"+
			"<LI>On a more Safety level: We advise that you never reveal any personal information about yourself or anyone else (for example: telephone number, home address or email address).</LI>"+
			"</OL>");

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
				testCourseBaseURL+"courseware/w2challenge/");
		content.addInformationObject(info);
		info.setContent("");
		
		SelectionObject sele = generateEncryptedMessageSelection();
		content.addInformationObject(sele);
		
		info = new InformationObject(getUUID(), 
				"Encrypt your message", 
				"Communicate with the opponents", 
				testCourseBaseURL+"courseware/w2challenge/");
		content.addInformationObject(info);
		info.setContent("");
		
		sele = generateEncryptionMethodSelection();
		content.addInformationObject(sele);
		
		info = new InformationObject(getUUID(), 
				"Encrypted Message received", 
				"How do you decrypt it?", 
				testCourseBaseURL+"courseware/w2challenge/");
		content.addInformationObject(info);
		info.setContent("");
		
		sele = generateDecryptionSelection();
		content.addInformationObject(sele);
		
		info = new InformationObject(getUUID(), 
				"Clan challenge results", 
				"How'd you do?", 
				testCourseBaseURL+"courseware/w2challenge/");
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
				testCourseBaseURL+"courseware/w3challenge/");
		content.addInformationObject(info);
		info.setContent("");
		
		SelectionObject sele = generateKnowledgeTest1();
		content.addInformationObject(sele);
		
		sele = generateKnowledgeTest2();
		content.addInformationObject(sele);
				
		info = new InformationObject(getUUID(), 
				"Your results", 
				"Your results", 
				testCourseBaseURL+"courseware/w3challenge/");
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
				testCourseBaseURL+"courseware/w4challenge/");
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
				"036-woman.png"
			};
		
		SelectionObject sele = new SelectionObject(getUUID(), 
				"Select your avatar", 
				"Select your avatar", 
				"");
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
				"Hello there! Please select one of the roles you want to cover in your clan:", 
				"Hello there! Please select one of the roles you want to cover in your clan:", 
				"");
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
				"001-hacker.png",
				"003-smartphone.png",
				"006-cctv.png",
				"009-email.png",
				"011-fingerprint-scan.png",
				"012-bitcoin.png",
				"017-hacker-1.png",
				"018-hacker-2.png",
				"032-server.png",
				"035-pendrive.png"
			};
		
		SelectionObject sele = new SelectionObject(getUUID(), 
				"Select your clan logo", 
				"Select your clan logo", 
				"");
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
				"Select your clan identity", 
				"Select your clan identity", 
				"");
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


	protected static SelectionObject generateEncryptedMessageSelection() {
		SelectionObject sele = new SelectionObject(getUUID(), 
				"Send an encrypted message!", 
				"decide your plaintext/ the message", 
				"");
		sele.setType(SelectionObjectType.CLAN_SELECTION);
		sele.setPurpose(SelectionObjectPurpose.CLAN_VOTING);
		
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
	
	protected static SelectionObject generateEncryptionMethodSelection() {
		SelectionObject sele = new SelectionObject(getUUID(), 
				"Send an encrypted message!", 
				"decide your encryption mechanism", 
				"");
		sele.setType(SelectionObjectType.CLAN_SELECTION);
		sele.setPurpose(SelectionObjectPurpose.CLAN_VOTING);
		
		SelectionOption option = new SelectionOption(getUUID(), 
				"Caesar moved by 5", 
				"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam nisl urna, tempus non congue eu, vulputate at nibh. Nullam maximus, ex ac aliquet eleifend, odio libero mollis est, ut vulputate.", 
				"");
		sele.addOption(option);
		
		option = new SelectionOption(getUUID(), 
				"Caesar moved by 12", 
				"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam nisl urna, tempus non congue eu, vulputate at nibh. Nullam maximus, ex ac aliquet eleifend, odio libero mollis est, ut vulputate.", 
				"");
		sele.addOption(option);
		
		option = new SelectionOption(getUUID(), 
				"Caesar moved by 17", 
				"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam nisl urna, tempus non congue eu, vulputate at nibh. Nullam maximus, ex ac aliquet eleifend, odio libero mollis est, ut vulputate.", 
				"");
		sele.addOption(option);
		
		option = new SelectionOption(getUUID(), 
				"Caesar moved by 22", 
				"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam nisl urna, tempus non congue eu, vulputate at nibh. Nullam maximus, ex ac aliquet eleifend, odio libero mollis est, ut vulputate.", 
				"");
		sele.addOption(option);
		
		return sele;
	}
	
	protected static SelectionObject generateDecryptionSelection() {
		SelectionObject sele = new SelectionObject(getUUID(), 
				"You received a secret message! It is encrypted? What does it mean?", 
				"Decide on the meaning of this message", 
				"");
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
	

	protected static SelectionObject generateKnowledgeTest1() {
		SelectionObject sele = new SelectionObject(getUUID(), 
				"Test", 
				"Which is correct?", 
				"");
		sele.setType(SelectionObjectType.CLAN_SINGLE_TEST);
		sele.setPurpose(SelectionObjectPurpose.TEST);
		
		SelectionOption option = new SelectionOption(getUUID(), 
				"Option a", 
				"Option a", 
				"");
		option.setCorrect(false);
		sele.addOption(option);
		
		option = new SelectionOption(getUUID(), 
				"Option b", 
				"Option b", 
				"");
		option.setCorrect(true);
		sele.addOption(option);
		
		option = new SelectionOption(getUUID(), 
				"Option c", 
				"Option c", 
				"");
		option.setCorrect(false);
		sele.addOption(option);

		option = new SelectionOption(getUUID(), 
				"Option d", 
				"Option d", 
				"");
		option.setCorrect(true);
		sele.addOption(option);
				
		return sele;
	}
	protected static SelectionObject generateKnowledgeTest2() {
		SelectionObject sele = new SelectionObject(getUUID(), 
				"Test", 
				"Which are also correct?", 
				"");
		sele.setType(SelectionObjectType.CLAN_MULTI_TEST);
		sele.setPurpose(SelectionObjectPurpose.TEST);
		
		SelectionOption option = new SelectionOption(getUUID(), 
				"Option e", 
				"Option e", 
				"");
		option.setCorrect(false);
		sele.addOption(option);
		
		option = new SelectionOption(getUUID(), 
				"Option f", 
				"Option f", 
				"");
		option.setCorrect(true);
		sele.addOption(option);
		
		option = new SelectionOption(getUUID(), 
				"Option g", 
				"Option g", 
				"");
		option.setCorrect(false);
		sele.addOption(option);
		
		option = new SelectionOption(getUUID(), 
				"Option h", 
				"Option h", 
				"");
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
