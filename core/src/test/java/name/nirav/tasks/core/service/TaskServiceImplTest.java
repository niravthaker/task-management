package name.nirav.tasks.core.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import name.nirav.tasks.core.dao.impl.NoSqlTaskDaoImpl;
import name.nirav.tasks.core.exceptions.CyclicTaskException;
import name.nirav.tasks.core.model.Task;
import name.nirav.tasks.core.model.TaskModelTests;
import name.nirav.tasks.core.model.impl.FreeFormTask;
import name.nirav.tasks.core.service.impl.TaskServiceImpl;

import org.junit.BeforeClass;
import org.junit.Test;

public class TaskServiceImplTest {
	static TaskService service;

	@BeforeClass
	public static void init() {
		service = new TaskServiceImpl(new NoSqlTaskDaoImpl());
	}

	@Test
	public void testCreate() {
	}

	@Test
	public void testget() {
		assertEquals(Task.NullTask, service.get("123"));
		Task newTask = TaskModelTests.newTask(100);
		Task get = service.create(newTask);
		Task task = service.get(get.getId());
		assertNotNull(task);
		assertEquals("Didn't find the right task.",100, task.getProgress());
	}

	@Test
	public void testUpdate() {
		Task newTask = TaskModelTests.newTask(100);
		Task task = service.create(newTask);
		task.addTask(TaskModelTests.newTask(50));
		FreeFormTask tobeDeleted = TaskModelTests.newTask(20);
		task.addTask(tobeDeleted);
		service.update(task.getId(), task);
		Task updated = service.get(task.getId());
		assertNotNull(updated);
		assertEquals(2,updated.children().size());
		assertEquals(50, updated.children().get(0).getProgress());
		assertEquals(20, updated.children().get(1).getProgress());
		updated.removeTask(tobeDeleted);
		service.update(updated.getId(), updated);
		updated = service.get(updated.getId());
		assertEquals(1, updated.children().size());
		assertEquals(50, updated.children().get(0).getProgress());
	}

	@Test
	public void testDelete() {
		Task toDelete = service.create(TaskModelTests.newTask(99));
		service.delete(toDelete.getId());
		assertEquals(Task.NullTask, service.get(toDelete.getId()));
	}
	
	@Test
	public void detectCycle(){
		Task project = TaskModelTests.newTask(10);
		FreeFormTask cyclicItem = TaskModelTests.newTask(5);
		project.addTask(cyclicItem);
		project.addTask(TaskModelTests.newTask(15));
		project.addTask(TaskModelTests.newTask(25));
		project = service.create(project);
		project.addTask(service.get(cyclicItem.getId()));
		try {
			service.update(project.getId(), project);
			fail("Update didn't detect cycles");
		} catch (CyclicTaskException e) {
		}
		Task task1 = TaskModelTests.newTask(1);
		task1.addTask(TaskModelTests.newTask(11));
		task1 = service.create(task1);
		Task task2 = TaskModelTests.newTask(2);
		task2.addTask(TaskModelTests.newTask(22));
		task2 = service.create(task2);
		Task task3 = TaskModelTests.newTask(3);
		task3.addTask(task1);
		try{
			task3 = service.create(task3);
			fail("Create didn't detect cycle");
		}catch (CyclicTaskException e) {
		}
	}

}
