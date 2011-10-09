package name.nirav.tasks.core.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import name.nirav.tasks.core.model.predef.NullTask;

public interface Task extends Serializable{
	final static Task NullTask = new NullTask();
	final static String NEW_ITEM_ID = "";

	Map<String, Object> diff(Task other);
	String toString();
	void setParent(Task parent);
	Task getParent();
	void setCategory(Category category);
	Category getCategory();
	void setId(String id);
	String getId();
	void setChildrenTasks(List<Task> childrenTasks);
	List<Task> getChildrenTasks();
	void setPredecessors(Set<Task> predecessors);
	Set<Task> getPredecessors();
	void setEndDate(Date endDate);
	Date getEndDate();
	void setStartDate(Date startDate);
	Date getStartDate();
	void setTitle(String title);
	String getTitle();
	Map.Entry<Date, Date> timeline();
	int duration();
	boolean isNew();
	List<Task> children();
	void addTask(Task task);
	void removeTask(Task task);
	int getProgress();
	void setProgress(int progress);
	TaskType getType();
}
