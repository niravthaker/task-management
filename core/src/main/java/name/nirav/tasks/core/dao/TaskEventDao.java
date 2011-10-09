package name.nirav.tasks.core.dao;

import java.util.List;

import name.nirav.tasks.core.eventsourcing.Event;

public interface TaskEventDao {

	void store(Event<String, ?> event);

	List<Event<String,?>> events(String id);

}