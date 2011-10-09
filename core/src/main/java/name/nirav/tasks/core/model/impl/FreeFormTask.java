package name.nirav.tasks.core.model.impl;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import name.nirav.tasks.core.model.Task;
import name.nirav.tasks.core.model.TaskType;

public class FreeFormTask extends AbstractTask implements Task {

	private static final long serialVersionUID = 6063187553171803105L;
	private int progress;

	public void setProgress(int progress) {
		this.progress = progress;
	}

	public FreeFormTask(String title, Date startDate, Date endDate, int progress) {
		super(title, startDate, endDate);
		this.progress = progress;
	}

	public int getProgress() {
		int totalProgress = progress;
		List<Task> children = children();
		if(children.isEmpty())
			return totalProgress;
		for (Task child : children) {
			totalProgress += child.getProgress();
		}
		return Math.round(totalProgress / (float)(children.size() + 1));
	}

	public TaskType getType() {
		return TaskType.FFT;
	}

	public Map<String, Object> diff(Task other) {
		return Collections.emptyMap();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(getType());
		builder.append(',');
		builder.append(getTitle());
		builder.append(',');
		builder.append(getStartDate());
		builder.append(',');
		builder.append(getEndDate());
		builder.append(',');
		builder.append(getProgress());
		builder.append(',');
		builder.append(getCategory().name());
		return builder.toString();
	}

}
