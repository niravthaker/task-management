package name.nirav.tasks.core.dao.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import name.nirav.tasks.core.dao.UserDao;
import name.nirav.tasks.core.model.User;

public class NoSqlUserDaoImpl implements UserDao{
	private Map<String, User> memDB = new HashMap<String, User>();
	
	public User create(User user){
		memDB.put(user.getUserId(), user);
		return user;
	}
	
	public void delete(String id){
		memDB.remove(id);
	}
	
	public void update(String userId, User user){
		memDB.put(userId, user);
	}
	
	public User get(String userId){
		return memDB.get(userId);
	}

	@Override
	public Collection<User> list(){
		return memDB.values();
	}
}
