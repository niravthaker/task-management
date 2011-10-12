var _baseURI = "/api/v1";
var _usersURI = _baseURI + "/users";
_tasks = {
		createUser: function(user, future){
			return $.ajax({
				type:'PUT', 
				cache: false,
				contentType: 'application/json',
				url: _usersURI, 
				data: JSON.stringify(user), 
				dataType: 'JSON',
				complete: function(e){
					if(e.status == 201)
						future.success(e);
					else 
						future.error(e);
				}
			});
		},
		getUser: function(userId){
			return $.get(_tasks._singleUserURI(userId));
		},
		
		deleteUser: function(userId){
			return $.ajax({type: 'DELETE', url: _tasks._singleUserURI(user)});
		},
		
		updateUser: function(user){
			return $.post(_usersURI, user);
		},
		
		listUsers: function(){
			return $.get(_usersURI);
		},
		
		createTask: function(task){
			
		},
		
		getTask: function(taskId){
			
		},
		
		updateTask: function(task){
			
		},
		
		deleteTask: function(taskId){
			
		},
		
		listTasks: function(userId){
			return $.get(_tasks._tasksURI(userId));
		},
		
		_singleUserURI : function(userId){
			return _usersURI + "/" + userId;
		},
		_tasksURI : function(userId){
			return _tasks.singleUserURI(userId) + "/tasks";
		},
		_singleTaskURI: function(userId, taskId){
			return _tasks.tasksURI(userId) + "/" + taskId;
		}
		
};