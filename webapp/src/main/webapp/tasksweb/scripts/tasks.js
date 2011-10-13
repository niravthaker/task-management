var _baseURI = "/api/v1";
var _usersURI = _baseURI + "/users";
_tasks = {
		createUser: function(user, future){
			return _tasks._create(_usersURI, user, future);
		},
		getUser: function(userId, future){
			return _tasks._get(_tasks._singleUserURI(userId), future);
		},
		
		deleteUser: function(userId, future){
			return _tasks._delete(_tasks._singleUserURI(userId), future);
		},
		
		updateUser: function(user, future){
			return _tasks._update(_usersURI, user, future);
		},
		
		listUsers: function(future){
			return _tasks._get(_usersURI, future);
		},
		
		createTask: function(userId, task, future){
			return _tasks._create(_tasks._tasksURI(userId), task ,future);
		},
		
		getTask: function(userId, taskId, future){
			return _tasks._get(_tasks._singleTaskURI(userId, taskId), future);
		},
		
		updateTask: function(userId, task, future){
			return _tasks._update(_tasks._tasksURI(userId), task ,future);
		},
		
		deleteTask: function(userId, taskId, future){
			return _tasks._delete(_tasks._singleTaskURI(userId, taskId), future);
		},
		
		listTasks: function(userId, future){
			return _tasks._get(_tasks._tasksURI(userId), future);
		},
		
		_get: function(_entityURI, future){
			return $.ajax({
				type:'GET', 
				cache: false,
				contentType: 'application/json',
				url: _entityURI, 
				dataType: 'JSON',
				success: function(e){
					future.success(e);
				},
				error: function(e){
					future.error(e);
				}
			});
		},
		_update: function(_entityURI, entity, future){
			return $.ajax({
				type:'POST', 
				cache: false,
				contentType: 'application/json',
				url: _entityURI, 
				data: JSON.stringify(entity), 
				dataType: 'JSON',
				success: function(e){
					future.success(e);
				},
				error: function(e){
					future.error(e);
				}
			});
		},
		_create: function(_entityURI, entity, future){
			return $.ajax({
				type:'PUT', 
				cache: false,
				contentType: 'application/json',
				url: _entityURI, 
				data: JSON.stringify(entity), 
				dataType: 'JSON',
				complete: function(e){
					if(e.status == 201)
						future.success(e);
					else 
						future.error(e);
				}
			});
		},
		_delete: function(entityURI, future){
			return $.ajax({
				type:'DELETE', 
				cache: false,
				contentType: 'application/json',
				url: entityURI, 
				data: JSON.stringify([]), 
				dataType: 'JSON',
				complete: function(e){
					if(e.status == 204)
						future.success(e);
					else 
						future.error(e);
				}
			});
		},
		
		_singleUserURI : function(userId){
			return _usersURI + "/" + userId;
		},
		_tasksURI : function(userId){
			return _tasks._singleUserURI(userId) + "/tasks";
		},
		_singleTaskURI: function(userId, taskId){
			return _tasks._tasksURI(userId) + "/" + taskId;
		}
		
};