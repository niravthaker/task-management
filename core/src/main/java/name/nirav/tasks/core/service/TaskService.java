package name.nirav.tasks.core.service;

import name.nirav.tasks.core.dao.TaskDao;
import name.nirav.tasks.core.model.Task;

public interface TaskService extends TaskDao<Task, String>{
}
