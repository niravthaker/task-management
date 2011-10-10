package name.nirav.tasks.core.service.impl;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import name.nirav.tasks.core.dao.TaskDao;
import name.nirav.tasks.core.exceptions.CyclicTaskException;
import name.nirav.tasks.core.model.Task;
import name.nirav.tasks.core.service.TaskService;

public class TaskServiceImpl implements TaskService {
	private TaskDao<Task, String> taskDao;
	
	public TaskServiceImpl(TaskDao<Task, String> taskDao) {
		this.taskDao = taskDao;
	}

	public Task get(String userId, String projectId) {
		Task task = taskDao.get(userId, projectId);
		return task == null ? Task.NullTask : task;
	}

	public Task create(String userId, Task project) throws CyclicTaskException{
		detectCycle(project);
		return taskDao.create(userId, project);
	}

	public void update(String userId, String projectId, Task project) throws CyclicTaskException{
		detectCycle(project);
		taskDao.update(userId, projectId, project);
	}

	public void delete(String userId, String taskId) {
		taskDao.delete(userId, taskId);
	}

	private void detectCycle(final Task project)throws CyclicTaskException {
		Set<String> visited = new HashSet<String>();
		if(dfs(visited, project))
			throw new CyclicTaskException("Project creates cycles.");
	}
	
	private boolean dfs(Set<String> visited, Task task){
		if(!task.isNew())
			if(visited.contains(task.getId()))
				return true;
			else
				visited.add(task.getId());
		List<Task> children = task.children();
		for (Task child : children) {
			boolean hasCycles = dfs(visited, child);
			if(hasCycles)
				return hasCycles;
		}
		return false;
	}

	@Override
	public Collection<Task> list(String userId) {
		return taskDao.list(userId);
	}

}
