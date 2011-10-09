package name.nirav.tasks.core.model;

public interface User {

	public abstract String getEmail();

	public abstract void setEmail(String email);

	public abstract String getLastName();

	public abstract void setLastName(String lastName);

	public abstract String getFirstName();

	public abstract void setFirstName(String firstName);

	public abstract String getPassword();

	public abstract void setPassword(String password);

	public abstract String getUserId();

	public abstract void setUserId(String userId);
}
