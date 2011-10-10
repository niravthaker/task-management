package name.nirav.tasks.core.eventsourcing.events;

import java.util.Date;

import name.nirav.tasks.core.eventsourcing.Event;

public abstract class BaseTaskEvent<D> implements Event<String, D> {

	private final String id;
	private final Date timestamp = new Date();
	private final String userId;
	private D data;

	public BaseTaskEvent(String userId, String taskId, D data) {
		this.userId = userId;
		this.id = taskId;
		this.data = data;
	}

	public String id() {
		return id;
	}

	public Date timestamp() {
		return timestamp;
	}

	public D eventData() {
		return data;
	}

	public String getUserId() {
		return userId;
	}

	public String serialize() {
		StringBuilder builder = new StringBuilder();
		builder.append(timestamp());
		builder.append(',');
		builder.append(id());
		builder.append(',');
		builder.append(type().name());
		builder.append(serializeEventData());
		return builder.toString();
	}

	protected abstract String serializeEventData();

}
