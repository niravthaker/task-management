package name.nirav.tasks.core.eventsourcing;

import java.io.Serializable;
import java.util.Date;

import name.nirav.tasks.core.eventsourcing.events.EventType;
import name.nirav.tasks.core.service.TaskService;

public interface Event<K extends Serializable, D> {
	K id();
	EventType type();
	D eventData();
	Date timestamp();
	void operate(TaskService taskService);
	String serialize();
	Event<K, D> deserialize(String state);
}
