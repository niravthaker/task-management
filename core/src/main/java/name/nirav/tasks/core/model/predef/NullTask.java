package name.nirav.tasks.core.model.predef;


import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

import name.nirav.tasks.core.model.Category;
import name.nirav.tasks.core.model.Task;
import name.nirav.tasks.core.model.TaskType;
import name.nirav.tasks.core.model.impl.FreeFormTask;


public class NullTask extends FreeFormTask{

	private static final long serialVersionUID = 4331377653058241917L;

	public NullTask(String title, Date startDate,Date endDate) {
		super(title, startDate, endDate, 0);
	}

	public NullTask() {
		super(null, null, null,0);
	}

	public String title() {
		return "Orphaned";
	}

	public int progress() {
		return 0;
	}

	public Date startDate() {
		return new Date();
	}

	public Date endDate() {
		return new Date();
	}

	public Set<Task> predecessors() {
		return Collections.emptySet();
	}

	public Set<Task> successors() {
		return Collections.emptySet();
	}

	public int duration() {
		return 0;
	}

	public void parent(Task parentTask) {
		throw new UnsupportedOperationException();
	}

	public Task parent() {
		throw new UnsupportedOperationException();
	}

	public void addTask(Task task) {
		throw new UnsupportedOperationException();
	}

	public void removeTask(Task task) {
		throw new UnsupportedOperationException();
	}

	public Category category() {
		return Category.UNCATEGORIZED;
	}

	public void categorize(Category catagory) {
		throw new UnsupportedOperationException();
	}

	public List<Task> children() {
		return Collections.emptyList();
	}

	public TaskType type() {
		return TaskType.UNKNOWN;
	}

}
