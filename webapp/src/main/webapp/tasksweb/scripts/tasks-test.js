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
	log.append("<div style='color: yellow'>WARN: " + msg + "</div>");
}
_taskTest = {
		run : function(log){
			var subLog = $(log);
			info(subLog, "Starting integration tests...");
			info(subLog, "Create User Test...");
			_taskTest.createUser(subLog);
			info(subLog, "Get User Test...");
			_taskTest.getUser(subLog);
			info(subLog, "Delete User Test...");
			_taskTest.deleteUser(subLog);
			info(subLog, "Update User Test...");
			_taskTest.updateUser(subLog);
			info(subLog, "List User Test...");
			_taskTest.listUsers(subLog);
			info(subLog, "User Tests finished");
			info(subLog, "Create Task Test...");
			_taskTest.createTask(subLog);
			info(subLog, "Delete Task Test...");
			_taskTest.deleteTask(subLog);
			info(subLog, "Update Task Test...");
			_taskTest.updateTask(subLog);
			info(subLog, "List Task Test...");
			_taskTest.listTasks(subLog);
			info(subLog, "Tests finished...");
		},
		
		createUser: function(log){
			var subLog = log.append("<div id='createUser' style='padding-left: 10px;'/>");
			var user = {firstName:'JSUser', lastName:'LastName', email:'email@email.com', userId:'jstest',password:'soup'};
			var user2 = [{firstName:'JSUser', lastName:'LastName', email:'email@email.com', userId:'jstest',password:'soup'}];
			_tasks.createUser(user, {
				success: function(e){
					infog($('#createUser'), "User created: " + e.status);
				}
			});
			_tasks.createUser(user2, {
				error: function(e){
					warn($('#createUser') , "Expected failure with error: " + e.statusText );
				}
			});
			_tasks.createUser(user,{
				success: function(e){
					error($("#createUser"), "Test failed, Duplicate user created.");
				}
			});
		},
		
		getUser : function(log){
			var subLog = log.append("<div id='getUser' style='padding-left: 10px;'/>");
			var userIdStr = 'jsgettest';
			var user = {firstName:'JSUser', lastName:'LastName', email:'email@email.com', userId:userIdStr,password:'soup'};
			_tasks.createUser(user, {
				error: function(e){
					error($('#getUser'), "Can't create user for get test: " + e.statusText);
				}
			});

			_tasks.getUser(userIdStr, {
				success: function(e){
					infog($("#getUser"), "Get user passed. User:" + JSON.stringify(e));
				},
				error: function(e){
					error($("#getUser"), "Get user failed with status: " + e.statusText);
				}
			});
		},
		
		deleteUser : function(log){
			var subLog = log.append("<div id='deleteUser' style='padding-left: 10px;'/>");
			var user = {firstName:'JSUser', lastName:'LastName', email:'email@email.com', userId:'tobedeleted',password:'soup'};
			_tasks.createUser(user,{
				error: function(e){error($("#deleteUser"), "Failed to create test user, unable to run delete test");},
				success: function(e){
					_tasks.deleteUser(user.userId, {
						error: function(e){error($("#deleteUser"), "Failed to delete test user, test failed.");},
						success: function(e){infog($("#deleteUser"), "User '" + user.userId + "' deleted.");}
					});
				}
			});
		},
		
		updateUser: function(log){
			var subLog = log.append("<div id='updateUser' style='padding-left: 10px;'/>");
			var updateUserId = 'tobeupdated';
			var user = {firstName:'JSUser', lastName:'LastName', email:'email@email.com', userId:updateUserId,password:'soup'};
			var updatedUser = {firstName:'JSUser(updated)', lastName:'LastName(updated)', 
					email:'email@email.com(updated)', userId:updateUserId,password:'soup'};
			
			_tasks.createUser(user, {
				error: function(e){
					error($("#updateUser"), "Failed to create user for update: " + e.statusText);
				},
				success: function(e){
					infog($("#updateUser"), "Created user to be updated:" + JSON.stringify(user));
					_tasks.updateUser(updatedUser, {
						error: function(e){
							error($("#updateUser"), "Failed to update user: " + e.statusText);
						},
						success: function(e){
							_tasks.getUser(updateUserId, {
								success : function(e){
									infog($("#updateUser"), "Update user passed, Updated User:" + JSON.stringify(e));
								},
								error: function(e){
									error($("#updateUser"), "Can't retrieve updated user : " + e.responseText);
								}
							});
						}
					});
				}
			});
		},
		
		listUsers: function(log){
			var subLog = log.append("<div id='listUsers' style='padding-left: 10px;'/>");
			_tasks.listUsers({
				error: function(e){error($("#listUsers"), "List users failed: " + e.statusText);}, 
				success: function(e){infog($("#listUsers"), "List User success, found " + e.length + " users : " + JSON.stringify(e));}
			});
		},
		
		createTask: function(log){
			var subLog = log.append("<div id='createTask' style='padding-left: 10px;'/>");
			var task = {title:'JS Task', startDate:new Date(), endDate: new Date(), progress: 10};
			_tasks.createTask('jstest',task, {
				success: function(e){
					infog($('#createTask'), "Task created: " + e.statusText);
				},
				error: function(e){
					error($('#createTask'), "Failed to create Task" + e.statusText);
				}
			});
		},
		
		deleteTask : function(log){
			var subLog = log.append("<div id='deleteTask' style='padding-left: 10px;'/>");
			var taskId = 'jstask';
			var task = {id:taskId, title:'JS Task to be deleted', startDate:new Date(), endDate: new Date(), progress: 10};
			_tasks.createTask('jstest',task, {
				success: function(e){
					infog($('#deleteTask'), "Task created: " + e.statusText + ", will delete it now");
					_tasks.deleteTask('jstest', taskId,{
						success: function(e){
							infog($('#deleteTask'), "Task '" + taskId + "' deleted");
						},
						error: function(e){
							error($('#deleteTask'), "Failed to delete Task: " + e.statusText);
						}
					});
				},
				error: function(e){
					error($('#deleteTask'), "Failed to create Task" + e.statusText);
				}
			});
			
		},
		
		updateTask: function(log){
			var subLog = log.append("<div id='updateTask' style='padding-left: 10px;'/>");
			_tasks.createTask('jstest',{id: "ID1" ,title:'JS Task to be updated', startDate:new Date(), endDate: new Date(), progress:  10}, {
				error: function(e){}, success: function(e){}
			});
			_tasks.updateTask('jstest',{id: "ID(updated)" ,title:'JS Task (updated_', startDate:new Date(), endDate: new Date(), progress: 20}, {
				error: function(e){
					error($("#updateTask"), "Update failed: " + e.responseText);
				}, 
				success: function(e){
					infog($("#updateTask"), "Update task passed, Updated task:" + JSON.stringify(e));
					_tasks.getTask('jstest','ID(updated)', {
						success : function(e){
							infog($("#updateTask"), "Update task retrieved, Updated task:" + JSON.stringify(e));
						},
						error : function(e){
							error($("#updateTask"), "Failed to get updated task: " + e.responseText);
						}
					});
				}
			});
		},
		
		listTasks: function(log){
			var subLog = log.append("<div id='listTasks' style='padding-left: 10px;'/>");
			for(var i = 0; i < 2; i++){
				_tasks.createTask('jstest',{id: "ID" + i ,title:'JS Task', startDate:new Date(), endDate: new Date(), progress: i + 10}, {
					error: function(e){}, success: function(e){}
				});
			}
			_tasks.listTasks('jstest',{
				error: function(e){error($("#listTasks"), "List tasks failed: " + e.statusText);}, 
				success: function(e){infog($("#listTasks"), "List tasks success, found " + e.length + " tasks : " + JSON.stringify(e));}
			});
		}
		
		
};