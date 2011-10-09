package name.nirav.tasks.core.eventsourcing;

public interface Adaptable<T> {
	T getAdapter(Class<T> adapter);
}
