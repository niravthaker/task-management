package name.nirav.tasks.core.dao;

import java.io.Serializable;
import java.util.Collection;

import name.nirav.tasks.core.model.Task;

public interface TaskDao<E extends Task, K extends Serializable>{
	Collection<E> list(K userId);

	E get(K userId, K taskId);

	void update(K userId, K taskId, E task);

	void delete(K userId, K taskId);

	E create(K userId, E entity);

}