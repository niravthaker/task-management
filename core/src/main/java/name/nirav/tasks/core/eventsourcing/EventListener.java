package name.nirav.tasks.core.eventsourcing;

import java.io.Serializable;

public interface EventListener {
	public void handle(Event<? extends Serializable, ?> event);
}
