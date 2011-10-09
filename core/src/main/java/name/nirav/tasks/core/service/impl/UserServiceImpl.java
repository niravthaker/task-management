package name.nirav.tasks.core.service.impl;

import java.util.Collection;

import name.nirav.tasks.core.dao.UserDao;
import name.nirav.tasks.core.model.User;
import name.nirav.tasks.core.service.UserService;

public class UserServiceImpl implements UserService {

	private final UserDao userDao;

	public UserServiceImpl(UserDao userDao) {
		this.userDao = userDao;
	}
	
	@Override
	public Collection<User> list() {
		return userDao.list();
	}

	@Override
	public User get(String userId) {
		return userDao.get(userId);
	}

	@Override
	public void update(String userId, User user) {
		userDao.update(userId, user);
	}

	@Override
	public void delete(String id) {
		userDao.delete(id);
	}

	@Override
	public User create(User user) {
		return userDao.create(user);
	}

}
