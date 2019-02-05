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

public class SP_CircularX_ProductionCourseCreationFactory {

	public static final String prodCourseId = "CircularX";
	public static final String prodCourseBaseURL = "https://courses.edx.org/courses/course-v1:Delftx+CircularX+1T2019/";
	public static final String prodCourseEditURL = "courseware/6a24e1b941504ab88b8f759e749d3304/05614be72c0445dba0a92fc5d6bfb296/1?activate_block_id=block-v1%3ADelftx%2BCircularX%2B1T2019%2Btype%40vertical%2Bblock%40e234c1c179174fcc94a74a20eca1874a";

	private static final Logger log = Logger.getLogger(SP_CircularX_ProductionCourseCreationFactory.class.getName());   

	public SP_CircularX_ProductionCourseCreationFactory() {
		// TODO Auto-generated constructor stub
	}

	
	public static CourseDescriptor generateProductionCourse() {
		log.info("generateProductionCourse: "+prodCourseId);
		
		// Course
			
		CourseDescriptor course = new CourseDescriptor(prodCourseId, 
			"Circular Economy: An Introduction", 
			"Circular Economy: An Introduction", 
			prodCourseBaseURL);

		//
		// Module Introduction
		generateCircularX(course);
		
		
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
			
		log.info("Course: "+course.getId()+", "+course.getTitle());
		for (ModuleDescriptor module: course.getModuleList()) {
			log.info("	Module: "+module.getId()+", "+module.getTitle());
			for (LessonDescriptor lesson: module.getLessonList()) {
				log.info("		Unit: "+lesson.getId()+", "+lesson.getTitle());
				for (ContentDescriptor content: lesson.getContentList()) {
					log.info("			Subunit: "+content.getId()+", "+content.getTitle()+", "+content.getUrl());
					log.info("\n" +
							"				<input type=\"hidden\" value=\""+content.getId()+"\" id=\"contentId\" name=\"contentId\">\n" + 
							"				<input type=\"hidden\" value=\""+content.getTitle()+"\" id=\"contentName\" name=\"contentName\">\n" + 
							"");
				}
			}
		}

		return course;
		
	}

	// ==== Module Introduction ====

	public static void generateCircularX(CourseDescriptor course) {
		ModuleDescriptor module = null;
		LessonDescriptor lesson = null;
		ContentDescriptor content = null;
		int m = -1;
		int l = 1;
		int c = 1;


		l=1;
		module = course.addModule(new ModuleDescriptor(course.getId()+"_m"+(m++), 
				"Section formatting guideline",
				"Section formatting guideline", ""));


		c=1;
		lesson = module.addLesson(new LessonDescriptor(module.getId()+"_l"+(l++), 
				"Guideline in here",
				"Guideline in here",
				""));	
	

		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"Guideline", 
				"Guideline", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@3d9eaf79278f431e9cfd95dc1f390b23"));


		//addGoalToModule(course, module);

		l=1;
		module = course.addModule(new ModuleDescriptor(course.getId()+"_m"+(m++), 
				"Welcome",
				"Welcome", ""));


		c=1;
		lesson = module.addLesson(new LessonDescriptor(module.getId()+"_l"+(l++), 
				"Word of Welcome",
				"Word of Welcome",
				""));	
	

		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"A word of welcome", 
				"A word of welcome", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@2e7fe68f9ff947049105871d0b7222a6"));

		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"What prior users of this course have been saying", 
				"What prior users of this course have been saying", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@39ba870b6caa4ef8a7307b15a8541986"));

		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"The Butterfly Diagram (a recurring theme in the course)", 
				"The Butterfly Diagram (a recurring theme in the course)", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@8415f807289743b7b2752efec7219363"));

		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"The purpose of this module in the course", 
				"The purpose of this module in the course", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@fd63a05eaec44753853dd39f25456f52"));


		c=1;
		lesson = module.addLesson(new LessonDescriptor(module.getId()+"_l"+(l++), 
				"Course structure, syllabus and readings",
				"Course structure, syllabus and readings",
				""));	
	
		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"What you will learn", 
				"What you will learn", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@9796d97893d1461c94ecfa2536daf94a"));

		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"Course structure and syllabus", 
				"Course structure and syllabus", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@1545c295b30341f78eb3aae633934bd2"));

		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"Reading guide", 
				"Reading guide", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@0fd37229dabb4a29a2b7023badd63240"));

		c=1;
		lesson = module.addLesson(new LessonDescriptor(module.getId()+"_l"+(l++), 
				"Tell Us About Yourself",
				"Tell Us About Yourself",
				""));	
	
		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"Introduce yourself", 
				"Introduce yourself", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@0c9e890d4f674d92b7cbc9e186ce4d3d"));

		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"Add yourself to the world map", 
				"Add yourself to the world map", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@2833334e02e54f0cafc343b5c6dd426d"));

		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"Map of learners from prior years", 
				"Map of learners from prior years", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@fec5945f534b4b6ea952aace04f8c841"));


		c=1;
		lesson = module.addLesson(new LessonDescriptor(module.getId()+"_l"+(l++), 
				"Practice quiz",
				"Practice quiz",
				""));	
	
		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"A practice quiz", 
				"A practice quiz", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@0e0825a1f0664f08a184482ad5eb2e5a"));


		c=1;
		lesson = module.addLesson(new LessonDescriptor(module.getId()+"_l"+(l++), 
				"Pre-Survey",
				"Pre-Survey",
				""));	
	
		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"Pre-course survey", 
				"Pre-course survey", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@4386a833738b483d8933a986ba389289"));


		c=1;
		lesson = module.addLesson(new LessonDescriptor(module.getId()+"_l"+(l++), 
				"Overview of online education at IDE TUDelft",
				"Overview of online education at IDE TUDelft",
				""));	
	
		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"Overview of MOOCs at IDE at TUDelft", 
				"Overview of MOOCs at IDE at TUDelft", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@a7bcbe96858e4cacaae089e9999c3617"));

		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"Online Professional Education from IDE", 
				"Online Professional Education from IDE", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@6b9c5398b30040b7994e52c2aabfea28"));



		//addGoalToModule(course, module);

		l=1;
		module = course.addModule(new ModuleDescriptor(course.getId()+"_m"+(m++), 
				"1. What is the Circular Economy?",
				"1. What is the Circular Economy?", ""));


		c=1;
		lesson = module.addLesson(new LessonDescriptor(module.getId()+"_l"+(l++), 
				"1.1 What is the Circular Economy?",
				"1.1 What is the Circular Economy?",
				""));	
	
		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"The Challenge", 
				"The Challenge", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@f3c1e7000dcc4f4389cd04bc310a1410"));

		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"What is the Circular Economy?", 
				"What is the Circular Economy?", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@579e72d6377e41b2a7e63d7d992a2442"));


		c=1;
		lesson = module.addLesson(new LessonDescriptor(module.getId()+"_l"+(l++), 
				"1.2 Principles of the Circular Economy",
				"1.2 Principles of the Circular Economy",
				""));	
	
		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"Principles of the Circular Economy (1/2)", 
				"Principles of the Circular Economy (1/2)", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@97bee04bacd446f88e261ad0420a3b60"));

		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"Principles of the Circular Economy (2/2)", 
				"Principles of the Circular Economy (2/2)", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@fb55047da7134f06b041881dcde44705"));

		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"Share your Thoughts: Great examples", 
				"Share your Thoughts: Great examples", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@4199a6240c3f438684066ede13a3306a"));


		c=1;
		lesson = module.addLesson(new LessonDescriptor(module.getId()+"_l"+(l++), 
				"1.3 Why do we need a Circular Economy?",
				"1.3 Why do we need a Circular Economy?",
				""));	
	
		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"Why we need a Circular Economy (1/3)", 
				"Why we need a Circular Economy (1/3)", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@c9cf16a57173425980aeabb3afd98c04"));

		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"Why we need a Circular Economy (2/3)", 
				"Why we need a Circular Economy (2/3)", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@b1d3ef16aa324b64a398db1b0f6f23d8"));

		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"Why we need a Circular Economy (3/3)", 
				"Why we need a Circular Economy (3/3)", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@bf020211a50c4007938631082a7ceef9"));

		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"Share Your Thoughts: From Linear to Circular: Word pairs", 
				"Share Your Thoughts: From Linear to Circular: Word pairs", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@9174158ad7bc47028fbd030522d8ae15"));


		c=1;
		lesson = module.addLesson(new LessonDescriptor(module.getId()+"_l"+(l++), 
				"1.4 Assignment 1: The Butterfly Diagram",
				"1.4 Assignment 1: The Butterfly Diagram",
				""));	
	
		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"An introduction to the Butterfly Diagram", 
				"An introduction to the Butterfly Diagram", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@fd56861782064bcdacf120a596bc9f90"));

		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"Assignment 1: the Butterfly Diagram", 
				"Assignment 1: the Butterfly Diagram", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@1fc3ac553c104c6c83940936fe042dea"));


		c=1;
		lesson = module.addLesson(new LessonDescriptor(module.getId()+"_l"+(l++), 
				"1.5 Quiz 1: Principles of the Circular Economy",
				"1.5 Quiz 1: Principles of the Circular Economy",
				""));	

		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"Quiz 1: Principles of the Circular Economy", 
				"Quiz 1: Principles of the Circular Economy", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@71bdd3adb62841378ab2fc12c1ac5b1d"));


		c=1;
		lesson = module.addLesson(new LessonDescriptor(module.getId()+"_l"+(l++), 
				"1.6 In-Depth: The roots of the Circular Economy",
				"1.6 In-Depth: The roots of the Circular Economy",
				""));	
	
		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"In-depth introduction", 
				"In-depth introduction", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@4b514b77d81545ce9d401b1d511b2524"));

		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"Industrial Ecology (1/2)", 
				"Industrial Ecology (1/2)", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@dcb6d2c5612147409b28f123cc2a649d"));

		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"Industrial Ecology (2/2)", 
				"Industrial Ecology (2/2)", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@f257e3932496453f8064478251a3a2a8"));

		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"Cradle to Cradle", 
				"Cradle to Cradle", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@3203bbe570e245e9a23169dfd22037ff"));

		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"Biomimicry", 
				"Biomimicry", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@fb4cc86cfa4d48d88571c392def54068"));

		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"Discussion: Schools of thought", 
				"Discussion: Schools of thought", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@b74f6e6c3f4548f99e81c034f08ce259"));



		addGoalToModule(course, module);

		l=1;
		module = course.addModule(new ModuleDescriptor(course.getId()+"_m"+(m++), 
				"2. Business Value in a Circular Economy",
				"2. Business Value in a Circular Economy", ""));

		c=1;
		lesson = module.addLesson(new LessonDescriptor(module.getId()+"_l"+(l++), 
				"2.1 Introduction",
				"2.1 Introduction",
				""));	
	
		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"The fastest growing companies...", 
				"The fastest growing companies...", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@1414329514a240a3b6194f0a0697ca12"));

		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"Introduction and feedback", 
				"Introduction and feedback", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@13240be1c4f243b4a07aa4bb5319ad34"));

		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"Recurring theme: the Butterfly Diagram", 
				"Recurring theme: the Butterfly Diagram", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@008f054d7eec4266b2bce3688810af48"));


		c=1;
		lesson = module.addLesson(new LessonDescriptor(module.getId()+"_l"+(l++), 
				"2.2 Closing Loops",
				"2.2 Closing Loops",
				""));	
	
		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"What does it take to close loops?", 
				"What does it take to close loops?", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@0a4cdcffadea4ebe928ebf7d98722b63"));

		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"Share Your Thoughts: examples of acquisition", 
				"Share Your Thoughts: examples of acquisition", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@761cf3e36c794622a4293cf9e1942e6e"));


		c=1;
		lesson = module.addLesson(new LessonDescriptor(module.getId()+"_l"+(l++), 
				"2.3 Value Creation in a Circular Economy",
				"2.3 Value Creation in a Circular Economy",
				""));	
	
		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"Four types of value creation (1/3)", 
				"Four types of value creation (1/3)", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@de3708cff43a4225a049907831fc6387"));

		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"Four types of value creation (2/3)", 
				"Four types of value creation (2/3)", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@38697bd7481948388203ca0d6bf3e93d"));

		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"Four types of value creation (3/3)", 
				"Four types of value creation (3/3)", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@c0c739f59e5f4f4ea9ca42c6d22373bb"));


		c=1;
		lesson = module.addLesson(new LessonDescriptor(module.getId()+"_l"+(l++), 
				"2.4 Case: Philips Healthcare",
				"2.4 Case: Philips Healthcare",
				""));	
	
		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"The business value of refurbishment (1/3)", 
				"The business value of refurbishment (1/3)", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@0dd6b6d2da2142179fa8cfcf98f012c6"));

		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"The business value of refurbishment  (2/3)", 
				"The business value of refurbishment  (2/3)", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@8656a233ead5411c9a2164618c6de257"));

		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"Quick test: Business processes", 
				"Quick test: Business processes", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@d0c217c50ae841bb9015362a5a921d42"));

		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"The business value of refurbishment  (3/3)", 
				"The business value of refurbishment  (3/3)", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@574efa6f5a7740329af348cfcbe36ea6"));


		c=1;
		lesson = module.addLesson(new LessonDescriptor(module.getId()+"_l"+(l++), 
				"2.5 Business Models for a Circular Economy",
				"2.5 Business Models for a Circular Economy",
				""));	
	
		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"Business models for a circular economy (1/2)", 
				"Business models for a circular economy (1/2)", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@67f9440a15e442d298a18334f5df0373"));

		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"Business models for a circular economy (2/2)", 
				"Business models for a circular economy (2/2)", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@42e0a837eab546d7b0bfd1c43358dcc1"));


		c=1;
		lesson = module.addLesson(new LessonDescriptor(module.getId()+"_l"+(l++), 
				"2.6 Assignment 2: The Case of Riversimple",
				"2.6 Assignment 2: The Case of Riversimple",
				""));	
	
		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"Assignment 2: The case of Riversimple (1/4)", 
				"Assignment 2: The case of Riversimple (1/4)", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@61420a0383e04d448986d60be640b26d"));

		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"Assignment 2: The case of Riversimple (2/4)", 
				"Assignment 2: The case of Riversimple (2/4)", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@4cbd81ba0b6b4b60973d82728a2ab68e"));

		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"Assignment 2: The case of Riversimple (3/4)", 
				"Assignment 2: The case of Riversimple (3/4)", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@09e0141eace846f38779df1672c44f90"));

		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"Assignment 2: The case of Riversimple (4/4)", 
				"Assignment 2: The case of Riversimple (4/4)", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@17fb11be034c4e249879c61fa1dec916"));


		c=1;
		lesson = module.addLesson(new LessonDescriptor(module.getId()+"_l"+(l++), 
				"2.7 Quiz 2: Closing Loops",
				"2.7 Quiz 2: Closing Loops",
				""));	
	
		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"Quiz 2: Closing loops", 
				"Quiz 2: Closing loops", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@78b4a15c75434043adee00c4dee5dcaf"));


		c=1;
		lesson = module.addLesson(new LessonDescriptor(module.getId()+"_l"+(l++), 
				"2.8 A Narrative for the Circular Economy (Part A)",
				"2.8 A Narrative for the Circular Economy (Part A)",
				""));	
	
		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"A Narrative for the Circular Economy (Part A)", 
				"A Narrative for the Circular Economy (Part A)", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@223b1933951c46d984c3ca585d7599fb"));


		c=1;
		lesson = module.addLesson(new LessonDescriptor(module.getId()+"_l"+(l++), 
				"2.9 In-depth: The Darker Side of Access",
				"2.9 In-depth: The Darker Side of Access",
				""));	
	
		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"In-depth introduction", 
				"In-depth introduction", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@072d46c01f1c473e825dfebae0c60b64"));

		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"The darker side of access", 
				"The darker side of access", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@ca7301d0744b4dd687c839b3085ae4ad"));

		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"Share Your thoughts: The darker side of access", 
				"Share Your thoughts: The darker side of access", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@223ff4c49d26458f8ffb796a5b5f5aee"));



		addGoalToModule(course, module);

		l=1;
		module = course.addModule(new ModuleDescriptor(course.getId()+"_m"+(m++), 
				"3. Longer Lasting Products",
				"3. Longer Lasting Products", ""));

		c=1;
		lesson = module.addLesson(new LessonDescriptor(module.getId()+"_l"+(l++), 
				"3.1 Introduction",
				"3.1 Introduction",
				""));	
	
		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"Dave drops new Fairphone", 
				"Dave drops new Fairphone", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@6608dfe2d3204cc184952443e14a3a77"));

		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"Introduction and feedback", 
				"Introduction and feedback", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@9d163ee9f90b4db8a9a16b4af0b0443c"));

		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"Recurring theme: the Butterfly Diagram", 
				"Recurring theme: the Butterfly Diagram", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@3001b9d95b2e457b9a9418733106f9c0"));


		c=1;
		lesson = module.addLesson(new LessonDescriptor(module.getId()+"_l"+(l++), 
				"3.2 Product life extension",
				"3.2 Product life extension",
				""));	
	
		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"Share your thoughts: longer lasting products", 
				"Share your thoughts: longer lasting products", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@b4aacff0aceb4380900d869a0b2afe9e"));

		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"Why product life extension?", 
				"Why product life extension?", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@ba5ee064fb334cf497ae6874d677c873"));

		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"Share your thoughts: the Inertia Principle", 
				"Share your thoughts: the Inertia Principle", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@fd5e863b51ca4572abe218408267cf8d"));

		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"Our thoughts: the Inertia Principle", 
				"Our thoughts: the Inertia Principle", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@85cf79f7ec114d68af71062308e15c98"));


		c=1;
		lesson = module.addLesson(new LessonDescriptor(module.getId()+"_l"+(l++), 
				"3.3 Case: Fairphone",
				"3.3 Case: Fairphone",
				""));	
	
		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"The Fairphone philosophy: product life extension", 
				"The Fairphone philosophy: product life extension", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@0ef0ae9a633249d2a5c8185f528d8d8c"));

		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"Fairphone: going circular", 
				"Fairphone: going circular", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@6903ebcdebaa4481a6bde9aabbf35708"));


		c=1;
		lesson = module.addLesson(new LessonDescriptor(module.getId()+"_l"+(l++), 
				"3.4 Designing longer lasting products",
				"3.4 Designing longer lasting products",
				""));	
	
		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"Designing longer lasting products", 
				"Designing longer lasting products", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@6bb214f928574e97b6012f3eb9fbd02c"));

		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"Six design strategies", 
				"Six design strategies", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@4934522ec7864eee88fe47da21befc57"));

		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"Test Yourself: 6 Design Strategies", 
				"Test Yourself: 6 Design Strategies", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@46acf0abfa274b6c94ec165d7e14ac42"));

		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"Fairphone demonstration", 
				"Fairphone demonstration", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@38473f8df43b4bceb4502e920fad7a65"));


		c=1;
		lesson = module.addLesson(new LessonDescriptor(module.getId()+"_l"+(l++), 
				"3.5 Assignment 3: Repair Criteria",
				"3.5 Assignment 3: Repair Criteria",
				""));	
	
		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"Assignment 3: Repair Criteria (1/2)", 
				"Assignment 3: Repair Criteria (1/2)", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@b5fa83c125cd430eb73af6de20379950"));

		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"Assignment 3: Repair Criteria (2/2)", 
				"Assignment 3: Repair Criteria (2/2)", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@acf517180f254f2eb2fdb1db2f6cf0b4"));


		c=1;
		lesson = module.addLesson(new LessonDescriptor(module.getId()+"_l"+(l++), 
				"3.6 Assignment 4: The Repair Café",
				"3.6 Assignment 4: The Repair Café",
				""));	
	
		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"Assignment 4: Repair cafe", 
				"Assignment 4: Repair cafe", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@8938e8927c4b48df96e70232dd99bfd4"));

		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"Step 1: Find a product to assess (real, or virtual) (1/8)", 
				"Step 1: Find a product to assess (real, or virtual) (1/8)", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@a7f4787429ee4d9aa0033810a136eb41"));

		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"Step 2: Take your product apart (2/8)", 
				"Step 2: Take your product apart (2/8)", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@ad876fddb4a243ca8ce49bcdba77738a"));

		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"Step 3: Put it back together (3/8)", 
				"Step 3: Put it back together (3/8)", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@b9ba6b940dba4324bea11c7c8466a24c"));

		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"Step 4: Determine the repairability score (4/8)", 
				"Step 4: Determine the repairability score (4/8)", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@85cc7b88c35e4548879b06ef9ed6db1b"));

		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"Step 5: Evaluate the product&#39;s repairability (5/8)", 
				"Step 5: Evaluate the product&#39;s repairability (5/8)", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@06a2d02495fe48cdb603d14690c58a49"));

		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"Step 6: Improve the product&#39;s repairability (6/8)", 
				"Step 6: Improve the product&#39;s repairability (6/8)", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@c2d9f844c10f4044824b84619386b8b3"));

		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"Step 7: Prepare to submit your work (7/8)", 
				"Step 7: Prepare to submit your work (7/8)", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@909bb454bd2a458ab492549abf449fd0"));

		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"Step 8: Upload your work (8/8)", 
				"Step 8: Upload your work (8/8)", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@716e51b024b44ad88e2d3b768177fc1c"));


		c=1;
		lesson = module.addLesson(new LessonDescriptor(module.getId()+"_l"+(l++), 
				"3.7 Quiz 3: Longer Lasting Products",
				"3.7 Quiz 3: Longer Lasting Products",
				""));	
	
		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"Quiz 3 Longer lasting products", 
				"Quiz 3 Longer lasting products", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@661d3bc3b2a941ff8d67bba20a0bec98"));


		c=1;
		lesson = module.addLesson(new LessonDescriptor(module.getId()+"_l"+(l++), 
				"3.8 In-depth: Planned Obsolescence",
				"3.8 In-depth: Planned Obsolescence",
				""));	
	
		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"In-depth introduction", 
				"In-depth introduction", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@e09bbee250d6437c81c708ae6b4bb59b"));

		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"Planned obsolescence in a circular economy?", 
				"Planned obsolescence in a circular economy?", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@61040f3e247549c5895dbbc1abfba998"));

		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"Share your thoughts: Planned obsolescence", 
				"Share your thoughts: Planned obsolescence", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@ddee5ffea39b482a98b388022ffb86b0"));



		addGoalToModule(course, module);

		l=1;
		module = course.addModule(new ModuleDescriptor(course.getId()+"_m"+(m++), 
				"4. Remanufacturing",
				"4. Remanufacturing", ""));

		c=1;
		lesson = module.addLesson(new LessonDescriptor(module.getId()+"_l"+(l++), 
				"4.1 Introduction",
				"4.1 Introduction",
				""));	
	
		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"A huge industry, but mostly unknown…", 
				"A huge industry, but mostly unknown…", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@fe7f33cabf4e4bc085de196789b58146"));

		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"Introduction and feedback", 
				"Introduction and feedback", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@6851c75c3c07481bb0eee5c87be27fe6"));

		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"Drawing Repair Criteria", 
				"Drawing Repair Criteria", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@27f5cd976a83414f972c78aabf41c2e5"));

		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"Recurring theme: the Butterfly Diagram", 
				"Recurring theme: the Butterfly Diagram", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@a52635b77e3246848f659b543828e763"));


		c=1;
		lesson = module.addLesson(new LessonDescriptor(module.getId()+"_l"+(l++), 
				"4.2 Business value of remanufacturing",
				"4.2 Business value of remanufacturing",
				""));	
	
		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"What is remanufacturing? (1/2)", 
				"What is remanufacturing? (1/2)", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@7282ece7338a42c8a093bc22d48e0da1"));

		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"What is remanufacturing? (2/2)", 
				"What is remanufacturing? (2/2)", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@76a7072614714173a12f05c7d8274bb8"));

		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"The business value of remanufacturing", 
				"The business value of remanufacturing", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@90c261096f2b4cd7be17a57e0d8fa5c9"));

		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"Remanufacturing: an untapped opportunity", 
				"Remanufacturing: an untapped opportunity", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@896b8cc639294e8ba0a79826acf5aeb4"));

		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"Share your Thoughts: Remanufacturing examples", 
				"Share your Thoughts: Remanufacturing examples", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@1eecfa023af9401e9449c0ac09445857"));


		c=1;
		lesson = module.addLesson(new LessonDescriptor(module.getId()+"_l"+(l++), 
				"4.3 Design for remanufacturing",
				"4.3 Design for remanufacturing",
				""));	
	
		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"Design for remanufacturing pays off", 
				"Design for remanufacturing pays off", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@c100329a36ea4c92a7ea0b37b4a76d47"));

		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"Industry perspective: Meritor", 
				"Industry perspective: Meritor", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@7b190886c05d4022a1f734e8a6aff061"));

		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"Industry perspective: ACtronics", 
				"Industry perspective: ACtronics", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@87d415fa1bd34292a188762d76101540"));


		c=1;
		lesson = module.addLesson(new LessonDescriptor(module.getId()+"_l"+(l++), 
				"4.4 Challenges for remanufacturing",
				"4.4 Challenges for remanufacturing",
				""));	
	
		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"Challenges to remanufacturing", 
				"Challenges to remanufacturing", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@ccfe7b3baf264777a48f502437645d85"));


		c=1;
		lesson = module.addLesson(new LessonDescriptor(module.getId()+"_l"+(l++), 
				"4.5 Assignment 5: The case of Caterpillar",
				"4.5 Assignment 5: The case of Caterpillar",
				""));	
	
		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"Assignment 5: The case of Caterpillar", 
				"Assignment 5: The case of Caterpillar", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@d6c132102e8f44fd8a010896149bd93d"));

		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"The case of Caterpillar (Cat Reman)", 
				"The case of Caterpillar (Cat Reman)", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@ad29a29e09654baca1abd952740186d2"));


		c=1;
		lesson = module.addLesson(new LessonDescriptor(module.getId()+"_l"+(l++), 
				"4.6 Quiz 4: Remanufacturing",
				"4.6 Quiz 4: Remanufacturing",
				""));	
	
		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"Quiz 4: Remanufacturing", 
				"Quiz 4: Remanufacturing", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@b16a9947857d45bd997cf4b767cbfe9e"));


		c=1;
		lesson = module.addLesson(new LessonDescriptor(module.getId()+"_l"+(l++), 
				"4.7 A Narrative for the circular economy (Part B)",
				"4.7 A Narrative for the circular economy (Part B)",
				""));	
	
		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"A narrative for the Circular Economy (Part B)", 
				"A narrative for the Circular Economy (Part B)", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@a16887c72363438e924d3332475bcadd"));


		c=1;
		lesson = module.addLesson(new LessonDescriptor(module.getId()+"_l"+(l++), 
				"4.8 In-depth: Markets for Remanufacturing",
				"4.8 In-depth: Markets for Remanufacturing",
				""));	
	
		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"In-depth introduction", 
				"In-depth introduction", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@2d179dabb2494edd810f864d7d9829ea"));

		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"Share Your Thoughts: Markets for Remanufacturing", 
				"Share Your Thoughts: Markets for Remanufacturing", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@f66c938b3d44430fa9876297838dd581"));



		addGoalToModule(course, module);

		l=1;
		module = course.addModule(new ModuleDescriptor(course.getId()+"_m"+(m++), 
				"5. Waste = Food",
				"5. Waste = Food", ""));

		c=1;
		lesson = module.addLesson(new LessonDescriptor(module.getId()+"_l"+(l++), 
				"5.1 Introduction",
				"5.1 Introduction",
				""));	
	
		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"Recycling refrigerators", 
				"Recycling refrigerators", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@7ff25f01123945a996f52ac239c1af9c"));

		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"Introduction and feedback", 
				"Introduction and feedback", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@aca1695f4e6548fcafa81acc32c91e2f"));

		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"Recurring theme: The Butterfly Diagram", 
				"Recurring theme: The Butterfly Diagram", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@03de857ece934124a9969cba406c76e5"));


		c=1;
		lesson = module.addLesson(new LessonDescriptor(module.getId()+"_l"+(l++), 
				"5.2 Recycling",
				"5.2 Recycling",
				""));	
	
		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"Recycling case study: Coolrec", 
				"Recycling case study: Coolrec", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@dbb917c720994decbe86cb3ae2a62158"));

		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"Description of the recycling process", 
				"Description of the recycling process", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@02490dad10ef4c6393e8ec1f1f3e89fa"));

		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"Share your Thoughts: Examples of recycling", 
				"Share your Thoughts: Examples of recycling", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@2268729d28c943c6879c75cb8a879252"));


		c=1;
		lesson = module.addLesson(new LessonDescriptor(module.getId()+"_l"+(l++), 
				"5.3 Case: Circular Textiles",
				"5.3 Case: Circular Textiles",
				""));	
	
		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"Circular Textiles: Introduction", 
				"Circular Textiles: Introduction", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@1a8ab220de934f978cef9830a119de87"));

		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"The World&#39;s second most polluting Industry", 
				"The World&#39;s second most polluting Industry", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@28d664e94f754f2f922bbef33c948221"));

		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"Textile-to-textile recycling", 
				"Textile-to-textile recycling", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@698b4504d75f4ceeb073b529e7cd27b4"));

		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"Barriers and opportunities", 
				"Barriers and opportunities", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@211aef673f9b474d9b79d89fe03a5268"));

		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"Business models and fashion design", 
				"Business models and fashion design", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@0144e963ccd74084aab1af6279142c68"));

		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"Quick test: The textiles and fashion industry", 
				"Quick test: The textiles and fashion industry", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@e16165b7cff04548ad33d04ff1b64694"));


		c=1;
		lesson = module.addLesson(new LessonDescriptor(module.getId()+"_l"+(l++), 
				"5.4 Nature Inspired Design",
				"5.4 Nature Inspired Design",
				""));	
	
		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"Nature-inspired Design: Entropy", 
				"Nature-inspired Design: Entropy", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@7965eacf5ee44ea182694912953132d5"));

		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"Architecture inspired by Nature (1/2)", 
				"Architecture inspired by Nature (1/2)", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@92f281388b1242009c4ea3d0ae9f5c79"));

		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"Architecture inspired by Nature (2/2)", 
				"Architecture inspired by Nature (2/2)", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@e5657cc7a72a4a82aa1c3be4f05966b0"));

		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"Casestudy: Interface Mission Zero", 
				"Casestudy: Interface Mission Zero", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@e806ee69e1a64abbbf6f9cae8675db3f"));


		c=1;
		lesson = module.addLesson(new LessonDescriptor(module.getId()+"_l"+(l++), 
				"5.5 Assignment 6: Nature Inspired Design",
				"5.5 Assignment 6: Nature Inspired Design",
				""));	
	
		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"Assignment 6: Nature-Inspired Design", 
				"Assignment 6: Nature-Inspired Design", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@8a679dfeaba74b2aa48a9842c68bb158"));


		c=1;
		lesson = module.addLesson(new LessonDescriptor(module.getId()+"_l"+(l++), 
				"5.6 Quiz 5: Waste equals Food",
				"5.6 Quiz 5: Waste equals Food",
				""));	
	
		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"Quiz 5: Waste equals food", 
				"Quiz 5: Waste equals food", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@cb5cfc9770aa4f20806b2812a0bdf6f0"));


		c=1;
		lesson = module.addLesson(new LessonDescriptor(module.getId()+"_l"+(l++), 
				"5.7 In-depth: A good disruption",
				"5.7 In-depth: A good disruption",
				""));	
	
		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"In-depth introduction", 
				"In-depth introduction", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@9fe33495bd11403780d106880229fbc3"));

		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"Share Your Thoughts: A good disruption", 
				"Share Your Thoughts: A good disruption", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@9a14108e6dd44acc8f46190e664127ae"));



		addGoalToModule(course, module);

		l=1;
		module = course.addModule(new ModuleDescriptor(course.getId()+"_m"+(m++), 
				"6. Thinking in Systems",
				"6. Thinking in Systems", ""));

		c=1;
		lesson = module.addLesson(new LessonDescriptor(module.getId()+"_l"+(l++), 
				"6.1 Introduction",
				"6.1 Introduction",
				""));	
	
		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"Introduction and feedback", 
				"Introduction and feedback", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@4efba729c03c49f6b69cfeca6022cfb5"));

		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"Drawing Nature Inspired Design", 
				"Drawing Nature Inspired Design", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@5ec021a571bf4781867557b7b52835ed"));

		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"Recurring theme: The Butterfly Diagram", 
				"Recurring theme: The Butterfly Diagram", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@752f098b71114fb290097e45cfb44ea1"));


		c=1;
		lesson = module.addLesson(new LessonDescriptor(module.getId()+"_l"+(l++), 
				"6.2 A Global Perspective",
				"6.2 A Global Perspective",
				""));	
	
		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"Taking the Circular Economy to the next level", 
				"Taking the Circular Economy to the next level", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@f9b5b19cc537433f8f6d7b4c3773a209"));


		c=1;
		lesson = module.addLesson(new LessonDescriptor(module.getId()+"_l"+(l++), 
				"6.3 A Circular Economy of Metals",
				"6.3 A Circular Economy of Metals",
				""));	
	
		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"Will a Circular Economy prevent us from running out of metals?", 
				"Will a Circular Economy prevent us from running out of metals?", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@64c9113748ef484ea9f13635de1e0fea"));

		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"Will a Circular Economy decrease our environmental impact?", 
				"Will a Circular Economy decrease our environmental impact?", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@734b162b43724de49e52f4b6a412b4e2"));

		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"Quick test: A Circular Economy of Metals", 
				"Quick test: A Circular Economy of Metals", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@769d62cd899c4da19a5dbffc2daa99d7"));


		c=1;
		lesson = module.addLesson(new LessonDescriptor(module.getId()+"_l"+(l++), 
				"6.4 The Urban Mine",
				"6.4 The Urban Mine",
				""));	
	
		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"Exploring the urban mine", 
				"Exploring the urban mine", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@4e0a48d34f224e07a2d6a695405abe58"));

		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"Urban mining for a Circular Economy", 
				"Urban mining for a Circular Economy", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@58c83fc28ef54540b185f4a4c79cafb0"));

		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"Stocks and flows", 
				"Stocks and flows", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@f125bb8e1eaf4360a1ed3221ed4af16a"));


		c=1;
		lesson = module.addLesson(new LessonDescriptor(module.getId()+"_l"+(l++), 
				"6.5 How Long Will it Take?",
				"6.5 How Long Will it Take?",
				""));	
	
		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"Conditions for a Circular Economy", 
				"Conditions for a Circular Economy", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@a1d64ac3f59c470c8b5ca8209fc27945"));

		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"A time horizon for the Circular Economy", 
				"A time horizon for the Circular Economy", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@4945e83503904c918d6e353e96dc8a53"));

		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"A Circular Economy agenda", 
				"A Circular Economy agenda", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@1ad7704f747345f88565c5730c202f63"));

		c=1;
		lesson = module.addLesson(new LessonDescriptor(module.getId()+"_l"+(l++), 
				"6.6 Quiz 6: Thinking in Systems",
				"6.6 Quiz 6: Thinking in Systems",
				""));	
	
		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"Quiz 6: Thinking in systems", 
				"Quiz 6: Thinking in systems", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@4e7940104d964d1690b44d979a76b2c5"));


		c=1;
		lesson = module.addLesson(new LessonDescriptor(module.getId()+"_l"+(l++), 
				"6.7 A Narrative For the Circular Economy (Part C)",
				"6.7 A Narrative For the Circular Economy (Part C)",
				""));	
	
		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"A Narrative for the Circular Economy (Part C)", 
				"A Narrative for the Circular Economy (Part C)", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@5b2618631adb4175ac620ec61a3b5c6b"));


		c=1;
		lesson = module.addLesson(new LessonDescriptor(module.getId()+"_l"+(l++), 
				"6.8 In-depth: Critical Materials",
				"6.8 In-depth: Critical Materials",
				""));	
	
		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"In-depth introduction", 
				"In-depth introduction", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@b0bdf284058546329083035bc44e5114"));

		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"What are critical materials?", 
				"What are critical materials?", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@a1e78a5d67dc4a0db9c0a5bfa1f30371"));

		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"Solving the critical materials problem", 
				"Solving the critical materials problem", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@9673ac8ce34b4558a76f79a85fea5aa0"));

		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"Share your thoughts: Solving the critical materials problem", 
				"Share your thoughts: Solving the critical materials problem", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@9cb087d623384566a461a09392a40a51"));


		addGoalToModule(course, module);

		l=1;
		module = course.addModule(new ModuleDescriptor(course.getId()+"_m"+(m++), 
				"7. Full Circle",
				"7. Full Circle", ""));

		c=1;
		lesson = module.addLesson(new LessonDescriptor(module.getId()+"_l"+(l++), 
				"7.1 Narratives for a Circular Economy",
				"7.1 Narratives for a Circular Economy",
				""));	
	
		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"Introduction and feedback", 
				"Introduction and feedback", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@0bf20d8e008e4a07bf577c69597b1244"));

		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"A narrative for the Circular Economy: introduction", 
				"A narrative for the Circular Economy: introduction", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@33dbba2126ba4377be6f8edbd094397d"));

		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"A Narrative for the Circular Economy, looking at your narratives", 
				"A Narrative for the Circular Economy, looking at your narratives", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@432b41ada48941e08238f02878992980"));

		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"Drawing the Narratives for a Circular Economy", 
				"Drawing the Narratives for a Circular Economy", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@c3ba50fc7ec94c7e8c237c297e26a2be"));


		c=1;
		lesson = module.addLesson(new LessonDescriptor(module.getId()+"_l"+(l++), 
				"7.2 Final Exam",
				"7.2 Final Exam",
				""));	
	
		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"Final Exam", 
				"Final Exam", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@28448a19ec864b4bb518c34bbbf763b1"));


		c=1;
		lesson = module.addLesson(new LessonDescriptor(module.getId()+"_l"+(l++), 
				"7.3 Share Your Thoughts",
				"7.3 Share Your Thoughts",
				""));	
	
		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"Share Your Thoughts", 
				"Share Your Thoughts", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@86cef121a351459da6fab30519a33b67"));


		c=1;
		lesson = module.addLesson(new LessonDescriptor(module.getId()+"_l"+(l++), 
				"7.4 Eager for more courses related to the Circular Economy?",
				"7.4 Eager for more courses related to the Circular Economy?",
				""));	
	
		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"Further learning about the Circular Economy", 
				"Further learning about the Circular Economy", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@ec72d47d7e7d4759b30c1f3027424819"));


		c=1;
		lesson = module.addLesson(new LessonDescriptor(module.getId()+"_l"+(l++), 
				"How was your course experience?",
				"How was your course experience?",
				""));	
	
		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"Post-course survey", 
				"Post-course survey", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@fe3b612ab5e64971a930c90a34ea1600"));



		addGoalToModule(course, module);

		l=1;
		module = course.addModule(new ModuleDescriptor(course.getId()+"_m"+(m++), 
				"What is coming up next?",
				"What is coming up next?", ""));

		c=1;
		lesson = module.addLesson(new LessonDescriptor(module.getId()+"_l"+(l++), 
				"What is coming up next?",
				"What is coming up next?",
				""));	
	
		content = lesson.addContent(new ContentDescriptor(lesson.getId()+"_c"+(c++), 
				"The next steps in this course", 
				"The next steps in this course", 
				prodCourseBaseURL+"jump_to/"+"block-v1:Delftx+CircularX+1T2019+type@vertical+block@c4f8ff3c4d2344a9ad29be5cd6bb41ba"));

	
		//addGoalToModule(course, module);

	}

	public static void addGoalToModule(CourseDescriptor course, ModuleDescriptor module) {
		GoalDescriptor goal = null;
		
		goal = course.addGoal(new GoalDescriptor(module.getId()+"_g1", module.getTitle(), 
				"I intend to participate in the course activities to learn about "+module.getTitle(), ""));
		ListIterator<LessonDescriptor> iterator = module.getLessons();
		while (iterator.hasNext()) {
			goal.addLesson(iterator.next());
		}
	}
}
