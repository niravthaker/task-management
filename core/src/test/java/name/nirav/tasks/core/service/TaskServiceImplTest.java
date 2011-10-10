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
	private static final String TEST_USERID = "nirav";
	@BeforeClass
	public static void init() {
		service = new TaskServiceImpl(new NoSqlTaskDaoImpl());
	}

	@Test
	public void testCreate() {
	}

	@Test
	public void testget() {
		assertEquals(Task.NullTask, service.get(TEST_USERID, "123"));
		Task newTask = TaskModelTests.newTask(100);
		Task get = service.create(TEST_USERID,newTask);
		Task task = service.get(TEST_USERID,get.getId());
		assertNotNull(task);
		assertEquals("Didn't find the right task.",100, task.getProgress());
	}

	@Test
	public void testUpdate() {
		Task newTask = TaskModelTests.newTask(100);
		Task task = service.create(TEST_USERID,newTask);
		task.addTask(TaskModelTests.newTask(50));
		FreeFormTask tobeDeleted = TaskModelTests.newTask(20);
		task.addTask(tobeDeleted);
		service.update(TEST_USERID,task.getId(), task);
		Task updated = service.get(TEST_USERID,task.getId());
		assertNotNull(updated);
		assertEquals(2,updated.children().size());
		assertEquals(50, updated.children().get(0).getProgress());
		assertEquals(20, updated.children().get(1).getProgress());
		updated.removeTask(tobeDeleted);
		service.update(TEST_USERID,updated.getId(), updated);
		updated = service.get(TEST_USERID,updated.getId());
		assertEquals(1, updated.children().size());
		assertEquals(50, updated.children().get(0).getProgress());
	}

	@Test
	public void testDelete() {
		Task toDelete = service.create(TEST_USERID,TaskModelTests.newTask(99));
		service.delete(TEST_USERID,toDelete.getId());
		assertEquals(Task.NullTask, service.get(TEST_USERID,toDelete.getId()));
	}
	
	@Test
	public void detectCycle(){
		Task project = TaskModelTests.newTask(10);
		FreeFormTask cyclicItem = TaskModelTests.newTask(5);
		project.addTask(cyclicItem);
		project.addTask(TaskModelTests.newTask(15));
		project.addTask(TaskModelTests.newTask(25));
		project = service.create(TEST_USERID,project);
		project.addTask(service.get(TEST_USERID,cyclicItem.getId()));
		try {
			service.update(TEST_USERID,project.getId(), project);
			fail("Update didn't detect cycles");
		} catch (CyclicTaskException e) {
		}
		Task task1 = TaskModelTests.newTask(1);
		task1.addTask(TaskModelTests.newTask(11));
		task1 = service.create(TEST_USERID,task1);
		Task task2 = TaskModelTests.newTask(2);
		task2.addTask(TaskModelTests.newTask(22));
		task2 = service.create(TEST_USERID,task2);
		Task task3 = TaskModelTests.newTask(3);
		task3.addTask(task1);
		try{
			task3 = service.create(TEST_USERID,task3);
			fail("Create didn't detect cycle");
		}catch (CyclicTaskException e) {
		}
	}

}
