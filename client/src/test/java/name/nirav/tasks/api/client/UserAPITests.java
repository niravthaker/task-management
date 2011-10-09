package name.nirav.tasks.api.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import name.nirav.tasks.core.model.User;
import name.nirav.tasks.core.model.impl.EndUser;

import org.junit.Test;

public class UserAPITests extends BaseAPITests {

	private static final String LNAME = "Thaker";
	private static final String FNAME = "Nirav";
	private static final String EMAIL = "a@b.com";
	private static final String PASS = "secret";
	private static final String USERID = "nthaker";

	@Test
	public void createUser() {
		try {
			createUserInternal(USERID, PASS, EMAIL, FNAME, LNAME);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Failed to create client");
		}
	}

	@Test
	public void getUser() {
		createUserInternal(USERID, PASS, EMAIL, FNAME, LNAME);
		User user = apiClient.getUser(USERID);
		assertUserFields(user, USERID, FNAME, LNAME, EMAIL, PASS);
	}

	@Test
	public void deleteUser() {
		createUserInternal(USERID, PASS, EMAIL, FNAME, LNAME);
		try {
			apiClient.delete(USERID);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Failed to delete user");
		}
	}

	@Test
	public void editUser() {
		createUserInternal(USERID, PASS, EMAIL, FNAME, LNAME);
		String updatedUserId = "niravn1";
		User editedUser = stubUser(updatedUserId, PASS, EMAIL, FNAME, LNAME);
		User updated = apiClient.update(editedUser);
		assertEquals(updatedUserId, updated.getUserId());
	}

	private void createUserInternal(String userId, String pass, String email,
			String fname, String lname) {
		User user = stubUser(userId, pass, email, fname, lname);
		apiClient.createUser(user);
	}

	private void assertUserFields(User user, String userId, String fname,
			String lname, String email, String pass) {
		assertEquals(userId, user.getUserId());
		assertEquals(fname, user.getFirstName());
		assertEquals(lname, user.getLastName());
		assertEquals(email, user.getEmail());
		assertEquals(pass, user.getPassword());
	}

	private User stubUser(String userId, String pass, String email,
			String fname, String lname) {
		User user = new EndUser();
		user.setUserId(userId);
		user.setPassword(pass);
		user.setEmail(email);
		user.setFirstName(fname);
		user.setLastName(lname);
		return user;
	}
}
