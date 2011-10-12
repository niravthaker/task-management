function info(log, msg){
	log.append("<div>INFO:" + msg + "</div>");
}
function infog(log, msg){
	log.append("<div style='color: green'>INFO: " + msg + "</div>");
}
function error(log, msg){
	log.append("<div style='color: red'>ERROR: " + msg + "</div>");
}
function warn(log, msg){
	log.append("<div style='color: yellow'>WARN:" + msg + "</div>");
}
_taskTest = {
		run : function(log){
			var subLog = $(log);
			info(subLog, "Starting integration tests...");
			info(subLog, "Create User Test...");
			_taskTest.createUser(subLog);
			info(subLog, "Delete User Test...");
			_taskTest.deleteUser(subLog);
			info(subLog, "Update User Test...");
			_taskTest.updateUser(subLog);
			info(subLog, "List User Test...");
			_taskTest.listUsers(subLog);
			info(subLog, "User Tests finished...");
			info(subLog, "Create Task Test...");
			_taskTest.createTask(subLog);
			info(subLog, "Delete Task Test...");
			_taskTest.deleteTask(subLog);
			info(subLog, "Update Task Test...");
			_taskTest.updateTask(subLog);
			info(subLog, "List Task Test...");
			_taskTest.listTask(subLog);
			info(subLog, "Tests finished...");
		},
		
		createUser: function(log){
			var subLog = log.append("<div id='create' style='padding-left: 10px;'/>");
			var user = {firstName:'JSUser', lastName:'LastName', email:'email@email.com', userId:'jstest',password:'soup'};
			var user2 = [{firstName:'JSUser', lastName:'LastName', email:'email@email.com', userId:'jstest',password:'soup'}];
			_tasks.createUser(user, {
				success: function(e){
					infog($('#create'), "User created: " + e.status);
				}
			});
			_tasks.createUser(user2, {
				error: function(e){
					warn($('#create') , "<span style='color: red'>Expected failure with error: " + e.statusText + "</span>");
				}
			});
		},
		
		deleteUser : function(log){
			
		},
		
		updateUser: function(log){
			
		},
		
		listUsers: function(log){
			
		},
		
		createTask: function(log){
			
		},
		
		deleteTask : function(log){
			
		},
		
		updateTask: function(log){
			
		},
		
		listTasks: function(log){
			
		}
		
		
};