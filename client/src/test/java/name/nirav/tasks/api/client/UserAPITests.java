package name.nirav.tasks.api.client;

import static name.nirav.tasks.api.client.APIUtils.URI_USERS;
import static org.junit.Assert.assertEquals;

import java.net.URI;

import javax.ws.rs.core.MediaType;

import name.nirav.tasks.core.model.User;
import name.nirav.tasks.core.model.impl.EndUser;

import org.junit.Test;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.ClientResponse.Status;
import com.sun.jersey.api.client.WebResource;

public class UserAPITests extends BaseAPITests{
	
	private static final String LNAME = "Thaker";
	private static final String FNAME = "Nirav";
	private static final String EMAIL = "a@b.com";
	private static final String PASS = "secret";
	private static final String USERID = "nthaker";
	
	private static String URI_USER(String userid) {
		return String.format("%s/%s", URI_USERS, userid);
	}

	
	@Test
	public void createUser(){
		assertEquals(Status.CREATED.getStatusCode(), createUserInternal(USERID, PASS, EMAIL, FNAME, LNAME).getStatus());
	}

	@Test
	public void getUser(){
		createUserInternal(USERID, PASS, EMAIL, FNAME, LNAME);
		ClientResponse resp = Client.create(cc).resource(URI_USER(USERID)).get(ClientResponse.class);
		assertEquals(Status.OK.getStatusCode(), resp.getStatus());
		User user = resp.getEntity(EndUser.class);
		assertUserFields(user, USERID, FNAME, LNAME, EMAIL, PASS);
	}

	@Test
	public void deleteUser(){
		createUserInternal(USERID, PASS, EMAIL, FNAME, LNAME);
		ClientResponse response = Client.create(cc).resource(URI_USER(USERID)).delete(ClientResponse.class);
		assertEquals(Status.OK.getStatusCode(),response.getStatus());
	}
	
	@Test
	public void editUser(){
		createUserInternal(USERID, PASS, EMAIL, FNAME, LNAME);
		String updatedUserId = "niravn1";
		User editedUser = stubUser(updatedUserId, PASS, EMAIL, FNAME, LNAME);
		Client client = Client.create(cc);
		ClientResponse response = client.resource(URI_USERS).type(MediaType.APPLICATION_JSON).post(ClientResponse.class, editedUser);
		assertEquals(Status.OK.getStatusCode(),response.getStatus());
		EndUser updatedUser = client.resource(response.getEntity(URI.class)).get(EndUser.class);
		assertUserFields(updatedUser, updatedUserId, FNAME, LNAME, EMAIL, PASS);
		
	}

	private ClientResponse createUserInternal(String userId, String pass, String email, String fname, String lname) {
		Client c = Client.create(cc);
		WebResource r = c.resource(URI_USERS);
		User user = stubUser(userId, pass, email, fname, lname);
		ClientResponse response = r.type(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON)
		.put(ClientResponse.class,user);
		return response;
	}

	private void assertUserFields(User user, String userId, String fname, String lname, String email, String pass) {
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
