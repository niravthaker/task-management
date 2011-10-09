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
	private TaskDao taskDao;
	
	public TaskServiceImpl(TaskDao taskDao) {
		this.taskDao = taskDao;
	}

	public Task get(String projectId) {
		Task task = taskDao.get(projectId);
		return task == null ? Task.NullTask : task;
	}

	public Task create(Task project) throws CyclicTaskException{
		if(!project.isNew())
			throw new IllegalArgumentException("Project is persistent with id:" +  project.getId());
		detectCycle(project);
		return taskDao.create(project);
	}

	public void update(String projectId, Task project) throws CyclicTaskException{
		if(project.isNew())
			throw new IllegalArgumentException("Task is not persistent.");
		detectCycle(project);
		taskDao.update(projectId, project);
	}

	public void delete(String id) {
		taskDao.delete(id);
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
	public Collection<Task> list() {
		return taskDao.list();
	}

}
