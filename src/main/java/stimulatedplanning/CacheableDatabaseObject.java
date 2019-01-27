package stimulatedplanning;

import java.util.Calendar;
import java.util.Date;

import com.google.appengine.api.datastore.Entity;

import stimulatedplanning.util.IObjectWithId;

public class CacheableDatabaseObject {

	private Entity entity;
	private IObjectWithId object;
	private Date expires = null;
	public CacheableDatabaseObject(Entity entity, IObjectWithId object) {
		this.entity = entity;
		this.object = object;
	}
	
	
	public Entity getEntity() {
		return entity;
	}
	
	public IObjectWithId getObject() {
		return object;
	}
	
	public String getCacheId() {
		return object.getClass().getName()+"_"+object.getId();
	}
	
	public String getObjectClass() {
		return object.getClass().getName();
	}
	
	public void setExpires(int expires) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.SECOND, expires);
		this.expires = cal.getTime();
	}
	
	public boolean isExpired() {
		if (expires == null) {
			return false;
		}
		return new Date().after(expires);
	}
	
}
