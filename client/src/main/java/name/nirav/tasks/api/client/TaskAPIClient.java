package name.nirav.tasks.api.client;

import java.net.URI;
import java.util.List;

import javax.ws.rs.core.MediaType;

import name.nirav.tasks.api.client.exceptions.ResourceNotFound;
import name.nirav.tasks.core.model.Task;
import name.nirav.tasks.core.model.User;
import name.nirav.tasks.core.model.impl.EndUser;
import name.nirav.tasks.core.model.impl.FreeFormTask;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.ClientResponse.Status;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;

public class TaskAPIClient {
    private final String baseUri;
	private final String version;
	private final Client client;
	private final String userId;

	public TaskAPIClient(String baseUri, String version, String userId, String password) {
		this.baseUri = baseUri;
		this.version = version;
		this.userId = userId;
		DefaultClientConfig cc = configureClient();
		client = Client.create(cc);

	}

	protected DefaultClientConfig configureClient() {
		DefaultClientConfig cc = new DefaultClientConfig();
		cc.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, true);
		return cc;
	}
	
	//User API////////////////////////////
	
	public List<User> getUsers(){
		return listEntities(getUsersURI());
	}
	public void createUser(User entity){
		createEntity(entity, getUsersURI(), entity.getUserId());
	}
	public User getUser(String userId){
		return getEntity(EndUser.class, getSingleUserURI(userId));
	}
	
	public User updateUser(User user){
		return updateEntity(user, getUsersURI(), EndUser.class);
	}

	public void deleteUser(String userId){
		deleteEntity(getSingleUserURI(userId));
	}
	//End User API////////////////////////////
	
	//Task API////////////////////////////
	public List<Task> getTasks(){
		return listEntities(getTasksURI(userId));
	}

	public Task getTask(String taskId){
		return getEntity(FreeFormTask.class, getSingleTaskURI(userId, taskId));
	}
	
	public void createTask(Task task){
		createEntity(task, getTasksURI(userId), task.getId());
	}
	public void deleteTask(String taskId){
		deleteEntity(getSingleTaskURI(userId,taskId));
	}
	public Task updateTask(Task task){
		return updateEntity(task, getTasksURI(userId), FreeFormTask.class);
	}
	//End Task API////////////////////////////
	protected String getTasksURI(String userId) {
		return String.format("%s/%s/tasks", getUsersURI(), userId);
	}
	protected String getSingleTaskURI(String userId, String taskId){
		return String.format("%s/%s", getTasksURI(userId), taskId);
	}
	protected String getUsersURI(){
		return String.format("%s/users", getBaseVersionURL());
	}
	protected String getSingleUserURI(String userId){
		return String.format("%s/%s", getUsersURI(), userId);
	}
	protected String getBaseVersionURL(){
		return String.format("%s/%s", baseUri, version);
	}

	protected <T> T getEntity(Class<? extends T> entityClass, String singleEntityURI) {
		ClientResponse response = client.resource(singleEntityURI)
					 .type(MediaType.APPLICATION_JSON)
					 .accept(MediaType.APPLICATION_JSON)
					 .get(ClientResponse.class);
		if(response.getStatus() != Status.OK.getStatusCode())
			throw new ResourceNotFound(String.format("Resource not found: %s - %s", response.getStatus(), response.getEntity(String.class)));
		return response.getEntity(entityClass);
		
	}

	protected <T> void createEntity(T entity, String uri, String id) {
		ClientResponse response = client.resource(uri)
										.type(MediaType.APPLICATION_JSON)
										.accept(MediaType.APPLICATION_JSON)
										.put(ClientResponse.class, entity);
		if(response.getStatus() != Status.CREATED.getStatusCode()) {
			throw new RuntimeException(String.format("Resource creation failed: '%s' %n ERROR: %s", 
														uri, id, response.getEntity(String.class)));
		}
	}

	@SuppressWarnings("unchecked")
	protected <T> List<T> listEntities(String uri) {
		return client.resource(uri).type(MediaType.APPLICATION_JSON)
					   .accept(MediaType.APPLICATION_JSON)
					   .get(List.class);
	}
	protected void deleteEntity(String entityURI) {
		client.resource(entityURI)
			  .type(MediaType.APPLICATION_JSON)
			  .accept(MediaType.APPLICATION_JSON)
			  .delete();
	}
	protected <T> T updateEntity(T user, String uri, Class<? extends T> entityClass) {
		ClientResponse response = client.resource(uri)
				.type(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.post(ClientResponse.class, user);
		if(response.getStatus() != Status.OK.getStatusCode())
			throw new RuntimeException(String.format("Update failed with status code: %s, %nERROR: %s", 
					response.getStatus(),
					response.getEntity(String.class)));
		return client.resource(response.getEntity(URI.class)).get(entityClass);
	}

	
}
