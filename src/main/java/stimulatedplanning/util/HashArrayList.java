package stimulatedplanning.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.Spliterator;
import java.util.function.Consumer;

import stimulatedplanning.GenericDescriptor;

public class HashArrayList<T extends GenericDescriptor> implements Iterable<T>, Serializable {
	
	private ArrayList<T> arrayList;
	private HashMap<String, T> hashMap;

	public HashArrayList() {
		arrayList = new ArrayList<T>();
		hashMap = new HashMap<String, T>();
		// TODO Auto-generated constructor stub
	}
	
	public void add(T t) {
		if (t != null && !containsKey(t.getId())) {
			arrayList.add(t);
			hashMap.put(t.getId(), t);
		}
	}
	
	public void remove(T t) {
		if (t != null) {
			arrayList.remove(t);
			hashMap.remove(t.getId());
		}
	}
	
	public void addOrReplace(T t) {
		if (t != null) {
			if (containsKey(t.getId())) {
				T t_old = hashMap.get(t.getId());
				if (t_old == t) {
					return;
				} else {
					hashMap.remove(t.getId());
					hashMap.put(t.getId(), t);
					int idx = arrayList.indexOf(t_old);
					if (idx > -1) {
						arrayList.set(idx, t);
					} else {
						arrayList.add(t);
					}
				}
			} else {
				add(t);
			}
		}
	}
	
	public T get(int index) {
		return arrayList.get(index);
	}
	
	public int size() {
		return arrayList.size();
	}
	
	public Iterator<T> iterator() {
		return arrayList.iterator();
	}

	public ListIterator<T> listIterator() {
		return arrayList.listIterator();
	}
	
	public void forEach(Consumer<? super T> action) {
		arrayList.forEach(action);
	}
	
	public void sort(Comparator<? super T> c) {
		arrayList.sort(c);
	}
	
	public Spliterator<T> spliterator() {
		return arrayList.spliterator();
	}

	public boolean containsKey(String key) {
		return hashMap.containsKey(key);
	}
	
	public T get(String key) {
		return hashMap.get(key);
	}
	
	public Set<String> keySet() {
		return hashMap.keySet();
	}
	
	public ArrayList<T> arrayList() {
		return arrayList;
	}
	
	public HashMap<String, T> hashMap() {
		return hashMap;
	}
	
	public Collection<T> values() {
		return hashMap.values();
	}
	
	public List<T> unmodifiableList() {
		List<T> unmodifiableList = Collections.unmodifiableList(arrayList);
		return unmodifiableList;
	}
	
	
}
