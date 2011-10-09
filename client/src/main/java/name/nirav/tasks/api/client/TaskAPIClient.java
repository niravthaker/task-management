package name.nirav.tasks.api.client;

import java.net.URI;
import java.util.List;

import javax.ws.rs.core.MediaType;

import name.nirav.tasks.core.model.User;
import name.nirav.tasks.core.model.impl.EndUser;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.ClientResponse.Status;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;

public class TaskAPIClient {
    private final String baseUri;
	private final String version;
	private final Client client;

	public TaskAPIClient(String baseUri, String version, String user, String password) {
		this.baseUri = baseUri;
		this.version = version;
		DefaultClientConfig cc = configureClient();
		client = Client.create(cc);

	}

	protected DefaultClientConfig configureClient() {
		DefaultClientConfig cc = new DefaultClientConfig();
		cc.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, true);
		return cc;
	}
	
	protected String getUsersURI(){
		return String.format("%s/%s/users", version, baseUri);
	}
	protected String getSingleUserURI(String userId){
		return String.format("%s/%s", getUsersURI(), userId);
	}
	
	@SuppressWarnings("unchecked")
	public List<User> getUsers(){
		WebResource resource = client.resource(getUsersURI());
		return resource.type(MediaType.APPLICATION_JSON)
					   .accept(MediaType.APPLICATION_JSON)
					   .get(List.class);
	}
    
	public void createUser(User user){
		ClientResponse response = client.resource(getUsersURI())
										.type(MediaType.APPLICATION_JSON)
										.accept(MediaType.APPLICATION_JSON)
										.put(ClientResponse.class, user);
		if(response.getStatus() != Status.CREATED.getStatusCode())
			throw new RuntimeException(String.format("User creation failed: '%s'", user.getUserId()));
	}
	
	public User getUser(String userId){
		return client.resource(getSingleUserURI(userId))
					 .type(MediaType.APPLICATION_JSON)
					 .accept(MediaType.APPLICATION_JSON)
					 .get(User.class);
	}
	
	public User update(User user){
		ClientResponse response = client.resource(getSingleUserURI(user.getUserId()))
				.type(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.post(ClientResponse.class, user);
		if(response.getStatus() != Status.OK.getStatusCode())
			throw new RuntimeException(String.format("Update failed with status code: %s", response.getStatus()));
		return client.resource(response.getEntity(URI.class)).get(EndUser.class);
	}
	
	public void delete(String userId){
		client.resource(getSingleUserURI(userId))
			  .type(MediaType.APPLICATION_JSON)
			  .accept(MediaType.APPLICATION_JSON)
			  .delete();
	}
}
