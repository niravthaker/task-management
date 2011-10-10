package name.nirav.tasks.core.eventsourcing.events;

import java.util.Map;
import java.util.Map.Entry;

import name.nirav.tasks.core.eventsourcing.Event;
import name.nirav.tasks.core.model.Task;
import name.nirav.tasks.core.service.TaskService;

public class UpdateTaskEvent extends BaseTaskEvent<Map.Entry<Task, Task>> {

	public UpdateTaskEvent(String userId, String id, Map.Entry<Task, Task> data) {
		super(userId, id, data);
	}

	public void operate(TaskService taskService) {
		String id = eventData().getValue().getId();
		taskService.update(getUserId(), id, eventData().getValue());
	}

	public EventType type() {
		return EventType.Update;
	}

	public Event<String, Entry<Task, Task>> deserialize(String state) {
		return null;
	}

	@Override
	protected String serializeEventData() {
		return String.format("%s~%s", eventData().getKey().toString(), eventData().getValue().toString());
	}

}
