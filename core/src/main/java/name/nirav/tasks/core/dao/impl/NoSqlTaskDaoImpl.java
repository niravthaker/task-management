package name.nirav.tasks.core.dao.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import name.nirav.tasks.core.dao.TaskDao;
import name.nirav.tasks.core.model.Task;

public class NoSqlTaskDaoImpl implements TaskDao {
	private Map<String, Task> memDB = new HashMap<String, Task>();
	private int identity = 0;
	
	/* (non-Javadoc)
	 * @see name.nirav.tasks.core.dao.TaskDao#byId(int)
	 */
	public Task get(String projectId) {
		return memDB.get(projectId);
	}

	/* (non-Javadoc)
	 * @see name.nirav.tasks.core.dao.TaskDao#create(name.nirav.tasks.core.model.Task)
	 */
	public Task create(Task project) {
		project.setId(String.valueOf(++identity));
		memDB.put(project.getId(), project);
		List<Task> children = project.children();
		for (Task task : children) {
			create(task);
		}
		return project;
	}

	/* (non-Javadoc)
	 * @see name.nirav.tasks.core.dao.TaskDao#update(name.nirav.tasks.core.model.Task, name.nirav.tasks.core.model.Task)
	 */
	public void update(String id, Task project) {
		memDB.put(id, project);
	}

	/* (non-Javadoc)
	 * @see name.nirav.tasks.core.dao.TaskDao#delete(name.nirav.tasks.core.model.Task)
	 */
	public void delete(String id) {
		memDB.remove(id);
	}

	/* (non-Javadoc)
	 * @see name.nirav.tasks.core.dao.GenericDao#list()
	 */
	@Override
	public Collection<Task> list() {
		return memDB.values();
	}

}
