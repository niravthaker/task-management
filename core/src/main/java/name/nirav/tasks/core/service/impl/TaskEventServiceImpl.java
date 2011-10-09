package name.nirav.tasks.core.service.impl;

import java.util.ArrayList;
import java.util.List;

import name.nirav.tasks.core.dao.impl.NoSqlTaskEventDaoImpl;
import name.nirav.tasks.core.eventsourcing.Event;
import name.nirav.tasks.core.eventsourcing.EventListener;
import name.nirav.tasks.core.service.TaskEventService;

public class TaskEventServiceImpl implements TaskEventService{
	private final NoSqlTaskEventDaoImpl eventDao;
	private final List<EventListener> listeners = new ArrayList<EventListener>();

	public TaskEventServiceImpl(NoSqlTaskEventDaoImpl eventDao) {
		this.eventDao = eventDao;
	}
	
	public void addListener(EventListener listener){
		listeners.add(listener);
	}
	
	public void removeListener(EventListener listener){
		listeners.remove(listener);
	}
	
	public void publish(Event<String, ?> event){
		eventDao.store(event);
		for (EventListener listener : listeners) {
			listener.handle(event);
		}
	}

	public List<Event<String,?>> events(String id) {
		return eventDao.events(id);
	}

	public void rollEvents(String id) {
		for (Event<String, ?> event : eventDao.events(id)) {
			for (EventListener listener : listeners) {
				listener.handle(event);
			}
		}
	}

	public void store(Event<String, ?> event) {
		eventDao.store(event);
	}
}
