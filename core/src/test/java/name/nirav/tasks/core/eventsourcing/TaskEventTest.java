package name.nirav.tasks.core.eventsourcing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.AbstractMap.SimpleEntry;

import name.nirav.tasks.core.dao.impl.NoSqlTaskDaoImpl;
import name.nirav.tasks.core.dao.impl.NoSqlTaskEventDaoImpl;
import name.nirav.tasks.core.eventsourcing.events.CreateTaskEvent;
import name.nirav.tasks.core.eventsourcing.events.DeleteTaskEvent;
import name.nirav.tasks.core.eventsourcing.events.UpdateTaskEvent;
import name.nirav.tasks.core.model.Task;
import name.nirav.tasks.core.model.TaskModelTests;
import name.nirav.tasks.core.service.TaskService;
import name.nirav.tasks.core.service.impl.TaskEventServiceImpl;
import name.nirav.tasks.core.service.impl.TaskServiceImpl;

import org.junit.Before;
import org.junit.Test;

public class TaskEventTest {
	
	private static final String TEST_USERID = "thakern";
	private TaskEventServiceImpl eventService;
	private TaskService taskService;
	private TaskServiceDelegatingListener delegatingListener;

	@Before
	public void init(){
		taskService = new TaskServiceImpl(new NoSqlTaskDaoImpl());
		eventService = new TaskEventServiceImpl(new NoSqlTaskEventDaoImpl());
		delegatingListener = new TaskServiceDelegatingListener(taskService);
		eventService.addListener(delegatingListener);
	}

	@Test
	public void testCRUDEvents() {
		
		//Create
		String taskTitle = "Test Task";
		Task task = TaskModelTests.newTask("1", taskTitle);
		eventService.publish(new CreateTaskEvent(TEST_USERID,task.getId(), task));
		assertEquals(1, eventService.events(task.getId()).size());
		Task snapshot = taskService.get(TEST_USERID,task.getId());
		assertNotNull(snapshot);
		assertEquals(taskTitle, snapshot.getTitle());
		
		//Update
		String updatedTitle = "Edited Test Task";
		Task updatedTask = TaskModelTests.newTask(task.getId(), updatedTitle);
		eventService.publish(new UpdateTaskEvent(TEST_USERID,task.getId(), new SimpleEntry<Task, Task>(task, updatedTask)));
		assertEquals(2, eventService.events(task.getId()).size());
		snapshot = taskService.get(TEST_USERID,task.getId());
		assertNotNull(snapshot);
		assertEquals(updatedTitle, snapshot.getTitle());
		
		//Delete
		eventService.publish(new DeleteTaskEvent(TEST_USERID,task.getId()));
		assertEquals(3, eventService.events(task.getId()).size());
		snapshot = taskService.get(TEST_USERID,task.getId());
		assertEquals(Task.NullTask, snapshot);
	}
	
	@Test
	public void testRebuild(){
		String TASK_ID = "1";
		Task testTask = TaskModelTests.newTask(TASK_ID, "Test");
		eventService.removeListener(delegatingListener);
		eventService.publish(new CreateTaskEvent(TEST_USERID,TASK_ID, testTask));
		String updatedTitle = "Updated Test";
		eventService.publish(new UpdateTaskEvent(TEST_USERID,TASK_ID, new SimpleEntry<Task, Task>(testTask, TaskModelTests.newTask(TASK_ID, updatedTitle))));
		eventService.publish(new DeleteTaskEvent(TEST_USERID,TASK_ID));
		assertEquals(3, eventService.events(TASK_ID).size());
		assertEquals(Task.NullTask, taskService.get(TEST_USERID,TASK_ID));
		eventService.addListener(delegatingListener);
		eventService.publish(new CreateTaskEvent(TEST_USERID,TASK_ID, testTask));
		eventService.publish(new UpdateTaskEvent(TEST_USERID,TASK_ID, new SimpleEntry<Task, Task>(testTask, TaskModelTests.newTask(TASK_ID, updatedTitle))));
		assertEquals(5, eventService.events(TASK_ID).size());
		eventService.rollEvents(TASK_ID);
		Task recreted = taskService.get(TEST_USERID,TASK_ID);
		assertTrue(recreted != Task.NullTask);
		assertEquals(TASK_ID, recreted.getId());
		assertEquals(updatedTitle, recreted.getTitle());
	}
	

}
