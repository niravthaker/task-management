package name.nirav.tasks.core.eventsourcing.events;

import name.nirav.tasks.core.eventsourcing.Event;
import name.nirav.tasks.core.service.TaskService;


public class DeleteTaskEvent extends BaseTaskEvent<String> {

	public DeleteTaskEvent(String userId, String id) {
		super(userId, id, id);
	}

	public void operate(TaskService taskService) {
		taskService.delete(getUserId(), id());
	}

	public EventType type() {
		return EventType.Delete;
	}

	public Event<String, String> deserialize(String state) {
		return null;
	}

	@Override
	protected String serializeEventData() {
		return "";
	}

}
