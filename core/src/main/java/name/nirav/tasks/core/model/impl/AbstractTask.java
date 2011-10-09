package name.nirav.tasks.core.model.impl;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import name.nirav.tasks.core.model.Category;
import name.nirav.tasks.core.model.Task;
import name.nirav.tasks.core.util.DateUtils;

public abstract class AbstractTask implements Task {

	private static final long serialVersionUID = -1056283105434124756L;

	private String title;
	private Date startDate;
	private Date endDate;
	private Set<Task> predecessors;
	private List<Task> childrenTasks;
	private String id = NEW_ITEM_ID;
	private Category category;
	private Task parent;

	public AbstractTask(String title, Date startDate, Date endDate) {
		this.title = title;
		this.startDate = startDate;
		this.endDate = endDate;
		predecessors = new HashSet<Task>();
		category = Category.UNCATEGORIZED;
		parent = Task.NullTask;
		childrenTasks = new ArrayList<Task>();
	}

	@Override
	public void removeTask(Task task) {
		childrenTasks.remove(task);
	}

	@Override
	public void addTask(Task task) {
		childrenTasks.add(task);
	}

	@Override
	public List<Task> children() {
		return dfs(childrenTasks);
	}
	@Override
	public boolean isNew() {
		return id == NEW_ITEM_ID;
	}
	
	public static List<Task> dfs(List<Task> tasks){
		List<Task> children = new LinkedList<Task>();
		for (Task child : tasks) {
			children.add(child);
			children.addAll(dfs(child.children()));
		}
		return children;
	}

	@Override
	public int duration() {
		Entry<Date, Date> timeline = timeline();
		return DateUtils.duration(timeline.getKey(), timeline.getValue());
	}


	@Override
	public Map.Entry<Date, Date> timeline() {
		List<Task> allItems = new ArrayList<Task>(children());
		allItems.add(this);
		return new SimpleEntry<Date, Date>(Collections.min(allItems,
				new Comparator<Task>() {
					public int compare(Task o1, Task o2) {
						return o1.getStartDate().compareTo(o2.getStartDate());
					}
				}).getStartDate(), Collections.max(allItems,
				new Comparator<Task>() {
					public int compare(Task o1, Task o2) {
						return o1.getEndDate().compareTo(o2.getEndDate());
					}
				}).getEndDate());
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public Date getStartDate() {
		return startDate;
	}

	@Override
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@Override
	public Date getEndDate() {
		return endDate;
	}

	@Override
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@Override
	public Set<Task> getPredecessors() {
		return predecessors;
	}

	@Override
	public void setPredecessors(Set<Task> predecessors) {
		this.predecessors = predecessors;
	}

	@Override
	public List<Task> getChildrenTasks() {
		return childrenTasks;
	}

	@Override
	public void setChildrenTasks(List<Task> childrenTasks) {
		this.childrenTasks = childrenTasks;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	@Override
	public Category getCategory() {
		return category;
	}

	@Override
	public void setCategory(Category category) {
		this.category = category;
	}

	@Override
	public Task getParent() {
		return parent;
	}

	@Override
	public void setParent(Task parent) {
		this.parent = parent;
	}
	
	
}