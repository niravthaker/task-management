package name.nirav.tasks.core.model;

import name.nirav.tasks.core.model.predef.Uncategorized;

public class Category {
	public static Category UNCATEGORIZED = new Uncategorized();
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
