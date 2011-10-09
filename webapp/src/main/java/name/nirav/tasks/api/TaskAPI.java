package name.nirav.tasks.api;

import java.util.Arrays;
import java.util.Collection;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import name.nirav.tasks.core.dao.impl.NoSqlTaskDaoImpl;
import name.nirav.tasks.core.dao.impl.NoSqlTaskEventDaoImpl;
import name.nirav.tasks.core.dao.impl.NoSqlUserDaoImpl;
import name.nirav.tasks.core.eventsourcing.TaskServiceDelegatingListener;
import name.nirav.tasks.core.model.Task;
import name.nirav.tasks.core.model.User;
import name.nirav.tasks.core.model.impl.EndUser;
import name.nirav.tasks.core.service.TaskEventService;
import name.nirav.tasks.core.service.TaskService;
import name.nirav.tasks.core.service.UserService;
import name.nirav.tasks.core.service.impl.TaskEventServiceImpl;
import name.nirav.tasks.core.service.impl.TaskServiceImpl;
import name.nirav.tasks.core.service.impl.UserServiceImpl;

@Path("v1")
public class TaskAPI {
	private static UserService userService = new UserServiceImpl(new NoSqlUserDaoImpl());
	private static TaskEventService taskEventService = new TaskEventServiceImpl(new NoSqlTaskEventDaoImpl());
	private static TaskService taskService = new TaskServiceImpl(new NoSqlTaskDaoImpl());

	static{
		taskEventService.addListener(new TaskServiceDelegatingListener(taskService));
	}
	
	public TaskAPI() {
	}
	
	
	@GET
	@Path("users")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<User> getUsers(){
		return userService.list();
	}
	
	@SuppressWarnings("unchecked")
	@GET
	@Path("users/{user}")
	@Produces(MediaType.APPLICATION_JSON)
	public User getUser(@PathParam("user") String id){
		User user = userService.get(id);
		if(user == null){
			throw new WebApplicationException(
					Response.status(Status.NOT_FOUND)
							.entity(Arrays.asList(Status.NOT_FOUND.getStatusCode(),String.format("%s not found", id)))
							.build());
		}
		return user;
	}
	
	@PUT
	@Path("users")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createUser(@Context UriInfo uinfo, EndUser user){
		User created = userService.create(user);
		return Response.created(uinfo.getAbsolutePathBuilder().path(created.getUserId()).build("")).build();
	}
	
	@POST
	@Path("users")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateUser(@Context UriInfo uinfo, EndUser user){
		userService.update(user.getUserId(),user);
		return Response.ok(uinfo.getAbsolutePathBuilder().path(user.getUserId()).build("")).build();
	}

	@DELETE
	@Path("users/{user}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteUser(@Context UriInfo uinfo, @PathParam("user") String user){
		userService.delete(user);
		return Response.ok().build();
	}

	
	@GET
	@Path("{user}/projects")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Task> getTasks(){
		return taskService.list();
	}
	
	@GET
	@Path("{user}/user/project/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Task getTasksForUser(@PathParam("user") String user, @PathParam("id")String id){
		return taskService.get(id);
	}
}
