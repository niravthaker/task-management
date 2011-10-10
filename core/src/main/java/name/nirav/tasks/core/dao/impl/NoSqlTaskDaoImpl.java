package name.nirav.tasks.core.dao.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import name.nirav.tasks.core.dao.TaskDao;
import name.nirav.tasks.core.model.Task;

public class NoSqlTaskDaoImpl implements TaskDao<Task, String> {
	private Map<String, Map<String,Task>> memDB = new HashMap<String, Map<String,Task>>();
	
	/* (non-Javadoc)
	 * @see name.nirav.tasks.core.dao.TaskDao#byId(int)
	 */
	public Task get(String userId, String taskId) {
		return getTasksForUser(userId).isEmpty()? Task.NullTask : getTasksForUser(userId).get(taskId);
	}

	/* (non-Javadoc)
	 * @see name.nirav.tasks.core.dao.TaskDao#create(name.nirav.tasks.core.model.Task)
	 */
	public Task create(String userId, Task task) {
		Map<String, Task> userTasks = getTasksForUser(userId);
		userTasks.put(task.getId(), task);
		List<Task> children = task.children();
		for (Task childTask : children) {
			create(userId, childTask);
		}
		return task;
	}

	/* (non-Javadoc)
	 * @see name.nirav.tasks.core.dao.TaskDao#update(name.nirav.tasks.core.model.Task, name.nirav.tasks.core.model.Task)
	 */
	public void update(String userId, String taskId, Task task) {
		getTasksForUser(userId).put(taskId, task);
	}

	private Map<String, Task> getTasksForUser(String userId) {
		Map<String, Task> userTasks = memDB.get(userId);
		if(userTasks == null){
			memDB.put(userId, userTasks = new HashMap<String, Task>());
		}
		return userTasks;
	}

	/* (non-Javadoc)
	 * @see name.nirav.tasks.core.dao.TaskDao#delete(name.nirav.tasks.core.model.Task)
	 */
	public void delete(String userId, String taskId) {
		getTasksForUser(userId).remove(taskId);
	}

	/* (non-Javadoc)
	 * @see name.nirav.tasks.core.dao.GenericDao#list()
	 */
	@Override
	public Collection<Task> list(String userId) {
		return getTasksForUser(userId).values();
	}

}
