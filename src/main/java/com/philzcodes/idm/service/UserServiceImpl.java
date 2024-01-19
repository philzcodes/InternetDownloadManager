package com.philzcodes.idm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.philzcodes.idm.model.User;
import com.philzcodes.idm.repository.UserRepository;



@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;
	
	@Override
	public List<User> getAllUsers() {
		
		return (List<User>) userRepository.findAll();
	}


	@Override
	public User findUser(Long id) {
		/*
		 * Optional<User> User = UserRepository.findById(id);
		 * if(User.isPresent()) return User.get();
		 */
		
		return userRepository.findById(id).get();
	}

	@Override
	public User saveUser(User user) {
		PasswordEncoder encode = new BCryptPasswordEncoder();
		User newUser = user;
		newUser.setPassword(encode.encode(user.getPassword()));
		
		return userRepository.save(newUser);
	}

	@Override
	public User updateUser(User User) {
		
		return userRepository.save(User);
	}

	@Override
	public void deleteUser(Long id) {
		 userRepository.deleteById(id);;
	}


	
}
