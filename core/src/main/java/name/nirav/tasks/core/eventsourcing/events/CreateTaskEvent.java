package name.nirav.tasks.core.eventsourcing.events;

import name.nirav.tasks.core.eventsourcing.Event;
import name.nirav.tasks.core.model.Task;
import name.nirav.tasks.core.service.TaskService;


public class CreateTaskEvent extends BaseTaskEvent<Task>{

	public CreateTaskEvent(String id, Task data) {
		super(id, data);
	}

	public void operate(TaskService taskService) {
		Task data = eventData();
		data.setId(Task.NEW_ITEM_ID);
		taskService.create(data);
	}

	public EventType type() {
		return EventType.Create;
	}

	public Event<String, Task> deserialize(String state) {
		return null;
	}

	@Override
	protected String serializeEventData() {
		return eventData().toString();
	}


}
