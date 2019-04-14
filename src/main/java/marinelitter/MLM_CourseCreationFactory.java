package marinelitter;

import java.util.Date;
import java.util.ListIterator;
import java.util.logging.Logger;

import senseofcommunity.SoC_ProductionCourseCreationFactory;
import stimulatedplanning.ContentDescriptor;
import stimulatedplanning.CourseDescriptor;
import stimulatedplanning.GoalDescriptor;
import stimulatedplanning.LessonDescriptor;
import stimulatedplanning.ModuleDescriptor;
import stimulatedplanning.StimulatedPlanningFactory;

public class MLM_CourseCreationFactory {

	public static final String prodCourseId = "MLMOOC18";
	public static final String prodCourseBaseURL = "https://ou.edia.nl/courses/course-v1:OUNL+MLMOOC18+2019_01/courseware/";

	private static final Logger log = Logger.getLogger(MLM_CourseCreationFactory.class.getName());   

	public MLM_CourseCreationFactory() {
		// TODO Auto-generated constructor stub
	}


	public static CourseDescriptor generateProductionCourse() {
		log.info("generateProductionCourse: "+prodCourseId);
		
		// Course
			
		CourseDescriptor course = new CourseDescriptor(prodCourseId, 
			"Marine Litter Mooc", 
			"Marine Litter Mooc", 
			prodCourseBaseURL);

			
		return course;
	}

	
	
}
