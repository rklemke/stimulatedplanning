package stimulatedplanning;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.ListIterator;
import java.util.logging.Logger;

import stimulatedplanning.ContentDescriptor;
import stimulatedplanning.CourseDescriptor;
import stimulatedplanning.GoalDescriptor;
import stimulatedplanning.LessonDescriptor;
import stimulatedplanning.ModuleDescriptor;
import stimulatedplanning.StimulatedPlanningFactory;

public class SP_TLA_ProductionCourseCreationFactory {

	public static final String prodCourseId = "TLA2019";
	public static final String prodCourseBaseURL = "https://ou.edia.nl/courses/course-v1:OUNL+TLA2019+2019_1/courseware/";
	public static final String prodCourseEditURL = "439e1cfd6bbb4406bdd9f0b4ebf1794a/6f065bf11bac40ad8f01a677e8ddcca1/?activate_block_id=block-v1%3AOUNL%2BTLA2019%2B2019_1%2Btype%40sequential%2Bblock%406f065bf11bac40ad8f01a677e8ddcca1";

	private static final Logger log = Logger.getLogger(SP_TLA_ProductionCourseCreationFactory.class.getName());   

	public SP_TLA_ProductionCourseCreationFactory() {
		// TODO Auto-generated constructor stub
	}

	
	public static CourseDescriptor generateProductionCourse() {
		log.info("generateProductionCourse: "+prodCourseId);
		
		// Course
			
		CourseDescriptor course = new CourseDescriptor(prodCourseId, 
			"Trusted Learning Analytics", 
			"Trusted Learning Analytics", 
			prodCourseBaseURL);

		//
		// Module Introduction
		generateTLAModule0(course);
		
		//
		// Module 1
		generateTLAModule1(course);
		
		//
		// Module 2
		generateTLAModule2(course);
		
		//
		// Module 3
		generateTLAModule3(course);
		
		//
		// Module 4
		generateTLAModule4(course);
		
		//
		// Goal
		//GoalDescriptor goal = new GoalDescriptor(StimulatedPlanningFactory.getUUID(), "Browsing the Course", "I intend to browse around", "");
		GoalDescriptor goal = new GoalDescriptor(course.getId()+"_completionGoal", "Browsing the Course", "I intend to browse around", "");
		goal.addCompletionGoal("100", "all materials (100%)");
		goal.addCompletionGoal("70", "most materials (70%)");
		goal.addCompletionGoal("40", "some materials (40%)");
		goal.addCompletionGoal("10", "less than 10%");
		goal.addCompletionGoal("0", "I have not decided yet");
		course.addGoal(goal);
			
		return course;
	}

	// ==== Module Introduction ====

	public static ModuleDescriptor generateTLAModule0(CourseDescriptor course) {
		ModuleDescriptor module = null;
		LessonDescriptor lesson = null;
		ContentDescriptor content = null;
		GoalDescriptor goal = null;
		int l = 0;
		String contentId;
	
		module = course.addModule(new ModuleDescriptor(course.getId()+"_m0", 
				"Welcome", 
				"Welcome", ""));

		lesson = module.addLesson(new LessonDescriptor(module.getId()+"_l"+(l++), 
				"Course Introduction",
				"Course Introduction",""));	
	
		contentId = "439e1cfd6bbb4406bdd9f0b4ebf1794a/d43238516a5b4379826171a2d1a4a59a/";
		content = lesson.addContent(new ContentDescriptor(contentId, 
				"Course Introduction", 
				"Course Introduction", 
				prodCourseBaseURL+contentId)); // "439e1cfd6bbb4406bdd9f0b4ebf1794a/d43238516a5b4379826171a2d1a4a59a/")); // ?activate_block_id=block-v1%3AOUNL%2BTLA2019%2B2019_1%2Btype%40sequential%2Bblock%40d43238516a5b4379826171a2d1a4a59a

		lesson = module.addLesson(new LessonDescriptor(module.getId()+"_l"+(l++), 
				"Instructor Introduction",
				"Instructor Introduction",""));	
	
		contentId = "439e1cfd6bbb4406bdd9f0b4ebf1794a/cf983613678b46d0b1a52e02915aaeb6/";
		content = lesson.addContent(new ContentDescriptor(contentId, 
				"Video", 
				"Video", 
				prodCourseBaseURL+contentId)); // "439e1cfd6bbb4406bdd9f0b4ebf1794a/cf983613678b46d0b1a52e02915aaeb6/")); // ?activate_block_id=block-v1%3AOUNL%2BTLA2019%2B2019_1%2Btype%40sequential%2Bblock%40cf983613678b46d0b1a52e02915aaeb6

		// https://ou.edia.nl/courses/course-v1:OUNL+TLA2019+2019_1/courseware/439e1cfd6bbb4406bdd9f0b4ebf1794a/6f065bf11bac40ad8f01a677e8ddcca1/?activate_block_id=block-v1%3AOUNL%2BTLA2019%2B2019_1%2Btype%40sequential%2Bblock%406f065bf11bac40ad8f01a677e8ddcca1
		lesson = module.addLesson(new LessonDescriptor(module.getId()+"_l"+(l++), 
				"Make your own plan",
				"Make your own plan",""));	
	
		contentId = "439e1cfd6bbb4406bdd9f0b4ebf1794a/6f065bf11bac40ad8f01a677e8ddcca1/";
		content = lesson.addContent(new ContentDescriptor(contentId, 
				"Make your own plan", 
				"Make your own plan", 
				prodCourseBaseURL+contentId)); // "439e1cfd6bbb4406bdd9f0b4ebf1794a/6f065bf11bac40ad8f01a677e8ddcca1/")); // ?activate_block_id=block-v1%3AOUNL%2BTLA2019%2B2019_1%2Btype%40sequential%2Bblock%406f065bf11bac40ad8f01a677e8ddcca1

		return module;
	}


	// ==== Module 1 ====

	public static ModuleDescriptor generateTLAModule1(CourseDescriptor course) {
		ModuleDescriptor module = null;
		LessonDescriptor lesson = null;
		ContentDescriptor content = null;
		GoalDescriptor goal = null;
		int l = 0;
		String contentId;
	
		module = course.addModule(new ModuleDescriptor(course.getId()+"_m1", 
				"Grounding: Learning Analytics in a Nutshell", 
				"Grounding: Learning Analytics in a Nutshell", ""));

		lesson = module.addLesson(new LessonDescriptor(module.getId()+"_l"+(l++), 
				"Introduction",
				"Introduction",""));	
	
		contentId = "22048851569a4ea1aa76cf167fdaa94f/17b56ddb659d49d2a9189ffa3c8b0084/";
		content = lesson.addContent(new ContentDescriptor(contentId, 
				"Introduction", 
				"Introduction", 
				prodCourseBaseURL+contentId)); // "22048851569a4ea1aa76cf167fdaa94f/17b56ddb659d49d2a9189ffa3c8b0084/")); // ?activate_block_id=block-v1%3AOUNL%2BTLA2019%2B2019_1%2Btype%40sequential%2Bblock%4017b56ddb659d49d2a9189ffa3c8b0084

		lesson = module.addLesson(new LessonDescriptor(module.getId()+"_l"+(l++), 
				"Video: Grounding: Learning Analytics in a Nutshell",
				"Video: Grounding: Learning Analytics in a Nutshell",""));	
	
		contentId = "22048851569a4ea1aa76cf167fdaa94f/26a086618f9f4200b61f7172ed9d4a60/";
		content = lesson.addContent(new ContentDescriptor(contentId, 
				"Video: Grounding: Learning Analytics in a Nutshell", 
				"Video: Grounding: Learning Analytics in a Nutshell", 
				prodCourseBaseURL+contentId)); // "22048851569a4ea1aa76cf167fdaa94f/26a086618f9f4200b61f7172ed9d4a60/")); // ?activate_block_id=block-v1%3AOUNL%2BTLA2019%2B2019_1%2Btype%40sequential%2Bblock%4026a086618f9f4200b61f7172ed9d4a60

		lesson = module.addLesson(new LessonDescriptor(module.getId()+"_l"+(l++), 
				"A Definition of Learning Analytics",
				"A Definition of Learning Analytics",""));	
	
		contentId = "22048851569a4ea1aa76cf167fdaa94f/d824b2a944914c4699f2f02d43036ff4/";
		content = lesson.addContent(new ContentDescriptor(contentId, 
				"A Definition of Learning Analytics", 
				"A Definition of Learning Analytics", 
				prodCourseBaseURL+contentId)); // "22048851569a4ea1aa76cf167fdaa94f/d824b2a944914c4699f2f02d43036ff4/")); // ?activate_block_id=block-v1%3AOUNL%2BTLA2019%2B2019_1%2Btype%40sequential%2Bblock%40d824b2a944914c4699f2f02d43036ff4

		lesson = module.addLesson(new LessonDescriptor(module.getId()+"_l"+(l++), 
				"Six Dimensions of Learning Analytics",
				"Six Dimensions of Learning Analytics",""));	
	
		contentId = "22048851569a4ea1aa76cf167fdaa94f/ab4c0c6d41ed440a9b769b849e3ef47c/";
		content = lesson.addContent(new ContentDescriptor(contentId, 
				"Six Dimensions of Learning Analytics", 
				"Six Dimensions of Learning Analytics", 
				prodCourseBaseURL+contentId)); // "22048851569a4ea1aa76cf167fdaa94f/ab4c0c6d41ed440a9b769b849e3ef47c/")); // ?activate_block_id=block-v1%3AOUNL%2BTLA2019%2B2019_1%2Btype%40sequential%2Bblock%40ab4c0c6d41ed440a9b769b849e3ef47c

//		lesson = module.addLesson(new LessonDescriptor(module.getId()+"_l"+(l++), 
//				"References",
//				"References",""));	
//	
//		contentId = "22048851569a4ea1aa76cf167fdaa94f/96a6f45c96694a93b29fc1fa6b9bd923/";
//		content = lesson.addContent(new ContentDescriptor(contentId, 
//				"References", 
//				"References", 
//				prodCourseBaseURL+contentId)); // "22048851569a4ea1aa76cf167fdaa94f/96a6f45c96694a93b29fc1fa6b9bd923/")); // ?activate_block_id=block-v1%3AOUNL%2BTLA2019%2B2019_1%2Btype%40sequential%2Bblock%4096a6f45c96694a93b29fc1fa6b9bd923

		lesson = module.addLesson(new LessonDescriptor(module.getId()+"_l"+(l++), 
				"Assignment: Getting Started with Learning Analytics",
				"Assignment: Getting Started with Learning Analytics",""));	
	
		contentId = "22048851569a4ea1aa76cf167fdaa94f/9167f560646249b8a3cf3cbd22c73307/";
		content = lesson.addContent(new ContentDescriptor(contentId, 
				"Assignment: Getting started with Learning Analytics", 
				"Assignment: Getting started with Learning Analytics", 
				prodCourseBaseURL+contentId)); // "22048851569a4ea1aa76cf167fdaa94f/9167f560646249b8a3cf3cbd22c73307/")); // ?activate_block_id=block-v1%3AOUNL%2BTLA2019%2B2019_1%2Btype%40sequential%2Bblock%409167f560646249b8a3cf3cbd22c73307
		
		goal = course.addGoal(new GoalDescriptor(module.getId()+"_g1", module.getTitle(), 
				"I intend to participate in the course activities to learn about "+module.getTitle(), ""));
		ListIterator<LessonDescriptor> iterator = module.getLessons();
		while (iterator.hasNext()) {
			goal.addLesson(iterator.next());
		}
		
		return module;
	}

	// ==== Module 2 ====

	public static ModuleDescriptor generateTLAModule2(CourseDescriptor course) {
		ModuleDescriptor module = null;
		LessonDescriptor lesson = null;
		ContentDescriptor content = null;
		GoalDescriptor goal = null;
		int l = 0;
		String contentId;
	
		module = course.addModule(new ModuleDescriptor(course.getId()+"_m2", 
				"Digging: Learning Analytics Implementation", 
				"Digging: Learning Analytics Implementation Challenges", ""));

		lesson = module.addLesson(new LessonDescriptor(module.getId()+"_l"+(l++), 
				"Introduction",
				"Introduction",""));	
	
		contentId = "d10190c443fa405a8455994650715c1d/013d5cd26cbf4bebb0bc1d1a4babdd57/";
		content = lesson.addContent(new ContentDescriptor(contentId, 
				"Introduction", 
				"Introduction", 
				prodCourseBaseURL+contentId)); // "d10190c443fa405a8455994650715c1d/013d5cd26cbf4bebb0bc1d1a4babdd57/")); // /?activate_block_id=block-v1%3AOUNL%2BTLA2019%2B2019_1%2Btype%40sequential%2Bblock%40013d5cd26cbf4bebb0bc1d1a4babdd57

		lesson = module.addLesson(new LessonDescriptor(module.getId()+"_l"+(l++), 
				"Video: Digging: Learning Analytics Implementation Challenges",
				"Video: Digging: Learning Analytics Implementation Challenges",""));	
	
		contentId = "d10190c443fa405a8455994650715c1d/0b5aa850821b4527a99f40a60dd0cc0d/";
		content = lesson.addContent(new ContentDescriptor(contentId, 
				"Video: Digging: Learning Analytics Implementation Challenges", 
				"Video: Digging: Learning Analytics Implementation Challenges", 
				prodCourseBaseURL+contentId)); // "d10190c443fa405a8455994650715c1d/0b5aa850821b4527a99f40a60dd0cc0d/")); // ?activate_block_id=block-v1%3AOUNL%2BTLA2019%2B2019_1%2Btype%40sequential%2Bblock%400b5aa850821b4527a99f40a60dd0cc0d

		lesson = module.addLesson(new LessonDescriptor(module.getId()+"_l"+(l++), 
				"Ethics and Privacy: Background and Definitions",
				"Ethics and Privacy: Background and Definitions",""));	
	
		contentId = "d10190c443fa405a8455994650715c1d/d9d194a431a44ae999c5f30f33bbf3ce/";
		content = lesson.addContent(new ContentDescriptor(contentId, 
				"Ethics and Privacy: Background and Definitions", 
				"Ethics and Privacy: Background and Definitions", 
				prodCourseBaseURL+contentId)); // "d10190c443fa405a8455994650715c1d/d9d194a431a44ae999c5f30f33bbf3ce/")); // ?activate_block_id=block-v1%3AOUNL%2BTLA2019%2B2019_1%2Btype%40sequential%2Bblock%40d9d194a431a44ae999c5f30f33bbf3ce

		lesson = module.addLesson(new LessonDescriptor(module.getId()+"_l"+(l++), 
				"Ethics and Privacy: Legal Frameworks",
				"Ethics and Privacy: Legal Frameworks",""));	
	
		contentId = "d10190c443fa405a8455994650715c1d/86a827fdf5504f86bea4d1bc922ef96a/";
		content = lesson.addContent(new ContentDescriptor(contentId, 
				"Ethics and Privacy: Legal Frameworks", 
				"Ethics and Privacy: Legal Frameworks", 
				prodCourseBaseURL+contentId)); // "d10190c443fa405a8455994650715c1d/86a827fdf5504f86bea4d1bc922ef96a/")); // ?activate_block_id=block-v1%3AOUNL%2BTLA2019%2B2019_1%2Btype%40sequential%2Bblock%4086a827fdf5504f86bea4d1bc922ef96a

		lesson = module.addLesson(new LessonDescriptor(module.getId()+"_l"+(l++), 
				"Ethics and Privacy: Resources for Trusted Learning Analytics",
				"Ethics and Privacy: Resources for Trusted Learning Analytics",""));	
	
		contentId = "d10190c443fa405a8455994650715c1d/7463d543c95c45b889db643b79153194/";
		content = lesson.addContent(new ContentDescriptor(contentId, 
				"Ethics and Privacy: Resources for Trusted Learning Analytics", 
				"Ethics and Privacy: Resources for Trusted Learning Analytics", 
				prodCourseBaseURL+contentId)); // "d10190c443fa405a8455994650715c1d/7463d543c95c45b889db643b79153194/")); // ?activate_block_id=block-v1%3AOUNL%2BTLA2019%2B2019_1%2Btype%40sequential%2Bblock%407463d543c95c45b889db643b79153194

		lesson = module.addLesson(new LessonDescriptor(module.getId()+"_l"+(l++), 
				"Learning Design: Background and Definitions",
				"Learning Design: Background and Definitions",""));	
	
		contentId = "d10190c443fa405a8455994650715c1d/3222715b28934bd1af92e2b00928dcd1/";
		content = lesson.addContent(new ContentDescriptor(contentId, 
				"Learning Design: Background and Definitions", 
				"Learning Design: Background and Definitions", 
				prodCourseBaseURL+contentId)); // "d10190c443fa405a8455994650715c1d/3222715b28934bd1af92e2b00928dcd1/")); // ?activate_block_id=block-v1%3AOUNL%2BTLA2019%2B2019_1%2Btype%40sequential%2Bblock%403222715b28934bd1af92e2b00928dcd1

		lesson = module.addLesson(new LessonDescriptor(module.getId()+"_l"+(l++), 
				"Learning Design: Types of Learning Design Models",
				"Learning Design: Types of Learning Design Models",""));	
	
		contentId = "d10190c443fa405a8455994650715c1d/604f5b0862fd46af8e60cc998c2146d5/";
		content = lesson.addContent(new ContentDescriptor(contentId, 
				"Learning Design: Types of Learning Design Models", 
				"Learning Design: Types of Learning Design Models", 
				prodCourseBaseURL+contentId)); // "d10190c443fa405a8455994650715c1d/604f5b0862fd46af8e60cc998c2146d5/")); // ?activate_block_id=block-v1%3AOUNL%2BTLA2019%2B2019_1%2Btype%40sequential%2Bblock%40604f5b0862fd46af8e60cc998c2146d5

		lesson = module.addLesson(new LessonDescriptor(module.getId()+"_l"+(l++), 
				"Learning Design: Future Challenges",
				"Learning Design: Future Challenges",""));	
	
		contentId = "d10190c443fa405a8455994650715c1d/f5ac3f520f8b4230a4e9a846143d2223/";
		content = lesson.addContent(new ContentDescriptor(contentId, 
				"Learning Design: Future Challenges", 
				"Learning Design: Future Challenges", 
				prodCourseBaseURL+contentId)); // "d10190c443fa405a8455994650715c1d/f5ac3f520f8b4230a4e9a846143d2223/")); // ?activate_block_id=block-v1%3AOUNL%2BTLA2019%2B2019_1%2Btype%40sequential%2Bblock%40f5ac3f520f8b4230a4e9a846143d2223

		lesson = module.addLesson(new LessonDescriptor(module.getId()+"_l"+(l++), 
				"Evaluating Learning Analytics",
				"Evaluating Learning Analytics",""));	
	
		contentId = "d10190c443fa405a8455994650715c1d/edaec2f21f504375a4056438aef61685/";
		content = lesson.addContent(new ContentDescriptor(contentId, 
				"Evaluating Learning Analytics", 
				"Evaluating Learning Analytics", 
				prodCourseBaseURL+contentId)); // "d10190c443fa405a8455994650715c1d/edaec2f21f504375a4056438aef61685/")); // ?activate_block_id=block-v1%3AOUNL%2BTLA2019%2B2019_1%2Btype%40sequential%2Bblock%40edaec2f21f504375a4056438aef61685 

//		lesson = module.addLesson(new LessonDescriptor(module.getId()+"_l"+(l++), 
//				"References",
//				"References",""));	
//	
//		contentId = "d10190c443fa405a8455994650715c1d/eb84787514234adea7df36dcd9484325/";
//		content = lesson.addContent(new ContentDescriptor(contentId, 
//				"References", 
//				"References", 
//				prodCourseBaseURL+contentId)); // "d10190c443fa405a8455994650715c1d/eb84787514234adea7df36dcd9484325/")); // ?activate_block_id=block-v1%3AOUNL%2BTLA2019%2B2019_1%2Btype%40sequential%2Bblock%40eb84787514234adea7df36dcd9484325

		lesson = module.addLesson(new LessonDescriptor(module.getId()+"_l"+(l++), 
				"Assignment: Quiz",
				"Assignment: Quiz",""));	
	
		contentId = "d10190c443fa405a8455994650715c1d/128a3e1f84134e4b9ee94a3802de4f00/";
		content = lesson.addContent(new ContentDescriptor(contentId, 
				"Assignment: Quiz", 
				"Assignment: Quiz", 
				prodCourseBaseURL+contentId)); // "d10190c443fa405a8455994650715c1d/128a3e1f84134e4b9ee94a3802de4f00/")); // ?activate_block_id=block-v1%3AOUNL%2BTLA2019%2B2019_1%2Btype%40sequential%2Bblock%40128a3e1f84134e4b9ee94a3802de4f00

		goal = course.addGoal(new GoalDescriptor(module.getId()+"_g1", module.getTitle(), 
				"I intend to participate in the course activities to learn about "+module.getTitle(), ""));
		ListIterator<LessonDescriptor> iterator = module.getLessons();
		while (iterator.hasNext()) {
			goal.addLesson(iterator.next());
		}
		
		return module;
	}

	// ==== Module 3 ====

	public static ModuleDescriptor generateTLAModule3(CourseDescriptor course) {
		ModuleDescriptor module = null;
		LessonDescriptor lesson = null;
		ContentDescriptor content = null;
		GoalDescriptor goal = null;
		int l = 0;
		String contentId;
	
		module = course.addModule(new ModuleDescriptor(course.getId()+"_m3", 
				"Peeling: Learning Analytics Dashboards", 
				"Peeling: Learning Analytics Dashboards", ""));

		lesson = module.addLesson(new LessonDescriptor(module.getId()+"_l"+(l++), 
				"Introduction",
				"Introduction",""));	
	
		contentId = "bc5f974bc18b4456863935e084b752b2/87478d71244e4e769f332165954b1bef/";
		content = lesson.addContent(new ContentDescriptor(contentId, 
				"Introduction", 
				"Introduction", 
				prodCourseBaseURL+contentId)); // "bc5f974bc18b4456863935e084b752b2/87478d71244e4e769f332165954b1bef/")); // ?activate_block_id=block-v1%3AOUNL%2BTLA2019%2B2019_1%2Btype%40sequential%2Bblock%4087478d71244e4e769f332165954b1bef

		lesson = module.addLesson(new LessonDescriptor(module.getId()+"_l"+(l++), 
				"Video: Peeling: Learning Analytics Dashboards",
				"Video: Peeling: Learning Analytics Dashboards",""));	
	
		contentId = "bc5f974bc18b4456863935e084b752b2/3949aecfd5ea4989a2c9fa469f7b231f/";
		content = lesson.addContent(new ContentDescriptor(contentId, // "bc5f974bc18b4456863935e084b752b2/3949aecfd5ea4989a2c9fa469f7b231f/", 
				"Video: Peeling: Learning Analytics Dashboards", 
				"Video: Peeling: Learning Analytics Dashboards", 
				prodCourseBaseURL+contentId)); // "bc5f974bc18b4456863935e084b752b2/3949aecfd5ea4989a2c9fa469f7b231f/")); // ?activate_block_id=block-v1%3AOUNL%2BTLA2019%2B2019_1%2Btype%40sequential%2Bblock%403949aecfd5ea4989a2c9fa469f7b231f

		lesson = module.addLesson(new LessonDescriptor(module.getId()+"_l"+(l++), 
				"Dashboard Design: Connection to Educational Sciences",
				"Dashboard Design: Connection to Educational Sciences",""));	
	
		contentId = "bc5f974bc18b4456863935e084b752b2/aab6363ea44b4c90ae0b9ba2346c1345/";
		content = lesson.addContent(new ContentDescriptor(contentId, // "bc5f974bc18b4456863935e084b752b2/aab6363ea44b4c90ae0b9ba2346c1345/", 
				"Dashboard Design: Connection to Educational Sciences", 
				"Dashboard Design: Connection to Educational Sciences", 
				prodCourseBaseURL+contentId)); // "bc5f974bc18b4456863935e084b752b2/aab6363ea44b4c90ae0b9ba2346c1345/")); // ?activate_block_id=block-v1%3AOUNL%2BTLA2019%2B2019_1%2Btype%40sequential%2Bblock%40aab6363ea44b4c90ae0b9ba2346c1345

		lesson = module.addLesson(new LessonDescriptor(module.getId()+"_l"+(l++), 
				"Dashboard Design: Indicators",
				"Dashboard Design: Indicators",""));	
	
		contentId = "bc5f974bc18b4456863935e084b752b2/2ccb83424f8a446b89b574932108d484/";
		content = lesson.addContent(new ContentDescriptor(contentId, // "bc5f974bc18b4456863935e084b752b2/2ccb83424f8a446b89b574932108d484/", 
				"Dashboard Design: Indicators", 
				"Dashboard Design: Indicators", 
				prodCourseBaseURL+contentId)); // "bc5f974bc18b4456863935e084b752b2/2ccb83424f8a446b89b574932108d484/")); // ?activate_block_id=block-v1%3AOUNL%2BTLA2019%2B2019_1%2Btype%40sequential%2Bblock%402ccb83424f8a446b89b574932108d484

		lesson = module.addLesson(new LessonDescriptor(module.getId()+"_l"+(l++), 
				"Dashboard Design: Visualisations",
				"Dashboard Design: Visualisations",""));	
	
		contentId = "bc5f974bc18b4456863935e084b752b2/4737f8f0593341e798d009eebc260647/";
		content = lesson.addContent(new ContentDescriptor(contentId, // "bc5f974bc18b4456863935e084b752b2/4737f8f0593341e798d009eebc260647/", 
				"Dashboard Design: Visualisations", 
				"Dashboard Design: Visualisations", 
				prodCourseBaseURL+contentId)); // "bc5f974bc18b4456863935e084b752b2/4737f8f0593341e798d009eebc260647/")); // ?activate_block_id=block-v1%3AOUNL%2BTLA2019%2B2019_1%2Btype%40sequential%2Bblock%404737f8f0593341e798d009eebc260647

		lesson = module.addLesson(new LessonDescriptor(module.getId()+"_l"+(l++), 
				"Dashboard Design: Interpreting Indicators and Visualisations",
				"Dashboard Design: Interpreting Indicators and Visualisations",""));	
	
		contentId = "bc5f974bc18b4456863935e084b752b2/bb85d875960148efb74785323f9cc6e9/";
		content = lesson.addContent(new ContentDescriptor(contentId, // "bc5f974bc18b4456863935e084b752b2/bb85d875960148efb74785323f9cc6e9/", 
				"Dashboard Design: Interpreting Indicators and Visualisations", 
				"Dashboard Design: Interpreting Indicators and Visualisations", 
				prodCourseBaseURL+contentId)); // "bc5f974bc18b4456863935e084b752b2/bb85d875960148efb74785323f9cc6e9/")); // ?activate_block_id=block-v1%3AOUNL%2BTLA2019%2B2019_1%2Btype%40sequential%2Bblock%40bb85d875960148efb74785323f9cc6e9

		lesson = module.addLesson(new LessonDescriptor(module.getId()+"_l"+(l++), 
				"Dashboard Evaluation",
				"Dashboard Evaluation",""));	
	
		contentId = "bc5f974bc18b4456863935e084b752b2/194f4cc7ea214af9a6ca58c07965ee0c/";
		content = lesson.addContent(new ContentDescriptor(contentId, // "bc5f974bc18b4456863935e084b752b2/194f4cc7ea214af9a6ca58c07965ee0c/", 
				"Dashboard Evaluation", 
				"Dashboard Evaluation", 
				prodCourseBaseURL+contentId)); // "bc5f974bc18b4456863935e084b752b2/194f4cc7ea214af9a6ca58c07965ee0c/")); // ?activate_block_id=block-v1%3AOUNL%2BTLA2019%2B2019_1%2Btype%40sequential%2Bblock%40194f4cc7ea214af9a6ca58c07965ee0c

		lesson = module.addLesson(new LessonDescriptor(module.getId()+"_l"+(l++), 
				"Recommendations for Designing Dashboards",
				"Recommendations for Designing Dashboards",""));	
	
		contentId = "bc5f974bc18b4456863935e084b752b2/79666db1a4424068b9e98af97aff7d68/";
		content = lesson.addContent(new ContentDescriptor(contentId, // "bc5f974bc18b4456863935e084b752b2/79666db1a4424068b9e98af97aff7d68/", 
				"Recommendations for Designing Dashboards", 
				"Recommendations for Designing Dashboards", 
				prodCourseBaseURL+contentId)); // "bc5f974bc18b4456863935e084b752b2/79666db1a4424068b9e98af97aff7d68/")); // ?activate_block_id=block-v1%3AOUNL%2BTLA2019%2B2019_1%2Btype%40sequential%2Bblock%4079666db1a4424068b9e98af97aff7d68

		lesson = module.addLesson(new LessonDescriptor(module.getId()+"_l"+(l++), 
				"LA Case Studies: Micro Level (learners)",
				"LA Case Studies: Micro Level (learners)",""));	
	
		contentId = "bc5f974bc18b4456863935e084b752b2/f9ee401b868c49059958eda98e35c2fa/";
		content = lesson.addContent(new ContentDescriptor(contentId, // "bc5f974bc18b4456863935e084b752b2/f9ee401b868c49059958eda98e35c2fa/", 
				"LA Case Studies: Micro Level (learners)", 
				"LA Case Studies: Micro Level (learners)", 
				prodCourseBaseURL+contentId)); // "bc5f974bc18b4456863935e084b752b2/f9ee401b868c49059958eda98e35c2fa/")); // ?activate_block_id=block-v1%3AOUNL%2BTLA2019%2B2019_1%2Btype%40sequential%2Bblock%40f9ee401b868c49059958eda98e35c2fa

		lesson = module.addLesson(new LessonDescriptor(module.getId()+"_l"+(l++), 
				"LA Case Studies: Meso Level (instructor/teacher)",
				"LA Case Studies: Meso Level (instructor/teacher)",""));	
	
		contentId = "bc5f974bc18b4456863935e084b752b2/20f3090cb8034ec9bd61ed5bb45438c9/";
		content = lesson.addContent(new ContentDescriptor(contentId, // "bc5f974bc18b4456863935e084b752b2/20f3090cb8034ec9bd61ed5bb45438c9/", 
				"LA Case Studies: Meso Level (instructor/teacher)", 
				"LA Case Studies: Meso Level (instructor/teacher)", 
				prodCourseBaseURL+contentId)); // "bc5f974bc18b4456863935e084b752b2/20f3090cb8034ec9bd61ed5bb45438c9/")); // ?activate_block_id=block-v1%3AOUNL%2BTLA2019%2B2019_1%2Btype%40sequential%2Bblock%4020f3090cb8034ec9bd61ed5bb45438c9

		lesson = module.addLesson(new LessonDescriptor(module.getId()+"_l"+(l++), 
				"LA Case Studies: Macro Level (institution)",
				"LA Case Studies: Macro Level (institution)",""));	
	
		contentId = "bc5f974bc18b4456863935e084b752b2/a310387c423b4be5ba7d665c12902093/";
		content = lesson.addContent(new ContentDescriptor(contentId, // "bc5f974bc18b4456863935e084b752b2/a310387c423b4be5ba7d665c12902093/", 
				"LA Case Studies: Macro Level (institution)", 
				"LA Case Studies: Macro Level (institution)", 
				prodCourseBaseURL+contentId)); // "bc5f974bc18b4456863935e084b752b2/a310387c423b4be5ba7d665c12902093/")); // ?activate_block_id=block-v1%3AOUNL%2BTLA2019%2B2019_1%2Btype%40sequential%2Bblock%40a310387c423b4be5ba7d665c12902093

//		lesson = module.addLesson(new LessonDescriptor(module.getId()+"_l"+(l++), 
//				"References",
//				"References",""));	
//	
//		contentId = "bc5f974bc18b4456863935e084b752b2/e5a7b5ce1bdb4cfb89b834624342bf70/";
//		content = lesson.addContent(new ContentDescriptor(contentId, // "bc5f974bc18b4456863935e084b752b2/e5a7b5ce1bdb4cfb89b834624342bf70/", 
//				"References", 
//				"References", 
//				prodCourseBaseURL+contentId)); // "bc5f974bc18b4456863935e084b752b2/e5a7b5ce1bdb4cfb89b834624342bf70/")); // ?activate_block_id=block-v1%3AOUNL%2BTLA2019%2B2019_1%2Btype%40sequential%2Bblock%40e5a7b5ce1bdb4cfb89b834624342bf70

		lesson = module.addLesson(new LessonDescriptor(module.getId()+"_l"+(l++), 
				"Assignment: Evaluate a Learning Analytics Dashboard",
				"Assignment: Evaluate a Learning Analytics Dashboard",""));	
	
		contentId = "bc5f974bc18b4456863935e084b752b2/867065d01a1045f7920de5da6b55e8bd/";
		content = lesson.addContent(new ContentDescriptor(contentId, // "bc5f974bc18b4456863935e084b752b2/867065d01a1045f7920de5da6b55e8bd/", 
				"Assignment: Evaluate a Learning Analytics Dashboard", 
				"Assignment: Evaluate a Learning Analytics Dashboard", 
				prodCourseBaseURL+contentId)); // "bc5f974bc18b4456863935e084b752b2/867065d01a1045f7920de5da6b55e8bd/")); // ?activate_block_id=block-v1%3AOUNL%2BTLA2019%2B2019_1%2Btype%40sequential%2Bblock%40867065d01a1045f7920de5da6b55e8bd

		goal = course.addGoal(new GoalDescriptor(module.getId()+"_g1", module.getTitle(), 
				"I intend to participate in the course activities to learn about "+module.getTitle(), ""));
		ListIterator<LessonDescriptor> iterator = module.getLessons();
		while (iterator.hasNext()) {
			goal.addLesson(iterator.next());
		}
		
		return module;
	}

	// ==== Module 4 ====

	public static ModuleDescriptor generateTLAModule4(CourseDescriptor course) {
		ModuleDescriptor module = null;
		LessonDescriptor lesson = null;
		ContentDescriptor content = null;
		GoalDescriptor goal = null;
		int l = 0;
		String contentId;
	
		module = course.addModule(new ModuleDescriptor(course.getId()+"_m4", 
				"Shining: Create Your Own Learning Analytics", 
				"Shining: Create Your Own Learning Analytics", ""));

		lesson = module.addLesson(new LessonDescriptor(module.getId()+"_l"+(l++), 
				"Introduction",
				"Introduction",""));	
	
		contentId = "d79feffa81444e429e9490b510dafc61/ffab7cf9e51e473196d175d8a2fbe2dd/";
		content = lesson.addContent(new ContentDescriptor(contentId, // "d79feffa81444e429e9490b510dafc61/ffab7cf9e51e473196d175d8a2fbe2dd/", 
				"Introduction", 
				"Introduction", 
				prodCourseBaseURL+contentId)); // "d79feffa81444e429e9490b510dafc61/ffab7cf9e51e473196d175d8a2fbe2dd/")); // ?activate_block_id=block-v1%3AOUNL%2BTLA2019%2B2019_1%2Btype%40sequential%2Bblock%40ffab7cf9e51e473196d175d8a2fbe2dd

		lesson = module.addLesson(new LessonDescriptor(module.getId()+"_l"+(l++), 
				"Video: Shining: Create Your Own Learning Analytics",
				"Video: Shining: Create Your Own Learning Analytics",""));	
	
		contentId = "d79feffa81444e429e9490b510dafc61/36133668ba2a4e549f08bf6d4a342548/";
		content = lesson.addContent(new ContentDescriptor(contentId, // "d79feffa81444e429e9490b510dafc61/36133668ba2a4e549f08bf6d4a342548/", 
				"Video: Shining: Create Your Own Learning Analytics", 
				"Video: Shining: Create Your Own Learning Analytics", 
				prodCourseBaseURL+contentId)); // "d79feffa81444e429e9490b510dafc61/36133668ba2a4e549f08bf6d4a342548/")); // ?activate_block_id=block-v1%3AOUNL%2BTLA2019%2B2019_1%2Btype%40sequential%2Bblock%4036133668ba2a4e549f08bf6d4a342548

		lesson = module.addLesson(new LessonDescriptor(module.getId()+"_l"+(l++), 
				"Final Assignment: Design Your Own Learning Analytics",
				"Final Assignment: Design Your Own Learning Analytics",""));	
	
		contentId = "d79feffa81444e429e9490b510dafc61/52ccff6cd52f465392885897e28d2d23/";
		content = lesson.addContent(new ContentDescriptor(contentId, // "d79feffa81444e429e9490b510dafc61/52ccff6cd52f465392885897e28d2d23/", 
				"Design Your Own Learning Analytics", 
				"Design Your Own Learning Analytics", 
				prodCourseBaseURL+contentId)); // "d79feffa81444e429e9490b510dafc61/52ccff6cd52f465392885897e28d2d23/")); // ?activate_block_id=block-v1%3AOUNL%2BTLA2019%2B2019_1%2Btype%40sequential%2Bblock%4052ccff6cd52f465392885897e28d2d23
		
		goal = course.addGoal(new GoalDescriptor(module.getId()+"_g1", module.getTitle(), 
				"I intend to participate in the course activities to learn about "+module.getTitle(), ""));
		ListIterator<LessonDescriptor> iterator = module.getLessons();
		while (iterator.hasNext()) {
			goal.addLesson(iterator.next());
		}
		
		return module;
	}
	
}
