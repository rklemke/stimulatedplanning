package senseofcommunity;

import java.util.Date;
import java.util.ListIterator;

import stimulatedplanning.ContentDescriptor;
import stimulatedplanning.CourseDescriptor;
import stimulatedplanning.GoalDescriptor;
import stimulatedplanning.LessonDescriptor;
import stimulatedplanning.ModuleDescriptor;
import stimulatedplanning.StimulatedPlanningFactory;

public class SoC_CourseCreationFactory {

	public static Date week1deadline;
	public static Date week2deadline;
	public static Date week3deadline;
	public static Date week4deadline;
	public static final String accCourseId = "TCC01";
	public static final String accCourseBaseURL = "https://ou.acc.edia.nl/courses/course-v1:OUNL+TCC01+2019_01/courseware/";

	public SoC_CourseCreationFactory() {
		// TODO Auto-generated constructor stub
	}

	public static ModuleDescriptor generateWeek1(CourseDescriptor course) {
		ModuleDescriptor module = new ModuleDescriptor(StimulatedPlanningFactory.getUUID(), 
				"Week 1", 
				"Week 1 - Introduction", "");
		course.addModule(module);
		LessonDescriptor lesson = new LessonDescriptor(StimulatedPlanningFactory.getUUID() ,
				"module1. lesson 1",
				"module1. lesson 1","");
		module.addLesson(lesson);	
		ContentDescriptor content = new ContentDescriptor(StimulatedPlanningFactory.getUUID(), 
				"Week 1 content", 
				"Week 1 content", 
				StimulatedPlanningFactory.testCourseBaseURL+"courseware/w1content/"); //?activate_block_id=block-v1%3AOUNL%2BICS18%2B2018_1%2Btype%40sequential%2Bblock%4083d79097d5d94304a6fa9a5aed25dce3");
		lesson.addContent(content);
	
		content = new ContentDescriptor(StimulatedPlanningFactory.getUUID(), 
				"Week 1 challenge", 
				"Week 1 challenge", 
				StimulatedPlanningFactory.testCourseBaseURL+"courseware/w1challenge/"); //?activate_block_id=block-v1%3AOUNL%2BICS18%2B2018_1%2Btype%40sequential%2Bblock%4083d79097d5d94304a6fa9a5aed25dce3");
		lesson.addContent(content);
	
		InformationObject info = generateWeek1ControlIntroductionText();
		content.addInformationObject(info);
	
		info = generateWeek1TreatmentIntroductionText();
		content.addInformationObject(info);
	
		SelectionObject sele = generateWeek1UserAvatarSelection();
		content.addInformationObject(sele);
		
		sele = generateWeek1ClanLogoSelection();
		content.addInformationObject(sele);
		
		sele = generateWeek1ClanIdentitySelection();
		content.addInformationObject(sele);
		
		sele = generateWeek1UserIdentitySelection();
		content.addInformationObject(sele);
		
		info = new InformationObject(StimulatedPlanningFactory.getUUID(), 
				"Clan rules", 
				"Clan rules", 
				StimulatedPlanningFactory.testCourseBaseURL+"courseware/w1challenge/",
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
		
		info = generateWeek1TreatmentTestIntroduction();
		content.addInformationObject(info);
	
	
		// Goal for Module 1
		
		GoalDescriptor goal = new GoalDescriptor(StimulatedPlanningFactory.getUUID(), module.getTitle(), 
				"I intend to participate in the course activities to learn about "+module.getTitle(), "");
		ListIterator<LessonDescriptor> iterator = module.getLessons();
		while (iterator.hasNext()) {
			goal.addLesson(iterator.next());
		}
		course.addGoal(goal);
	
		
		return module;
	}

	public static ModuleDescriptor generateWeek2(CourseDescriptor course) {
		ModuleDescriptor module = new ModuleDescriptor(StimulatedPlanningFactory.getUUID(), 
				"Week2", 
				"Week 2 - Tips on how to protect your computer", "");
		course.addModule(module);
		
		LessonDescriptor lesson = new LessonDescriptor(StimulatedPlanningFactory.getUUID() ,
				"Threats and attacks",
				"Threats and attacks","");
		module.addLesson(lesson);
		
		ContentDescriptor content = new ContentDescriptor(StimulatedPlanningFactory.getUUID(), 
				"Week 2 content", 
				"Week 2 content", 
				StimulatedPlanningFactory.testCourseBaseURL+"courseware/w2content/"); //?activate_block_id=block-v1%3AOUNL%2BICS18%2B2018_1%2Btype%40sequential%2Bblock%400b957f040f954b6ab1f4e64b533ba65b");
		lesson.addContent(content);
		
		content = new ContentDescriptor(StimulatedPlanningFactory.getUUID(), 
				"Week 2 challenge", 
				"Week 2 challenge", 
				StimulatedPlanningFactory.testCourseBaseURL+"courseware/w2challenge/"); //?activate_block_id=block-v1%3AOUNL%2BICS18%2B2018_1%2Btype%40sequential%2Bblock%400b957f040f954b6ab1f4e64b533ba65b");
		lesson.addContent(content);
		
		InformationObject info = new InformationObject(StimulatedPlanningFactory.getUUID(), 
				"Communicate with the opponents", 
				"Communicate with the opponents", 
				StimulatedPlanningFactory.testCourseBaseURL+"courseware/w2challenge/",
				true, true, true);
		content.addInformationObject(info);
		info.setContent("");
		
		info = new InformationObject(StimulatedPlanningFactory.getUUID(), 
				"Encrypt your message", 
				"Communicate with the opponents", 
				StimulatedPlanningFactory.testCourseBaseURL+"courseware/w2challenge/",
				true, true, true);
		content.addInformationObject(info);
		info.setContent("");
		
		info = new InformationObject(StimulatedPlanningFactory.getUUID(), 
				"Encrypted Message received", 
				"How do you decrypt it?", 
				StimulatedPlanningFactory.testCourseBaseURL+"courseware/w2challenge/",
				true, true, true);
		content.addInformationObject(info);
		info.setContent("");
		
		SelectionObject sele = generateWeek2DecryptionSelection();
		content.addInformationObject(sele);
		
		info = new InformationObject(StimulatedPlanningFactory.getUUID(), 
				"Clan challenge results", 
				"How'd you do?", 
				StimulatedPlanningFactory.testCourseBaseURL+"courseware/w2challenge/",
				true, true, true);
		content.addInformationObject(info);
		info.setContent("");
		
		// Goal for module 2
		
		GoalDescriptor goal = new GoalDescriptor(StimulatedPlanningFactory.getUUID(), module.getTitle(), 
				"I intend to participate in the course activities to learn about "+module.getTitle(), "");
		ListIterator<LessonDescriptor> iterator = module.getLessons();
		while (iterator.hasNext()) {
			goal.addLesson(iterator.next());
		}
		course.addGoal(goal);
		
		return module;
	}

	public static ModuleDescriptor generateWeek3(CourseDescriptor course) {
		ModuleDescriptor module = new ModuleDescriptor(StimulatedPlanningFactory.getUUID(), 
				"Week 3", 
				"Week 3 - Tips on how to protect your computer", "");
		course.addModule(module);
		
		LessonDescriptor lesson = new LessonDescriptor(StimulatedPlanningFactory.getUUID() ,
				"Threats and attacks",
				"Threats and attacks","");
		module.addLesson(lesson);
		
		ContentDescriptor content = new ContentDescriptor(StimulatedPlanningFactory.getUUID(), 
				"Week 3 content", 
				"Week 3 content", 
				StimulatedPlanningFactory.testCourseBaseURL+"courseware/w3content/"); //?activate_block_id=block-v1%3AOUNL%2BICS18%2B2018_1%2Btype%40sequential%2Bblock%400b957f040f954b6ab1f4e64b533ba65b");
		lesson.addContent(content);
		
		content = new ContentDescriptor(StimulatedPlanningFactory.getUUID(), 
				"Week 3 challenge", 
				"Week 3 challenge", 
				StimulatedPlanningFactory.testCourseBaseURL+"courseware/w3challenge/"); //?activate_block_id=block-v1%3AOUNL%2BICS18%2B2018_1%2Btype%40sequential%2Bblock%400b957f040f954b6ab1f4e64b533ba65b");
		lesson.addContent(content);
	
		InformationObject info = generateWeek2TreatmentTestIntroduction();
		content.addInformationObject(info);
		info.setContent("");
		
		info = generateWeek2ControlTestIntroduction();
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
				
		info = new InformationObject(StimulatedPlanningFactory.getUUID(), 
				"Your results", 
				"Your results", 
				StimulatedPlanningFactory.testCourseBaseURL+"courseware/w3challenge/",
				true, true, true);
		content.addInformationObject(info);
		info.setContent("");
		
		// Goal for module 2
		
		GoalDescriptor goal = new GoalDescriptor(StimulatedPlanningFactory.getUUID(), module.getTitle(), 
				"I intend to participate in the course activities to learn about "+module.getTitle(), "");
		ListIterator<LessonDescriptor> iterator = module.getLessons();
		while (iterator.hasNext()) {
			goal.addLesson(iterator.next());
		}
		course.addGoal(goal);
		
		return module;
	}

	public static ModuleDescriptor generateWeek4(CourseDescriptor course) {
		ModuleDescriptor module = new ModuleDescriptor(StimulatedPlanningFactory.getUUID(), 
				"Week 4", 
				"Week 4 - Tips on how to protect your computer", "");
		course.addModule(module);
		
		LessonDescriptor lesson = new LessonDescriptor(StimulatedPlanningFactory.getUUID() ,
				"Threats and attacks",
				"Threats and attacks","");
		module.addLesson(lesson);
		
		ContentDescriptor content = new ContentDescriptor(StimulatedPlanningFactory.getUUID(), 
				"Week 4 content", 
				"Week 4 content", 
				StimulatedPlanningFactory.testCourseBaseURL+"courseware/w4content/"); //?activate_block_id=block-v1%3AOUNL%2BICS18%2B2018_1%2Btype%40sequential%2Bblock%400b957f040f954b6ab1f4e64b533ba65b");
		lesson.addContent(content);
		
		content = new ContentDescriptor(StimulatedPlanningFactory.getUUID(), 
				"Week 4 challenge", 
				"Week 4 challenge", 
				StimulatedPlanningFactory.testCourseBaseURL+"courseware/w4challenge/"); //?activate_block_id=block-v1%3AOUNL%2BICS18%2B2018_1%2Btype%40sequential%2Bblock%400b957f040f954b6ab1f4e64b533ba65b");
		lesson.addContent(content);
	
		InformationObject info = new InformationObject(StimulatedPlanningFactory.getUUID(), 
				"Discuss about cryptography", 
				"Discuss about cryptography", 
				StimulatedPlanningFactory.testCourseBaseURL+"courseware/w4challenge/",
				true, true, true);
		content.addInformationObject(info);
		info.setContent("");
		
		// Goal for module 2
		
		GoalDescriptor goal = new GoalDescriptor(StimulatedPlanningFactory.getUUID(), module.getTitle(), 
				"I intend to participate in the course activities to learn about "+module.getTitle(), "");
		ListIterator<LessonDescriptor> iterator = module.getLessons();
		while (iterator.hasNext()) {
			goal.addLesson(iterator.next());
		}
		course.addGoal(goal);
		
		return module;
	}

	public static ModuleDescriptor generateAccWeek1(CourseDescriptor course) {
		ModuleDescriptor module = null;
		LessonDescriptor lesson = null;
		ContentDescriptor content = null;
		InformationObject info = null;
		SelectionObject sele = null;
				
		module = new ModuleDescriptor(StimulatedPlanningFactory.getUUID(), 
				"General Introduction", 
				"General Introduction", "");
		course.addModule(module);
	
	
		// Content		
		lesson = new LessonDescriptor(StimulatedPlanningFactory.getUUID() ,
				"Welcome",
				"Welcome","");
		module.addLesson(lesson);	
	
		content = new ContentDescriptor("99afb1a25d4a4d4b87712f67fe313bde/50d96a7ac43243bba77b330f36e6c29b/", 
				"Welcome", 
				"Welcome", 
				SoC_CourseCreationFactory.accCourseBaseURL+"99afb1a25d4a4d4b87712f67fe313bde/50d96a7ac43243bba77b330f36e6c29b/");
		lesson.addContent(content);
	
		lesson = new LessonDescriptor(StimulatedPlanningFactory.getUUID() ,
				"The Origins",
				"The Origins","");
		module.addLesson(lesson);	
	
		content = new ContentDescriptor("99afb1a25d4a4d4b87712f67fe313bde/924936fcf16f4b319f2e7bdee9dd18f1/", 
				"History and terminology", 
				"History and terminology", 
				SoC_CourseCreationFactory.accCourseBaseURL+"99afb1a25d4a4d4b87712f67fe313bde/924936fcf16f4b319f2e7bdee9dd18f1/");
		lesson.addContent(content);
	
	
		// Assignment
		lesson = new LessonDescriptor(StimulatedPlanningFactory.getUUID() ,
				"Week 1 Assignment",
				"Week 1 Assignment","");
		module.addLesson(lesson);	
	
		content = new ContentDescriptor("99afb1a25d4a4d4b87712f67fe313bde/d51e8b0e73c147f0beca638a09c1b3da/", 
				"Week 1 Assignment", 
				"Week 1 Assignment", 
				SoC_CourseCreationFactory.accCourseBaseURL+"99afb1a25d4a4d4b87712f67fe313bde/d51e8b0e73c147f0beca638a09c1b3da/");
		lesson.addContent(content);
	
	
		// Assignment content
		info = generateWeek1ControlIntroductionText();
		content.addInformationObject(info);
	
		info = generateWeek1TreatmentIntroductionText();
		content.addInformationObject(info);
	
		sele = generateWeek1UserAvatarSelection();
		sele.setDeadline(week1deadline);
		content.addInformationObject(sele);
		
		sele = generateWeek1ClanLogoSelection();
		sele.setDeadline(week1deadline);
		content.addInformationObject(sele);
		
		sele = generateWeek1ClanIdentitySelection();
		sele.setDeadline(week1deadline);
		content.addInformationObject(sele);
		
		sele = generateWeek1UserIdentitySelection();
		sele.setDeadline(week1deadline);
		content.addInformationObject(sele);
		
		sele = generateWeek1ClanARulesSelection();
		sele.setDeadline(week1deadline);
		content.addInformationObject(sele);
		
		sele = generateWeek1ClanBRulesSelection();
		sele.setDeadline(week1deadline);
		content.addInformationObject(sele);
		
		info = generateWeek1TreatmentTestIntroduction();
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
		
		GoalDescriptor goal = new GoalDescriptor(StimulatedPlanningFactory.getUUID(), module.getTitle(), 
				"I intend to participate in the course activities to learn about "+module.getTitle(), "");
		ListIterator<LessonDescriptor> iterator = module.getLessons();
		while (iterator.hasNext()) {
			goal.addLesson(iterator.next());
		}
		course.addGoal(goal);
	
		
		return module;
	}

	public static ModuleDescriptor generateAccWeek2(CourseDescriptor course) {
		ModuleDescriptor module = null;
		LessonDescriptor lesson = null;
		ContentDescriptor content = null;
		InformationObject info = null;
		SelectionObject sele = null;
				
		module = new ModuleDescriptor(StimulatedPlanningFactory.getUUID(), 
				"Symmetric Encryption", 
				"Symmetric Encryption", "");
		course.addModule(module);
	
	
		// Content		
		lesson = new LessonDescriptor(StimulatedPlanningFactory.getUUID() ,
				"Caesar and Cryptanalysis",
				"Caesar and Cryptanalysis","");
		module.addLesson(lesson);	
	
		content = new ContentDescriptor("8e4e29d1f93b459bb40d11f93f77a85a/657cb604d580479796cd253af89a809b/", 
				"Caesar Cipher", 
				"Caesar Cipher", 
				SoC_CourseCreationFactory.accCourseBaseURL+"8e4e29d1f93b459bb40d11f93f77a85a/657cb604d580479796cd253af89a809b/");
		lesson.addContent(content);
	
		lesson = new LessonDescriptor(StimulatedPlanningFactory.getUUID() ,
				"Vigenère and cryptanalysis",
				"Vigenère and cryptanalysis","");
		module.addLesson(lesson);	
	
		content = new ContentDescriptor("8e4e29d1f93b459bb40d11f93f77a85a/13925f19c0644d6a80ed77c74b2c7505/", 
				"Vigenère Part 1 and 2", 
				"Vigenère Part 1 and 2", 
				SoC_CourseCreationFactory.accCourseBaseURL+"8e4e29d1f93b459bb40d11f93f77a85a/13925f19c0644d6a80ed77c74b2c7505/");
		lesson.addContent(content);
	
		lesson = new LessonDescriptor(StimulatedPlanningFactory.getUUID() ,
				"Playfair",
				"Playfair","");
		module.addLesson(lesson);	
	
		content = new ContentDescriptor("8e4e29d1f93b459bb40d11f93f77a85a/e8053ac8660340c4967a88b0381fe265/", 
				"Playfair", 
				"Playfair", 
				SoC_CourseCreationFactory.accCourseBaseURL+"8e4e29d1f93b459bb40d11f93f77a85a/e8053ac8660340c4967a88b0381fe265/");
		lesson.addContent(content);
	
		lesson = new LessonDescriptor(StimulatedPlanningFactory.getUUID() ,
				"Unbreakable encryption: one time pad",
				"Unbreakable encryption: one time pad","");
		module.addLesson(lesson);	
	
		content = new ContentDescriptor("8e4e29d1f93b459bb40d11f93f77a85a/51532bfb08fa40d8a111af0d0630717e/", 
				"Unbreakable Encryption: One Time Pad", 
				"Unbreakable Encryption: One Time Pad", 
				SoC_CourseCreationFactory.accCourseBaseURL+"8e4e29d1f93b459bb40d11f93f77a85a/51532bfb08fa40d8a111af0d0630717e/");
		lesson.addContent(content);
	
		lesson = new LessonDescriptor(StimulatedPlanningFactory.getUUID() ,
				"Modern Symmetric Cryptography",
				"Modern Symmetric Cryptography","");
		module.addLesson(lesson);	
	
		content = new ContentDescriptor("8e4e29d1f93b459bb40d11f93f77a85a/2a2dc9c5b4804017b6fb9b5c4a9d8f5b/", 
				"Modern Symmetric Cryptography", 
				"Modern Symmetric Cryptography", 
				SoC_CourseCreationFactory.accCourseBaseURL+"8e4e29d1f93b459bb40d11f93f77a85a/2a2dc9c5b4804017b6fb9b5c4a9d8f5b/");
		lesson.addContent(content);
	
	
		// Assignment
		lesson = new LessonDescriptor(StimulatedPlanningFactory.getUUID() ,
				"Week 2 Assignment",
				"Week 2 Assignment","");
		module.addLesson(lesson);	
	
		content = new ContentDescriptor("8e4e29d1f93b459bb40d11f93f77a85a/bfc34daba1ec4aa09dd4f88e00aa5cd0/", 
				"Week 2 Assignment", 
				"Week 2 Assignment", 
				SoC_CourseCreationFactory.accCourseBaseURL+"8e4e29d1f93b459bb40d11f93f77a85a/bfc34daba1ec4aa09dd4f88e00aa5cd0/");
		lesson.addContent(content);
	
	
		// Assignment content
		info = new InformationObject(StimulatedPlanningFactory.getUUID(), 
				"Communicate with the opponents", 
				"Communicate with the opponents", 
				"",
				false, true, true);
		content.addInformationObject(info);
		info.setContent("");
		
		sele = generateWeek2EncryptedMessageSelection();
		sele.setDeadline(week2deadline);
		content.addInformationObject(sele);
		
		info = new InformationObject(StimulatedPlanningFactory.getUUID(), 
				"Encrypt your message", 
				"Communicate with the opponents", 
				"",
				false, true, true);
		content.addInformationObject(info);
		info.setContent("");
		
		sele = generateWeek2EncryptionMethodSelectionClan1();
		sele.setDeadline(week2deadline);
		content.addInformationObject(sele);
		
		sele = generateWeek2EncryptionMethodSelectionClan2();
		sele.setDeadline(week2deadline);
		content.addInformationObject(sele);
		
		info = new InformationObject(StimulatedPlanningFactory.getUUID(), 
				"Encrypted Message received", 
				"How do you decrypt it?", 
				"",
				false, true, true);
		content.addInformationObject(info);
		info.setContent("");
		
		sele = generateWeek2DecryptionSelection();
		sele.setDeadline(week2deadline);
		content.addInformationObject(sele);
		
		info = new InformationObject(StimulatedPlanningFactory.getUUID(), 
				"Clan challenge results", 
				"How'd you do?", 
				"",
				false, true, true);
		content.addInformationObject(info);
		info.setContent("");
		
		info = generateWeek2TreatmentTestIntroduction();
		content.addInformationObject(info);
		info.setContent("");
		
		info = generateWeek2ControlTestIntroduction();
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
				
		info = generateWeek2TestConclusion();
		content.addInformationObject(info);
		info.setContent("");
	
		
		// Goal for Module 2
		
		GoalDescriptor goal = new GoalDescriptor(StimulatedPlanningFactory.getUUID(), module.getTitle(), 
				"I intend to participate in the course activities to learn about "+module.getTitle(), "");
		ListIterator<LessonDescriptor> iterator = module.getLessons();
		while (iterator.hasNext()) {
			goal.addLesson(iterator.next());
		}
		course.addGoal(goal);
	
		
		return module;
	}

	public static ModuleDescriptor generateAccWeek3(CourseDescriptor course) {
		ModuleDescriptor module = null;
		LessonDescriptor lesson = null;
		ContentDescriptor content = null;
		InformationObject info = null;
		SelectionObject sele = null;
				
		module = new ModuleDescriptor(StimulatedPlanningFactory.getUUID(), 
				"Asymmetric Encryption", 
				"Asymmetric Encryption", "");
		course.addModule(module);
	
	
		// Content		
		lesson = new LessonDescriptor(StimulatedPlanningFactory.getUUID() ,
				"Intro to week 3- Asymmetric Encryption",
				"Intro to week 3- Asymmetric Encryption","");
		module.addLesson(lesson);	
	
		content = new ContentDescriptor("085e5e7bbd294467a5dfc7adee0dc8e7/f075fc2c2e834471ac7c645fb2ff2ca2/", 
				"Intro to Week 3", 
				"Intro to Week 3", 
				SoC_CourseCreationFactory.accCourseBaseURL+"085e5e7bbd294467a5dfc7adee0dc8e7/f075fc2c2e834471ac7c645fb2ff2ca2/");
		lesson.addContent(content);
	
		lesson = new LessonDescriptor(StimulatedPlanningFactory.getUUID() ,
				"Clock Arithmetic",
				"Clock Arithmetic","");
		module.addLesson(lesson);	
	
		content = new ContentDescriptor("085e5e7bbd294467a5dfc7adee0dc8e7/8abacf327658455abd7f6312250a5bcb/", 
				"Counting with clocks", 
				"Counting with clocks", 
				SoC_CourseCreationFactory.accCourseBaseURL+"085e5e7bbd294467a5dfc7adee0dc8e7/8abacf327658455abd7f6312250a5bcb/");
		lesson.addContent(content);
	
		lesson = new LessonDescriptor(StimulatedPlanningFactory.getUUID() ,
				"RSA",
				"RSA","");
		module.addLesson(lesson);	
	
		content = new ContentDescriptor("085e5e7bbd294467a5dfc7adee0dc8e7/52dcb3e8bc3c4de1a3caa0873fa5e251/", 
				"How to use it and proof of correctness", 
				"How to use it and proof of correctness", 
				SoC_CourseCreationFactory.accCourseBaseURL+"085e5e7bbd294467a5dfc7adee0dc8e7/52dcb3e8bc3c4de1a3caa0873fa5e251/");
		lesson.addContent(content);
	
		lesson = new LessonDescriptor(StimulatedPlanningFactory.getUUID() ,
				"Diffie-Hellman",
				"Diffie-Hellman","");
		module.addLesson(lesson);	
	
		content = new ContentDescriptor("085e5e7bbd294467a5dfc7adee0dc8e7/4b198ca8530d488eadef85cf1af008f1/", 
				"(Exponential) arithmetic with letters and Diffie-Hellman key exchange", 
				"(Exponential) arithmetic with letters and Diffie-Hellman key exchange", 
				SoC_CourseCreationFactory.accCourseBaseURL+"085e5e7bbd294467a5dfc7adee0dc8e7/4b198ca8530d488eadef85cf1af008f1/");
		lesson.addContent(content);
	
		lesson = new LessonDescriptor(StimulatedPlanningFactory.getUUID() ,
				"ElGamal",
				"ElGamal","");
		module.addLesson(lesson);	
	
		content = new ContentDescriptor("085e5e7bbd294467a5dfc7adee0dc8e7/14df7edafc7e472a9fa455247159e227/", 
				"ElGamal", 
				"ElGamal", 
				SoC_CourseCreationFactory.accCourseBaseURL+"085e5e7bbd294467a5dfc7adee0dc8e7/14df7edafc7e472a9fa455247159e227/");
		lesson.addContent(content);
	
	
		// Assignment
		lesson = new LessonDescriptor(StimulatedPlanningFactory.getUUID() ,
				"Week 3 Assignment",
				"Week 3 Assignment","");
		module.addLesson(lesson);	
	
		content = new ContentDescriptor("085e5e7bbd294467a5dfc7adee0dc8e7/6778b583bc084d87824bf443a739f8e7/", 
				"Week 3 Assignment", 
				"Week 3 Assignment", 
				SoC_CourseCreationFactory.accCourseBaseURL+"085e5e7bbd294467a5dfc7adee0dc8e7/6778b583bc084d87824bf443a739f8e7/");
		lesson.addContent(content);
	
	
		// Assignment content
		info = generateWeek3TreatmentTestIntroduction();
		content.addInformationObject(info);
		info.setContent("");
		
		info = generateWeek3ControlTestIntroduction();
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
				
		info = generateWeek3TestConclusion();
		content.addInformationObject(info);
		info.setContent("");
	
		
		// Goal for Module 3
		
		GoalDescriptor goal = new GoalDescriptor(StimulatedPlanningFactory.getUUID(), module.getTitle(), 
				"I intend to participate in the course activities to learn about "+module.getTitle(), "");
		ListIterator<LessonDescriptor> iterator = module.getLessons();
		while (iterator.hasNext()) {
			goal.addLesson(iterator.next());
		}
		course.addGoal(goal);
	
		
		return module;
	}

	public static ModuleDescriptor generateAccWeek4(CourseDescriptor course) {
		ModuleDescriptor module = null;
		LessonDescriptor lesson = null;
		ContentDescriptor content = null;
		InformationObject info = null;
		SelectionObject sele = null;
				
		module = new ModuleDescriptor(StimulatedPlanningFactory.getUUID(), 
				"Using Cryptography", 
				"Using Cryptography", "");
		course.addModule(module);
	
	
		// Content		
		lesson = new LessonDescriptor(StimulatedPlanningFactory.getUUID() ,
				"Digital signatures",
				"Digital signatures","");
		module.addLesson(lesson);	
	
		content = new ContentDescriptor("f916b1af5d144670a980c5d5e0fee3a7/b2913720de144705ad1bd5a6e637cb9f/", 
				"Digital signatures", 
				"Digital signatures", 
				SoC_CourseCreationFactory.accCourseBaseURL+"f916b1af5d144670a980c5d5e0fee3a7/b2913720de144705ad1bd5a6e637cb9f/");
		lesson.addContent(content);
	
		lesson = new LessonDescriptor(StimulatedPlanningFactory.getUUID() ,
				"PKI and certificate chains",
				"PKI and certificate chains","");
		module.addLesson(lesson);	
	
		content = new ContentDescriptor("f916b1af5d144670a980c5d5e0fee3a7/aa28fa12231d4cea8923564f63aeafcf/", 
				"PKI and certificate chains", 
				"PKI and certificate chains", 
				SoC_CourseCreationFactory.accCourseBaseURL+"f916b1af5d144670a980c5d5e0fee3a7/aa28fa12231d4cea8923564f63aeafcf/");
		lesson.addContent(content);
	
		lesson = new LessonDescriptor(StimulatedPlanningFactory.getUUID() ,
				"Homomorphic cryptography",
				"Homomorphic cryptography","");
		module.addLesson(lesson);	
	
		content = new ContentDescriptor("f916b1af5d144670a980c5d5e0fee3a7/3b66d6481cd74e9e811cf6be9ed6a5de/", 
				"Homomorphic cryptography", 
				"Homomorphic cryptography", 
				SoC_CourseCreationFactory.accCourseBaseURL+"f916b1af5d144670a980c5d5e0fee3a7/3b66d6481cd74e9e811cf6be9ed6a5de/");
		lesson.addContent(content);
	
		lesson = new LessonDescriptor(StimulatedPlanningFactory.getUUID() ,
				"mixnets",
				"mixnets","");
		module.addLesson(lesson);	
	
		content = new ContentDescriptor("f916b1af5d144670a980c5d5e0fee3a7/ebfa676671b94b518e1d525adfe4ddd0/", 
				"mixnets", 
				"mixnets", 
				SoC_CourseCreationFactory.accCourseBaseURL+"f916b1af5d144670a980c5d5e0fee3a7/ebfa676671b94b518e1d525adfe4ddd0/");
		lesson.addContent(content);
	
		lesson = new LessonDescriptor(StimulatedPlanningFactory.getUUID() ,
				"hash chains and merkle trees",
				"hash chains and merkle trees","");
		module.addLesson(lesson);	
	
		content = new ContentDescriptor("f916b1af5d144670a980c5d5e0fee3a7/f903c3afa93f431cb5db9016c44712d7/", 
				"hash chains and merkle trees", 
				"hash chains and merkle trees", 
				SoC_CourseCreationFactory.accCourseBaseURL+"f916b1af5d144670a980c5d5e0fee3a7/f903c3afa93f431cb5db9016c44712d7/");
		lesson.addContent(content);
	
	
		// Assignment
		lesson = new LessonDescriptor(StimulatedPlanningFactory.getUUID() ,
				"Week 4 Assignment",
				"Week 4 Assignment","");
		module.addLesson(lesson);	
	
		content = new ContentDescriptor("f916b1af5d144670a980c5d5e0fee3a7/f7e5d9bef8304d09bf28e5713b680466/", 
				"Week 4 Assignment", 
				"Week 4 Assignment", 
				SoC_CourseCreationFactory.accCourseBaseURL+"f916b1af5d144670a980c5d5e0fee3a7/f7e5d9bef8304d09bf28e5713b680466/");
		lesson.addContent(content);
	
	
		// Assignment content
		info = new InformationObject(StimulatedPlanningFactory.getUUID(), 
				"Discuss about cryptography", 
				"Discuss about cryptography", 
				StimulatedPlanningFactory.testCourseBaseURL+"courseware/w4challenge/",
				true, true, true);
		content.addInformationObject(info);
		info.setContent("");
	
		info = generateWeek4TreatmentTestIntroduction();
		content.addInformationObject(info);
		info.setContent("");
		
		info = generateWeek4ControlTestIntroduction();
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
				
		info = generateWeek4TestConclusion();
		content.addInformationObject(info);
		info.setContent("");
	
		// Goal for Module 1
		
		GoalDescriptor goal = new GoalDescriptor(StimulatedPlanningFactory.getUUID(), module.getTitle(), 
				"I intend to participate in the course activities to learn about "+module.getTitle(), "");
		ListIterator<LessonDescriptor> iterator = module.getLessons();
		while (iterator.hasNext()) {
			goal.addLesson(iterator.next());
		}
		course.addGoal(goal);
	
		
		return module;
	}

	public static InformationObject generateWeek1ControlIntroductionText() {
		InformationObject info = new InformationObject(StimulatedPlanningFactory.getUUID(), 
				"<span style='font-size:2em;'>Test your knowledge!</span><br>", 
				"<br>", 
				"",
				true, false, false);
	
		info.setContent("<span style='font-size:1.5em;'>Welcome 😊 to the first week assignment, click on next 👉🏽 to take the test. </span><BR>" + 
				"<BR>" + 
				"<span style='font-size:1.5em;'>Thank you in advance!</span><BR>" + 
				"<BR>" + 
				"The OUNL Team<BR>" + 
				"(Alessandra, Hugo and Roland)<BR>" + 
				"");
		
		return info;
	}

	//Defender Welcome.
	protected static InformationObject generateWeek1TreatmentIntroductionText() {
		InformationObject info = new InformationObject(StimulatedPlanningFactory.getUUID(), 
				"<span style='font-size:2em;'>Welcome to your clan!</span><br>", 
				"<br>", 
				"",
				false, true, true);
	
		info.setContent("<span style='font-size:1.5em;'>You have been selected among the participants of this course to join our clan 😊 </span><BR>" + 
				"<span style='font-size:1.5em;'>You will decide to which degree you would like to be involved.</span><BR>" + 
				"<BR>" + 
				"<span style='font-size:1.5em;'>At the moment we do not have a name and neither a logo that define who we are. It is our task this week to agree on these and define as well as our rules.</span><BR>" + 
				"<BR>" + 
				"<span style='font-size:1.5em;'>Be aware that there will be another clan, they will act against us, we need to act together for defeating them. </span><BR>" + 
				"<BR>" + 
				"<span style='font-size:1.5em;'>Are you ready? Let’s do this 💪🏽 </span><BR>" + 
				"<BR>" + 
				"The OUNL Team<BR>" + 
				"(Alessandra, Hugo and Roland)<BR>" + 
				"");
		
		return info;
	}

	public static SelectionObject generateWeek1UserAvatarSelection() {
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
		
		SelectionObject sele = new SelectionObject(StimulatedPlanningFactory.getUUID(), 
				"Your choice", 
				"Select your avatar", 
				"",
				false, true, true);
		sele.setType(SelectionObjectType.SINGLE_USER_SELECTION);
		sele.setPurpose(SelectionObjectPurpose.USER_AVATAR);
		
		SelectionOption option = null;
		for (String url : userIconFiles) {
			option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
					url, 
					url, 
					"/img/profile/"+url);
			sele.addOption(option);
		}
		
		return sele;
	}

	public static SelectionObject generateWeek1UserIdentitySelection() {
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
		
		SelectionObject sele = new SelectionObject(StimulatedPlanningFactory.getUUID(), 
				"Your choice", 
				"Which role would you like to play in our clan?", 
				"",
				false, true, true);
		sele.setType(SelectionObjectType.SINGLE_USER_SELECTION);
		sele.setPurpose(SelectionObjectPurpose.USER_IDENTITY);
		
		SelectionOption option = null;
		for (int i=0; i<userTitles.length; i++) {
			option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
					userTitles[i], 
					userDescriptions[i], 
					"");
			sele.addOption(option);
		}
		
		return sele;
	}

	protected static SelectionObject generateWeek1ClanLogoSelection() {
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
		
		SelectionObject sele = new SelectionObject(StimulatedPlanningFactory.getUUID(), 
				"Clan vote", 
				"Vote for your preferred clan logos", 
				"",
				false, true, true);
		sele.setType(SelectionObjectType.CLAN_SELECTION);
		sele.setPurpose(SelectionObjectPurpose.CLAN_AVATAR);
		
		SelectionOption option = null;
		for (String url : clanLogoFiles) {
			option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
					url, 
					url, 
					"/img/clan/"+url);
			sele.addOption(option);
		}
		
		return sele;
	}

	protected static SelectionObject generateWeek1ClanIdentitySelection() {
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
		
		SelectionObject sele = new SelectionObject(StimulatedPlanningFactory.getUUID(), 
				"Clan vote", 
				"Vote for your preferred clan names", 
				"",
				false, true, true);
		sele.setType(SelectionObjectType.CLAN_SELECTION);
		sele.setPurpose(SelectionObjectPurpose.CLAN_IDENTITY);
		
		SelectionOption option = null;
		for (int i=0; i<clanTitles.length; i++) {
			option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
					clanTitles[i], 
					clanDescriptions[i], 
					"");
			sele.addOption(option);
		}
		
		return sele;
	}

	public static SelectionObject generateWeek1ClanARulesSelection() {
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
		
		SelectionObject sele = new SelectionObject(StimulatedPlanningFactory.getUUID(), 
				"Clan vote", 
				"Vote for your preferred clan rules", 
				"",
				false, true, false);
		sele.setType(SelectionObjectType.CLAN_SELECTION);
		sele.setPurpose(SelectionObjectPurpose.CLAN_VOTING);
		
		SelectionOption option = null;
		for (int i=0; i<clanTitles.length; i++) {
			option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
					clanTitles[i], 
					clanDescriptions[i], 
					"");
			sele.addOption(option);
		}
		
		return sele;
	}

	public static SelectionObject generateWeek1ClanBRulesSelection() {
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
		
		SelectionObject sele = new SelectionObject(StimulatedPlanningFactory.getUUID(), 
				"Clan vote", 
				"Vote for your preferred clan rules", 
				"",
				false, false, true);
		sele.setType(SelectionObjectType.CLAN_SELECTION);
		sele.setPurpose(SelectionObjectPurpose.CLAN_VOTING);
		
		SelectionOption option = null;
		for (int i=0; i<clanTitles.length; i++) {
			option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
					clanTitles[i], 
					clanDescriptions[i], 
					"");
			sele.addOption(option);
		}
		
		return sele;
	}

	public static InformationObject generateWeek1TreatmentTestIntroduction() {
		InformationObject info = new InformationObject(StimulatedPlanningFactory.getUUID(), 
				"<span style='font-size:2em;'>Let’s now challenge your ability to work together!</span> <BR> ", 
				"<BR><span style='font-size:1.5em;'>Your mission is to solve the following knowledge test!</span><BR>", 
				"",
				false, true, true);
	
		info.setContent("<span style='font-size:1.5em;'><strong>Remember</strong> it is a group effort: only the most voted answer will count, be sure you all select the correct one!</span><BR>" + 
				"<BR>" + 
				"<span style='font-size:1.5em;'>Work well on this simple test because we are going to level up in the next one</span><BR>" + 
				"<BR>"  +
				"The OUNL team<BR>" + 
				"");
		
		return info;
	}

	public static InformationObject generateWeek1TestConclusion() {
		InformationObject info = new InformationObject(StimulatedPlanningFactory.getUUID(), 
				"<span style='font-size:2em;'>We have saved your answers!</span><BR>", 
				"<br>", 
				"",
				true, true, true);
	
		info.setContent("<span style='font-size:1.5em;'>Are you curious to know how did you perform?</span><BR> "
				+ "<br><span style='font-size:1.5em;'>If yes check out the “Test's Results week-1” page that will be visible from next Monday morning.</span><BR> "
				+ "<BR>"
				+ "The OUNL team\n" + 
				"");
		
		return info;
	}

	public static SelectionObject generateWeek1KnowledgeTest1() {
		SelectionObject sele = new SelectionObject(StimulatedPlanningFactory.getUUID(), 
				"Test", 
				"What does cryptography mean?", 
				"",
				true, true, true);
		sele.setType(SelectionObjectType.CLAN_SINGLE_TEST);
		sele.setPurpose(SelectionObjectPurpose.TEST);
		
		SelectionOption option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
				"Hidden words", 
				"Hidden words", 
				"");
		option.setCorrect(false);
		sele.addOption(option);
		
		option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
				"Hidden writing", 
				"Hidden writing", 
				"");
		option.setCorrect(true);
		sele.addOption(option);
		
		option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
				"Hidden secret", 
				"Hidden secret", 
				"");
		option.setCorrect(false);
		sele.addOption(option);
	
		option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
				"Hidden letters", 
				"Hidden letters", 
				"");
		option.setCorrect(false);
		sele.addOption(option);
				
		return sele;
	}

	public static SelectionObject generateWeek1KnowledgeTest2() {
		SelectionObject sele = new SelectionObject(StimulatedPlanningFactory.getUUID(), 
				"Test", 
				"Cryptography dates back to?", 
				"",
				true, true, true);
		sele.setType(SelectionObjectType.CLAN_SINGLE_TEST);
		sele.setPurpose(SelectionObjectPurpose.TEST);
		
		SelectionOption option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
				"The Egyptian (Prior to 3100 BC)", 
				"The Egyptian (Prior to 3100 BC)", 
				"");
		option.setCorrect(false);
		sele.addOption(option);
		
		option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
				"Creation of the Internet  (1960)", 
				"Creation of the Internet  (1960)", 
				"");
		option.setCorrect(false);
		sele.addOption(option);
		
		option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
				"The Greek (600 BC)", 
				"The Greek (600 BC)", 
				"");
		option.setCorrect(true);
		sele.addOption(option);
		
		option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
				"The Roman (27 BC)", 
				"The Roman (27 BC)", 
				"");
		option.setCorrect(false);
		sele.addOption(option);
				
		return sele;
	}

	public static SelectionObject generateWeek1KnowledgeTest3() {
		SelectionObject sele = new SelectionObject(StimulatedPlanningFactory.getUUID(), 
				"Test", 
				"Which of the following statements  is correct?", 
				"",
				true, true, true);
		sele.setType(SelectionObjectType.CLAN_MULTI_TEST);
		sele.setPurpose(SelectionObjectPurpose.TEST);
		
		SelectionOption option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
				"The plaintext is the input for encryption.", 
				"The plaintext is the input for encryption.", 
				"");
		option.setCorrect(true);
		sele.addOption(option);
		
		option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
				"The ciphertext is the input for encryption.", 
				"The ciphertext is the input for encryption.", 
				"");
		option.setCorrect(false);
		sele.addOption(option);
		
		option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
				"Encrypting results in a key.", 
				"Encrypting results in a key.", 
				"");
		option.setCorrect(false);
		sele.addOption(option);
				
		option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
				"You cannot decrypt a ciphertext with the same key it was encrypted with.", 
				"You cannot decrypt a ciphertext with the same key it was encrypted with.", 
				"");
		option.setCorrect(false);
		sele.addOption(option);
		
		return sele;
	}

	public static SelectionObject generateWeek1KnowledgeTest4() {
		SelectionObject sele = new SelectionObject(StimulatedPlanningFactory.getUUID(), 
				"Test", 
				"What is a cipher?", 
				"",
				true, true, true);
		sele.setType(SelectionObjectType.CLAN_SINGLE_TEST);
		sele.setPurpose(SelectionObjectPurpose.TEST);
		
		SelectionOption option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
				"the (unreadable) message of the sender", 
				"the (unreadable) message of the sender", 
				"");
		option.setCorrect(false);
		sele.addOption(option);
		
		option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
				"the (unreadable) message once decrypted", 
				"the (unreadable) message once decrypted", 
				"");
		option.setCorrect(true);
		sele.addOption(option);
		
		option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
				"the (unreadable) message of the cipher", 
				"the (unreadable) message of the cipher", 
				"");
		option.setCorrect(false);
		sele.addOption(option);
		
		option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
				"the (unreadable) message once encrypted", 
				"the (unreadable) message once encrypted", 
				"");
		option.setCorrect(true);
		sele.addOption(option);
				
		return sele;
	}

	public static SelectionObject generateWeek1KnowledgeTest5() {
		SelectionObject sele = new SelectionObject(StimulatedPlanningFactory.getUUID(), 
				"Test", 
				"Which of the below describes a cipher?", 
				"",
				true, true, true);
		sele.setType(SelectionObjectType.CLAN_SINGLE_TEST);
		sele.setPurpose(SelectionObjectPurpose.TEST);
		
		SelectionOption option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
				"The pictorial instructions on a washing label", 
				"The pictorial instructions on a washing label", 
				"");
		option.setCorrect(false);
		sele.addOption(option);
		
		option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
				"The NATO phonetic alphabet (Alpha, Bravo, Charlie, Delta, Echo, …)", 
				"The NATO phonetic alphabet (Alpha, Bravo, Charlie, Delta, Echo, …)", 
				"");
		option.setCorrect(true);
		sele.addOption(option);
		
		option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
				"Barcodes on supermarket items", 
				"Barcodes on supermarket items", 
				"");
		option.setCorrect(false);
		sele.addOption(option);
		
		option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
				"Descriptions in a crossword puzzle", 
				"Descriptions in a crossword puzzle", 
				"");
		option.setCorrect(false);
		sele.addOption(option);
				
		return sele;
	}

	protected static SelectionObject generateWeek1KnowledgeTest6() {
		SelectionObject sele = new SelectionObject(StimulatedPlanningFactory.getUUID(), 
				"Test", 
				"What is Kerckhoffs’ principle?", 
				"",
				true, true, true);
		sele.setType(SelectionObjectType.CLAN_SINGLE_TEST);
		sele.setPurpose(SelectionObjectPurpose.TEST);
		
		SelectionOption option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
				"Something in French", 
				"Something in French", 
				"");
		option.setCorrect(false);
		sele.addOption(option);
		
		option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
				"That you must keep the system secret from the adversary", 
				"That you must keep the system secret from the adversary", 
				"");
		option.setCorrect(false);
		sele.addOption(option);
		
		option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
				"That the key should be kept secret", 
				"That the key should be kept secret", 
				"");
		option.setCorrect(false);
		sele.addOption(option);
		
		option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
				"That the system is secure if the enemy can’t decrypt without the key", 
				"That the system is secure if the enemy can’t decrypt without the key", 
				"");
		option.setCorrect(true);
		sele.addOption(option);
				
		return sele;
	}

	protected static SelectionObject generateWeek2EncryptedMessageSelection() {
		SelectionObject sele = new SelectionObject(StimulatedPlanningFactory.getUUID(), 
				"Send an encrypted message!", 
				"decide your plaintext/ the message", 
				"",
				false, true, true);
		sele.setType(SelectionObjectType.CLAN_SELECTION);
		sele.setPurpose(SelectionObjectPurpose.CLAN_MESSAGE);
		
		SelectionOption option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
				"Make them aware ...", 
				"“One of the most singular characteristics of the art of deciphering is the strong conviction possessed by every person, even moderately acquainted with it, that he is able to construct a cipher which nobody else can decipher. I have also observed that the cleverer the person, the more intimate is his conviction.”" + 
				"― Charles Babbage", 
				"");
		sele.addOption(option);
		
		option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
				"Whisper...", 
				"In this age of communications that span both distance and time, the only tool we have that approximates a 'whisper' is encryption. When I cannot whisper in my wife's ear or the ears of my business partners, and have to communicate electronically, then encryption is our tool to keep our secrets secret."
				+ "― John McAfee", 
				"");
		sele.addOption(option);
		
		option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
				"Give them a good advice...", 
				"“Cybercriminals can use personal details, such as your favorite color, the last four digits of your credit card and your email addresses, to make educated guesses about your sign-in credentials. They might contact a service provider posing as a user, provide identifying details and gain even greater access to accounts. This is why it is so important to prevent your security credentials from becoming stagnant. Rotate your password every few months and consider using new security questions and answers, too.”"
				+ "– Larry Alton", 
				"");
		sele.addOption(option);
		
		option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
				"Tease them...", 
				"“All warfare is based on deception. Hence, when we are able to attack, we must seem unable; when using our forces, we must appear inactive; when we are near, we must make the enemy believe we are far away; when far away, we must make him believe we are near.”" + 
				"― Sun tzu, The Art of War", 
				"");
		sele.addOption(option);
		
		return sele;
	}

	public static SelectionObject generateWeek2EncryptionMethodSelectionClan1() {
		SelectionObject sele = new SelectionObject("encryptionMethod-"+Clan.CLAN_1_ID, 
				"Send an encrypted message!", 
				"decide your encryption mechanism", 
				"",
				false, true, false);
		sele.setType(SelectionObjectType.CLAN_SELECTION);
		sele.setPurpose(SelectionObjectPurpose.CLAN_VOTING);
		
		SelectionOption option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
				"Caesar shift of 5", 
				"tsj tk ymj rtxy xnslzqfw hmfwfhyjwnxynhx tk ymj fwy tk ijhnumjwnsl nx ymj xywtsl htsanhynts utxxjxxji gd jajwd ujwxts, jajs rtijwfyjqd fhvzfnsyji bnym ny, ymfy mj nx fgqj yt htsxywzhy f hnumjw bmnhm stgtid jqxj hfs ijhnumjw. n mfaj fqxt tgxjwaji ymfy ymj hqjajwjw ymj ujwxts, ymj rtwj nsynrfyj nx mnx htsanhynts ― hmfwqjx gfggflj", 
				"");
		sele.addOption(option);
		
		option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
				"Caesar shift of 12", 
				"azq ar ftq yaef euzsgxmd otmdmofqduefuoe ar ftq mdf ar pqoubtqduzs ue ftq efdazs oazhuofuaz baeeqeeqp nk qhqdk bqdeaz, qhqz yapqdmfqxk mocgmuzfqp iuft uf, ftmf tq ue mnxq fa oazefdgof m oubtqd ituot zanapk qxeq omz pqoubtqd. u tmhq mxea aneqdhqp ftmf ftq oxqhqdqd ftq bqdeaz, ftq yadq uzfuymfq ue tue oazhuofuaz \n" + 
				"― otmdxqe nmnnmsq", 
				"");
		sele.addOption(option);
		
		option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
				"Caesar shift of 17", 
				"fev fw kyv dfjk jzexlcri tyrirtkvizjkztj fw kyv rik fw uvtzgyvizex zj kyv jkifex tfemztkzfe gfjjvjjvu sp vmvip gvijfe, vmve dfuvirkvcp rthlrzekvu nzky zk, kyrk yv zj rscv kf tfejkiltk r tzgyvi nyzty efsfup vcjv tre uvtzgyvi. z yrmv rcjf fsjvimvu kyrk kyv tcvmvivi kyv gvijfe, kyv dfiv zekzdrkv zj yzj tfemztkzfe \n" + 
				"― tyricvj srssrxv", 
				"");
		sele.addOption(option);
		
		option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
				"Caesar shift of 22", 
				"kja kb pda ikop oejcqhwn ydwnwypaneopeyo kb pda wnp kb zayeldanejc eo pda opnkjc ykjreypekj lkooaooaz xu aranu lanokj, araj ikzanwpahu wymqwejpaz sepd ep, pdwp da eo wxha pk ykjopnqyp w yeldan sdeyd jkxkzu ahoa ywj zayeldan. e dwra whok kxoanraz pdwp pda yharanan pda lanokj, pda ikna ejpeiwpa eo deo ykjreypekj \n" + 
				"― ydwnhao xwxxwca", 
				"");
		sele.addOption(option);
		
		return sele;
	}

	public static SelectionObject generateWeek2EncryptionMethodSelectionClan2() {
		SelectionObject sele = new SelectionObject("encryptionMethod-"+Clan.CLAN_2_ID, 
				"Send an encrypted message!", 
				"decide your encryption mechanism", 
				"",
				false, false, true);
		sele.setType(SelectionObjectType.CLAN_SELECTION);
		sele.setPurpose(SelectionObjectPurpose.CLAN_VOTING);
		
		SelectionOption option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
				"Caesar shift of 5", 
				"tsj tk ymj rtxy xnslzqfw hmfwfhyjwnxynhx tk ymj fwy tk ijhnumjwnsl nx ymj xywtsl htsanhynts utxxjxxji gd jajwd ujwxts, jajs rtijwfyjqd fhvzfnsyji bnym ny, ymfy mj nx fgqj yt htsxywzhy f hnumjw bmnhm stgtid jqxj hfs ijhnumjw. n mfaj fqxt tgxjwaji ymfy ymj hqjajwjw ymj ujwxts, ymj rtwj nsynrfyj nx mnx htsanhynts ― hmfwqjx gfggflj", 
				"");
		sele.addOption(option);
		
		option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
				"Caesar shift of 12", 
				"azq ar ftq yaef euzsgxmd otmdmofqduefuoe ar ftq mdf ar pqoubtqduzs ue ftq efdazs oazhuofuaz baeeqeeqp nk qhqdk bqdeaz, qhqz yapqdmfqxk mocgmuzfqp iuft uf, ftmf tq ue mnxq fa oazefdgof m oubtqd ituot zanapk qxeq omz pqoubtqd. u tmhq mxea aneqdhqp ftmf ftq oxqhqdqd ftq bqdeaz, ftq yadq uzfuymfq ue tue oazhuofuaz \n" + 
				"― otmdxqe nmnnmsq", 
				"");
		sele.addOption(option);
		
		option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
				"Caesar shift of 17", 
				"fev fw kyv dfjk jzexlcri tyrirtkvizjkztj fw kyv rik fw uvtzgyvizex zj kyv jkifex tfemztkzfe gfjjvjjvu sp vmvip gvijfe, vmve dfuvirkvcp rthlrzekvu nzky zk, kyrk yv zj rscv kf tfejkiltk r tzgyvi nyzty efsfup vcjv tre uvtzgyvi. z yrmv rcjf fsjvimvu kyrk kyv tcvmvivi kyv gvijfe, kyv dfiv zekzdrkv zj yzj tfemztkzfe \n" + 
				"― tyricvj srssrxv", 
				"");
		sele.addOption(option);
		
		option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
				"Caesar shift of 22", 
				"kja kb pda ikop oejcqhwn ydwnwypaneopeyo kb pda wnp kb zayeldanejc eo pda opnkjc ykjreypekj lkooaooaz xu aranu lanokj, araj ikzanwpahu wymqwejpaz sepd ep, pdwp da eo wxha pk ykjopnqyp w yeldan sdeyd jkxkzu ahoa ywj zayeldan. e dwra whok kxoanraz pdwp pda yharanan pda lanokj, pda ikna ejpeiwpa eo deo ykjreypekj \n" + 
				"― ydwnhao xwxxwca", 
				"");
		sele.addOption(option);
		
		return sele;
	}

	public static SelectionObject generateWeek2DecryptionSelection() {
		SelectionObject sele = new SelectionObject(StimulatedPlanningFactory.getUUID(), 
				"You received a secret message! It is encrypted? What does it mean?", 
				"Decide on the meaning of this message", 
				"",
				false, true, true);
		sele.setType(SelectionObjectType.CLAN_SELECTION);
		sele.setPurpose(SelectionObjectPurpose.CLAN_VOTING);
		sele.setContent("");
		
		SelectionOption option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
				"We got you", 
				"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam nisl urna, tempus non congue eu, vulputate at nibh. Nullam maximus, ex ac aliquet eleifend, odio libero mollis est, ut vulputate.", 
				"");
		sele.addOption(option);
		
		option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
				"We are after you", 
				"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam nisl urna, tempus non congue eu, vulputate at nibh. Nullam maximus, ex ac aliquet eleifend, odio libero mollis est, ut vulputate.", 
				"");
		sele.addOption(option);
		
		option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
				"You will never be able to read this", 
				"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam nisl urna, tempus non congue eu, vulputate at nibh. Nullam maximus, ex ac aliquet eleifend, odio libero mollis est, ut vulputate.", 
				"");
		sele.addOption(option);
		
		option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
				"You should be warned", 
				"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam nisl urna, tempus non congue eu, vulputate at nibh. Nullam maximus, ex ac aliquet eleifend, odio libero mollis est, ut vulputate.", 
				"");
		sele.addOption(option);
		
		option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
				"Read carefully!", 
				"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam nisl urna, tempus non congue eu, vulputate at nibh. Nullam maximus, ex ac aliquet eleifend, odio libero mollis est, ut vulputate.", 
				"");
		sele.addOption(option);
		
		return sele;
	}

	public static InformationObject generateWeek2TreatmentTestIntroduction() {
		InformationObject info = new InformationObject(StimulatedPlanningFactory.getUUID(), 
				"<span style='font-size:2em;'>Let's again challenge your ability to work together!</span><br>", 
				"<br>Your mission is to solve the following knowledge test!<BR>", 
				"",
				false, true, true);
	
		info.setContent("<span style='font-size:1.5em;'>Remember it is a group effort: only the most voted answer will count, be sure you all select the correct one!</span><BR>" + 
				"<BR>" + 
				"<span style='font-size:1.5em;'>Work well on this simple test because we are going to level up in the next one</span><BR>" + 
				"<BR>" + 
				"The OUNL team<BR>" + 
				"");
		
		return info;
	}

	public static InformationObject generateWeek2ControlTestIntroduction() {
		InformationObject info = new InformationObject(StimulatedPlanningFactory.getUUID(), 
				"<span style='font-size:2em;'>Test your knowledge!</span><br>", 
				"<br>", 
				"",
				true, false, false);
	
		info.setContent("<span style='font-size:1.5em;'>Welcome 😊 to the second week assignment, click on next 👉🏽 to take the test.</span><BR>" + 
				"<BR>" + 
				"<span style='font-size:1.5em;'>Thank you in advance!</span><BR>" + 
				"<BR>" + 
				"The OUNL Team<BR>" + 
				"(Alessandra, Hugo and Roland)<BR>" + 
				"");
		
		return info;
	}

	/*
	1. Which of the below statements is correct?
	
	The text “MJQQT BTWQI” is a Caesar encryption of an English sentence.
	There are 26 possible keys for Caesar.
	When using permutations, there are twice as many keys as in standard Caesar.
	Caesar satisfies Kerckhoffs’ Principle.
	
	 */
		public static SelectionObject generateWeek2KnowledgeTest1() {
			SelectionObject sele = new SelectionObject(StimulatedPlanningFactory.getUUID(), 
					"Test", 
					"Which of the below statements is correct?", 
					"",
					true, true, true);
			sele.setType(SelectionObjectType.CLAN_SINGLE_TEST);
			sele.setPurpose(SelectionObjectPurpose.TEST);
			
			SelectionOption option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
					"The text “MJQQT BTWQI” is a Caesar encryption of an English sentence.", "The text “MJQQT BTWQI” is a Caesar encryption of an English sentence.", "");
			option.setCorrect(true);
			sele.addOption(option);
			
			option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
					"There are 26 possible keys for Caesar.", "There are 26 possible keys for Caesar.", "");
			option.setCorrect(false);
			sele.addOption(option);
			
			option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
					"When using permutations, there are twice as many keys as in standard Caesar.", "When using permutations, there are twice as many keys as in standard Caesar.", "");
			option.setCorrect(false);
			sele.addOption(option);
			
			option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
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
			SelectionObject sele = new SelectionObject(StimulatedPlanningFactory.getUUID(), 
					"Test", 
					"Which of the below statements is correct?", 
					"",
					true, true, true);
			sele.setType(SelectionObjectType.CLAN_SINGLE_TEST);
			sele.setPurpose(SelectionObjectPurpose.TEST);
			
			SelectionOption option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
					"The e is the most frequent letter in a ciphertext.", "The e is the most frequent letter in a ciphertext.", "");
			option.setCorrect(false);
			sele.addOption(option);
			
			option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
					"Caesar encryption hides the structure of the plaintext.", "Caesar encryption hides the structure of the plaintext.", "");
			option.setCorrect(false);
			sele.addOption(option);
			
			option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
					"Caesar merely shifts the distribution of the frequency of letters.", "Caesar merely shifts the distribution of the frequency of letters.", "");
			option.setCorrect(true);
			sele.addOption(option);
			
			option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
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
		public static SelectionObject generateWeek2KnowledgeTest3() {
			SelectionObject sele = new SelectionObject(StimulatedPlanningFactory.getUUID(), 
					"Test", 
					"The plaintext “HELLO” is encrypted with Vigenère. Which of the following is not a possible ciphertext?", 
					"",
					true, true, true);
			sele.setType(SelectionObjectType.CLAN_SINGLE_TEST);
			sele.setPurpose(SelectionObjectPurpose.TEST);
			
			SelectionOption option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
					"EARTH", "EARTH", "");
			option.setCorrect(false);
			sele.addOption(option);
			
			option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
					"AAAAA", "AAAAA", "");
			option.setCorrect(false);
			sele.addOption(option);
			
			option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
					"LLLLLL", "LLLLLL", "");
			option.setCorrect(true);
			sele.addOption(option);
			
			option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
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
		public static SelectionObject generateWeek2KnowledgeTest4() {
			SelectionObject sele = new SelectionObject(StimulatedPlanningFactory.getUUID(), 
					"Test", 
					"What key should you use in Vigenère to encrypt “BAGEL” as “DONUT”?", 
					"",
					true, true, true);
			sele.setType(SelectionObjectType.CLAN_SINGLE_TEST);
			sele.setPurpose(SelectionObjectPurpose.TEST);
			
			SelectionOption option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
					"COHQI", "COHQI", "");
			option.setCorrect(true);
			sele.addOption(option);
			
			option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
					"EOTYE", "EOTYE", "");
			option.setCorrect(false);
			sele.addOption(option);
			
			option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
					"MBLRT", "MBLRT", "");
			option.setCorrect(false);
			sele.addOption(option);
			
			option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
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
		public static SelectionObject generateWeek2KnowledgeTest5() {
			SelectionObject sele = new SelectionObject(StimulatedPlanningFactory.getUUID(), 
					"Test", 
					"Suppose you want to encrypt a plaintext with Vigenère, first with the key “YES” and then with the key “NO”. How could you do this?", 
					"",
					true, true, true);
			sele.setType(SelectionObjectType.CLAN_SINGLE_TEST);
			sele.setPurpose(SelectionObjectPurpose.TEST);
			
			SelectionOption option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
					"The only option is to encrypt twice with Vigenère", "The only option is to encrypt twice with Vigenère", "");
			option.setCorrect(false);
			sele.addOption(option);
			
			option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
					"You can encrypt in one go with the key “YESNO”", "You can encrypt in one go with the key “YESNO”", "");
			option.setCorrect(false);
			sele.addOption(option);
			
			option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
					"You can encrypt in one go with the key “NOYES”", "You can encrypt in one go with the key “NOYES”", "");
			option.setCorrect(false);
			sele.addOption(option);
			
			option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
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
		public static SelectionObject generateWeek2KnowledgeTest6() {
			SelectionObject sele = new SelectionObject(StimulatedPlanningFactory.getUUID(), 
					"Test", 
					"Which of the following statements about Vigenère keys is correct?", 
					"",
					true, true, true);
			sele.setType(SelectionObjectType.CLAN_SINGLE_TEST);
			sele.setPurpose(SelectionObjectPurpose.TEST);
			
			SelectionOption option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
					"A password is always long enough.", "A password is always long enough.", "");
			option.setCorrect(false);
			sele.addOption(option);
			
			option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
					"A book key is best done using an obscure, unknown book.", "A book key is best done using an obscure, unknown book.", "");
			option.setCorrect(false);
			sele.addOption(option);
			
			option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
					"Autokey results in a key as long as the plaintext.", "Autokey results in a key as long as the plaintext.", "");
			option.setCorrect(true);
			sele.addOption(option);
			
			option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
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
			SelectionObject sele = new SelectionObject(StimulatedPlanningFactory.getUUID(), 
					"Test", 
					"Is frequency analysis useless against Vigenère?", 
					"",
					true, true, true);
			sele.setType(SelectionObjectType.CLAN_SINGLE_TEST);
			sele.setPurpose(SelectionObjectPurpose.TEST);
			
			SelectionOption option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
					"Yes.", "Yes.", "");
			option.setCorrect(false);
			sele.addOption(option);
			
			option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
					"No, you can easily spot the shift.", "No, you can easily spot the shift.", "");
			option.setCorrect(false);
			sele.addOption(option);
			
			option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
					"No, but you have to first determine the key length.", "No, but you have to first determine the key length.", "");
			option.setCorrect(true);
			sele.addOption(option);
			
			option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
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
			SelectionObject sele = new SelectionObject(StimulatedPlanningFactory.getUUID(), 
					"Test", 
					"Which of the below statements is correct?", 
					"",
					true, true, true);
			sele.setType(SelectionObjectType.CLAN_SINGLE_TEST);
			sele.setPurpose(SelectionObjectPurpose.TEST);
			
			SelectionOption option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
					"Caesar and Vigenère are both monoalphabetic ciphers.", "Caesar and Vigenère are both monoalphabetic ciphers.", "");
			option.setCorrect(false);
			sele.addOption(option);
			
			option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
					"Vigenère and Playfair are both monoalphabetic ciphers.", "Vigenère and Playfair are both monoalphabetic ciphers.", "");
			option.setCorrect(false);
			sele.addOption(option);
			
			option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
					"Caesar and Vigenère are both polyalphabetic ciphers.", "Caesar and Vigenère are both polyalphabetic ciphers.", "");
			option.setCorrect(false);
			sele.addOption(option);
			
			option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
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
		public static SelectionObject generateWeek2KnowledgeTest9() {
			SelectionObject sele = new SelectionObject(StimulatedPlanningFactory.getUUID(), 
					"Test", 
					"What is the Playfair encryption of the plaintext “TEST” with the key “hello”?", 
					"",
					true, true, true);
			sele.setType(SelectionObjectType.CLAN_SINGLE_TEST);
			sele.setPurpose(SelectionObjectPurpose.TEST);
			
			SelectionOption option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
					"UDTU", "UDTU", "");
			option.setCorrect(false);
			sele.addOption(option);
			
			option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
					"ROTU", "ROTU", "");
			option.setCorrect(true);
			sele.addOption(option);
			
			option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
					"DUUT", "DUUT", "");
			option.setCorrect(false);
			sele.addOption(option);
			
			option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
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
		public static SelectionObject generateWeek2KnowledgeTest10() {
			SelectionObject sele = new SelectionObject(StimulatedPlanningFactory.getUUID(), 
					"Test", 
					"Is the One Time Pad completely secure?", 
					"",
					true, true, true);
			sele.setType(SelectionObjectType.CLAN_SINGLE_TEST);
			sele.setPurpose(SelectionObjectPurpose.TEST);
			
			SelectionOption option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
					"Yes, unless you have a really powerful computer.", "Yes, unless you have a really powerful computer.", "");
			option.setCorrect(false);
			sele.addOption(option);
			
			option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
					"Yes, unless you are forced to reveal the plaintext.", "Yes, unless you are forced to reveal the plaintext.", "");
			option.setCorrect(false);
			sele.addOption(option);
			
			option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
					"Yes, unless you re-use the key.", "Yes, unless you re-use the key.", "");
			option.setCorrect(true);
			sele.addOption(option);
			
			option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
					"Yes, unless the key is completely random.", "Yes, unless the key is completely random.", "");
			option.setCorrect(false);
			sele.addOption(option);
					
			return sele;
		}

	protected static InformationObject generateWeek2TestConclusion() {
		InformationObject info = new InformationObject(StimulatedPlanningFactory.getUUID(), 
				"<span style='font-size:2em;'>We have saved your answers!</span><br>", 
				"<br>", 
				"",
				true, true, true);
	
		info.setContent("<span style='font-size:1.5em;'>Are you curious to know how you performed?</span><BR> "
				+ "<span style='font-size:1.5em;'>If yes check out the Test's Results page week-2 that will be visible from next Monday morning.</span><BR> "
				+ "<BR>"
				+ "The OUNL team\n" + 
				"");
		
		return info;
	}

	protected static InformationObject generateWeek3TreatmentTestIntroduction() {
		InformationObject info = new InformationObject(StimulatedPlanningFactory.getUUID(), 
				"<span style='font-size:2em;'>Let's again challenge your ability to work together!</span><br>", 
				"<br><span style='font-size:1.5em;'>Your mission is to solve the following knowledge test!</span><BR>", 
				"",
				false, true, true);
	
		info.setContent("<span style='font-size:1.5em;'>Remember it is a group effort: only the most voted answer will count, be sure you all select the correct one!</span><BR>" + 
				"<BR>" + 
				"<span style='font-size:1.5em;'>Work well on this simple test because we are going to level up in the next one</span><BR>" + 
				"<BR>" + 
				"The OUNL team<BR>" + 
				"");
		
		return info;
	}

	public static InformationObject generateWeek3ControlTestIntroduction() {
		InformationObject info = new InformationObject(StimulatedPlanningFactory.getUUID(), 
				"<span style='font-size:2em;'>Test your knowledge!</span><br>", 
				"<br>", 
				"",
				true, false, false);
	
		info.setContent("<span style='font-size:1.5em;'>Welcome 😊 to the third week assignment, click on next 👉🏽  to take the test.</span><BR>" + 
				"<BR>" + 
				"<span style='font-size:1.5em;'>Thank you in advance!</span><BR>" + 
				"<BR>" + 
				"The OUNL Team<BR>" + 
				"(Alessandra, Hugo and Roland)<BR>" + 
				"");
		
		return info;
	}

	/*
	1. What is 5 * 17 mod 13?
	a. 5
	b. 6
	c. 7
	d. 8
	
	 */
		public static SelectionObject generateWeek3KnowledgeTest1() {
			SelectionObject sele = new SelectionObject(StimulatedPlanningFactory.getUUID(), 
					"Test", 
					"What is 5 * 17 mod 13?", 
					"",
					true, true, true);
			sele.setType(SelectionObjectType.CLAN_SINGLE_TEST);
			sele.setPurpose(SelectionObjectPurpose.TEST);
			
			SelectionOption option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
					"5", "5", "");
			option.setCorrect(false);
			sele.addOption(option);
			
			option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
					"6", "6", "");
			option.setCorrect(false);
			sele.addOption(option);
			
			option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
					"7", "7", "");
			option.setCorrect(false);
			sele.addOption(option);
			
			option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
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
				SelectionObject sele = new SelectionObject(StimulatedPlanningFactory.getUUID(), 
						"Test", 
						"What value of x makes this equation work? 5x = 1 mod 11", 
						"",
						true, true, true);
				sele.setType(SelectionObjectType.CLAN_SINGLE_TEST);
				sele.setPurpose(SelectionObjectPurpose.TEST);
				
				SelectionOption option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
						"6", "6", "");
				option.setCorrect(false);
				sele.addOption(option);
				
				option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
						"7", "7", "");
				option.setCorrect(false);
				sele.addOption(option);
				
				option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
						"8", "8", "");
				option.setCorrect(false);
				sele.addOption(option);
				
				option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
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
			SelectionObject sele = new SelectionObject(StimulatedPlanningFactory.getUUID(), 
					"Test", 
					"Can you set up RSA with p = 6 and q = 8?", 
					"",
					true, true, true);
			sele.setType(SelectionObjectType.CLAN_SINGLE_TEST);
			sele.setPurpose(SelectionObjectPurpose.TEST);
			
			SelectionOption option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
					"Yes", "Yes", "");
			option.setCorrect(false);
			sele.addOption(option);
			
			option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
					"No, because p and q cannot both be even.", "No, because p and q cannot both be even.", "");
			option.setCorrect(false);
			sele.addOption(option);
			
			option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
					"No, because q should be smaller than p.", "No, because q should be smaller than p.", "");
			option.setCorrect(false);
			sele.addOption(option);
			
			option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
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
			SelectionObject sele = new SelectionObject(StimulatedPlanningFactory.getUUID(), 
					"Test", 
					"Suppose we have RSA set up with e = 9 and n = 1189. What is the correct encryption of M = 19?", 
					"",
					true, true, true);
			sele.setType(SelectionObjectType.CLAN_SINGLE_TEST);
			sele.setPurpose(SelectionObjectPurpose.TEST);
			
			SelectionOption option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
					"517", "517", "");
			option.setCorrect(false);
			sele.addOption(option);
			
			option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
					"13", "13", "");
			option.setCorrect(false);
			sele.addOption(option);
			
			option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
					"719", "719", "");
			option.setCorrect(false);
			sele.addOption(option);
			
			option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
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
			SelectionObject sele = new SelectionObject(StimulatedPlanningFactory.getUUID(), 
					"Test", 
					"In practice, how long should be an RSA key for decent security?", 
					"",
					true, true, true);
			sele.setType(SelectionObjectType.CLAN_SINGLE_TEST);
			sele.setPurpose(SelectionObjectPurpose.TEST);
			
			SelectionOption option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
					"Slightly more than 1000 bits", "Slightly more than 1000 bits", "");
			option.setCorrect(false);
			sele.addOption(option);
			
			option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
					"Slightly more than 2000 bits", "Slightly more than 2000 bits", "");
			option.setCorrect(false);
			sele.addOption(option);
			
			option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
					"Slightly more than 3000 bits", "Slightly more than 3000 bits", "");
			option.setCorrect(true);
			sele.addOption(option);
			
			option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
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
		public static SelectionObject generateWeek3KnowledgeTest6() {
			SelectionObject sele = new SelectionObject(StimulatedPlanningFactory.getUUID(), 
					"Test", 
					"What is the theoretical reason for RSA’s security?", 
					"",
					true, true, true);
			sele.setType(SelectionObjectType.CLAN_SINGLE_TEST);
			sele.setPurpose(SelectionObjectPurpose.TEST);
			
			SelectionOption option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
					"Padding with OAEP", "Padding with OAEP", "");
			option.setCorrect(false);
			sele.addOption(option);
			
			option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
					"Factoring is hard", "Factoring is hard", "");
			option.setCorrect(true);
			sele.addOption(option);
			
			option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
					"Discrete log is hard", "Discrete log is hard", "");
			option.setCorrect(false);
			sele.addOption(option);
			
			option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
					"Euler’s generalisation of Fermat’s little theorem", "Euler’s generalisation of Fermat’s little theorem", "");
			option.setCorrect(false);
			sele.addOption(option);
					
			return sele;
		}

	protected static InformationObject generateWeek3TestConclusion() {
		InformationObject info = new InformationObject(StimulatedPlanningFactory.getUUID(), 
				"<span style='font-size:2em;'>We have saved your answers!</span><br>", 
				"<br>", 
				"",
				true, true, true);
	
		info.setContent("<span style='font-size:1.5em;'>Are you curious to know how you performed?</span><BR> "
				+ "<span style='font-size:1.5em;'>If yes check out the Test's Results week-3 page that will be visible from next Monday morning.</span><BR> "
				+ "<BR>"
				+ "The OUNL team\n" + 
				"");
		
		return info;
	}

	protected static InformationObject generateWeek4TreatmentTestIntroduction() {
		InformationObject info = new InformationObject(StimulatedPlanningFactory.getUUID(), 
				"<span style='font-size:2em;'>Let's again challenge your ability to work together!</span><br>", 
				"<br><span style='font-size:1.5em;'>Your mission is to solve the following knowledge test!</span><BR>", 
				"",
				false, true, true);
	
		info.setContent("<span style='font-size:1.5em;'><strong>Remember</strong> it is a group effort: only the most voted answer will count, be sure you all select the correct one!</span><BR>" + 
				"<BR>" + 
				"<span style='font-size:1.5em;'>Work well on this simple test because we are going to level up in the next one</span><BR>" + 
				"<BR>" + 
				"The OUNL team<BR>" + 
				"");
		
		return info;
	}

	protected static InformationObject generateWeek4ControlTestIntroduction() {
		InformationObject info = new InformationObject(StimulatedPlanningFactory.getUUID(), 
				"<span style='font-size:2em;'>Test your knowledge!</span><br>", 
				"<br>", 
				"",
				true, false, false);
	
		info.setContent("<span style='font-size:1.5em;'>Welcome 😊 to the fourth week assignment, click on next 👉🏽 to take the test.</span><BR>" + 
				"<BR>" + 
				"<span style='font-size:1.5em;'>Thank you in advance!</span><BR>" + 
				"<BR>" + 
				"The OUNL Team<BR>" + 
				"(Alessandra, Hugo and Roland)<BR>" + 
				"");
		
		return info;
	}

	/*
	1. When is authenticity satisfied?
	
	If the used key(s) is/are kept secret.
	If no outsider learns the message.
	If the message arrives as it was sent.
	If the message was indeed sent by the apparent sender.
	 */
		protected static SelectionObject generateWeek4KnowledgeTest1() {
			SelectionObject sele = new SelectionObject(StimulatedPlanningFactory.getUUID(), 
					"Test", 
					"When is authenticity satisfied?", 
					"",
					true, true, true);
			sele.setType(SelectionObjectType.CLAN_SINGLE_TEST);
			sele.setPurpose(SelectionObjectPurpose.TEST);
			
			SelectionOption option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
					"If the used key(s) is/are kept secret.", "If the used key(s) is/are kept secret.", "");
			option.setCorrect(false);
			sele.addOption(option);
			
			option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
					"If no outsider learns the message.", "If no outsider learns the message.", "");
			option.setCorrect(false);
			sele.addOption(option);
			
			option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
					"If the message arrives as it was sent.", "If the message arrives as it was sent.", "");
			option.setCorrect(false);
			sele.addOption(option);
			
			option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
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
			SelectionObject sele = new SelectionObject(StimulatedPlanningFactory.getUUID(), 
					"Test", 
					"How can you achieve confidentiality for messages sent over the Internet?", 
					"",
					true, true, true);
			sele.setType(SelectionObjectType.CLAN_SINGLE_TEST);
			sele.setPurpose(SelectionObjectPurpose.TEST);
			
			SelectionOption option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
					"You need encryption.", "You need encryption.", "");
			option.setCorrect(true);
			sele.addOption(option);
			
			option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
					"You need encryption and hash functions.", "You need encryption and hash functions.", "");
			option.setCorrect(false);
			sele.addOption(option);
			
			option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
					"You need encryption and digital signatures.", "You need encryption and digital signatures.", "");
			option.setCorrect(false);
			sele.addOption(option);
			
			option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
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
			SelectionObject sele = new SelectionObject(StimulatedPlanningFactory.getUUID(), 
					"Test", 
					"Consider a function “length” that returns the length of the input string. Is this function suitable as a hash function?", 
					"",
					true, true, true);
			sele.setType(SelectionObjectType.CLAN_SINGLE_TEST);
			sele.setPurpose(SelectionObjectPurpose.TEST);
			
			SelectionOption option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
					"Yes.", "Yes.", "");
			option.setCorrect(false);
			sele.addOption(option);
			
			option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
					"No, it violates pre-image resistance.", "No, it violates pre-image resistance.", "");
			option.setCorrect(false);
			sele.addOption(option);
			
			option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
					"No, it violates collision resistance.", "No, it violates collision resistance.", "");
			option.setCorrect(true);
			sele.addOption(option);
			
			option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
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
			SelectionObject sele = new SelectionObject(StimulatedPlanningFactory.getUUID(), 
					"Test", 
					"Suppose there is a blockchain, and an attacker makes a change to the 3-but-newest block. When would this be detected?", 
					"",
					true, true, true);
			sele.setType(SelectionObjectType.CLAN_SINGLE_TEST);
			sele.setPurpose(SelectionObjectPurpose.TEST);
			
			SelectionOption option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
					"When a new block is created.", "When a new block is created.", "");
			option.setCorrect(false);
			sele.addOption(option);
			
			option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
					"When someone verifies correctness of the new block (and only the new block).", "When someone verifies correctness of the new block (and only the new block).", "");
			option.setCorrect(false);
			sele.addOption(option);
			
			option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
					"When someone verifies correctness of the entire blockchain.", "When someone verifies correctness of the entire blockchain.", "");
			option.setCorrect(true);
			sele.addOption(option);
			
			option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
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
		public static SelectionObject generateWeek4KnowledgeTest5() {
			SelectionObject sele = new SelectionObject(StimulatedPlanningFactory.getUUID(), 
					"Test", 
					"Suppose Bob receives an encrypted message, Encpk(bob)(m), and the hash of the message, hash(m). It seems to have come from Alice. What are all security guarantees that Bob has?", 
					"",
					true, true, true);
			sele.setType(SelectionObjectType.CLAN_SINGLE_TEST);
			sele.setPurpose(SelectionObjectPurpose.TEST);
			
			SelectionOption option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
					"Nothing, this may have been sent by an attacker", "Nothing, this may have been sent by an attacker", "");
			option.setCorrect(true);
			sele.addOption(option);
			
			option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
					"Confidentiality: no one but Alice and Bob know the message", "Confidentiality: no one but Alice and Bob know the message", "");
			option.setCorrect(false);
			sele.addOption(option);
			
			option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
					"Integrity: no one could have changed the message in transit", "Integrity: no one could have changed the message in transit", "");
			option.setCorrect(false);
			sele.addOption(option);
			
			option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
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
			SelectionObject sele = new SelectionObject(StimulatedPlanningFactory.getUUID(), 
					"Test", 
					"Suppose you visit a website “yahooogle.com” and it claims to be a company started by Google and Yahoo. What information in the certificate would make you trust the website’s security?", 
					"",
					true, true, true);
			sele.setType(SelectionObjectType.CLAN_SINGLE_TEST);
			sele.setPurpose(SelectionObjectPurpose.TEST);
			
			SelectionOption option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
					"If the name in the certificate is “yahooogle.com”.", "If the name in the certificate is “yahooogle.com”.", "");
			option.setCorrect(false);
			sele.addOption(option);
			
			option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
					"Answer a AND the certificate is valid.", "Answer a AND the certificate is valid.", "");
			option.setCorrect(false);
			sele.addOption(option);
			
			option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
					"Answer b AND your browser trusts the certificate.", "Answer b AND your browser trusts the certificate.", "");
			option.setCorrect(true);
			sele.addOption(option);
			
			option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
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
			SelectionObject sele = new SelectionObject(StimulatedPlanningFactory.getUUID(), 
					"Test", 
					"Which mixnets can straightforwardly be used for routing?", 
					"",
					true, true, true);
			sele.setType(SelectionObjectType.CLAN_SINGLE_TEST);
			sele.setPurpose(SelectionObjectPurpose.TEST);
			
			SelectionOption option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
					"Neither.", "Neither.", "");
			option.setCorrect(false);
			sele.addOption(option);
			
			option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
					"Re-encryption mixnets", "Re-encryption mixnets", "");
			option.setCorrect(false);
			sele.addOption(option);
			
			option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
					"Decryption mixnets", "Decryption mixnets", "");
			option.setCorrect(true);
			sele.addOption(option);
			
			option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
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
			SelectionObject sele = new SelectionObject(StimulatedPlanningFactory.getUUID(), 
					"Test", 
					"How does TOR preserve anonymity of your traffic?", 
					"",
					true, true, true);
			sele.setType(SelectionObjectType.CLAN_SINGLE_TEST);
			sele.setPurpose(SelectionObjectPurpose.TEST);
			
			SelectionOption option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
					"Entry nodes don’t know to which site the sender is talking.", "Entry nodes don’t know to which site the sender is talking.", "");
			option.setCorrect(false);
			sele.addOption(option);
			
			option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
					"Exit nodes don’t know who is talking to the recipient site", "Exit nodes don’t know who is talking to the recipient site", "");
			option.setCorrect(false);
			sele.addOption(option);
			
			option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
					"Routing nodes don’t know who sender is, nor who recipient is.", "Routing nodes don’t know who sender is, nor who recipient is.", "");
			option.setCorrect(false);
			sele.addOption(option);
			
			option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
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
			SelectionObject sele = new SelectionObject(StimulatedPlanningFactory.getUUID(), 
					"Test", 
					"Which kinds of cryptography (of those treated in the course) can quantum computers break?", 
					"",
					true, true, true);
			sele.setType(SelectionObjectType.CLAN_SINGLE_TEST);
			sele.setPurpose(SelectionObjectPurpose.TEST);
			
			SelectionOption option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
					"All cryptography", "All cryptography", "");
			option.setCorrect(false);
			sele.addOption(option);
			
			option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
					"Only cryptography that is not information-theoretically secure.", "Only cryptography that is not information-theoretically secure.", "");
			option.setCorrect(true);
			sele.addOption(option);
			
			option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
					"Only cryptography that is not computationally secure.", "Only cryptography that is not computationally secure.", "");
			option.setCorrect(false);
			sele.addOption(option);
			
			option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
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
			SelectionObject sele = new SelectionObject(StimulatedPlanningFactory.getUUID(), 
					"Test", 
					"Which of the below is true?", 
					"",
					true, true, true);
			sele.setType(SelectionObjectType.CLAN_SINGLE_TEST);
			sele.setPurpose(SelectionObjectPurpose.TEST);
			
			SelectionOption option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
					"If a cryptosystem satisfies IND-CPA, it will satisfy IND-CCA.", "If a cryptosystem satisfies IND-CPA, it will satisfy IND-CCA.", "");
			option.setCorrect(false);
			sele.addOption(option);
			
			option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
					"If a cryptosystem satisfies IND-CCA, it will be information-theoretically secure.", "If a cryptosystem satisfies IND-CCA, it will be information-theoretically secure.", "");
			option.setCorrect(false);
			sele.addOption(option);
			
			option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
					"The One Time Pad is computationally secure.", "The One Time Pad is computationally secure.", "");
			option.setCorrect(false);
			sele.addOption(option);
			
			option = new SelectionOption(StimulatedPlanningFactory.getUUID(), 
					"Computationally secure cryptosystems may be broken by advances in theory.", "Computationally secure cryptosystems may be broken by advances in theory.", "");
			option.setCorrect(true);
			sele.addOption(option);
					
			return sele;
		}

	protected static InformationObject generateWeek4TestConclusion() {
		InformationObject info = new InformationObject(StimulatedPlanningFactory.getUUID(), 
				"<span style='font-size:2em;'>We have saved your answers!</span><br>", 
				"<br>", 
				"",
				true, true, true);
	
		info.setContent("<span style='font-size:1.5em;'>Are you curious to know how you performed?</span><BR> "
				+ "<span style='font-size:1.5em;'>If yes check out the Test's Results page week-4 that will be visible from next Sunday morning.</span><BR> "
				+ "<BR>"
				+ "The OUNL team\n" + 
				"");
		
		return info;
	}

	
	
}
