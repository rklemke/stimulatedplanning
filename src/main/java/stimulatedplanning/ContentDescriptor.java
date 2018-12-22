package stimulatedplanning;

import java.util.List;
import java.util.ListIterator;

import senseofcommunity.InformationObject;
import senseofcommunity.SelectionObject;
import stimulatedplanning.util.HashArrayList;

public class ContentDescriptor extends GenericDescriptor {

	protected HashArrayList<InformationObject> informationObjects;
	protected HashArrayList<InformationObject> allInformationObjects;
	protected HashArrayList<SelectionObject> selectionObjects;

	public void addInformationObject(InformationObject informationObject) {
		if (informationObject.getSequence()==-1) {
			informationObject.setSequence(informationObjects.size()+selectionObjects.size());
		}
		if (informationObject instanceof SelectionObject) {
			this.selectionObjects.add((SelectionObject)informationObject);
		} else {
			this.informationObjects.add(informationObject);
		}
		this.allInformationObjects.add(informationObject);
	}
	
	public ListIterator<InformationObject> getAllInformationObjects() {
		return allInformationObjects.listIterator();
	}

	public List<InformationObject> getAllInformationObjectList() {
		return allInformationObjects.unmodifiableList();
	}
	
	public ListIterator<InformationObject> getInformationObjects() {
		return informationObjects.listIterator();
	}


//	public void addSelectionObject(SelectionObject selectionObject) {
//		this.selectionObjects.add(selectionObject);
//	}
//	
	public ListIterator<SelectionObject> getSelectionObjects() {
		return selectionObjects.listIterator();
	}
	
	public ContentDescriptor() {
		super();
		// TODO Auto-generated constructor stub
		informationObjects = new HashArrayList<>();
		selectionObjects = new HashArrayList<>();
		allInformationObjects = new HashArrayList<>();
	}

	public ContentDescriptor(String id, String title, String description, String url) {
		super(id, title, description, url);
		// TODO Auto-generated constructor stub
		informationObjects = new HashArrayList<>();
		selectionObjects = new HashArrayList<>();
		allInformationObjects = new HashArrayList<>();
	}

}
