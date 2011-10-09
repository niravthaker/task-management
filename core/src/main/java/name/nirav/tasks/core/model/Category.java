package name.nirav.tasks.core.model;

import name.nirav.tasks.core.model.predef.Uncategorized;


public interface Category {
	Category UNCATEGORIZED = new Uncategorized();

	String name();
}
