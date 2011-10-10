package name.nirav.tasks.api;

import java.net.URI;
import java.util.AbstractMap.SimpleEntry;
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
import name.nirav.tasks.core.eventsourcing.events.CreateTaskEvent;
import name.nirav.tasks.core.eventsourcing.events.DeleteTaskEvent;
import name.nirav.tasks.core.eventsourcing.events.UpdateTaskEvent;
import name.nirav.tasks.core.model.Task;
import name.nirav.tasks.core.model.User;
import name.nirav.tasks.core.model.impl.EndUser;
import name.nirav.tasks.core.model.impl.FreeFormTask;
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
	@Path("users/{user}/tasks")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Task> getTasks(@PathParam("user") String user){
		return taskService.list(user);
	}
	
	@GET
	@Path("users/{user}/tasks/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTasksForUser(@PathParam("user") String user, @PathParam("id")String taksId){
		Task task = taskService.get(user, taksId);
		return task == Task.NullTask ? Response.status(Status.NOT_FOUND).build() : Response.ok(task).build() ;
	}
	
	@DELETE
	@Path("users/{user}/tasks/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteTask(@PathParam("user") String user, @PathParam("id")String id){
		taskEventService.publish(new DeleteTaskEvent(user, id));
		return Response.ok().build();
	}
	
	@PUT
	@Path("users/{user}/tasks")
	@Produces(MediaType.APPLICATION_JSON)
	public Response createTask(@Context UriInfo uinfo, @PathParam("user")String user, FreeFormTask task){
		taskEventService.publish(new CreateTaskEvent(user, task.getId(), task));
		return Response.created(uinfo.getAbsolutePathBuilder().path(task.getId()).build("")).build();
	}

	@POST
	@Path("users/{user}/tasks")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public URI updateTask(@Context UriInfo uinfo, @PathParam("user")String user, FreeFormTask task){
		String taskId = task.getId();
		Task oldTask = taskService.get(user, taskId);
		taskEventService.publish(new UpdateTaskEvent(user, taskId, new SimpleEntry<Task, Task>(oldTask, task)));
		return uinfo.getAbsolutePathBuilder().path(taskId).build("");
	}
	
}
