package stimulatedplanning;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.EmbeddedEntity;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.KeyRange;
//import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
//import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class PersistentStore {
	
	  public static void writeUser(User user) throws Exception {
		    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

		    Entity userEntity = null;
		    try {
			    userEntity = datastore.get(KeyFactory.createKey(user.getClass().getName(), user.getId()));
		    } catch(Exception e) {
				e.printStackTrace();
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
		    	User user = new User((String)userEntity.getProperty("name"), (String)userEntity.getProperty("name"));
		    	return user;
		    }
		    
		    return null;
		}


	  public static void writeLog(Map<String, String[]> parameters) throws Exception {
		    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

		    Entity logEntity = null;
		    try {
			    String id = parameters.get("userid")[0]+"_"+parameters.get("_")[0];
			    Key logKey = KeyFactory.createKey("logEntity", id);
			    try {
				    logEntity = datastore.get(logKey);
			    } catch(Exception e) {
					//e.printStackTrace();
					logEntity = null;
			    }
			    
			    if (logEntity == null) {
				    logEntity = new Entity(logKey);
			    }
			    
			    for (String property : parameters.keySet()) {
			    	String value = parameters.get(property)[0];
				    logEntity.setProperty(property, value);
			    }

			    datastore.put(logEntity);
		    } catch (Exception e1) {
		    	System.out.println("FATAL: Logging failed.");
		    	e1.printStackTrace();
		    	
		    }
		    
		}

}
