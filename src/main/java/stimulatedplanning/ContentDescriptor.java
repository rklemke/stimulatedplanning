package stimulatedplanning;

import java.util.List;
import java.util.ListIterator;

import senseofcommunity.InformationObject;
import senseofcommunity.SelectionObject;
import stimulatedplanning.util.HashArrayList;

public class ContentDescriptor extends GenericDescriptor {

	protected HashArrayList<InformationObject> informationObjects;
	public void addInformationObject(InformationObject informationObject) {
		this.informationObjects.add(informationObject);
	}
	
	public ListIterator<InformationObject> getInformationObjects() {
		return informationObjects.listIterator();
	}

	public List<InformationObject> getInformationObjectList() {
		return informationObjects.unmodifiableList();
	}
	

//	protected HashArrayList<SelectionObject> selectionObjects;
//	public void addSelectionObject(SelectionObject selectionObject) {
//		this.selectionObjects.add(selectionObject);
//	}
//	
//	public ListIterator<SelectionObject> getSelectionObjects() {
//		return selectionObjects.listIterator();
//	}
	
	public ContentDescriptor() {
		super();
		// TODO Auto-generated constructor stub
		informationObjects = new HashArrayList<>();
//		selectionObjects = new HashArrayList<>();
	}

	public ContentDescriptor(String id, String title, String description, String url) {
		super(id, title, description, url);
		// TODO Auto-generated constructor stub
		informationObjects = new HashArrayList<>();
//		selectionObjects = new HashArrayList<>();
	}

}
