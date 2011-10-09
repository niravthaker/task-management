package name.nirav.tasks.core.eventsourcing;

import java.io.Serializable;

import name.nirav.tasks.core.service.TaskService;

public class TaskServiceDelegatingListener implements EventListener {
	private final TaskService taskService;
	
	public TaskServiceDelegatingListener(TaskService taskService) {
		this.taskService = taskService;
	}
	public void handle(Event<? extends Serializable, ?> event) {
		event.operate(taskService);
	}

}
