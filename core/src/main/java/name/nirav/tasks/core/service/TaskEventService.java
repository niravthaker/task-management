package name.nirav.tasks.core.service;

import name.nirav.tasks.core.dao.TaskEventDao;
import name.nirav.tasks.core.eventsourcing.Event;
import name.nirav.tasks.core.eventsourcing.EventListener;

public interface TaskEventService extends TaskEventDao {

	void rollEvents(String id);

	void publish(Event<String, ?> event);

	void removeListener(EventListener listener);

	void addListener(EventListener listener);

}