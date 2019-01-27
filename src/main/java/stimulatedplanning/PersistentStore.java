package stimulatedplanning;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.EmbeddedEntity;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.KeyRange;
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
import senseofcommunity.SelectionObjectPurpose;
import senseofcommunity.SelectionObjectType;
import senseofcommunity.SelectionOption;
import senseofcommunity.UserOnlineStatus;
import senseofcommunity.UserSelectedOption;
import stimulatedplanning.util.HashArrayList;
import stimulatedplanning.util.IObjectWithId;

import com.google.appengine.api.datastore.Text;

import chat.ChatRoom;
import chat.ChatRoomList;
import chat.Message;

//import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
//import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

public class PersistentStore {
	private static final Logger log = Logger.getLogger(PersistentStore.class.getName());   

	public static void writeUser(User user) throws Exception {
		if (!StimulatedPlanningFactory.userGuest.equals(user.getName()) && !StimulatedPlanningFactory.userUnknown.equals(user.getId())) {
			DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

			Entity userEntity = null;
			try {
				userEntity = datastore.get(KeyFactory.createKey(user.getClass().getName(), user.getId()));
			} catch (Exception e) {
				log.info("User "+user.getId()+" does not exist yet. Creating it.");
				userEntity = null;
			}
			if (userEntity == null) {
				userEntity = new Entity(user.getClass().getName(), user.getId());
			}

			userEntity.setProperty("name", user.getName());
			userEntity.setProperty("uid", user.getId());
			userEntity.setProperty("treatmentGroup", user.isTreatmentGroup());
			if (user.isTreatmentGroup()) {
				userEntity.setProperty("clan", user.getClan().getId());
			}
			userEntity.setProperty("onlineStatus", user.getOnlineStatus().getId());
			userEntity.setProperty("avatarUrl", user.getAvatarUrl());

			datastore.put(userEntity);
			StimulatedPlanningFactory.addObject(user, userEntity);
		}
	}

	public static User getUser(String userId, Entity userEntity, HashMap<String, Object> cache) throws Exception {
		return getUser(userId, userEntity, cache, true);
	}
	public static User getUser(String userId, Entity userEntity, HashMap<String, Object> cache, boolean useCache) throws Exception {
		User user = null;
		try {
			if (useCache) {
				user = (User)StimulatedPlanningFactory.getObject(User.class.getName(), userId);
			}
			if (user == null) {
				user = (User)cache.get(userId);
			}
		} catch (Exception exc) {
			exc.printStackTrace();
		}
		if (user == null) {
			if (userEntity != null) {
				user = new User((String) userEntity.getProperty("name"), userId);
				cache.put(user.getId(), user);
				user.setTreatmentGroup(readBooleanProperty(userEntity, "treatmentGroup", StimulatedPlanningFactory.random.nextBoolean()));
				if (user.isTreatmentGroup()) {
					Clan clan = StimulatedPlanningFactory.getClan(readStringProperty(userEntity, "clan", null));
					user.setClan(clan);
				}
				UserOnlineStatus onlineStatus = (UserOnlineStatus)readUserOnlineStatus(readStringProperty(userEntity, "onlineStatus", null), user, cache);
				user.setOnlineStatus(onlineStatus);
				user.setAvatarUrl((String) userEntity.getProperty("avatarUrl"));
				if (!userEntity.hasProperty("treatmentGroup")) {
					writeUser(user);
				}
				StimulatedPlanningFactory.addObject(user, userEntity);
			}
		}
		return user;
	}

	public static User getUser(String userId, HashMap<String, Object> cache) throws Exception {
		return getUser(userId, cache, true);
	}
	public static User getUser(String userId, HashMap<String, Object> cache, boolean useCache) throws Exception {
		User user = null;
		try {
			if (useCache) {
				user = (User)StimulatedPlanningFactory.getObject(User.class.getName(), userId);
			}
			if (user == null) {
				user = (User)cache.get(userId);
			}
		} catch (Exception exc) {
			exc.printStackTrace();
		}
		if (user == null) {
			DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	
			Entity userEntity = datastore.get(KeyFactory.createKey(User.class.getName(), userId));
			if (userEntity != null) {
				return getUser(userId, userEntity, cache, useCache);
			}
		}
		return user;
	}

	public static HashArrayList<User> readAllControlUsers() {
		HashArrayList<User> users = new HashArrayList<User>();
		User user = null;
		try {
			//log.info("readAllControlUsers: start");
			DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
			Filter controlFilter = new FilterPredicate("treatmentGroup", FilterOperator.EQUAL, false);

			Query q = new Query(User.class.getName()).setFilter(controlFilter);
			
			PreparedQuery pq = datastore.prepare(q);

			for (Entity result : pq.asIterable()) {
				String id = (String) result.getProperty("uid");
				//log.info("readAllControlUsers: "+id);
				user = (User)getUser(id, result, new HashMap<String, Object>());
				users.add(user);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return users;
	}


	
	protected static Entity createGenericEntity(GenericDescriptor generic) {
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

		log.info("createGenericEntity: "+generic.getTitle()+", "+generic.getClass().getName()+", "+generic.getId());

		Entity entity = null;
		try {
			entity = StimulatedPlanningFactory.getEntity(generic.getClass().getName(), generic.getId());
			if (entity == null) {
				entity = datastore.get(KeyFactory.createKey(generic.getClass().getName(), generic.getId()));
			}
		} catch (Exception e) {
			//e.printStackTrace();
			entity = null;
		}
		if (entity == null) {
			entity = new Entity(generic.getClass().getName(), generic.getId());
		}

		entity.setProperty("uid", generic.getId());
		entity.setProperty("title", generic.getTitle());
		entity.setProperty("description", new Text(generic.getDescription()));
		entity.setProperty("url", generic.getUrl());
		
		StimulatedPlanningFactory.addObject(generic, entity);

		return entity;
	}

	protected static Entity createGenericUserEntity(GenericUserObject generic) {
		//log.info("createGenericUserEntity: "+generic.getTitle()+", "+generic.getClass().getName()+", "+generic.getId());
		Entity entity = null;

		try {
			entity = createGenericEntity(generic);

			entity.setProperty("userid", generic.getUser().getId());
			
		} catch (Exception e1) {
			log.info("FATAL: Writing generic user entity failed.");
			e1.printStackTrace();

		}

		return entity;
	}

	protected static Entity createInformationObjectEntity(InformationObject generic) {
		//log.info("createInformationObjectEntity: "+generic.getClass().getName()+", "+generic.getId()+", "+generic.getTitle()+", content: "+generic.getContent());
		Entity entity = null;

		try {
			entity = createGenericEntity(generic);

			entity.setProperty("content", new Text(generic.getContent()));
			entity.setProperty("sequence", generic.getSequence());
			entity.setProperty("isControlGroupVisible", generic.isControlGroupVisible());
			entity.setProperty("isClanAVisible", generic.isClanAVisible());
			entity.setProperty("isClanBVisible", generic.isClanBVisible());
			
		} catch (Exception e1) {
			log.info("FATAL: Writing generic user entity failed.");
			e1.printStackTrace();

		}

		return entity;
	}
	
	
	public static void updateCacheableObject(CacheableDatabaseObject cdo) {
		Entity entity = cdo.getEntity();
		IObjectWithId object = cdo.getObject();
		String className = cdo.getObjectClass();
		
		if (User.class.getName().equals(className)) {
			try {
				User user = getUser(object.getId(), new HashMap<String, Object>(), false);
				UserOnlineStatus status = readUserOnlineStatus(user);
			} catch(Exception exc) {
				exc.printStackTrace();
			}
		} else {
			try {
				readDescriptor(className, object.getId(), new HashMap<String, Object>(), null, false);
			} catch(Exception exc) {
				exc.printStackTrace();
			}
		}
	}

	public static GenericDescriptor readDescriptor(String type, String id, HashMap<String, Object> cache, Entity retrievedEntity) throws Exception {
		return readDescriptor(type, id, cache, retrievedEntity, true);
	}	
	
	public static GenericDescriptor readDescriptor(String type, String id, HashMap<String, Object> cache, Entity retrievedEntity, boolean useCache) throws Exception {
		//log.info("readDescriptor: "+type+", "+id);
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

		GenericDescriptor genericDescriptor = null;
		try {
			if (useCache) {
				genericDescriptor = (GenericDescriptor)StimulatedPlanningFactory.getObject(type, id);
			}
			if (genericDescriptor != null) {
				return genericDescriptor;
			}
		} catch (Exception exc) {
			exc.printStackTrace();
		}
		
		Entity genericEntity = retrievedEntity;
		if (genericEntity == null) {
			try {
				if (useCache) {
					genericEntity = StimulatedPlanningFactory.getEntity(type, id);
				}
				if (genericEntity == null) {
					genericEntity = datastore.get(KeyFactory.createKey(type, id));
				}
			} catch (Exception e) {
				e.printStackTrace();
				genericEntity = null;
			}			
		}
		
		if (genericEntity != null) {
			if (CourseDescriptor.class.getName().equals(type)) {
				genericDescriptor = readCourseDescriptor(genericEntity, cache, useCache);
			} else if (ModuleDescriptor.class.getName().equals(type)) {
				genericDescriptor = readModuleDescriptor(genericEntity, cache, useCache);
			} else if (GoalDescriptor.class.getName().equals(type)) {
				genericDescriptor = readGoalDescriptor(genericEntity, cache);
			} else if (LessonDescriptor.class.getName().equals(type)) {
				genericDescriptor = readLessonDescriptor(genericEntity, cache, useCache);
			} else if (ContentDescriptor.class.getName().equals(type)) {
				genericDescriptor = readContentDescriptor(genericEntity, cache, useCache);
			} else if (UserPlan.class.getName().equals(type)) {
				genericDescriptor = readUserPlan(genericEntity, cache, useCache);
			} else if (UserGoal.class.getName().equals(type)) {
				genericDescriptor = readUserGoal(genericEntity, cache, useCache);
			} else if (UserLesson.class.getName().equals(type)) {
				genericDescriptor = readUserLesson(genericEntity, cache, useCache);
			} else if (UserContent.class.getName().equals(type)) {
				genericDescriptor = readUserContent(genericEntity, cache, useCache);
			} else if (PlanItem.class.getName().equals(type)) {
				genericDescriptor = readPlanItem(genericEntity, cache, useCache);
			} else if (UserProfile.class.getName().equals(type)) {
				genericDescriptor = readUserProfile(genericEntity, cache, useCache);
			} else if (UserOnlineStatus.class.getName().equals(type)) {
				genericDescriptor = readUserOnlineStatus(genericEntity, cache, useCache);
			} else if (Clan.class.getName().equals(type)) {
				genericDescriptor = readClan(genericEntity, cache, useCache);
			} else if (InformationObject.class.getName().equals(type)) {
				genericDescriptor = readInformationObject(genericEntity, cache, useCache);
			} else if (SelectionObject.class.getName().equals(type)) {
				genericDescriptor = readSelectionObject(genericEntity, cache, useCache);
			} else if (SelectionOption.class.getName().equals(type)) {
				genericDescriptor = readSelectionOption(genericEntity, cache, useCache);
			} else if (UserSelectedOption.class.getName().equals(type)) {
				genericDescriptor = readUserSelectedOption(genericEntity, cache, useCache);
			}
			if (genericDescriptor != null) {
				StimulatedPlanningFactory.addObject(genericDescriptor, genericEntity);
			}
			return genericDescriptor;
		}
		return null;
	}
	
	public static void writeAnyGenericDescriptor(GenericDescriptor generic) throws Exception {
		if (generic != null) {
			if (generic instanceof CourseDescriptor) writeDescriptor((CourseDescriptor)generic);
			else if (generic instanceof ModuleDescriptor) writeDescriptor((ModuleDescriptor)generic);
			else if (generic instanceof GoalDescriptor) writeDescriptor((GoalDescriptor)generic);
			else if (generic instanceof LessonDescriptor) writeDescriptor((LessonDescriptor)generic);
			else if (generic instanceof ContentDescriptor) writeDescriptor((ContentDescriptor)generic);
			else if (generic instanceof UserPlan) writeDescriptor((UserPlan)generic);
			else if (generic instanceof UserGoal) writeDescriptor((UserGoal)generic);
			else if (generic instanceof UserLesson) writeDescriptor((UserLesson)generic);
			else if (generic instanceof UserContent) writeDescriptor((UserContent)generic);
			else if (generic instanceof PlanItem) writeDescriptor((PlanItem)generic);
			else if (generic instanceof Clan) writeDescriptor((Clan)generic);
			else if (generic instanceof UserOnlineStatus) writeDescriptor((UserOnlineStatus)generic);
			else if (generic instanceof SelectionObject) writeDescriptor((SelectionObject)generic);
			else if (generic instanceof SelectionOption) writeDescriptor((SelectionOption)generic);
			else if (generic instanceof InformationObject) writeDescriptor((InformationObject)generic);
			else if (generic instanceof UserSelectedOption) writeDescriptor((UserSelectedOption)generic);
			else writeDescriptor(generic);
		}
	}
	
	
	public static void writeDescriptor(GenericDescriptor generic) throws Exception {
		//log.info("writeDescriptor (GenericDescriptor): "+generic.getTitle()+", "+generic.getClass().getName()+", "+generic.getId());
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	
		try {
			Entity entity = createGenericEntity(generic);
	
			datastore.put(entity);
		} catch (Exception e1) {
			log.info("FATAL: Writing generic entity failed.");
			e1.printStackTrace();
	
		}
	
	}

	protected static CourseDescriptor readCourseDescriptor(Entity genericEntity, HashMap<String, Object> cache) throws Exception {
		return readCourseDescriptor(genericEntity, cache, true);
	}
	protected static CourseDescriptor readCourseDescriptor(Entity genericEntity, HashMap<String, Object> cache, boolean useCache) throws Exception {
		CourseDescriptor course = null;
		try {
			String id = readStringProperty(genericEntity, "uid", null);
			if (useCache) {
				course = (CourseDescriptor)StimulatedPlanningFactory.getObject(CourseDescriptor.class.getName(), id);
			}
			if (course == null) {
				course = (CourseDescriptor)cache.get(id);
			}
		} catch (Exception exc) {
			exc.printStackTrace();
		}
		if (course == null) {
			ArrayList<GenericDescriptor> relationList = null;
			course = new CourseDescriptor(readStringProperty(genericEntity, "uid", null),
					readStringProperty(genericEntity, "title", null), readStringProperty(genericEntity, "description", null),
					readStringProperty(genericEntity, "url", null));
			cache.put(course.getId(), course);
			StimulatedPlanningFactory.addObject(course, genericEntity);
			relationList = readToManyRelation(course, "modules", ModuleDescriptor.class.getName(), true, cache, useCache);
			for (GenericDescriptor generic : relationList) {
				course.addModule((ModuleDescriptor)generic);
			}
			relationList = readToManyRelation(course, "goals", GoalDescriptor.class.getName(), true, cache, useCache);
			for (GenericDescriptor generic : relationList) {
				course.addGoal((GoalDescriptor)generic);
			}
		}
		return course;
	}
	
	public static CourseDescriptor readCourse(String id) {
		CourseDescriptor course = null;
		try {
			course = (CourseDescriptor)readDescriptor(CourseDescriptor.class.getName(), id, new HashMap<String, Object>(), null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return course;
	}

	public static void writeDescriptor(CourseDescriptor course) throws Exception {
		//log.info("writeDescriptor (CourseDescriptor): "+course.getTitle()+", "+course.getClass().getName()+", "+course.getId());
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	
		try {
			Entity courseEntity = createGenericEntity(course);
	
			writeToManyRelation(course, course.getModules(), "modules", course.modules.size(), true);
			writeToManyRelation(course, course.getGoals(), "goals", course.goals.size(), true);
	
			datastore.put(courseEntity);
		} catch (Exception e1) {
			log.info("FATAL: Writing course failed.");
			e1.printStackTrace();
	
		}
	
	}

	protected static ModuleDescriptor readModuleDescriptor(Entity genericEntity, HashMap<String, Object> cache) throws Exception {
		return readModuleDescriptor(genericEntity, cache, true);
	}
	protected static ModuleDescriptor readModuleDescriptor(Entity genericEntity, HashMap<String, Object> cache, boolean useCache) throws Exception {
		ModuleDescriptor module = null;
		try {
			String id = readStringProperty(genericEntity, "uid", null);
			if (useCache) {
				module = (ModuleDescriptor)StimulatedPlanningFactory.getObject(ModuleDescriptor.class.getName(), id);
			}
			if (module == null) {
				module = (ModuleDescriptor)cache.get(id);
			}
		} catch (Exception exc) {
			exc.printStackTrace();
		}
		if (module == null) {
			ArrayList<GenericDescriptor> relationList = null;
			module = new ModuleDescriptor(readStringProperty(genericEntity,"uid", null),
					readStringProperty(genericEntity, "title", null), readStringProperty(genericEntity, "description", null),
					readStringProperty(genericEntity, "url", null));
			cache.put(module.getId(), module);
			StimulatedPlanningFactory.addObject(module, genericEntity);
			relationList = readToManyRelation(module, "lessons", LessonDescriptor.class.getName(), true, cache, useCache);
			for (GenericDescriptor generic : relationList) {
				module.addLesson((LessonDescriptor)generic);
			}
		}
		return module;
	}
	
	public static ModuleDescriptor readModuleDescriptorForRepair(Entity genericEntity, HashMap<String, Object> cache) throws Exception {
		ModuleDescriptor module = null;
		try {
			module = readModuleDescriptor(genericEntity, cache);
		} catch (Exception exc) {
			exc.printStackTrace();
		}
		return module;
	}
	
	public static void writeDescriptor(ModuleDescriptor module) throws Exception {
		//log.info("writeDescriptor (ModuleDescriptor): "+module.getTitle()+", "+module.getClass().getName()+", "+module.getId());
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	
		try {
			Entity moduleEntity = createGenericEntity(module);
	
			writeToManyRelation(module, module.getLessons(), "lessons", module.lessons.size(), true);
	
			datastore.put(moduleEntity);
		} catch (Exception e1) {
			log.info("FATAL: Writing module failed.");
			e1.printStackTrace();
	
		}
	
	}

	protected static GoalDescriptor readGoalDescriptor(Entity genericEntity, HashMap<String, Object> cache) throws Exception {
		return readGoalDescriptor(genericEntity, cache, true);
	}
	protected static GoalDescriptor readGoalDescriptor(Entity genericEntity, HashMap<String, Object> cache, boolean useCache) throws Exception {
		GoalDescriptor goal = null;
		try {
			String id = readStringProperty(genericEntity, "uid", null);
			if (useCache) {
				goal = (GoalDescriptor)StimulatedPlanningFactory.getObject(GoalDescriptor.class.getName(), id);
			}
			if (goal == null) {
				goal = (GoalDescriptor)cache.get(id);
			}
		} catch (Exception exc) {
			exc.printStackTrace();
		}
		if (goal == null) {
			ArrayList<GenericDescriptor> relationList = null;
			goal = new GoalDescriptor(readStringProperty(genericEntity, "uid", null),
					readStringProperty(genericEntity, "title", null), readStringProperty(genericEntity, "description", null),
					readStringProperty(genericEntity, "url", null));
			cache.put(goal.getId(), goal);
			StimulatedPlanningFactory.addObject(goal, genericEntity);
			relationList = readToManyRelation(goal, "lessons", LessonDescriptor.class.getName(), true, cache, useCache);
			for (GenericDescriptor generic : relationList) {
				goal.addLesson((LessonDescriptor)generic);
			}
			EmbeddedEntity ee = (EmbeddedEntity) genericEntity.getProperty("completionGoals");
			String keys = readStringProperty(genericEntity, "completionGoalKeys", null);
			if (keys != null && keys.length()>0) {
				keys = keys.replace("[", "").replace("]", "").replace(" ", "");
				//log.info("Reading completionGoals: " + keys);
				if (keys.length()>0) {
					ArrayList<String> keysList = new ArrayList<>(Arrays.asList(keys.split(",")));
					if (ee != null && keysList != null && keysList.size() > 0) {
					    for (String key : keysList) {
					    	//log.info("Key: "+key+", "+(String) ee.getProperty(key));
					        goal.addCompletionGoal(key, (String) ee.getProperty(key));
					    }
					}
				}
			}
		}
		return goal;
	}
	
	public static void writeDescriptor(GoalDescriptor goal) throws Exception {
		//log.info("writeDescriptor (GoalDescriptor): "+goal.getTitle()+", "+goal.getClass().getName()+", "+goal.getId());
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	
		try {
			Entity goalEntity = createGenericEntity(goal);
	
			writeToManyRelation(goal, goal.getLessons(), "lessons", goal.lessons.size(), true);
			
			EmbeddedEntity ee = new EmbeddedEntity();
		    Map<String, String> map = goal.completionGoals;
	
		    for (String key : map.keySet()) { // TODO: maybe there is a more efficient way of solving this
		        ee.setProperty(key, map.get(key));
		    }
	
		    goalEntity.setProperty("completionGoals", ee);
	
		    ArrayList<String> keys = goal.completionGoalKeys;
		    goalEntity.setProperty("completionGoalKeys", keys.toString());
	
			datastore.put(goalEntity);
		} catch (Exception e1) {
			log.info("FATAL: Writing goal failed.");
			e1.printStackTrace();
	
		}
	
	}

	protected static LessonDescriptor readLessonDescriptor(Entity genericEntity, HashMap<String, Object> cache) throws Exception {
		return readLessonDescriptor(genericEntity, cache, true);
	}
	protected static LessonDescriptor readLessonDescriptor(Entity genericEntity, HashMap<String, Object> cache, boolean useCache) throws Exception {
		LessonDescriptor lesson = null;
		try {
			String id = readStringProperty(genericEntity, "uid", null);
			if (useCache) {
				lesson = (LessonDescriptor)StimulatedPlanningFactory.getObject(LessonDescriptor.class.getName(), id);
			}
			if (lesson == null) {
				lesson = (LessonDescriptor)cache.get(id);
			}
		} catch (Exception exc) {
			exc.printStackTrace();
		}
		if (lesson == null) {
			ArrayList<GenericDescriptor> relationList = null;
			lesson = new LessonDescriptor(readStringProperty(genericEntity, "uid", null),
					readStringProperty(genericEntity, "title", null), readStringProperty(genericEntity, "description", null),
					readStringProperty(genericEntity, "url", null));
			cache.put(lesson.getId(), lesson);
			StimulatedPlanningFactory.addObject(lesson, genericEntity);
			relationList = readToManyRelation(lesson, "contents", ContentDescriptor.class.getName(), true, cache, useCache);
			for (GenericDescriptor generic : relationList) {
				lesson.addContent((ContentDescriptor)generic);
			}
		}
		return lesson;
	}
	
	public static void writeDescriptor(LessonDescriptor lesson) throws Exception {
		//log.info("writeDescriptor (LessonDescriptor): "+lesson.getTitle()+", "+lesson.getClass().getName()+", "+lesson.getId());
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	
		try {
			Entity lessonEntity = createGenericEntity(lesson);
	
			writeToManyRelation(lesson, lesson.getContents(), "contents", lesson.contents.size(), true);
	
			datastore.put(lessonEntity);
		} catch (Exception e1) {
			log.info("FATAL: Writing lesson failed.");
			e1.printStackTrace();
	
		}
	
	}

	protected static ContentDescriptor readContentDescriptor(Entity genericEntity, HashMap<String, Object> cache) throws Exception {
		return readContentDescriptor(genericEntity, cache, true);
	}
	protected static ContentDescriptor readContentDescriptor(Entity genericEntity, HashMap<String, Object> cache, boolean useCache) throws Exception {
		ContentDescriptor content = null;
		try {
			String id = readStringProperty(genericEntity, "uid", null);
			if (useCache) {
				content = (ContentDescriptor)StimulatedPlanningFactory.getObject(ContentDescriptor.class.getName(), id);
			}
			if (content == null) {
				content = (ContentDescriptor)cache.get(id);
			}
		} catch (Exception exc) {
			exc.printStackTrace();
		}
		if (content == null) {
			ArrayList<GenericDescriptor> relationList1 = null;
			ArrayList<GenericDescriptor> relationList2 = null;
			content = new ContentDescriptor(readStringProperty(genericEntity, "uid", null),
					readStringProperty(genericEntity, "title", null), readStringProperty(genericEntity, "description", null),
					readStringProperty(genericEntity, "url", null));
			cache.put(content.getId(), content);
			StimulatedPlanningFactory.addObject(content, genericEntity);
			relationList1 = readToManyRelation(content, "informationObjects", InformationObject.class.getName(), true, cache, useCache);
			//log.info("informationObjects: "+relationList1.size());
			relationList2 = readToManyRelation(content, "selectionObjects", SelectionObject.class.getName(), true, cache, useCache);
			//log.info("selectionObjects: "+relationList2.size());
			if (relationList1.size()>0 && relationList2.size()>0) {
				relationList1.addAll(relationList2);
			} else if (relationList2.size()>0) {
				relationList1 = relationList2;
			}
			ArrayList<InformationObject> relationListAll = (ArrayList)relationList1;
			Collections.sort(relationListAll, new Comparator<InformationObject>( ) {
				public int compare(InformationObject o1, InformationObject o2) {
					if (o1 == null || o2 == null) {
						//log.info("compare (null): "+o1+", "+o2);
						return 0;
					}
					//log.info("compare: "+o1+", "+o2);
					return o1.getSequence()-o2.getSequence();
				}
			});
			for (GenericDescriptor generic : relationListAll) {
				content.addInformationObject((InformationObject)generic);
			}
	//		relationList = readToManyRelation(content, "selectionObjects", SelectionObject.class.getName(), true);
	//		for (GenericDescriptor generic : relationList) {
	//			content.addSelectionObject((SelectionObject)generic);
	//		}
		}
		return content;
	}
	
	public static void writeDescriptor(ContentDescriptor content) throws Exception {
		//log.info("writeDescriptor (ContentDescriptor): "+content.getTitle()+", "+content.getClass().getName()+", "+content.getId());
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	
		try {
			Entity contentEntity = createGenericEntity(content);
	
			writeToManyRelation(content, content.getInformationObjects(), "informationObjects", content.informationObjects.size(), true);
			writeToManyRelation(content, content.getSelectionObjects(), "selectionObjects", content.selectionObjects.size(), true);
	
			datastore.put(contentEntity);
		} catch (Exception e1) {
			log.info("FATAL: Writing content failed.");
			e1.printStackTrace();
	
		}
	
	}

	protected static UserPlan readUserPlan(Entity genericEntity, HashMap<String, Object> cache) throws Exception {
		return readUserPlan(genericEntity, cache, true);
	}
	protected static UserPlan readUserPlan(Entity genericEntity, HashMap<String, Object> cache, boolean useCache) throws Exception {
		UserPlan plan = null;
		try {
			String id = readStringProperty(genericEntity, "uid", null);
			if (useCache) {
				plan = (UserPlan)StimulatedPlanningFactory.getObject(UserPlan.class.getName(), id);
			}
			if (plan == null) {
				plan = (UserPlan)cache.get(id);
			}
		} catch (Exception exc) {
			exc.printStackTrace();
		}
		if (plan == null) {
			ArrayList<GenericDescriptor> relationList = null;
			User user = getUser(readStringProperty(genericEntity, "userid", null), cache);
			CourseDescriptor course = (CourseDescriptor)StimulatedPlanningFactory.getObject(CourseDescriptor.class.getName(), readStringProperty(genericEntity, "course", null));
			plan = new UserPlan(readStringProperty(genericEntity, "uid", null), user);
			cache.put(plan.getId(), plan);
			StimulatedPlanningFactory.addObject(plan, genericEntity);
			plan.setCourse(course);
			plan.setPlannedTimePerWeek(readStringProperty(genericEntity, "plannedTimePerWeek", null));
			plan.setAllCourseIntention(readBooleanProperty(genericEntity, "isAllCourseIntention", false));
			plan.setIntentionCompleted(readBooleanProperty(genericEntity, "intentionCompleted", false));
			plan.setObstacles(readStringProperty(genericEntity, "obstacles", null));
			plan.setCopingPlan(readStringProperty(genericEntity, "copingPlan", null));
	
			relationList = readToManyRelation(plan, "userGoals", UserGoal.class.getName(), true, cache, useCache);
			for (GenericDescriptor generic : relationList) {
				plan.addGoal((UserGoal)generic);
			}
			relationList = readToManyRelation(plan, "planItems", PlanItem.class.getName(), true, cache, useCache);
			for (GenericDescriptor generic : relationList) {
				plan.addPlanItem((PlanItem)generic);
			}
			EmbeddedEntity ee = (EmbeddedEntity) genericEntity.getProperty("completionStatusMap");
			if (ee != null) {
				Map<String, Object> map = ee.getProperties();
				Set<String> keys = map.keySet();
				for (String key : keys) {
					Object val = map.get(key);
					if (val != null) {
						if (val instanceof String) {
							plan.completionStatusMap.put(key, (String)val);
						} else if (val instanceof Text) {
							plan.completionStatusMap.put(key, ((Text)val).getValue());
						} else if (val instanceof Integer) {
							plan.completionStatusMap.put(key, String.valueOf(val));
						}
					}
				}
			}
		}
		return plan;
	}
	
	public static UserPlan readUserPlan(User user, CourseDescriptor course) {
		UserPlan plan = null;
		try {
			//log.info("read userPlan for "+user.getName());
			DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
			Filter userFilter = new FilterPredicate("userid", FilterOperator.EQUAL, user.getId());
			Filter courseFilter = new FilterPredicate("course", FilterOperator.EQUAL, course.getId());
			CompositeFilter userCourseFilter = CompositeFilterOperator.and(userFilter, courseFilter);
			Query q = new Query(UserPlan.class.getName()).setFilter(userCourseFilter);
			
			PreparedQuery pq = datastore.prepare(q);
	
			for (Entity result : pq.asIterable()) {
				String id = (String) result.getProperty("uid");
				//log.info("read userPlan for "+user.getName()+", "+id);
				plan = (UserPlan)readDescriptor(UserPlan.class.getName(), id, new HashMap<String, Object>(), result);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return plan;
	}

	public static void writeDescriptor(UserPlan userPlan) throws Exception {
		//log.info("writeDescriptor (UserPlan): "+userPlan.getUser().getName()+", "+userPlan.getClass().getName()+", "+userPlan.getId());
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	
		try {
			Entity planEntity = createGenericUserEntity(userPlan);
			String courseId = userPlan.getCourse().getId();
			planEntity.setProperty("course", courseId);
			planEntity.setProperty("plannedTimePerWeek", userPlan.getPlannedTimePerWeek());
			planEntity.setProperty("isAllCourseIntention", userPlan.isAllCourseIntention());
			planEntity.setProperty("obstacles", new Text(userPlan.getObstacles()));
			planEntity.setProperty("copingPlan", new Text(userPlan.getCopingPlan()));
			planEntity.setProperty("intentionCompleted", userPlan.isIntentionCompleted());
			
	
			EmbeddedEntity ee = new EmbeddedEntity();
		    Map<String, String> map = userPlan.completionStatusMap;
	
		    for (String key : map.keySet()) { 
				String value = map.get(key);
				if (value != null && value.getBytes().length > 1500) {
					ee.setProperty(key, new Text(value));
				} else {
					ee.setProperty(key, value);
				}
		    }
	
		    planEntity.setProperty("completionStatusMap", ee);
	
	
			writeToManyRelation(userPlan, userPlan.getGoals(), "userGoals", userPlan.goals.size(), true);
			writeToManyRelation(userPlan, userPlan.getPlanItems(), "planItems", userPlan.planItems.size(), true);
			
			datastore.put(planEntity);
		} catch (Exception e1) {
			log.info("FATAL: Writing UserPlan failed.");
			e1.printStackTrace();
	
		}
	
	}

	protected static UserGoal readUserGoal(Entity genericEntity, HashMap<String, Object> cache) throws Exception {
		return readUserGoal(genericEntity, cache, true);
	}
	protected static UserGoal readUserGoal(Entity genericEntity, HashMap<String, Object> cache, boolean useCache) throws Exception {
		UserGoal userGoal = null;
		try {
			String id = readStringProperty(genericEntity, "uid", null);
			if (useCache) {
				userGoal = (UserGoal)StimulatedPlanningFactory.getObject(UserGoal.class.getName(), id);
			}
			if (userGoal == null) {
				userGoal = (UserGoal)cache.get(id);
			}
		} catch (Exception exc) {
			exc.printStackTrace();
		}
		if (userGoal == null) {
			ArrayList<GenericDescriptor> relationList = null;
			User user = getUser(readStringProperty(genericEntity, "userid", null), cache);
			GoalDescriptor goal = (GoalDescriptor)StimulatedPlanningFactory.getObject(GoalDescriptor.class.getName(), readStringProperty(genericEntity, "goal", null));				
			userGoal = new UserGoal(readStringProperty(genericEntity, "uid", null), user, goal);
			cache.put(userGoal.getId(), userGoal);
			StimulatedPlanningFactory.addObject(userGoal, genericEntity);
			userGoal.setCompletionGoal(readStringProperty(genericEntity, "completionGoal", null));
			userGoal.setStatus(readStatus(genericEntity));
			relationList = readToManyRelation(userGoal, "lessons", UserLesson.class.getName(), true, cache, useCache);
			for (GenericDescriptor generic : relationList) {
				userGoal.addLesson((UserLesson)generic);
			}
		}
		return userGoal;
	}
	
	public static void writeDescriptor(UserGoal userGoal) throws Exception {
		//log.info("writeDescriptor (UserGoal): "+userGoal.getUser().getName()+", "+userGoal.getClass().getName()+", "+userGoal.getId());
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	
		try {
			Entity goalEntity = createGenericUserEntity(userGoal);
			goalEntity.setProperty("goal", userGoal.getGoalDescriptor().getId());
			goalEntity.setProperty("completionGoal", userGoal.getCompletionGoal());
			goalEntity.setProperty("status", userGoal.getStatus().toString());
	
			writeToManyRelation(userGoal, userGoal.getLessons(), "lessons", userGoal.lessons.size(), true);
			
			datastore.put(goalEntity);
		} catch (Exception e1) {
			log.info("FATAL: Writing goal failed.");
			e1.printStackTrace();
	
		}
	
	}

	protected static UserLesson readUserLesson(Entity genericEntity, HashMap<String, Object> cache) throws Exception {
		return readUserLesson(genericEntity, cache, true);
	}
	protected static UserLesson readUserLesson(Entity genericEntity, HashMap<String, Object> cache, boolean useCache) throws Exception {
		UserLesson userLesson = null;
		try {
			String id = readStringProperty(genericEntity, "uid", null);
			if (useCache) {
				userLesson = (UserLesson)StimulatedPlanningFactory.getObject(UserLesson.class.getName(), id);
			}
			if (userLesson == null) {
				userLesson = (UserLesson)cache.get(id);
			}
		} catch (Exception exc) {
			exc.printStackTrace();
		}
		if (userLesson == null) {
			ArrayList<GenericDescriptor> relationList = null;
			User user = getUser(readStringProperty(genericEntity, "userid", null), cache);
			LessonDescriptor lesson = (LessonDescriptor)StimulatedPlanningFactory.getObject(LessonDescriptor.class.getName(), readStringProperty(genericEntity, "lesson", null));				
			userLesson = new UserLesson(readStringProperty(genericEntity, "uid", null), user, lesson);
			cache.put(userLesson.getId(), userLesson);
			StimulatedPlanningFactory.addObject(userLesson, genericEntity);
			userLesson.setStatus(readStatus(genericEntity));
			relationList = readToManyRelation(userLesson, "contents", UserContent.class.getName(), true, cache, useCache);
			for (GenericDescriptor generic : relationList) {
				userLesson.addContent((UserContent)generic);
			}
		}
		return userLesson;
	}
	
	public static void writeDescriptor(UserLesson userLesson) throws Exception {
		//log.info("writeDescriptor (UserLesson): "+userLesson.getUser().getName()+", "+userLesson.getClass().getName()+", "+userLesson.getId());
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	
		try {
			Entity lessonEntity = createGenericUserEntity(userLesson);
			lessonEntity.setProperty("lesson", userLesson.getLesson().getId());
			lessonEntity.setProperty("status", userLesson.getStatus().toString());
	
			writeToManyRelation(userLesson, userLesson.getContents(), "contents", userLesson.contents.size(), true);
			
			datastore.put(lessonEntity);
		} catch (Exception e1) {
			log.info("FATAL: Writing goal failed.");
			e1.printStackTrace();
	
		}
	
	}

	protected static UserContent readUserContent(Entity genericEntity, HashMap<String, Object> cache) throws Exception {
		return readUserContent(genericEntity, cache, true);
	}
	protected static UserContent readUserContent(Entity genericEntity, HashMap<String, Object> cache, boolean useCache) throws Exception {
		UserContent userContent = null;
		try {
			String id = readStringProperty(genericEntity, "uid", null);
			if (useCache) {
				userContent = (UserContent)StimulatedPlanningFactory.getObject(UserContent.class.getName(), id);
			}
			if (userContent == null) {
				userContent = (UserContent)cache.get(id);
			}
		} catch (Exception exc) {
			exc.printStackTrace();
		}
		if (userContent == null) {
			User user = getUser(readStringProperty(genericEntity, "userid", null), cache);
			ContentDescriptor content = (ContentDescriptor)StimulatedPlanningFactory.getObject(ContentDescriptor.class.getName(), readStringProperty(genericEntity, "content", null));				
			userContent = new UserContent(readStringProperty(genericEntity, "uid", null), user, content);
			userContent.setStatus(readStatus(genericEntity));
			StimulatedPlanningFactory.addObject(userContent, genericEntity);
		}
		return userContent;
	}
	
	public static void writeDescriptor(UserContent userContent) throws Exception {
		//log.info("writeDescriptor (UserContent): "+userContent.getUser().getName()+", "+userContent.getClass().getName()+", "+userContent.getId());
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	
		try {
			Entity lessonEntity = createGenericUserEntity(userContent);
			lessonEntity.setProperty("content", userContent.getContent().getId());
			lessonEntity.setProperty("status", userContent.getStatus().toString());
	
			datastore.put(lessonEntity);
		} catch (Exception e1) {
			log.info("FATAL: Writing goal failed.");
			e1.printStackTrace();
	
		}
	
	}

	protected static PlanItem readPlanItem(Entity genericEntity, HashMap<String, Object> cache) throws Exception {
		return readPlanItem(genericEntity, cache, true);
	}
	protected static PlanItem readPlanItem(Entity genericEntity, HashMap<String, Object> cache, boolean useCache) throws Exception {
		PlanItem planItem = null;
		try {
			String id = readStringProperty(genericEntity, "uid", null);
			if (useCache) {
				planItem = (PlanItem)StimulatedPlanningFactory.getObject(PlanItem.class.getName(), id);
			}
			if (planItem == null) {
				planItem = (PlanItem)cache.get(id);
			}
		} catch (Exception exc) {
			exc.printStackTrace();
		}
		if (planItem == null) {
			User user = getUser(readStringProperty(genericEntity, "userid", null), cache);
			LessonDescriptor lesson = (LessonDescriptor)StimulatedPlanningFactory.getObject(LessonDescriptor.class.getName(), readStringProperty(genericEntity, "lesson", null));
			String jsonPlanItem = readStringProperty(genericEntity, "jsonPlanItem", null);
			planItem = new PlanItem(readStringProperty(genericEntity, "uid", null), user, lesson, jsonPlanItem);
			cache.put(planItem.getId(), planItem);
			StimulatedPlanningFactory.addObject(planItem, genericEntity);
			planItem.setStatus(readStatus(genericEntity));
			planItem.setPlanStatus(readPlanStatus(genericEntity));
			planItem.setPlanCompletionStatus(readPlanCompletionStatus(genericEntity));
			//log.info("readDescriptor: planItem: pre trackPlanStatus: "+planItem.getId()+", "+planItem.getTitle()+", "+planItem.getStatus());
			planItem.trackPlanStatus();
			planItem.updateMapAndJson();
			writeDescriptor(planItem);
			//log.info("readDescriptor: planItem: post trackPlanStatus: "+planItem.getId()+", "+planItem.getTitle()+", "+planItem.getStatus());
		}
		return planItem;
	}
	
	public static void writeDescriptor(PlanItem planItem) throws Exception {
		//log.info("writeDescriptor (PlanItem): "+planItem.getUser().getName()+", "+planItem.getClass().getName()+", "+planItem.getId());
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	
		try {
			Entity planItemEntity = createGenericUserEntity(planItem);
			planItemEntity.setProperty("lesson", planItem.getLesson().getId());
			planItemEntity.setProperty("status", planItem.getStatus().toString());
			planItemEntity.setProperty("planStatus", planItem.getPlanStatus().toString());
			planItemEntity.setProperty("planCompletionStatus", planItem.getPlanCompletionStatus().toString());
			planItemEntity.setProperty("jsonPlanItem", planItem.getJsonPlanItem());
	
			datastore.put(planItemEntity);
		} catch (Exception e1) {
			log.info("FATAL: Writing goal failed.");
			e1.printStackTrace();
	
		}
	
	}

	protected static UserProfile readUserProfile(Entity genericEntity, HashMap<String, Object> cache) throws Exception {
		return readUserProfile(genericEntity, cache, true);
	}
	protected static UserProfile readUserProfile(Entity genericEntity, HashMap<String, Object> cache, boolean useCache) throws Exception {
		UserProfile userProfile = null;
		try {
			String id = readStringProperty(genericEntity, "uid", null);
			if (useCache) {
				userProfile = (UserProfile)StimulatedPlanningFactory.getObject(UserProfile.class.getName(), id);
			}
			if (userProfile == null) {
				userProfile = (UserProfile)cache.get(id);
			}
		} catch (Exception exc) {
			exc.printStackTrace();
		}
		if (userProfile == null) {
			User user = getUser(readStringProperty(genericEntity, "userid", null), cache);
			userProfile = new UserProfile(readStringProperty(genericEntity, "uid", null), user, readStringProperty(genericEntity, "email", null));
			userProfile.setFullName(readStringProperty(genericEntity, "fullName", null));
			cache.put(userProfile.getId(), userProfile);
			StimulatedPlanningFactory.addObject(userProfile, genericEntity);
		}
		return userProfile;
	}
	
	public static ArrayList<UserProfile> getUserProfiles() {
		ArrayList<UserProfile> profiles = new ArrayList<UserProfile>();
		UserProfile profile = null;
		try {
			DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
			Query q = new Query(UserProfile.class.getName());
			
			PreparedQuery pq = datastore.prepare(q);
	
			for (Entity result : pq.asIterable()) {
				String id = (String) result.getProperty("uid");
				profile = (UserProfile)readDescriptor(UserProfile.class.getName(), id, new HashMap<String, Object>(), result);
				profiles.add(profile);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return profiles;
	}

	public static void writeDescriptor(UserProfile userProfile) throws Exception {
		//log.info("writeDescriptor (UserProfile): "+userProfile.getUser().getName()+", "+userProfile.getClass().getName()+", "+userProfile.getId());
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	
		try {
			Entity profileEntity = createGenericUserEntity(userProfile);
			profileEntity.setProperty("email", userProfile.getEmail());
			profileEntity.setProperty("fullName", userProfile.getFullName());
	
			datastore.put(profileEntity);
		} catch (Exception e1) {
			log.info("FATAL: Writing goal failed.");
			e1.printStackTrace();
	
		}
	
	}

	protected static UserOnlineStatus readUserOnlineStatus(Entity genericEntity, HashMap<String, Object> cache) throws Exception {
		return readUserOnlineStatus(genericEntity, cache, true);
	}
	protected static UserOnlineStatus readUserOnlineStatus(Entity genericEntity, HashMap<String, Object> cache, boolean useCache) throws Exception {
		UserOnlineStatus onlineStatus = null;
		try {
			String id = readStringProperty(genericEntity, "uid", null);
			if (useCache) {
				onlineStatus = (UserOnlineStatus)StimulatedPlanningFactory.getObject(UserOnlineStatus.class.getName(), id);
			}
			if (onlineStatus == null) {
				onlineStatus = (UserOnlineStatus)cache.get(id);
			}
		} catch (Exception exc) {
			exc.printStackTrace();
		}
		if (onlineStatus == null) {
			User user = getUser(readStringProperty(genericEntity, "userid", null), cache);
			onlineStatus = new UserOnlineStatus(readStringProperty(genericEntity, "uid", null),
					user);
			cache.put(onlineStatus.getId(), onlineStatus);
			StimulatedPlanningFactory.addObject(onlineStatus, genericEntity);
			onlineStatus.setLastAccess(readDateProperty(genericEntity, "lastAccess", null));
			onlineStatus.setLastUrl(readStringProperty(genericEntity, "lastUrl", null));
			onlineStatus.setUrl(readStringProperty(genericEntity, "url", null));
			onlineStatus.setTitle(readStringProperty(genericEntity, "title", null));
			onlineStatus.setDescription(readStringProperty(genericEntity, "description", null));
			user.setOnlineStatus(onlineStatus);
		}
		return onlineStatus;
	}
	
	protected static UserOnlineStatus readUserOnlineStatus(String id, User user, HashMap<String, Object> cache) throws Exception {
		UserOnlineStatus onlineStatus = null;
		try {
			onlineStatus = (UserOnlineStatus)StimulatedPlanningFactory.getObject(UserOnlineStatus.class.getName(), id);
			if (onlineStatus == null) {
				onlineStatus = (UserOnlineStatus)cache.get(id);
			}
		} catch (Exception exc) {
			exc.printStackTrace();
		}
		if (onlineStatus == null) {
			DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

			Entity genericEntity = null;

			try {
				genericEntity = datastore.get(KeyFactory.createKey(UserOnlineStatus.class.getName(), id));
			} catch (Exception e) {
				e.printStackTrace();
				genericEntity = null;
			}
			if (genericEntity != null) {
				onlineStatus = new UserOnlineStatus(readStringProperty(genericEntity, "uid", null),
						user);
				cache.put(onlineStatus.getId(), onlineStatus);
				StimulatedPlanningFactory.addObject(onlineStatus, genericEntity);
				user.setOnlineStatus(onlineStatus);
				onlineStatus.setLastAccess(new Date(Long.parseLong(readStringProperty(genericEntity, "lastAccess", null))));
				onlineStatus.setLastUrl(readStringProperty(genericEntity, "lastUrl", null));
				onlineStatus.setTitle(readStringProperty(genericEntity, "title", null));
				onlineStatus.setDescription(readStringProperty(genericEntity, "description", null));
				return onlineStatus;
			}
		}
		return null;
	}

	public static UserOnlineStatus readUserOnlineStatus(User user) {
		UserOnlineStatus onlineStatus = null;
		try {
			//.info("read userOnlineStatus for "+user.getName());
			DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
			Filter userFilter = new FilterPredicate("userid", FilterOperator.EQUAL, user.getId());
			Query q = new Query(UserOnlineStatus.class.getName()).setFilter(userFilter);
			
			PreparedQuery pq = datastore.prepare(q);
	
			for (Entity result : pq.asIterable()) {
				String id = (String) result.getProperty("uid");
				//log.info("read userPlan for "+user.getName()+", "+id);
				onlineStatus = (UserOnlineStatus)readDescriptor(UserOnlineStatus.class.getName(), id, new HashMap<String, Object>(), result);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return onlineStatus;
	}

	public static void writeDescriptor(UserOnlineStatus userOnlineStatus) throws Exception {
		if (!StimulatedPlanningFactory.userGuest.equals(userOnlineStatus.getUser().getName()) && !StimulatedPlanningFactory.userUnknown.equals(userOnlineStatus.getUser().getId())) {
			//log.info("writeDescriptor (UserOnlineStatus): "+userOnlineStatus.getUser().getName()+", "+userOnlineStatus.getClass().getName()+", "+userOnlineStatus.getId());
			DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

			try {
				Entity userOnlineStatusEntity = createGenericUserEntity(userOnlineStatus);
				userOnlineStatusEntity.setProperty("lastAccess", userOnlineStatus.getLastAccess().getTime());
				userOnlineStatusEntity.setProperty("lastUrl", userOnlineStatus.getLastUrl());

				datastore.put(userOnlineStatusEntity);
			} catch (Exception e1) {
				log.info("FATAL: Writing goal failed.");
				e1.printStackTrace();
			}
		}
	}


	protected static Clan readClan(Entity genericEntity, HashMap<String, Object> cache) throws Exception {
		return readClan(genericEntity, cache, true);
	}
	protected static Clan readClan(Entity genericEntity, HashMap<String, Object> cache, boolean useCache) throws Exception {
		ArrayList<GenericDescriptor> relationList = null;
		Clan clan = null;
		if (StimulatedPlanningFactory.getNoOfClans() > 0) {
			clan = StimulatedPlanningFactory.getClan(readStringProperty(genericEntity, "uid", null));
		}
		if (clan == null) {
			try {
				String id = readStringProperty(genericEntity, "uid", null);
				if (useCache) {
					clan = (Clan)StimulatedPlanningFactory.getObject(Clan.class.getName(), id);
				}
				if (clan == null) {
					clan = (Clan)cache.get(id);
				}
			} catch (Exception exc) {
				exc.printStackTrace();
			}
		}
		if (clan == null) {
			clan = new Clan(readStringProperty(genericEntity, "uid", null),
				readStringProperty(genericEntity, "title", null), readStringProperty(genericEntity, "description", null),
				readStringProperty(genericEntity, "url", null));
			cache.put(clan.getId(), clan);
			clan.setClanLogo(readStringProperty(genericEntity, "clanLogo", null));
			//relationList = readToManyRelation(clan, "userStati", UserOnlineStatus.class.getName(), true, cache);
			//for (GenericDescriptor generic : relationList) {
			//	clan.addUserOnlineStatus((UserOnlineStatus)generic);
			//}
		}
		return clan;

	}
	
	public static HashArrayList<Clan> readAllClans() {
		HashArrayList<Clan> clans = new HashArrayList<Clan>();
		Clan clan = null;
		try {
			DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
			Query q = new Query(Clan.class.getName());
			
			PreparedQuery pq = datastore.prepare(q);

			for (Entity result : pq.asIterable()) {
				String id = (String) result.getProperty("uid");
				clan = (Clan)readDescriptor(Clan.class.getName(), id, new HashMap<String, Object>(), result);
				clans.add(clan);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return clans;
	}

	public static void writeDescriptor(Clan generic) throws Exception {
		//log.info("writeDescriptor (Clan): "+generic.getTitle()+", "+generic.getClass().getName()+", "+generic.getId());
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

		try {
			Entity entity = createInformationObjectEntity(generic);
			
			entity.setProperty("clanLogo", generic.getClanLogo());
			writeToManyRelation(generic, generic.getUserOnlineStatus(), "userStati", generic.userCount(), true);

			datastore.put(entity);
		} catch (Exception e1) {
			log.info("FATAL: Writing generic entity failed.");
			e1.printStackTrace();

		}

	}




	public static SelectionObject readSelectionObject(Entity genericEntity, HashMap<String, Object> cache) throws Exception {
		return readSelectionObject(genericEntity, cache, true);
	}
	public static SelectionObject readSelectionObject(Entity genericEntity, HashMap<String, Object> cache, boolean useCache) throws Exception {
		SelectionObject selectionObject = null;
		try {
			String id = readStringProperty(genericEntity, "uid", null);
			if (useCache) {
				selectionObject = (SelectionObject)StimulatedPlanningFactory.getObject(SelectionObject.class.getName(), id);
			}
			if (selectionObject == null) {
				selectionObject = (SelectionObject)cache.get(id);
			}
		} catch (Exception exc) {
			exc.printStackTrace();
		}
		if (selectionObject == null) {
			ArrayList<GenericDescriptor> relationList = null;
			selectionObject = new SelectionObject(readStringProperty(genericEntity, "uid", null),
					readStringProperty(genericEntity, "title", null), 
					readStringProperty(genericEntity, "description", null),
					readStringProperty(genericEntity, "url", null), 
					readBooleanProperty(genericEntity, "isControlGroupVisible", true),
					readBooleanProperty(genericEntity, "isClanAVisible", true),
					readBooleanProperty(genericEntity, "isClanBVisible", true)
			);
			cache.put(selectionObject.getId(), selectionObject);
			StimulatedPlanningFactory.addObject(selectionObject, genericEntity);
			selectionObject.setContent(readStringProperty(genericEntity, "content", null));
			selectionObject.setSequence(Integer.valueOf(readStringProperty(genericEntity, "sequence", null)));
			selectionObject.setType(readSelectionObjectType(genericEntity));
			selectionObject.setPurpose(readSelectionObjectPurpose(genericEntity));
			selectionObject.setDeadline(readDateProperty(genericEntity, "deadline", null));
			relationList = readToManyRelation(selectionObject, "options", SelectionOption.class.getName(), true, cache, useCache);
			for (GenericDescriptor generic : relationList) {
				selectionObject.addOption((SelectionOption)generic);
			}
		}
		return selectionObject;
	}
	
	protected static SelectionObjectType readSelectionObjectType(Entity genericEntity) {
		if (genericEntity.hasProperty("type")) {
			SelectionObjectType status = SelectionObjectType.valueOf((String)genericEntity.getProperty("type"));
			return status;
		} else {
			return SelectionObjectType.SINGLE_USER_SELECTION;
		}
	}

	protected static SelectionObjectPurpose readSelectionObjectPurpose(Entity genericEntity) {
		if (genericEntity.hasProperty("purpose")) {
			SelectionObjectPurpose status = SelectionObjectPurpose.valueOf((String)genericEntity.getProperty("purpose"));
			return status;
		} else {
			return SelectionObjectPurpose.TEST;
		}
	}

	public static void writeDescriptor(SelectionObject generic) throws Exception {
		//log.info("writeDescriptor (SelectionObject): "+generic.getTitle()+", "+generic.getClass().getName()+", "+generic.getId());
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

		try {
			Entity entity = createInformationObjectEntity(generic);
			entity.setProperty("type", generic.getType().toString());
			entity.setProperty("purpose", generic.getPurpose().toString());
			if (generic.hasDeadline()) {
				entity.setProperty("deadline", generic.getDeadline().getTime());
			}

			writeToManyRelation(generic, generic.getOptions(), "options", generic.getOptionCount(), true);

			datastore.put(entity);
		} catch (Exception e1) {
			log.info("FATAL: Writing generic entity failed.");
			e1.printStackTrace();

		}

	}

	public static SelectionOption readSelectionOption(Entity genericEntity, HashMap<String, Object> cache) {
		return readSelectionOption(genericEntity, cache, true);
	}
	public static SelectionOption readSelectionOption(Entity genericEntity, HashMap<String, Object> cache, boolean useCache) {
		SelectionOption selectionOption = null;
		try {
			String id = readStringProperty(genericEntity, "uid", null);
			if (useCache) {
				selectionOption = (SelectionOption)StimulatedPlanningFactory.getObject(SelectionOption.class.getName(), id);
			}
			if (selectionOption == null) {
				selectionOption = (SelectionOption)cache.get(id);
			}
		} catch (Exception exc) {
			exc.printStackTrace();
		}
		if (selectionOption == null) {
			selectionOption = new SelectionOption(readStringProperty(genericEntity, "uid", null),
					readStringProperty(genericEntity, "title", null), readStringProperty(genericEntity, "description", null),
					readStringProperty(genericEntity, "url", null));
			cache.put(selectionOption.getId(), selectionOption);
			StimulatedPlanningFactory.addObject(selectionOption, genericEntity);
			selectionOption.setContent(readStringProperty(genericEntity, "content", null));
			selectionOption.setCorrect(readBooleanProperty(genericEntity, "isCorrect", false));
		}
		return selectionOption;
	}
	
	public static void writeDescriptor(SelectionOption generic) throws Exception {
		//log.info("writeDescriptor (SelectionOption): "+generic.getTitle()+", "+generic.getClass().getName()+", "+generic.getId());
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

		try {
			Entity entity = createInformationObjectEntity(generic);
			entity.setProperty("isCorrect", generic.isCorrect());				

			datastore.put(entity);
		} catch (Exception e1) {
			log.info("FATAL: Writing generic entity failed.");
			e1.printStackTrace();

		}

	}

	protected static UserSelectedOption readUserSelectedOption(Entity genericEntity, HashMap<String, Object> cache) throws Exception {
		return readUserSelectedOption(genericEntity, cache, true);
	}
	
	protected static UserSelectedOption readUserSelectedOption(Entity genericEntity, HashMap<String, Object> cache, boolean useCache) throws Exception {
		UserSelectedOption userSelectedOption = null;
		try {
			String id = readStringProperty(genericEntity, "uid", null);
			if (useCache) {
				userSelectedOption = (UserSelectedOption)StimulatedPlanningFactory.getObject(UserSelectedOption.class.getName(), id);
			}
			if (userSelectedOption == null) {
				userSelectedOption = (UserSelectedOption)cache.get(id);
			}
		} catch (Exception exc) {
			exc.printStackTrace();
		}
		if (userSelectedOption == null) {
			User user = getUser(readStringProperty(genericEntity, "userid", null), cache);
			SelectionObject selectionObject = (SelectionObject)StimulatedPlanningFactory.getObject(SelectionObject.class.getName(), readStringProperty(genericEntity, "selectionObject", null));	
			SelectionOption selectedOption = (SelectionOption)StimulatedPlanningFactory.getObject(SelectionOption.class.getName(), readStringProperty(genericEntity, "selectedOption", null));	
	
			userSelectedOption = new UserSelectedOption(readStringProperty(genericEntity, "uid", null), user);
			cache.put(userSelectedOption.getId(), userSelectedOption);
			StimulatedPlanningFactory.addObject(userSelectedOption, genericEntity);
			userSelectedOption.setLastAccess(new Date(Long.parseLong(readStringProperty(genericEntity, "lastAccess", null))));
			userSelectedOption.setSelectionObject(selectionObject);
			userSelectedOption.setSelectedOption(selectedOption);
		}
		return userSelectedOption;
	}

	public static UserSelectedOption readUserSelectionOption(User user, SelectionObject selectionObject, SelectionOption selectionOption) {
		UserSelectedOption selectedOption = null;
		try {
			DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
			Filter userFilter = new FilterPredicate("userid", FilterOperator.EQUAL, user.getId());
			Filter selectionObjectFilter = new FilterPredicate("selectionObject", FilterOperator.EQUAL, selectionObject.getId());
			Filter selectionOptionFilter = new FilterPredicate("selectedOption", FilterOperator.EQUAL, selectionOption.getId());
			CompositeFilter userSelectedFilter = CompositeFilterOperator.and(userFilter, selectionObjectFilter, selectionOptionFilter);
			Query q = new Query(UserSelectedOption.class.getName()).setFilter(userSelectedFilter);
			
			PreparedQuery pq = datastore.prepare(q);

			for (Entity result : pq.asIterable()) {
				String id = (String) result.getProperty("uid");
				//log.info("read userPlan for "+user.getName()+", "+id);
				selectedOption = (UserSelectedOption)readDescriptor(UserSelectedOption.class.getName(), id, new HashMap<String, Object>(), result);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return selectedOption;
	}
	
	
	public static HashMap<String, UserSelectedOption> readUserSelectionOptions(SelectionObject selectionObject, SelectionOption selectionOption) {
		UserSelectedOption selectedOption = null;
		HashMap<String, UserSelectedOption> optionMap = new HashMap<>();
		try {
			DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
			Filter selectionObjectFilter = new FilterPredicate("selectionObject", FilterOperator.EQUAL, selectionObject.getId());
			Filter selectionOptionFilter = new FilterPredicate("selectedOption", FilterOperator.EQUAL, selectionOption.getId());
			CompositeFilter userSelectedFilter = CompositeFilterOperator.and(selectionObjectFilter, selectionOptionFilter);
			Query q = new Query(UserSelectedOption.class.getName()).setFilter(userSelectedFilter);
			
			PreparedQuery pq = datastore.prepare(q);

			for (Entity result : pq.asIterable()) {
				String id = (String) result.getProperty("uid");
				String userid = (String) result.getProperty("userid");
				//log.info("read userPlan for "+user.getName()+", "+id);
				selectedOption = (UserSelectedOption)readDescriptor(UserSelectedOption.class.getName(), id, new HashMap<String, Object>(), result);
				optionMap.put(userid, selectedOption);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return optionMap;
	}
	
	
	public static void writeDescriptor(UserSelectedOption userSelectedOption) throws Exception {
		//log.info("writeDescriptor (UserSelectedOption): "+userOnlineStatus.getUser().getName()+", "+userOnlineStatus.getClass().getName()+", "+userOnlineStatus.getId());
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

		try {
			Entity userSelectedOptionEntity = createGenericUserEntity(userSelectedOption);
			userSelectedOptionEntity.setProperty("lastAccess", userSelectedOption.getLastAccess().getTime());
			userSelectedOptionEntity.setProperty("selectionObject", userSelectedOption.getSelectionObject().getId());
			userSelectedOptionEntity.setProperty("selectedOption", userSelectedOption.getSelectedOption().getId());

			datastore.put(userSelectedOptionEntity);
		} catch (Exception e1) {
			log.info("FATAL: Writing goal failed.");
			e1.printStackTrace();

		}

	}
	
	
	
	protected static InformationObject readInformationObject(Entity genericEntity, HashMap<String, Object> cache) {
		return readInformationObject(genericEntity, cache, true);
	}
	protected static InformationObject readInformationObject(Entity genericEntity, HashMap<String, Object> cache, boolean useCache) {
		InformationObject informationObject = null;
		try {
			String id = readStringProperty(genericEntity, "uid", null);
			if (useCache) {
				informationObject = (InformationObject)StimulatedPlanningFactory.getObject(InformationObject.class.getName(), id);
			}
			if (informationObject == null) {
				informationObject = (InformationObject)cache.get(id);
			}
		} catch (Exception exc) {
			exc.printStackTrace();
		}
		if (informationObject == null) {
			informationObject = new InformationObject(readStringProperty(genericEntity, "uid", null),
					readStringProperty(genericEntity, "title", null), 
					readStringProperty(genericEntity, "description", null),
					readStringProperty(genericEntity, "url", null), 
					readBooleanProperty(genericEntity, "isControlGroupVisible", true),
					readBooleanProperty(genericEntity, "isClanAVisible", true),
					readBooleanProperty(genericEntity, "isClanBVisible", true)
			);
			cache.put(informationObject.getId(), informationObject);
			StimulatedPlanningFactory.addObject(informationObject, genericEntity);
			informationObject.setContent(readStringProperty(genericEntity, "content", null));
			informationObject.setSequence(Integer.valueOf(readStringProperty(genericEntity, "sequence", null)));
		}
		return informationObject;
	}
	
	public static void writeDescriptor(InformationObject generic) throws Exception {
		//log.info("writeDescriptor (Clan): "+generic.getTitle()+", "+generic.getClass().getName()+", "+generic.getId());
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	
		try {
			Entity entity = createInformationObjectEntity(generic);
			
			datastore.put(entity);
		} catch (Exception e1) {
			log.info("FATAL: Writing generic entity failed.");
			e1.printStackTrace();
	
		}
	
	}
	
	
	
	public static void writeMessage(Message chatMessage) {
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

		Entity entity = null;
		try {
			entity = datastore.get(KeyFactory.createKey(chatMessage.getClass().getName(), chatMessage.getId()));
		} catch (Exception e) {
			//e.printStackTrace();
			entity = null;
		}
		if (entity == null) {
			entity = new Entity(chatMessage.getClass().getName(), chatMessage.getId());
		}

		entity.setProperty("uid", chatMessage.getId());
		if (chatMessage.getUser() != null) {
			entity.setProperty("user", chatMessage.getUser().getId());
		} else {
			entity.setProperty("user", null);
		}
		entity.setProperty("room", chatMessage.getRoom().getId());
		entity.setProperty("roomList", chatMessage.getRoomList().getId());
		entity.setProperty("message", new Text(chatMessage.getMessage()));
		entity.setProperty("timestamp", chatMessage.getTimeStamp());
		
		try {
			datastore.put(entity);
		} catch (Exception e1) {
			log.info("FATAL: Writing message failed.");
			e1.printStackTrace();
	
		}
	}
	

	public static ArrayList<Message> readMessagesForChat(ChatRoomList roomlist, ChatRoom room) {
		//log.info("readMessagesForChat: "+roomlist.getId()+", "+room.getId());
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		//Entity entity = null;
		
		ArrayList<Message> arrayList = new ArrayList<Message>();

		if (roomlist == null || room == null) {
			return arrayList;
		}
		
		Filter roomFilter = new FilterPredicate("room", FilterOperator.EQUAL, room.getId());
		Filter roomListFilter = new FilterPredicate("roomList", FilterOperator.EQUAL, roomlist.getId());
		CompositeFilter chatRoomFilter = CompositeFilterOperator.and(roomFilter, roomListFilter);

		Query q = new Query(Message.class.getName()).setFilter(chatRoomFilter).addSort("timestamp", SortDirection.ASCENDING);
		PreparedQuery pq = datastore.prepare(q);

		for (Entity entity : pq.asIterable()) {
			if (entity != null) {
				Message message = readMessage(entity, roomlist, room);
				if (message != null) {
					arrayList.add(message);
				}
			}
		}

		return arrayList;
	}
	
	
	public static ArrayList<Message> readMessagesForTeacherChat() {
		//log.info("readMessagesForTeacherChat.");
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		//Entity entity = null;
		
		ArrayList<Message> arrayList = new ArrayList<Message>();

		Query q = new Query(Message.class.getName()).addSort("timestamp", SortDirection.DESCENDING);
		PreparedQuery pq = datastore.prepare(q);
		
		HashMap<String, ChatRoomList> chatRoomListMap = StimulatedPlanningFactory.getChatRoomLists();
		ChatRoomList roomlist = null;
		ChatRoom room = null;
		
		for (Entity entity : pq.asIterable()) {
			if (entity != null) {
				roomlist = chatRoomListMap.get(entity.getProperty("roomList"));
				if (roomlist != null) {
					room = roomlist.getRoom((String)entity.getProperty("room"));
					if (room != null) {
						Message message = readMessage(entity, roomlist, room);
						if (message != null) {
							arrayList.add(message);
						}
					}
				}
			}
		}

		return arrayList;
	}
	
	
	public static Message readMessage(Entity msgEntity, ChatRoomList roomlist, ChatRoom room) {
		String id = readStringProperty(msgEntity, "uid", null);
		String userId = readStringProperty(msgEntity, "user", null);
		String message = readStringProperty(msgEntity, "message", null);
		Date sTimeStamp = readDateProperty(msgEntity, "timestamp", null);
		
		User user = null;
		if (userId != null) {
			user = StimulatedPlanningFactory.getUser(userId, userId);
		}
		
		Message msg = new Message(user, message, sTimeStamp.getTime(), room, roomlist, id);
		return msg;
	}
	

	protected static LessonStatus readStatus(Entity genericEntity) {
		//log.info("readStatus 1: "+genericEntity.getProperties().toString());
		if (genericEntity.hasProperty("status")) {
			//log.info("readStatus 2a: "+genericEntity.getProperty("status"));
			LessonStatus status = LessonStatus.valueOf((String)genericEntity.getProperty("status"));
			//log.info("readStatus 2b: "+status);
			return status;
		} else {
			//log.info("readStatus 3: "+LessonStatus.INITIAL);
			return LessonStatus.INITIAL;
		}
		
	}

	protected static PlanStatus readPlanStatus(Entity genericEntity) {
		if (genericEntity.hasProperty("planStatus")) {
			PlanStatus status = PlanStatus.valueOf((String)genericEntity.getProperty("planStatus"));
			return status;
		} else {
			return PlanStatus.PLANNED;
		}
		
	}
	
	protected static PlanCompletionStatus readPlanCompletionStatus(Entity genericEntity) {
		if (genericEntity.hasProperty("planCompletionStatus")) {
			PlanCompletionStatus status = PlanCompletionStatus.valueOf((String)genericEntity.getProperty("planCompletionStatus"));
			return status;
		} else {
			return PlanCompletionStatus.OPEN;
		}
		
	}
	
	public static String readStringProperty(Entity genericEntity, String key, String defaultValue) {
		Object propObj = genericEntity.getProperty(key);
		String prop = null;
		if (propObj != null) {
			if (propObj instanceof String) {
				prop = (String)propObj;
			} else if (propObj instanceof Text) {
				Text text = (Text)propObj;
				prop = text.getValue();
			} else {
				prop = propObj.toString();
			}
		}
		if (prop == null) {
			prop = defaultValue;
		}
		return prop;
	}

	public static Date readDateProperty(Entity genericEntity, String key, Date defaultValue) {
		Object propObj = genericEntity.getProperty(key);
		Date prop = null;
		if (propObj != null) {
			if (propObj instanceof Date) {
				prop = (Date)propObj;
			} else if (propObj instanceof Long) {
				prop = new Date((Long)propObj);
			} else if (propObj instanceof String) {
				prop = new Date(Long.parseLong((String)propObj));
			} else {
				log.info("cannot read date for: "+key+", "+propObj.getClass().getName()+", "+propObj+". Using default value: "+defaultValue);
				prop = defaultValue;
			}
		}
		if (prop == null) {
			prop = defaultValue;
		}
		return prop;
	}

	protected static boolean readBooleanProperty(Entity genericEntity, String key, boolean defaultValue) {
		boolean prop;
		try {
			prop = (boolean) genericEntity.getProperty(key);
		} catch (Exception e) {
			prop = defaultValue;
		}
		return prop;
	}

	public static void deleteGenericEntity(GenericDescriptor generic) {
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

		log.info("deleteGenericEntity: "+generic.getTitle()+", "+generic.getClass().getName()+", "+generic.getId());
		
		if (generic instanceof UserPlan) {
			for(UserGoal goal : ((UserPlan)generic).goals) {
				deleteGenericEntity(goal);
			}
			deleteToManyRelation(generic, "userGoals", UserGoal.class.getName());
			for(PlanItem item : ((UserPlan)generic).planItems) {
				deleteGenericEntity(item);
			}
			deleteToManyRelation(generic, "planItems", PlanItem.class.getName());
		} else if (generic instanceof UserGoal) {
			for(UserLesson lesson : ((UserGoal)generic).lessons) {
				deleteGenericEntity(lesson);
			}
			deleteToManyRelation(generic, "lessons", UserLesson.class.getName());
		} else if (generic instanceof UserLesson) {
			for(UserContent content : ((UserLesson)generic).contents) {
				deleteGenericEntity(content);
			}
			deleteToManyRelation(generic, "contents", UserContent.class.getName());
		} else if (generic instanceof CourseDescriptor) {
			for (ModuleDescriptor module: ((CourseDescriptor)generic).modules) {
				deleteGenericEntity(module);
			}
			deleteToManyRelation(generic, "modules", ModuleDescriptor.class.getName());
			for (GoalDescriptor goal: ((CourseDescriptor)generic).goals) {
				deleteGenericEntity(goal);
			}
			deleteToManyRelation(generic, "goals", GoalDescriptor.class.getName());
		} else if (generic instanceof ModuleDescriptor) {
			for (LessonDescriptor lesson: ((ModuleDescriptor)generic).lessons) {
				deleteGenericEntity(lesson);
			}
			deleteToManyRelation(generic, "lessons", LessonDescriptor.class.getName());
		} else if (generic instanceof LessonDescriptor) {
			for (ContentDescriptor content: ((LessonDescriptor)generic).contents) {
				deleteGenericEntity(content);
			}
			deleteToManyRelation(generic, "contents", ContentDescriptor.class.getName());
		} else if (generic instanceof ContentDescriptor) {
			for (InformationObject info: ((ContentDescriptor)generic).informationObjects) {
				deleteGenericEntity(info);
			}
			deleteToManyRelation(generic, "informationObjects", InformationObject.class.getName());
			for (SelectionObject sele: ((ContentDescriptor)generic).selectionObjects) {
				deleteGenericEntity(sele);
			}
			deleteToManyRelation(generic, "selectionObjects", SelectionObject.class.getName());
		} else if (generic instanceof SelectionObject) {
			for (SelectionOption option: ((SelectionObject)generic).getOptionList()) {
				deleteGenericEntity(option);
			}
			deleteToManyRelation(generic, "options", SelectionOption.class.getName());
		}

		try {
			Entity entity = StimulatedPlanningFactory.getEntity(generic.getClass().getName(), generic.getId());
			if (entity != null) {
				log.info("deleting generic object with entity: "+generic.getClass().getName()+", "+generic.getId());
				datastore.delete(entity.getKey());
				StimulatedPlanningFactory.removeObject(generic.getClass().getName(), generic.getId());
			} else {
				log.info("Warning: deleting generic object only from datastore, not from cache: "+generic.getClass().getName()+", "+generic.getId());
				Key entityKey = KeyFactory.createKey(generic.getClass().getName(), generic.getId());
				datastore.delete(entityKey);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	protected static void writeToManyRelation(GenericDescriptor source, ListIterator<? extends GenericDescriptor> targets,
			String relation, int size, boolean writeTarget) throws Exception {
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Entity entity = null;

		if (source == null || targets == null || !targets.hasNext()) {
			//log.info("writeToManyRelation: nothing to write: "+relation);
			return;
		}


		int l = 0;

		while (targets.hasNext()) {
			GenericDescriptor target = targets.next();

			if (writeTarget) {
				if (target instanceof CourseDescriptor) writeDescriptor((CourseDescriptor)target);
				else if (target instanceof ModuleDescriptor) writeDescriptor((ModuleDescriptor)target);
				else if (target instanceof GoalDescriptor) writeDescriptor((GoalDescriptor)target);
				else if (target instanceof LessonDescriptor) writeDescriptor((LessonDescriptor)target);
				else if (target instanceof ContentDescriptor) writeDescriptor((ContentDescriptor)target);
				else if (target instanceof UserPlan) writeDescriptor((UserPlan)target);
				else if (target instanceof UserGoal) writeDescriptor((UserGoal)target);
				else if (target instanceof UserLesson) writeDescriptor((UserLesson)target);
				else if (target instanceof UserContent) writeDescriptor((UserContent)target);
				else if (target instanceof PlanItem) writeDescriptor((PlanItem)target);
				else if (target instanceof Clan) writeDescriptor((Clan)target);
				else if (target instanceof UserOnlineStatus) writeDescriptor((UserOnlineStatus)target);
				else if (target instanceof SelectionObject) writeDescriptor((SelectionObject)target);
				else if (target instanceof SelectionOption) writeDescriptor((SelectionOption)target);
				else if (target instanceof InformationObject) writeDescriptor((InformationObject)target);
				else if (target instanceof UserSelectedOption) writeDescriptor((UserSelectedOption)target);
				else writeDescriptor(target);
			}

			String key = source.getClass().getName() + "_" + relation + "_" + target.getClass().getName();
			String id = source.getId() + "_" + relation + "_" + l;
			
			//log.info("writeToManyRelation: "+key+", "+id);

			try {
				entity = datastore.get(KeyFactory.createKey(key, id));
			} catch (Exception e) {
				//e.printStackTrace();
				entity = null;
			}
			if (entity == null) {
				entity = new Entity(key, id);
			}

			entity.setProperty("source", source.getId());
			entity.setProperty("sourceClass", source.getClass().getName());
			entity.setProperty("target", target.getId());
			entity.setProperty("targetClass", target.getClass().getName());
			entity.setProperty("relation", relation);
			entity.setProperty("order", l);
			entity.setProperty("size", size);

			datastore.put(entity);
			l++;
		}

	}

	public static ArrayList<GenericDescriptor> readToManyRelation(GenericDescriptor source, String relation, String targetClass, boolean readTarget, HashMap<String, Object> cache) throws Exception {
		return readToManyRelation(source, relation, targetClass, readTarget, cache, true);
	}
	public static ArrayList<GenericDescriptor> readToManyRelation(GenericDescriptor source, String relation, String targetClass, boolean readTarget, HashMap<String, Object> cache, boolean useCache) throws Exception {
		//log.info("readToManyRelation: "+source.getClass().getName()+", "+relation+", "+targetClass);
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		//Entity entity = null;
		
		ArrayList<GenericDescriptor> arrayList = new ArrayList<GenericDescriptor>();

		if (source == null || relation == null) {
			//log.info("readToManyRelation: "+source.getClass().getName()+", "+relation+", "+targetClass+", size: "+arrayList.size());
			return arrayList;
		}
		
		String key = source.getClass().getName() + "_" + relation + "_" + targetClass;
		Filter sourceFilter = new FilterPredicate("source", FilterOperator.EQUAL, source.getId());
		long l = 0;
		long size = 1; // a relation must have at least one element, otherwise wouldn't have been written. If the first element is not found, an empty array will be returned
		
		Query q = new Query(key).setFilter(sourceFilter).addSort("order", SortDirection.ASCENDING);
		PreparedQuery pq = datastore.prepare(q);

		for (Entity entity : pq.asIterable()) {
			if (entity != null) {
				String targetId = (String)entity.getProperty("target");
				long order = (long)entity.getProperty("order");
				long sizen = (long)entity.getProperty("size");
				
				if (size < sizen) {
					size = sizen;
				}
				
				GenericDescriptor generic = readDescriptor(targetClass, targetId, cache, null, useCache);
				if (generic != null) {
					arrayList.add(generic);
				} else {
					log.info("relation target not found: "+l+", "+size+", "+targetClass+", "+targetId);
				}
			}
			l++;		
		}


//		while (l < size) {
//			String key = source.getClass().getName() + "_" + relation + "_" + targetClass;
//			String id = source.getId() + "_" + relation + "_" + l;
//
//			try {
//				entity = datastore.get(KeyFactory.createKey(key, id));
//			} catch (Exception e) {
//				//e.printStackTrace();
//				entity = null;
//			}
//		}

		//log.info("readToManyRelation: "+source.getClass().getName()+" ["+source.getId()+"], "+relation+", "+targetClass+", size: "+arrayList.size());
		return arrayList;
	}

	public static void deleteToManyRelation(GenericDescriptor source, String relation, String targetClass) {
		log.info("deleteToManyRelation: "+source.getClass().getName()+", "+relation+", "+targetClass);
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		
		if (source == null || relation == null) {
			return;
		}

		String key = source.getClass().getName() + "_" + relation + "_" + targetClass;
		Filter sourceFilter = new FilterPredicate("source", FilterOperator.EQUAL, source.getId());

		Query q = new Query(key).setFilter(sourceFilter).addSort("order", SortDirection.ASCENDING);
		PreparedQuery pq = datastore.prepare(q);

		for (Entity entity : pq.asIterable()) {
			if (entity != null) {
				datastore.delete(entity.getKey());
			}
		}
		
//		Entity entity = null;
//		long l = 0;
//		long size = 1; // a relation must have at least one element, otherwise wouldn't have been written. If the first element is not found, an empty array will be returned
//		
//		while (l < size) {
//			String key = source.getClass().getName() + "_" + relation + "_" + targetClass;
//			String id = source.getId() + "_" + relation + "_" + l;
//			
//			Key relationKey = KeyFactory.createKey(key, id);
//
//			try {
//				entity = datastore.get(relationKey);
//			} catch (Exception e) {
//				//e.printStackTrace();
//				entity = null;
//			}
//			if (entity != null) {
//				long sizen = (long)entity.getProperty("size");
//				
//				if (size < sizen) {
//					size = sizen;
//				}
//				
//				datastore.delete(relationKey);
//				
//			}
//			l++;		
//		}
	}
	
	
	public static void writeLog(Map<String, String[]> parameters) throws Exception {
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

		Entity logEntity = null;
		try {
			String id = StimulatedPlanningFactory.getUUID();
			Key logKey = KeyFactory.createKey("logEntity", id);
			try {
				logEntity = datastore.get(logKey);
			} catch (Exception e) {
				// e.printStackTrace();
				logEntity = null;
			}

			if (logEntity == null) {
				logEntity = new Entity(logKey);
			}

			for (String property : parameters.keySet()) {
				String[] values = parameters.get(property);
				if (values != null && values.length>0) {
					String value = parameters.get(property)[0];
					if (value != null && value.getBytes().length > 1500) {
						logEntity.setProperty(property, new Text(value));
					} else {
						logEntity.setProperty(property, value);
					}
				}
			}

			datastore.put(logEntity);
		} catch (Exception e1) {
			log.info("FATAL: Logging failed.");
			e1.printStackTrace();

		}

	}

	public static void clearLog() {
		log.info("clearLog");
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		
		Query q = new Query("logEntity");
		
		PreparedQuery pq = datastore.prepare(q);
		
		for (Entity result : pq.asIterable()) {
			datastore.delete(result.getKey());
		}
		
	}
	

	
	public static PreparedQuery getEntitiesOfType(String entityType) {
		try {
			DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
			Query q = new Query(entityType);
			
			PreparedQuery pq = datastore.prepare(q);

			return pq;
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	

}
