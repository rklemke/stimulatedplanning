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
import com.google.appengine.api.datastore.Text;

//import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
//import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

public class PersistentStore {

	public static void writeUser(User user) throws Exception {
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

		Entity userEntity = null;
		try {
			userEntity = datastore.get(KeyFactory.createKey(user.getClass().getName(), user.getId()));
		} catch (Exception e) {
			System.out.println("User "+user.getId()+" does not exist yet. Creating it.");
			userEntity = null;
		}
		if (userEntity == null) {
			userEntity = new Entity(user.getClass().getName(), user.getId());
		}

		userEntity.setProperty("name", user.getName());
		userEntity.setProperty("uid", user.getId());

		datastore.put(userEntity);

	}

	public static User getUser(String userId) throws Exception {
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

		Entity userEntity = datastore.get(KeyFactory.createKey(User.class.getName(), userId));
		if (userEntity != null) {
			User user = new User((String) userEntity.getProperty("name"), userId);
			return user;
		}

		return null;
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
			System.out.println("FATAL: Logging failed.");
			e1.printStackTrace();

		}

	}

	private static void writeToManyRelation(GenericDescriptor source, ListIterator<? extends GenericDescriptor> targets,
			String relation, int size, boolean writeTarget) throws Exception {
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Entity entity = null;

		if (source == null || targets == null || !targets.hasNext()) {
			System.out.println("writeToManyRelation: nothing to write: "+relation);
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
				else writeDescriptor(target);
			}

			String key = source.getClass().getName() + "_" + relation + "_" + target.getClass().getName();
			String id = source.getId() + "_" + relation + "_" + l;
			
			System.out.println("writeToManyRelation: "+key+", "+id);

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

	private static ArrayList<GenericDescriptor> readToManyRelation(GenericDescriptor source, String relation, String targetClass, boolean readTarget) throws Exception {
		System.out.println("readToManyRelation: "+source.getClass().getName()+", "+relation+", "+targetClass);
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Entity entity = null;
		
		ArrayList<GenericDescriptor> arrayList = new ArrayList<GenericDescriptor>();

		if (source == null || relation == null) {
			return arrayList;
		}

		long l = 0;
		long size = 1; // a relation must have at least one element, otherwise wouldn't have been written. If the first element is not found, an empty array will be returned
		
		while (l < size) {
			String key = source.getClass().getName() + "_" + relation + "_" + targetClass;
			String id = source.getId() + "_" + relation + "_" + l;

			try {
				entity = datastore.get(KeyFactory.createKey(key, id));
			} catch (Exception e) {
				//e.printStackTrace();
				entity = null;
			}
			if (entity != null) {
				String targetId = (String)entity.getProperty("target");
				long order = (long)entity.getProperty("order");
				long sizen = (long)entity.getProperty("size");
				
				if (size < sizen) {
					size = sizen;
				}
				
				GenericDescriptor generic = readDescriptor(targetClass, targetId);
				arrayList.add(generic);
			}
			l++;		
		}
		return arrayList;
	}

	private static Entity createGenericEntity(GenericDescriptor generic) {
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

		System.out.println("createGenericEntity: "+generic.getTitle()+", "+generic.getClass().getName()+", "+generic.getId());

		Entity entity = null;
		try {
			entity = datastore.get(KeyFactory.createKey(generic.getClass().getName(), generic.getId()));
		} catch (Exception e) {
			//e.printStackTrace();
			entity = null;
		}
		if (entity == null) {
			entity = new Entity(generic.getClass().getName(), generic.getId());
		}

		entity.setProperty("uid", generic.getId());
		entity.setProperty("title", generic.getTitle());
		entity.setProperty("description", generic.getDescription());
		entity.setProperty("url", generic.getUrl());

		return entity;
	}

	private static Entity createGenericUserEntity(GenericUserObject generic) {
		System.out.println("createGenericUserEntity: "+generic.getTitle()+", "+generic.getClass().getName()+", "+generic.getId());
		Entity entity = null;

		try {
			entity = createGenericEntity(generic);

			entity.setProperty("userid", generic.getUser().getId());
			
		} catch (Exception e1) {
			System.out.println("FATAL: Writing generic user entity failed.");
			e1.printStackTrace();

		}

		return entity;
	}

	private static GenericDescriptor readDescriptor(String type, String id) throws Exception {
		System.out.println("readDescriptor: "+type+", "+id);
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

		Entity genericEntity = null;

		try {
			genericEntity = datastore.get(KeyFactory.createKey(type, id));
		} catch (Exception e) {
			e.printStackTrace();
			genericEntity = null;
		}
		ArrayList<GenericDescriptor> relationList = null;
		if (genericEntity != null) {
			if (CourseDescriptor.class.getName().equals(type)) {
				CourseDescriptor course = new CourseDescriptor((String) genericEntity.getProperty("uid"),
						(String) genericEntity.getProperty("title"), (String) genericEntity.getProperty("description"),
						(String) genericEntity.getProperty("url"));
				relationList = readToManyRelation(course, "modules", ModuleDescriptor.class.getName(), true);
				for (GenericDescriptor generic : relationList) {
					course.addModule((ModuleDescriptor)generic);
				}
				relationList = readToManyRelation(course, "goals", GoalDescriptor.class.getName(), true);
				for (GenericDescriptor generic : relationList) {
					course.addGoal((GoalDescriptor)generic);
				}
				return course;
			} else if (ModuleDescriptor.class.getName().equals(type)) {
				ModuleDescriptor module = new ModuleDescriptor((String) genericEntity.getProperty("uid"),
						(String) genericEntity.getProperty("title"), (String) genericEntity.getProperty("description"),
						(String) genericEntity.getProperty("url"));
				relationList = readToManyRelation(module, "lessons", LessonDescriptor.class.getName(), true);
				for (GenericDescriptor generic : relationList) {
					module.addLesson((LessonDescriptor)generic);
				}
				return module;
			} else if (GoalDescriptor.class.getName().equals(type)) {
				GoalDescriptor goal = new GoalDescriptor((String) genericEntity.getProperty("uid"),
						(String) genericEntity.getProperty("title"), (String) genericEntity.getProperty("description"),
						(String) genericEntity.getProperty("url"));
				relationList = readToManyRelation(goal, "lessons", LessonDescriptor.class.getName(), true);
				for (GenericDescriptor generic : relationList) {
					goal.addLesson((LessonDescriptor)generic);
				}
				EmbeddedEntity ee = (EmbeddedEntity) genericEntity.getProperty("completionGoals");
				String keys = (String)genericEntity.getProperty("completionGoalKeys");
				ArrayList<String> keysList = new ArrayList<>(Arrays.asList(keys.replaceAll("[]", "")));
				if (ee != null && keysList != null) {
				    for (String key : keysList) {
				        goal.addCompletionGoal(key, (String) ee.getProperty(key));
				    }
				}
				
				return goal;
			} else if (LessonDescriptor.class.getName().equals(type)) {
				LessonDescriptor lesson = new LessonDescriptor((String) genericEntity.getProperty("uid"),
						(String) genericEntity.getProperty("title"), (String) genericEntity.getProperty("description"),
						(String) genericEntity.getProperty("url"));
				relationList = readToManyRelation(lesson, "contents", ContentDescriptor.class.getName(), true);
				for (GenericDescriptor generic : relationList) {
					lesson.addContent((ContentDescriptor)generic);
				}
				return lesson;
			} else if (ContentDescriptor.class.getName().equals(type)) {
				ContentDescriptor content = new ContentDescriptor((String) genericEntity.getProperty("uid"),
						(String) genericEntity.getProperty("title"), (String) genericEntity.getProperty("description"),
						(String) genericEntity.getProperty("url"));
				return content;
			} else if (UserPlan.class.getName().equals(type)) {
				User user = getUser((String) genericEntity.getProperty("userid"));
				CourseDescriptor course = (CourseDescriptor)StimulatedPlanningFactory.getObject((String) genericEntity.getProperty("course"));				
				UserPlan plan = new UserPlan((String) genericEntity.getProperty("uid"), user);
				plan.setCourse(course);
				plan.setPlannedTimePerWeek((String) genericEntity.getProperty("plannedTimePerWeek"));
				relationList = readToManyRelation(plan, "userGoals", UserGoal.class.getName(), true);
				for (GenericDescriptor generic : relationList) {
					plan.addGoal((UserGoal)generic);
				}
				relationList = readToManyRelation(plan, "planItems", PlanItem.class.getName(), true);
				for (GenericDescriptor generic : relationList) {
					plan.addPlanItem((PlanItem)generic);
				}
				return plan;
			} else if (UserGoal.class.getName().equals(type)) {
				User user = getUser((String) genericEntity.getProperty("userid"));
				GoalDescriptor goal = (GoalDescriptor)StimulatedPlanningFactory.getObject((String) genericEntity.getProperty("goal"));				
				UserGoal userGoal = new UserGoal((String) genericEntity.getProperty("uid"), user, goal);
				userGoal.setCompletionGoal((String) genericEntity.getProperty("completionGoal"));
				relationList = readToManyRelation(userGoal, "lessons", UserLesson.class.getName(), true);
				for (GenericDescriptor generic : relationList) {
					userGoal.addLesson((UserLesson)generic);
				}
				return userGoal;
			} else if (UserLesson.class.getName().equals(type)) {
				User user = getUser((String) genericEntity.getProperty("userid"));
				LessonDescriptor lesson = (LessonDescriptor)StimulatedPlanningFactory.getObject((String) genericEntity.getProperty("lesson"));				
				UserLesson userLesson = new UserLesson((String) genericEntity.getProperty("uid"), user, lesson);
				LessonStatus status = LessonStatus.valueOf((String)genericEntity.getProperty("status"));
				userLesson.setStatus(status);
				relationList = readToManyRelation(userLesson, "contents", UserContent.class.getName(), true);
				for (GenericDescriptor generic : relationList) {
					userLesson.addContent((UserContent)generic);
				}
				return userLesson;
			} else if (UserContent.class.getName().equals(type)) {
				User user = getUser((String) genericEntity.getProperty("userid"));
				ContentDescriptor content = (ContentDescriptor)StimulatedPlanningFactory.getObject((String) genericEntity.getProperty("content"));				
				UserContent userContent = new UserContent((String) genericEntity.getProperty("uid"), user, content);
				LessonStatus status = LessonStatus.valueOf((String)genericEntity.getProperty("status"));
				userContent.setStatus(status);
				return userContent;
			} else if (PlanItem.class.getName().equals(type)) {
				User user = getUser((String) genericEntity.getProperty("userid"));
				LessonDescriptor lesson = (LessonDescriptor)StimulatedPlanningFactory.getObject((String) genericEntity.getProperty("lesson"));
				String jsonPlanItem = (String) genericEntity.getProperty("jsonPlanItem");
				PlanItem planItem = new PlanItem((String) genericEntity.getProperty("uid"), user, lesson, jsonPlanItem);
				LessonStatus status = LessonStatus.valueOf((String)genericEntity.getProperty("status"));
				planItem.setStatus(status);
				return planItem;
			}
		}

		return null;
	}

	public static void writeDescriptor(GenericDescriptor generic) throws Exception {
		System.out.println("writeDescriptor (GenericDescriptor): "+generic.getTitle()+", "+generic.getClass().getName()+", "+generic.getId());
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

		try {
			Entity entity = createGenericEntity(generic);

			datastore.put(entity);
		} catch (Exception e1) {
			System.out.println("FATAL: Writing generic entity failed.");
			e1.printStackTrace();

		}

	}

	public static void writeDescriptor(CourseDescriptor course) throws Exception {
		System.out.println("writeDescriptor (CourseDescriptor): "+course.getTitle()+", "+course.getClass().getName()+", "+course.getId());
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

		try {
			Entity courseEntity = createGenericEntity(course);

			writeToManyRelation(course, course.getModules(), "modules", course.modules.size(), true);
			writeToManyRelation(course, course.getGoals(), "goals", course.goals.size(), true);

			datastore.put(courseEntity);
		} catch (Exception e1) {
			System.out.println("FATAL: Writing course failed.");
			e1.printStackTrace();

		}

	}

	public static void writeDescriptor(ModuleDescriptor module) throws Exception {
		System.out.println("writeDescriptor (ModuleDescriptor): "+module.getTitle()+", "+module.getClass().getName()+", "+module.getId());
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

		try {
			Entity moduleEntity = createGenericEntity(module);

			writeToManyRelation(module, module.getLessons(), "lessons", module.lessons.size(), true);

			datastore.put(moduleEntity);
		} catch (Exception e1) {
			System.out.println("FATAL: Writing module failed.");
			e1.printStackTrace();

		}

	}

	public static void writeDescriptor(GoalDescriptor goal) throws Exception {
		System.out.println("writeDescriptor (GoalDescriptor): "+goal.getTitle()+", "+goal.getClass().getName()+", "+goal.getId());
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
			System.out.println("FATAL: Writing goal failed.");
			e1.printStackTrace();

		}

	}

	public static void writeDescriptor(LessonDescriptor lesson) throws Exception {
		System.out.println("writeDescriptor (LessonDescriptor): "+lesson.getTitle()+", "+lesson.getClass().getName()+", "+lesson.getId());
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

		try {
			Entity lessonEntity = createGenericEntity(lesson);

			writeToManyRelation(lesson, lesson.getContents(), "contents", lesson.contents.size(), true);

			datastore.put(lessonEntity);
		} catch (Exception e1) {
			System.out.println("FATAL: Writing lesson failed.");
			e1.printStackTrace();

		}

	}

	public static void writeDescriptor(ContentDescriptor content) throws Exception {
		System.out.println("writeDescriptor (ContentDescriptor): "+content.getTitle()+", "+content.getClass().getName()+", "+content.getId());
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

		try {
			Entity contentEntity = createGenericEntity(content);

			datastore.put(contentEntity);
		} catch (Exception e1) {
			System.out.println("FATAL: Writing content failed.");
			e1.printStackTrace();

		}

	}
	
	public static void writeDescriptor(UserPlan userPlan) throws Exception {
		System.out.println("writeDescriptor (UserPlan): "+userPlan.getUser().getName()+", "+userPlan.getClass().getName()+", "+userPlan.getId());
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

		try {
			Entity planEntity = createGenericUserEntity(userPlan);
			String courseId = userPlan.getCourse().getId();
			planEntity.setProperty("course", courseId);
			planEntity.setProperty("plannedTimePerWeek", userPlan.getPlannedTimePerWeek());

			writeToManyRelation(userPlan, userPlan.getGoals(), "userGoals", userPlan.goals.size(), true);
			writeToManyRelation(userPlan, userPlan.getPlanItems(), "planItems", userPlan.planItems.size(), true);
			
			datastore.put(planEntity);
		} catch (Exception e1) {
			System.out.println("FATAL: Writing UserPlan failed.");
			e1.printStackTrace();

		}

	}

	public static void writeDescriptor(UserGoal userGoal) throws Exception {
		System.out.println("writeDescriptor (UserGoal): "+userGoal.getUser().getName()+", "+userGoal.getClass().getName()+", "+userGoal.getId());
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

		try {
			Entity goalEntity = createGenericUserEntity(userGoal);
			goalEntity.setProperty("goal", userGoal.getGoalDescriptor().getId());
			goalEntity.setProperty("completionGoal", userGoal.getCompletionGoal());

			writeToManyRelation(userGoal, userGoal.getLessons(), "lessons", userGoal.lessons.size(), true);
			
			datastore.put(goalEntity);
		} catch (Exception e1) {
			System.out.println("FATAL: Writing goal failed.");
			e1.printStackTrace();

		}

	}


	public static void writeDescriptor(UserLesson userLesson) throws Exception {
		System.out.println("writeDescriptor (UserLesson): "+userLesson.getUser().getName()+", "+userLesson.getClass().getName()+", "+userLesson.getId());
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

		try {
			Entity lessonEntity = createGenericUserEntity(userLesson);
			lessonEntity.setProperty("lesson", userLesson.getLesson().getId());
			lessonEntity.setProperty("status", userLesson.getStatus().toString());

			writeToManyRelation(userLesson, userLesson.getContents(), "contents", userLesson.contents.size(), true);
			
			datastore.put(lessonEntity);
		} catch (Exception e1) {
			System.out.println("FATAL: Writing goal failed.");
			e1.printStackTrace();

		}

	}

	public static void writeDescriptor(UserContent userContent) throws Exception {
		System.out.println("writeDescriptor (UserContent): "+userContent.getUser().getName()+", "+userContent.getClass().getName()+", "+userContent.getId());
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

		try {
			Entity lessonEntity = createGenericUserEntity(userContent);
			lessonEntity.setProperty("content", userContent.getContent().getId());
			lessonEntity.setProperty("status", userContent.getStatus().toString());

			datastore.put(lessonEntity);
		} catch (Exception e1) {
			System.out.println("FATAL: Writing goal failed.");
			e1.printStackTrace();

		}

	}


	public static void writeDescriptor(PlanItem planItem) throws Exception {
		System.out.println("writeDescriptor (PlanItem): "+planItem.getUser().getName()+", "+planItem.getClass().getName()+", "+planItem.getId());
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

		try {
			Entity planItemEntity = createGenericUserEntity(planItem);
			planItemEntity.setProperty("lesson", planItem.getLesson().getId());
			planItemEntity.setProperty("status", planItem.getStatus().toString());
			planItemEntity.setProperty("jsonPlanItem", planItem.getJsonPlanItem());

			datastore.put(planItemEntity);
		} catch (Exception e1) {
			System.out.println("FATAL: Writing goal failed.");
			e1.printStackTrace();

		}

	}


	
	
	public static CourseDescriptor readCourse(String id) {
		CourseDescriptor course = null;
		try {
			course = (CourseDescriptor)readDescriptor(CourseDescriptor.class.getName(), id);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return course;
	}



	public static UserPlan readUserPlan(User user, CourseDescriptor course) {
		UserPlan plan = null;
		try {
			System.out.println("read userPlan for "+user.getName());
			DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
			Filter userFilter = new FilterPredicate("userid", FilterOperator.EQUAL, user.getId());
			Filter courseFilter = new FilterPredicate("course", FilterOperator.EQUAL, course.getId());
			CompositeFilter userCourseFilter = CompositeFilterOperator.and(userFilter, courseFilter);
			Query q = new Query(UserPlan.class.getName()).setFilter(userCourseFilter);
			
			PreparedQuery pq = datastore.prepare(q);

			for (Entity result : pq.asIterable()) {
				String id = (String) result.getProperty("uid");
				System.out.println("read userPlan for "+user.getName()+", "+id);
				plan = (UserPlan)readDescriptor(UserPlan.class.getName(), id);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return plan;
	}
	
	
	public static void deleteGenericEntity(GenericDescriptor generic) {
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

		System.out.println("deleteGenericEntity: "+generic.getTitle()+", "+generic.getClass().getName()+", "+generic.getId());
		
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
		} 

		try {
			Key entityKey = KeyFactory.createKey(generic.getClass().getName(), generic.getId());
			datastore.delete(entityKey);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	public static void deleteToManyRelation(GenericDescriptor source, String relation, String targetClass) {
		System.out.println("deleteToManyRelation: "+source.getClass().getName()+", "+relation+", "+targetClass);
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		
		if (source == null || relation == null) {
			return;
		}

		Entity entity = null;
		long l = 0;
		long size = 1; // a relation must have at least one element, otherwise wouldn't have been written. If the first element is not found, an empty array will be returned
		
		while (l < size) {
			String key = source.getClass().getName() + "_" + relation + "_" + targetClass;
			String id = source.getId() + "_" + relation + "_" + l;
			
			Key relationKey = KeyFactory.createKey(key, id);

			try {
				entity = datastore.get(relationKey);
			} catch (Exception e) {
				//e.printStackTrace();
				entity = null;
			}
			if (entity != null) {
				long sizen = (long)entity.getProperty("size");
				
				if (size < sizen) {
					size = sizen;
				}
				
				datastore.delete(relationKey);
				
			}
			l++;		
		}
	}
	
	
	public static void clearLog() {
		System.out.println("clearLog");
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		
		Query q = new Query("logEntity");
		
		PreparedQuery pq = datastore.prepare(q);
		
		for (Entity result : pq.asIterable()) {
			datastore.delete(result.getKey());
		}
		
	}


}
