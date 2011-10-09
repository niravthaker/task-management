package name.nirav.tasks.core.dao;

import java.io.Serializable;
import java.util.Collection;

public interface GenericDao<K extends Serializable, E> {
	Collection<E> list();

	E get(K id);

	void update(K id, E entity);

	void delete(K entity);

	E create(E entity);

}
