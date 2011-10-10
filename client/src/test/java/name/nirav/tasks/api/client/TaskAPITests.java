package name.nirav.tasks.api.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Date;

import name.nirav.tasks.api.client.exceptions.ResourceNotFound;
import name.nirav.tasks.core.model.Task;
import name.nirav.tasks.core.model.User;
import name.nirav.tasks.core.model.impl.EndUser;
import name.nirav.tasks.core.model.impl.FreeFormTask;

import org.junit.BeforeClass;
import org.junit.Test;

public class TaskAPITests extends BaseAPITests{
	
	@BeforeClass
	public static void init(){
		BaseAPITests.init();
		User user = new EndUser();
		user.setUserId(APIUtils.getUser());
		apiClient.createUser(user);
	}
	
	@Test
	public void createTask(){
		try {
			createSimpleTaskOnServer("Test Task");
		} catch (Exception e) {
			e.printStackTrace();
			fail("Failed to create a simple task!");
		}
	}
	@Test
	public void getTask(){
		Date now = new Date();
		String taskId = "100";
		String title = "Task1";
		int progress = 20;
		FreeFormTask task = new FreeFormTask(title, now, now, progress);
		task.setId(taskId);
		apiClient.deleteTask(taskId);
		apiClient.createTask(task);
		Task createdTask = apiClient.getTask(taskId);
		assertTaskFields(createdTask, now, taskId, title, progress);
	}
	
	@Test
	public void deleteTask(){
		FreeFormTask task = createTask("Task");
		String taskId = "2";
		task.setId(taskId);
		apiClient.createTask(task);
		apiClient.deleteTask(taskId);
		try {
			apiClient.getTask(taskId);
			fail("Item was not deleted successfully");
		} catch (ResourceNotFound e) {
		}
	}
	@Test
	public void updateTask(){
		FreeFormTask task = createTask("Task");
		String taskId = "2";
		task.setId(taskId);
		apiClient.createTask(task);
		task.setTitle("Edited Task");
		task.setProgress(20);
		Task updateTask = apiClient.updateTask(task);
		assertTaskFields(updateTask, task.getStartDate(), taskId, task.getTitle(), 20);
	}

	private void createSimpleTaskOnServer(String title) {
		apiClient.createTask(createTask(title));
	}
	protected FreeFormTask createTask(String title) {
		return new FreeFormTask(title, new Date(), new Date(), 0);
	}

	private void assertTaskFields(Task createdTask, Date now, String taskId, String title, int progress) {
		assertEquals(taskId, createdTask.getId());
		assertEquals(title, createdTask.getTitle());
		assertEquals(progress, createdTask.getProgress());
		assertEquals(now, createdTask.getStartDate());
		assertEquals(now, createdTask.getEndDate());
	}
}
