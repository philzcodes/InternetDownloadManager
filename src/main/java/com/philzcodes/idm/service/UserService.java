package com.philzcodes.idm.service;

import java.util.List;

import com.philzcodes.idm.model.User;


public interface UserService {
	
	List<User> getAllUsers();
	User saveUser(User user);
	User updateUser(User user);
	void deleteUser(Long id);
	User findUser(Long id);
}
