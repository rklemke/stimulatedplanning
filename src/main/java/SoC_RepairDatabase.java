

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.CompositeFilter;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query.SortDirection;

import senseofcommunity.Clan;
import senseofcommunity.InformationObject;
import senseofcommunity.SelectionObject;
import senseofcommunity.SelectionOption;
import senseofcommunity.SoC_ProductionCourseCreationFactory;
import senseofcommunity.UserOnlineStatus;
import senseofcommunity.UserSelectedOption;
import stimulatedplanning.ContentDescriptor;
import stimulatedplanning.CourseDescriptor;
import stimulatedplanning.GenericDescriptor;
import stimulatedplanning.GoalDescriptor;
import stimulatedplanning.LessonDescriptor;
import stimulatedplanning.ModuleDescriptor;
import stimulatedplanning.PersistentStore;
import stimulatedplanning.PlanItem;
import stimulatedplanning.StimulatedPlanningFactory;
import stimulatedplanning.UserContent;
import stimulatedplanning.UserGoal;
import stimulatedplanning.UserLesson;
import stimulatedplanning.UserPlan;
import stimulatedplanning.UserProfile;

/**
 * Servlet implementation class ExportEntitiesToCSV
 */
@WebServlet("/SoC_RepairDatabase")
public class SoC_RepairDatabase extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private static final Logger log = Logger.getLogger(SoC_RepairDatabase.class.getName());   

	private ArrayList<Object> cleanUpList;
	private ArrayList<Entity> updateList;
	private ArrayList<GenericDescriptor> newObjectList;
	private String report = "";
	private boolean isReporting = false;
	private boolean isCleanup = false;
	private DatastoreService datastore;
	

       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SoC_RepairDatabase() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    private void addToCleanup(GenericDescriptor existingDescriptor, GenericDescriptor newDescriptor) {
    	if (existingDescriptor != null && newDescriptor != null && !existingDescriptor.getId().equals(newDescriptor.getId())) {
    		cleanUpList.add(existingDescriptor);
    	}
    }

    private void checkAndRepairSelectionOption(SelectionOption existingOption, SelectionOption newOption, SelectionObject existingSele, SelectionObject newSele) {
		if (existingOption == null) {
			if (isReporting) report += "<font color='red'>SoC_RepairDatabase.checkAndRepairSelectionOption: selectionOption retrieval error: existingOption is null."+"</font><BR>\n";
		} else if (!newOption.getTitle().equals(existingOption.getTitle())) {
			if (isReporting) report += "<font color='red'>SoC_RepairDatabase.checkAndRepairSelectionOption: selectionOption title mismatch: existingInfo: "+existingOption.getId()+", "+existingOption.getTitle()+", newSele: "+newOption.getId()+", "+newOption.getTitle()+"</font><BR>\n";
		} else {
			if (isReporting) report += "SoC_RepairDatabase.checkAndRepairSelectionOption: matching selection options: existing: "+existingOption.getId()+", "+existingOption.getTitle()+", new lesson: "+newOption.getId()+", "+newOption.getTitle()+"<BR>\n";
			
			Filter newSelectionObjectFilter = new FilterPredicate("selectionObject", FilterOperator.EQUAL, newSele.getId());
			Filter newSelectionOptionFilter = new FilterPredicate("selectedOption", FilterOperator.EQUAL, newOption.getId());
			CompositeFilter newSeleOptionFilter = CompositeFilterOperator.and(newSelectionObjectFilter, newSelectionOptionFilter);
			
			Query q1 = new Query(UserSelectedOption.class.getName()).setFilter(newSeleOptionFilter);
			PreparedQuery pq1 = datastore.prepare(q1);

			if (isReporting) report += "SoC_RepairDatabase.checkAndRepairSelectionOption: querying new user selected options.<BR>\n";
			// NEW OPTIONS
			for (Entity entity1 : pq1.asIterable()) {
				if (isReporting) report += "SoC_RepairDatabase.checkAndRepairSelectionOption: querying new user selected option:"+entity1.getProperty("uid")+"<BR>\n";
			} // NEW OPTIONS
			
			Filter existingSelectionObjectFilter = new FilterPredicate("selectionObject", FilterOperator.EQUAL, existingSele.getId());
			Filter existingSelectionOptionFilter = new FilterPredicate("selectedOption", FilterOperator.EQUAL, existingOption.getId());
			CompositeFilter existingSeleOptionFilter = CompositeFilterOperator.and(existingSelectionObjectFilter, existingSelectionOptionFilter);
			
			Query q2 = new Query(UserSelectedOption.class.getName()).setFilter(existingSeleOptionFilter);
			PreparedQuery pq2 = datastore.prepare(q2);

			if (isReporting) report += "SoC_RepairDatabase.checkAndRepairSelectionOption: querying existing user selected options.<BR>\n";
			// NEW OPTIONS
			for (Entity entity2 : pq2.asIterable()) {
			if (isReporting) report += "<font color='green'>SoC_RepairDatabase.checkAndRepairSelectionOption: querying existing user selected option:"+entity2.getProperty("uid")+", "+entity2.getProperty("selectionObject")+", "+entity2.getProperty("selectedOption")+" --> "+newSele.getId()+", "+newOption.getId()+"</font><BR>\n";
				String objId = (String)entity2.getProperty("selectionObject");
				String optId = (String)entity2.getProperty("selectedOption");
				if (newSele.getId().equals(objId) && newOption.getId().equals(optId)) {
					if (isReporting) report += "<font color='green'>SoC_RepairDatabase.checkAndRepairSelectionOption: already up to date</font><BR>\n";
				} else {
					if (isReporting) report += "<font color='orange'>SoC_RepairDatabase.checkAndRepairSelectionOption: updating:"+entity2.getProperty("uid")+", "+entity2.getProperty("selectionObject")+", "+entity2.getProperty("selectedOption")+" --> "+newSele.getId()+", "+newOption.getId()+"</font><BR>\n";
					entity2.setProperty("selectionObject", newSele.getId());
					entity2.setProperty("selectedOption", newOption.getId());
					updateList.add(entity2);
				}
			} // NEW OPTIONS
			
			addToCleanup(existingOption, newOption);
		}
    }
    
    private void checkAndRepairSelectionObject(SelectionObject existingSele, SelectionObject newSele) {
		if (existingSele == null) {
			if (isReporting) report += "<font color='red'>SoC_RepairDatabase.checkAndRepairSelectionObject: selectionObject retrieval error: existingSele is null."+"</font><BR>\n";
		} else if (!newSele.getTitle().equals(existingSele.getTitle())) {
			if (isReporting) report += "<font color='red'>SoC_RepairDatabase.checkAndRepairSelectionObject: selectionObject title mismatch: existingInfo: "+existingSele.getId()+", "+existingSele.getTitle()+", newSele: "+newSele.getId()+", "+newSele.getTitle()+"</font><BR>\n";
		} else {
			if (isReporting) report += "SoC_RepairDatabase.checkAndRepairSelectionObject: matching selection objects: existing: "+existingSele.getId()+", "+existingSele.getTitle()+", new lesson: "+newSele.getId()+", "+newSele.getTitle()+"<BR>\n";
			
			int sopLength = newSele.getOptionList().size();
			if (sopLength != existingSele.getOptionList().size()) {
				if (isReporting) report += "<font color='red'>SoC_RepairDatabase.checkAndRepairSelectionObject: SelectionOption size mismatch warning: new: "+newSele.getOptionList().size()+", existing: "+existingSele.getOptionList().size()+"</font><BR>\n";
				sopLength = Math.min(sopLength, existingSele.getOptionList().size());
			}
			if (isReporting) report += "SoC_RepairDatabase.checkAndRepairSelectionObject: iterating selectionOptions: "+sopLength+"<BR>\n";

			for (SelectionOption newOption: newSele.getOptionList()) {
				if (isReporting) report += "SoC_RepairDatabase.checkAndRepairSelectionObject: list new selectionOptions: "+newOption.getId()+", "+newOption.getTitle()+"<BR>\n";
			}
			// SELECTION OBJECTS
			for (int so=0; so<sopLength; so++) {
				SelectionOption newOption = newSele.getOptionList().get(so);
				SelectionOption existingOption = existingSele.getOptionList().get(so);
				
				checkAndRepairSelectionOption(existingOption, newOption, existingSele, newSele);
				
				
			} // SELECTION OBJECTS

			addToCleanup(existingSele, newSele);
		}
		
   	
    }
    
    
    private void checkAndRepairInformationObject(InformationObject existingInfo, InformationObject newInfo) {
		if (existingInfo == null) {
			if (isReporting) report += "<font color='red'>SoC_RepairDatabase.checkAndRepairInformationObject: informationObject retrieval error: existingInfo is null."+"</font><BR>\n";
		} else if (!newInfo.getTitle().equals(existingInfo.getTitle())) {
			if (isReporting) report += "<font color='red'>SoC_RepairDatabase.checkAndRepairInformationObject: informationObject title mismatch: existingInfo: "+existingInfo.getId()+", "+existingInfo.getTitle()+", newInfo: "+newInfo.getId()+", "+newInfo.getTitle()+"</font><BR>\n";
		} else {
			if (isReporting) report += "SoC_RepairDatabase.checkAndRepairInformationObject: matching info objects: existing: "+existingInfo.getId()+", "+existingInfo.getTitle()+", new lesson: "+newInfo.getId()+", "+newInfo.getTitle()+"<BR>\n";
			addToCleanup(existingInfo, newInfo);
		}
		
    }
    
    
    private void checkAndRepairContent(ContentDescriptor existingContent, ContentDescriptor newContent) {
		if (existingContent == null) {
			if (isReporting) report += "<font color='red'>SoC_RepairDatabase.checkAndRepairContent: content retrieval error: existingContent is null."+"</font><BR>\n";
		} else if (!newContent.getTitle().equals(existingContent.getTitle())) {
			if (isReporting) report += "<font color='red'>SoC_RepairDatabase.checkAndRepairContent: content title mismatch: existingContent: "+existingContent.getId()+", "+existingContent.getTitle()+", newContent: "+newContent.getId()+", "+newContent.getTitle()+"</font><BR>\n";
		} else {
			if (isReporting) report += "SoC_RepairDatabase.checkAndRepairContent: iterating contents: existing: "+existingContent.getId()+", "+existingContent.getTitle()+", new lesson: "+newContent.getId()+", "+newContent.getTitle()+"<BR>\n";
			
			int iLength = newContent.getInformationObjectList().size();
			if (iLength != existingContent.getInformationObjectList().size()) {
				if (isReporting) report += "<font color='red'>SoC_RepairDatabase.checkAndRepairContent: InformationObject size mismatch warning: new: "+newContent.getInformationObjectList().size()+", existing: "+existingContent.getInformationObjectList().size()+"</font><BR>\n";
				iLength = Math.min(iLength, existingContent.getInformationObjectList().size());
			}
			if (isReporting) report += "SoC_RepairDatabase.checkAndRepairContent: iterating informationObjects: "+iLength+"<BR>\n";

			for (InformationObject newInfo: newContent.getInformationObjectList()) {
				if (isReporting) report += "SoC_RepairDatabase.checkAndRepairContent: list new informationObjects: "+newInfo.getId()+", "+newInfo.getTitle()+"<BR>\n";
			}
			// INFORMATION OBJECTS
			for (int io=0; io<iLength; io++) {
				InformationObject newInfo = newContent.getInformationObjectList().get(io);
				InformationObject existingInfo = existingContent.getInformationObjectList().get(io);
				
				checkAndRepairInformationObject(existingInfo, newInfo);
				
			} // INFORMATION OBJECTS
			
			int sLength = newContent.getSelectionObjectList().size();
			if (sLength != existingContent.getSelectionObjectList().size()) {
				if (isReporting) report += "<font color='red'>SoC_RepairDatabase.checkAndRepairContent: InformationObject size mismatch warning: new: "+newContent.getSelectionObjectList().size()+", existing: "+existingContent.getSelectionObjectList().size()+"</font><BR>\n";
				sLength = Math.min(sLength, existingContent.getSelectionObjectList().size());
			}
			if (isReporting) report += "SoC_RepairDatabase.checkAndRepairContent: iterating selectionObjects: "+sLength+"<BR>\n";

			for (SelectionObject newSele: newContent.getSelectionObjectList()) {
				if (isReporting) report += "SoC_RepairDatabase.checkAndRepairContent: list new selectionObjects: "+newSele.getId()+", "+newSele.getTitle()+"<BR>\n";
			}
			// SELECTION OBJECTS
			for (int so=0; so<sLength; so++) {
				SelectionObject newSele = newContent.getSelectionObjectList().get(so);
				SelectionObject existingSele = existingContent.getSelectionObjectList().get(so);
				
				checkAndRepairSelectionObject(existingSele, newSele);
				
				
			} // SELECTION OBJECTS
			addToCleanup(existingContent, newContent);
		}
		
    }
    
    private void checkAndRepairLesson(LessonDescriptor existingLesson, LessonDescriptor newLesson) {
		if(existingLesson == null) {
			if (isReporting) report += "<font color='red'>SoC_RepairDatabase.checkAndRepairLesson: lesson retrieval error: existingLesson is null."+"</font><BR>\n";
		} else if (!(newLesson.getTitle().equals(existingLesson.getTitle()))) {
			if (isReporting) report += "<font color='red'>SoC_RepairDatabase.checkAndRepairLesson: lesson title mismatch: existingLesson: "+existingLesson.getId()+", "+existingLesson.getTitle()+", newLesson: "+newLesson.getId()+", "+newLesson.getTitle()+"</font><BR>\n";
		} else {
			if (isReporting) report += "SoC_RepairDatabase.checkAndRepairLesson: iterating lessons: existing: "+existingLesson.getId()+", "+existingLesson.getTitle()+", new lesson: "+newLesson.getId()+", "+newLesson.getTitle()+"<BR>\n";
			int cLength = newLesson.getContentList().size();
			if (cLength != existingLesson.getContentList().size()) {
				if (isReporting) report += "<font color='red'>SoC_RepairDatabase.checkAndRepairLesson: content size mismatch warning: new: "+newLesson.getContentList().size()+", existing: "+existingLesson.getContentList().size()+"</font><BR>\n";
				cLength = Math.min(cLength, existingLesson.getContentList().size());
			}
			if (isReporting) report += "SoC_RepairDatabase.checkAndRepairLesson: iterating contents: "+cLength+"<BR>\n";

			for (ContentDescriptor newContent: newLesson.getContentList()) {
				if (isReporting) report += "SoC_RepairDatabase.checkAndRepairLesson: list new contents: "+newContent.getId()+", "+newContent.getTitle()+"<BR>\n";
			}
			// CONTENTS
			for (int c=0; c<cLength; c++) {
				ContentDescriptor newContent = newLesson.getContentList().get(c);
				ContentDescriptor existingContent = existingLesson.getContentList().get(c);
				
				checkAndRepairContent(existingContent, newContent);
				
			} // CONTENST
			
			addToCleanup(existingLesson, newLesson);
		}
		
    	
    }
    
    
    private void checkAndRepairModule(ModuleDescriptor existingModule, ModuleDescriptor newModule) {
		log.info("SoC_RepairDatabase.checkAndRepairModule: iterating existing modules: "+existingModule.getId()+", "+existingModule.getTitle()+", "+newModule.getId()+", "+newModule.getTitle());
		if (isReporting) report += "SoC_RepairDatabase.checkAndRepairModule: iterating existing modules: "+existingModule.getId()+", "+existingModule.getTitle()+", "+newModule.getId()+", "+newModule.getTitle()+"<BR>\n";
		
		int lLength = newModule.getLessonList().size();
		if (lLength != existingModule.getLessonList().size()) {
			if (isReporting) report += "<font color='red'>SoC_RepairDatabase.checkAndRepairLesson: lesson size mismatch warning: new: "+newModule.getLessonList().size()+", existing: "+existingModule.getLessonList().size()+"</font><BR>\n";
			lLength = Math.min(lLength, existingModule.getLessonList().size());
		}
		if (isReporting) report += "SoC_RepairDatabase.checkAndRepairLesson: iterating lessons: "+lLength+"<BR>\n";

		for (LessonDescriptor newLesson: newModule.getLessonList()) {
			if (isReporting) report += "SoC_RepairDatabase.checkAndRepairModule: list new lessons: "+newLesson.getId()+", "+newLesson.getTitle()+"<BR>\n";
		}
		// LESSONS
		for (int l=0; l<lLength; l++) {
			LessonDescriptor newLesson = newModule.getLessonList().get(l);
			LessonDescriptor existingLesson = existingModule.getLessonList().get(l);
			
			checkAndRepairLesson(existingLesson, newLesson);
			
			addToCleanup(existingModule, newModule);
		} // LESSONS
		
    }
    
    
    private void checkAndRepairUserSelectedOption(Entity usoEntity, CourseDescriptor newCourse) {
		String selectedOptionId = (String)usoEntity.getProperty("selectedOption");
		String selectionObjectId = (String)usoEntity.getProperty("selectionObject");

		Key selectedOptionKey = KeyFactory.createKey(SelectionOption.class.getName(), selectedOptionId);
		Key selectionObjectKey = KeyFactory.createKey(SelectionObject.class.getName(), selectionObjectId);

		try {
			Entity selectedOptionEntity = datastore.get(selectedOptionKey);
			Entity selectionObjectEntity = datastore.get(selectionObjectKey);
			
			SelectionOption selectedOption = PersistentStore.readSelectionOption(selectedOptionEntity, new HashMap<String, Object>());
			SelectionObject selectionObject = PersistentStore.readSelectionObject(selectionObjectEntity, new HashMap<String, Object>());
			
			boolean found = false;

			if (selectedOption != null && selectionObject != null) {
				for (ModuleDescriptor module: newCourse.getModuleList()) {
					for (LessonDescriptor lesson: module.getLessonList()) {
						for (ContentDescriptor content: lesson.getContentList()) {
							for (SelectionObject sele: content.getSelectionObjectList()) {
								for (SelectionOption option: sele.getOptionList()) {
									if (option != null && sele != null) {
										if (((selectedOption.getId().equals(option.getId()) 
													|| (selectedOption.getTitle().equals(option.getTitle()) 
														&& selectedOption.getDescription().equals(option.getDescription()) 
														&& selectedOption.getContent().equals(option.getContent()))) 
												&& (selectionObject.getId().equals(sele.getId()) 
													|| (selectionObject.getTitle().equals(sele.getTitle()) 
														&& selectionObject.getDescription().equals(sele.getDescription()) 
														&& selectionObject.getContent().equals(sele.getContent()) 
														&& selectionObject.getDeadline().equals(sele.getDeadline()) 
														&& selectionObject.isClanAVisible() == sele.isClanAVisible() 
														&& selectionObject.isClanBVisible() == sele.isClanBVisible() 
														&& selectionObject.isControlGroupVisible() == sele.isControlGroupVisible()))) 
												|| (selectionObject.getId().equals(sele.getId()) 
													&& selectionObject.getId().startsWith("encryption") 
													&& selectedOption.getTitle().equals(option.getTitle()))) {
											
											found = true;
											if (isReporting) report += "<font color='blue'> found correct version for: "+usoEntity.getKey()+": "+selectionObjectId+", "+selectedOptionId+" --> "+sele.getId()+", "+option.getId()+"</font><BR>\n";
											log.info("found correct version for: "+usoEntity.getKey()+": "+selectionObjectId+", "+selectedOptionId+" --> "+sele.getId()+", "+option.getId());
											
											if (isCleanup) {
												usoEntity.setProperty("selectedOption", option.getId());
												usoEntity.setProperty("selectionObject", sele.getId());
												try {
													datastore.put(usoEntity);
													if (isReporting) report += "<font color='green'> cleanup done for: "+usoEntity.getKey()+": "+sele.getId()+", "+option.getId()+"</font><BR>\n";
													log.info("cleanup done for: "+usoEntity.getKey()+": "+sele.getId()+", "+option.getId());
												} catch(Exception exc) {
													if (isReporting) report += "<font color='red'> cleanup failed for: "+usoEntity.getKey()+": "+sele.getId()+", "+option.getId()+"</font><BR>\n";
													log.info("cleanup failed for: "+usoEntity.getKey()+": "+sele.getId()+", "+option.getId());
													exc.printStackTrace();
												}
												
											}
											
										}
									}
								}
							}
						}
					}
				}
			}
			if (!found) {
				log.info("did not find correct version for: "+usoEntity.getKey()+": "+selectionObjectId+", "+selectedOptionId);
				if (isReporting) report += "<font color='orange'> did not find correct version for: "+usoEntity.getKey()+": "+selectionObjectId+", "+selectedOptionId+"</font><BR>\n";
			}
 		} catch (Exception exc) {
			log.info("unable to repair UserSelectedOption: "+usoEntity.getKey()+", "+selectionObjectId+", "+selectedOptionId);
			exc.printStackTrace();
		}
    }
    
    private void checkAndRepairUserSelectedOptions(CourseDescriptor newCourse) {
    	PreparedQuery pq_uso = PersistentStore.getEntitiesOfType(UserSelectedOption.class.getName());
    	
    	ArrayList<Entity> allEntities = new ArrayList<>();
    	
    	for (Entity usoEntity: pq_uso.asIterable()) {
    		allEntities.add(usoEntity);
    	}
    	
    	for (Entity usoEntity: allEntities) {
    		if (usoEntity != null) {
        		checkAndRepairUserSelectedOption(usoEntity, newCourse);
    		}
    	}

    }
    
  
    protected void deleteEntitiesofType(String entityType) {
		log.info("SoC_RepairDatabase.deleteEntitiesofType: "+entityType);
		if (isReporting) report += "SoC_RepairDatabase.deleteEntitiesofType: "+entityType+"<BR>\n";
		boolean found = true;
		long count = 0;
		while (found) {
			found = false;
			try {
				DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
				Query q = new Query(entityType);

				PreparedQuery pq = datastore.prepare(q);
				FetchOptions fetchOptions = FetchOptions.Builder.withChunkSize(500);
				
				ArrayList<Entity> allEntities = new ArrayList<>();
				
				for (Entity entity: pq.asIterable(fetchOptions)) {
					found = true;
					allEntities.add(entity);
				}

				for (Entity entity: allEntities) {
					try {
						datastore.delete(entity.getKey());
						count++;
					} catch (Exception exc) {
						if (isReporting) report += "<font color='red'> unable to delete entity: "+entityType+": "+entity.getKey()+"</font><BR>\n";
						log.info("unable to delete entity: "+entityType+": "+entity.getKey());
						exc.printStackTrace();
					}
				}
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		log.info("SoC_RepairDatabase.deleteEntitiesofType: deleted "+count+" duplicates of type "+entityType);
		if (isReporting) report += "<font color='blue'> SoC_RepairDatabase.deleteEntitiesofType: deleted "+count+" duplicates of type "+entityType+"</font><BR>\n";
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String commandType = request.getParameter("commandType");
		isReporting = false;
		isCleanup = false;
		
		if (commandType == null || (!"REPORTING".equals(commandType) && !"CLEANUP".equals(commandType))) {
			response.getWriter().append("Forbidden.");
			return;
		} else if ("REPORTING".equals(commandType)) {
			isCleanup = false;
			isReporting = true;
		} else if ("CLEANUP".equals(commandType)) {
			isCleanup = true;
			isReporting = true;
		}
		
		cleanUpList = new ArrayList<>(); 
		updateList = new ArrayList<>();
		newObjectList = new ArrayList<>();

		report = "<HTML><HEAD></HEAD><BODY>\n";
		
		try {
			log.info("SoC_RepairDatabase.doGet: start.");
			if (isReporting) report += "SoC_RepairDatabase.startup: "+commandType+"<BR>\n";
			
			datastore = DatastoreServiceFactory.getDatastoreService();
			
			log.info("SoC_RepairDatabase.doGet: reference course creation.");
			if (isReporting) report += "SoC_RepairDatabase.doGet: creating reference course structure. "+"<BR>\n";

			CourseDescriptor newCourse = SoC_ProductionCourseCreationFactory.generateProductionCourse();

			newObjectList.add(newCourse);
			
			log.info("SoC_RepairDatabase.doGet: working on UserSelectedOptions.");
			if (isReporting) report += "SoC_RepairDatabase.doGet: working on UserSelectedOptions. "+"<BR>\n";
			
			checkAndRepairUserSelectedOptions(newCourse);

			if (isCleanup) {
				log.info("SoC_RepairDatabase.doGet: starting database cleanup.");
				if (isReporting) report += "SoC_RepairDatabase.doGet: starting database cleanup."+"<BR>\n";
				
				deleteEntitiesofType(ModuleDescriptor.class.getName());
				deleteEntitiesofType(LessonDescriptor.class.getName());
				deleteEntitiesofType(ContentDescriptor.class.getName());
				deleteEntitiesofType(InformationObject.class.getName());
				deleteEntitiesofType(SelectionObject.class.getName());
				deleteEntitiesofType(SelectionOption.class.getName());
				deleteEntitiesofType(GoalDescriptor.class.getName());
				
				deleteEntitiesofType(CourseDescriptor.class.getName()+"_modules_"+ModuleDescriptor.class.getName());
				deleteEntitiesofType(CourseDescriptor.class.getName()+"_goals_"+GoalDescriptor.class.getName());
				deleteEntitiesofType(ModuleDescriptor.class.getName()+"_lessons_"+LessonDescriptor.class.getName());
				deleteEntitiesofType(LessonDescriptor.class.getName()+"_contents_"+ContentDescriptor.class.getName());
				deleteEntitiesofType(ContentDescriptor.class.getName()+"_informationObjects_"+InformationObject.class.getName());
				deleteEntitiesofType(ContentDescriptor.class.getName()+"_selectionObjects_"+SelectionObject.class.getName());
				deleteEntitiesofType(SelectionObject.class.getName()+"_options_"+SelectionOption.class.getName());
				deleteEntitiesofType(GoalDescriptor.class.getName()+"_lessons_"+LessonDescriptor.class.getName());
				
				
				log.info("SoC_RepairDatabase.doGet: creating reference objects: "+newObjectList.size());
				for (GenericDescriptor createDescriptor: newObjectList) {
					if (isReporting) report += "<font color='blue'>SoC_RepairDatabase.doGet creating: "+createDescriptor.getClass().getName()+": "+createDescriptor.getId()+"</font><BR>\n";
					if (isCleanup) {
						try {
							PersistentStore.writeAnyGenericDescriptor(createDescriptor);
						} catch (Exception exc) {
							exc.printStackTrace();
							report += "<font color='red'>SoC_RepairDatabase.doGet Exception occourred in writing. Cleanup failed: "+exc.toString()+"<BR>\n";
						}
					}
				}
				
				
			}
			
			
//			List<ModuleDescriptor> newModules = newCourse.getModuleList();
//			HashMap<String, Object> cache = new HashMap<>();
//			
//			if (isReporting) report += "SoC_RepairDatabase.doGet: iterating modules. Start."+"<BR>\n";
//
//			for (ModuleDescriptor newModule: newModules) {
//				if (isReporting) report += "SoC_RepairDatabase.doGet: list new modules: "+newModule.getId()+", "+newModule.getTitle()+"<BR>\n";
//			}	
//			
//			newModules = newCourse.getModuleList();
//			// NEW MODULES
//			for (int md=0; md<newModules.size(); md++) {
//				ModuleDescriptor newModule = newModules.get(md);
//				log.info("SoC_RepairDatabase.doGet: module: "+newModule.getId()+", "+newModule.getTitle());
//				if (isReporting) report += "SoC_RepairDatabase.doGet: next module: "+newModule.getId()+", "+newModule.getTitle()+"<BR>\n";
//				Filter titleFilter = new FilterPredicate("title", FilterOperator.EQUAL, newModule.getTitle());
//				Query q = new Query(ModuleDescriptor.class.getName()).setFilter(titleFilter);
//				PreparedQuery pq = datastore.prepare(q);
//
//				if (isReporting) report += "SoC_RepairDatabase.doGet: querying existing modules.<BR>\n";
//				// EXISTING MODULES
//				for (Entity entity : pq.asIterable()) {
//					try {
//						ModuleDescriptor existingModule = PersistentStore.readModuleDescriptorForRepair(entity, cache);
//						checkAndRepairModule(existingModule, newModule);
//					} catch (Exception exc) {
//						log.info("Exception during module processing: "+entity.getKey());
//						exc.printStackTrace();
//					}
//					
//					// POST PROCESSING
//					log.info("SoC_RepairDatabase.doGet: updating object references: "+updateList.size());
//					for (Entity updateEntity: updateList) {
//						if (isReporting) report += "<font color='green'>SoC_RepairDatabase.doGet updating: "+updateEntity.getKey()+", "+updateEntity.getProperty("uid")+"</font><BR>\n";
//						// TODO: updating
//						if (isCleanup) {
//							try {
//								datastore.put(updateEntity);
//							} catch (Exception e1) {
//								report += "<font color='red'>SoC_RepairDatabase.doGet exception: FATAL: Writing message failed: "+e1.toString()+"</font><BR>\n";
//								e1.printStackTrace();
//							}
//						}
//					}
//					updateList = new ArrayList<>();
//
//					log.info("SoC_RepairDatabase.doGet: deleting duplicates: "+cleanUpList.size());
//					for (Object cleanupObject: cleanUpList) {
//						if (cleanupObject instanceof GenericDescriptor) {
//							if (isReporting) report += "<font color='orange'>SoC_RepairDatabase.doGet cleaning: "+cleanupObject.getClass().getName()+": "+((GenericDescriptor)cleanupObject).getId()+"</font><BR>\n";
//						} else {
//							if (isReporting) report += "SoC_RepairDatabase.doGet cleaning: "+cleanupObject.getClass().getName()+"<BR>\n";
//						}
//						
//						if (isCleanup) {
//							if (cleanupObject instanceof GenericDescriptor) {
//								PersistentStore.deleteGenericEntity((GenericDescriptor)cleanupObject);
//							} else {
//								report += "<font color='red'>SoC_RepairDatabase.doGet: Cannot delete object: "+cleanupObject.getClass().getName()+", "+cleanupObject.toString()+"</font><BR>\n";
//							}
//						}
//					}
//					cleanUpList = new ArrayList<>();
//					
//					log.info("SoC_RepairDatabase.doGet: creating reference objects: "+newObjectList.size());
//					for (GenericDescriptor createDescriptor: newObjectList) {
//						if (isReporting) report += "<font color='blue'>SoC_RepairDatabase.doGet creating: "+createDescriptor.getClass().getName()+": "+createDescriptor.getId()+"</font><BR>\n";
//						if (isCleanup) {
//							try {
//								PersistentStore.writeAnyGenericDescriptor(createDescriptor);
//							} catch (Exception exc) {
//								exc.printStackTrace();
//								report += "<font color='red'>SoC_RepairDatabase.doGet Exception occourred in writing. Cleanup failed: "+exc.toString()+"<BR>\n";
//							}
//						}
//					}
//					//newObjectList = new ArrayList<>();
//					
//				} // EXISTING MODULES
//			} // NEW MODULES
		} catch (Exception exc) {
			exc.printStackTrace();
			if (isReporting) report += "<font color='red'>SoC_RepairDatabase.doGet exception: "+exc+"</font><BR>\n";
		}

		log.info("SoC_RepairDatabase.doGet: done.");
		report += "\n</BODY></HTML>\n";

		
		response.getWriter().append(report + "\n");
		log.info(report);
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
