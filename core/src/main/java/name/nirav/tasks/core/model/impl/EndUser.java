package name.nirav.tasks.core.model.impl;

import name.nirav.tasks.core.model.User;

public class EndUser implements User {
	private String userId;
	private String password;
	private String firstName;
	private String lastName;
	private String email;
	@Override
	public void setUserId(String userId) {
		this.userId = userId;
	}
	@Override
	public String getUserId() {
		return userId;
	}
	@Override
	public void setPassword(String password) {
		this.password = password;
	}
	@Override
	public String getPassword() {
		return password;
	}
	@Override
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	@Override
	public String getFirstName() {
		return firstName;
	}
	@Override
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	@Override
	public String getLastName() {
		return lastName;
	}
	@Override
	public void setEmail(String email) {
		this.email = email;
	}
	@Override
	public String getEmail() {
		return email;
	}
	
}
