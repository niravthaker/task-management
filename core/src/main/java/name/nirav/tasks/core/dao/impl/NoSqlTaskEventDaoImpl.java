package name.nirav.tasks.core.dao.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import name.nirav.tasks.core.dao.TaskEventDao;
import name.nirav.tasks.core.eventsourcing.Event;


public class NoSqlTaskEventDaoImpl implements TaskEventDao {

	private Map<String, List<Event<String, ?>>> memDB = new HashMap<String, List<Event<String,?>>>();

	public void store(Event<String, ?> event) {
		List<Event<String, ?>> list = memDB.get(event.id());
		if(list == null)
			memDB.put(event.id(), list = new ArrayList<Event<String,?>>());
		list.add(event);
	}

	public List<Event<String, ?>> events(String id) {
		List<Event<String, ?>> list = memDB.get(id);
		return list == null ? Collections.<Event<String, ?>>emptyList() : list;
	}

}
